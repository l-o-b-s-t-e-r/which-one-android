package com.android.project.activities.detail;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.activities.wall.WallFragment;
import com.android.project.model.Record;
import com.android.project.util.QuizViewBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordDetailActivity extends AppCompatActivity implements RecordDetailPresenter.View {

    public static final String RECORD_ID = "RECORD_ID";
    private static final String TAG = RecordDetailActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.detail_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @Inject
    RecordDetailPresenter.ActionListener presenter;

    private String mUsername;
    private Record mRecord;
    private RecordDetailRecyclerViewAdapter mRecyclerViewAdapter;
    private List<QuizViewBuilder.ViewHolder> mViewHolderOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        radioGroup.setOnCheckedChangeListener(getOnCheckedChangeListener());

        mRecyclerViewAdapter = new RecordDetailRecyclerViewAdapter();
        mUsername = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.user_name), "");

        recyclerView.setAdapter(mRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        presenter = new RecordDetailPresenterImpl(this);
        presenter.loadRecord(getIntent().getLongExtra(RECORD_ID, -1L));
    }

    @Override
    public void showRecord(Record record) {
        Log.i(TAG, "showRecord: record - " + record.toString());

        mRecord = record;
        description.setText(record.getDescription());
        mRecyclerViewAdapter.updateData(record.getImages());

        radioGroup.removeAllViews();
        if (mRecord.getSelectedOption() != null) {
            QuizViewBuilder.getInstance().createVotedOptions(radioGroup, mRecord);
        } else {
            QuizViewBuilder.getInstance().createRadioOptions(radioGroup, mRecord);
            }
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
    public void updateQuiz(Record newRecord) {
        mRecord = newRecord;
        addRecordToSharedPreferences();

        for (QuizViewBuilder.ViewHolder viewHolder : mViewHolderOptions) {
            viewHolder.setContent(mRecord);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    private void addRecordToSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RecordDetailActivity.this);
        Set<String> IDs = sharedPreferences.getStringSet(WallFragment.RECORD_ID, new HashSet<>());
        IDs.add(mRecord.getRecordId().toString());
        sharedPreferences.edit()
                .putStringSet(WallFragment.RECORD_ID, IDs)
                .apply();
    }

    private RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return (group, checkedId) -> {
                radioGroup.removeAllViews();

                mViewHolderOptions = QuizViewBuilder.getInstance().createProgressOption(radioGroup, mRecord);

            presenter.sendVote(
                        mRecord,
                        mRecord.getOptions().get(checkedId),
                        mUsername
                );
        };
    }
}
