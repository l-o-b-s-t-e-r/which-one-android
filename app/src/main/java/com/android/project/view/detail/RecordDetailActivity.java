package com.android.project.view.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.Record;
import com.android.project.model.User;
import com.android.project.util.ImageManager;
import com.android.project.util.QuizViewBuilder;
import com.android.project.view.detail.di.RecordDetailModule;
import com.android.project.view.userpage.UserPageActivity;
import com.android.project.view.wall.WallFragment;
import com.bumptech.glide.RequestManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordDetailActivity extends AppCompatActivity implements RecordDetailPresenter.View {

    public static final String RECORD_ID = "RECORD_ID";
    private static final String TAG = RecordDetailActivity.class.getSimpleName();

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.record_detail_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolBarTitle;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    User user;
    @Inject
    RecordDetailPresenter.ActionListener presenter;
    @Inject
    RecordDetailRecyclerViewAdapter recyclerViewAdapter;
    @Inject
    QuizViewBuilder quizViewBuilder;
    @Inject
    RequestManager glide;

    private Record mRecord;
    private List<QuizViewBuilder.ViewHolder> mViewHolderOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_detail_activity);
        ButterKnife.bind(this);

        WhichOneApp.getUserComponent()
                .plus(new RecordDetailModule(this))
                .inject(this);

        toolBarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "Lobster-Regular.ttf"));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        swipeRefreshLayout.setOnRefreshListener(getOnRefreshListener());

        radioGroup.setOnCheckedChangeListener(getOnCheckedChangeListener());

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        presenter.loadRecordFromDB(getIntent().getLongExtra(RECORD_ID, -1L));
    }

    @Override
    public void showRecord(Record record) {
        Log.i(TAG, "showRecord: record - " + record.toString() + user.getAvatar());

        mRecord = record;

        glide.load(ImageManager.IMAGE_URL + record.getAvatar())
                .asBitmap()
                .into(ImageManager.getInstance().createTarget(
                        ImageManager.SMALL_AVATAR_SIZE, ImageManager.SMALL_AVATAR_SIZE, avatar
                ));

        username.setText(mRecord.getUsername());

        description.setText(record.getDescription());
        recyclerViewAdapter.updateData(record.getImages());

        radioGroup.removeAllViews();
        if (mRecord.isVoted()) {
            mViewHolderOptions = quizViewBuilder.createVotedOptions(radioGroup, mRecord);
        } else {
            quizViewBuilder.createRadioOptions(radioGroup, mRecord);
        }
    }

    @OnClick({R.id.avatar, R.id.username})
    public void onUserClick() {
        openUserPage(mRecord.getUsername());
    }

    @Override
    public void openUserPage(String username) {
        Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
        intent.putExtra(getString(R.string.user_name_opened_page), username);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateQuiz(Record updatedRecord) {
        addRecordToSharedPreferences(updatedRecord);
        mRecord = updatedRecord;

        Integer allVotesCount = mRecord.getVoteCount();
        if (mRecord.isVoted()) {
            for (QuizViewBuilder.ViewHolder viewHolder : mViewHolderOptions) {
                viewHolder.setContent(mRecord, allVotesCount);
            }
        }
    }

    private void addRecordToSharedPreferences(Record record) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RecordDetailActivity.this);
        Set<String> IDs = sharedPreferences.getStringSet(WallFragment.RECORD_ID, new HashSet<>());
        IDs.add(record.getRecordId().toString());
        sharedPreferences.edit()
                .putStringSet(WallFragment.RECORD_ID, IDs)
                .apply();
    }

    private RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return (group, checkedId) -> {
            radioGroup.removeAllViews();

            mViewHolderOptions = quizViewBuilder.createProgressOption(radioGroup, mRecord);

            presenter.sendVote(
                    mRecord,
                    mRecord.getOptions().get(checkedId),
                    user.getUsername()
            );
        };
    }

    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return () -> {
            presenter.stop();
            presenter.loadRecordFromServer(mRecord.getRecordId(), user.getUsername());
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }
}
