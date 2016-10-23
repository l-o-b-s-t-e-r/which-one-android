package com.android.project.activities.detail;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;

import com.android.project.R;
import com.android.project.activities.wall.WallFragment;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.QuizViewBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class RecordDetailActivity extends AppCompatActivity implements RecordDetailPresenter.View {

    public static final String RECORD_ID = "RECORD_ID";
    private static final String TAG = RecordDetailActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.detail_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    private String mUsername;
    private Record mRecord;
    private RecordDetailPresenter.ActionListener mActionListener;
    private RecordDetailRecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        radioGroup.setOnCheckedChangeListener(getOnCheckedChangeListener());

        mRecyclerViewAdapter = new RecordDetailRecyclerViewAdapter();
        mUsername = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.user_name), "");

        recyclerView.setAdapter(mRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mActionListener = new RecordDetailPresenterImpl(this);
        mActionListener.loadRecord(getIntent().getLongExtra(RECORD_ID, -1L));
    }

    @Override
    public void showRecord(Record record) {
        mRecord = record;
        mRecyclerViewAdapter.updateData(record.getImages());

        radioGroup.removeAllViews();
        if (!mRecord.getAllVotes().contains(mUsername)) {
            for (int i = 0; i < record.getOptions().size(); i++) {
                radioGroup.addView(QuizViewBuilder.createBaseOption(record.getOptions().get(i), i));
            }
        } else {
            int allVotesCount = mRecord.getAllVotes().size();
            for (Option option : mRecord.getOptions()) {
                radioGroup.addView(QuizViewBuilder.createFinalOption(
                        option,
                        allVotesCount,
                        option.getVotes().contains(mUsername)));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActionListener.onStop();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    private RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent broadcastIntent = new Intent("com.android.project.activities.wall");
                broadcastIntent.putExtra(WallFragment.RECORD_ID, mRecord.getRecordId());
                LocalBroadcastManager.getInstance(RecordDetailActivity.this).sendBroadcast(broadcastIntent);

                mRecord.getOptions()
                        .get(checkedId)
                        .getVotes()
                        .add(mUsername);

                radioGroup.removeAllViews();
                int allVotesCount = mRecord.getAllVotes().size();
                List<Subscriber<Void>> subscribers = new ArrayList<>();
                for (Option option : mRecord.getOptions()) {
                    Subscriber<Void> subscriber =
                            QuizViewBuilder.createFinalOption(
                                    radioGroup,
                                    option,
                                    allVotesCount,
                                    option.getVotes().contains(mUsername));

                    subscribers.add(subscriber);
                }

                mActionListener.sendVote(
                        mRecord.getRecordId(),
                        mRecord.getOptions().get(checkedId),
                        mUsername,
                        subscribers
                );
            }
        };
    }
}
