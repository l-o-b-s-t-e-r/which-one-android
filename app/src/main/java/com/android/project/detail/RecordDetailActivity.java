package com.android.project.detail;

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
import com.android.project.cofig.WhichOneApp;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.QuizViewBuilder;
import com.android.project.wall.WallFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordDetailActivity extends AppCompatActivity implements DetailPresenter.View {

    public static final String RECORD_ID = "RECORD_ID";
    private static final String TAG = RecordDetailActivity.class.getName();
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    private DetailPresenter.ActionListener mActionListener;
    private String mUsername;
    private Record mRecord;
    private DetailRecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);

        mUsername = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.user_name), "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerViewAdapter = new DetailRecyclerViewAdapter(this);

        RecyclerView recyclerView = ButterKnife.findById(this, R.id.record_recycler);
        recyclerView.setAdapter(mRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        mActionListener = new DetailPresenterImpl(this, ((WhichOneApp) getApplication()).getMainComponent());
        mActionListener.loadRecord(getIntent().getLongExtra(RECORD_ID, -1L));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mActionListener.sendVote(
                        mRecord.getRecordId(),
                        mRecord.getOptions().get(checkedId),
                        mUsername
                );

                Intent broadcastIntent = new Intent("com.android.project.wall");
                broadcastIntent.putExtra(WallFragment.RECORD_ID, mRecord.getRecordId());
                LocalBroadcastManager.getInstance(RecordDetailActivity.this).sendBroadcast(broadcastIntent);

                mRecord.getOptions()
                        .get(checkedId)
                        .getVotes()
                        .add(mUsername);

                radioGroup.removeAllViews();
                int allVotesCount = mRecord.getAllVotes().size();
                for (Option o : mRecord.getOptions()) {
                    radioGroup.addView(QuizViewBuilder.createFinalOption(
                            RecordDetailActivity.this,
                            o,
                            allVotesCount,
                            o.getVotes().contains(mUsername)));
                }
            }
        });
    }

    @Override
    public void showRecord(Record record) {
        mRecord = record;
        mRecyclerViewAdapter.updateData(record.getImages());

        radioGroup.removeAllViews();
        if (!mRecord.getAllVotes().contains(mUsername)) {
            for (int i = 0; i < record.getOptions().size(); i++) {
                radioGroup.addView(QuizViewBuilder.createBaseOption(this, record.getOptions().get(i), i));
            }
        } else {
            int allVotesCount = mRecord.getAllVotes().size();
            for (Option o : mRecord.getOptions()) {
                radioGroup.addView(QuizViewBuilder.createFinalOption(
                        this,
                        o,
                        allVotesCount,
                        o.getVotes().contains(mUsername)));
            }
        }
    }

}
