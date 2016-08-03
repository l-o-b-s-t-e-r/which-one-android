package com.android.project.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.android.project.R;
import com.android.project.login.LogInActivity;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.QuizViewBuilder;
import com.android.project.util.RecordServiceImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordDetailActivityFragment extends Fragment implements DetailPresenter.View {

    public static final String RECORD_ID = "RECORD_ID";

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    private Record mRecord;
    private DetailPresenter.ActionListener mActionListener;
    private DetailRecyclerViewAdapter mRecyclerViewAdapter;

    public RecordDetailActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_detail, container, false);
        ButterKnife.bind(this, view);

        mRecyclerViewAdapter = new DetailRecyclerViewAdapter(getContext());

        RecyclerView recyclerView = ButterKnife.findById(view, R.id.record_recycler);
        recyclerView.setAdapter(mRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, true));

        mActionListener = new DetailPresenterImpl(new RecordServiceImpl(getString(R.string.base_uri)), this);
        mActionListener.loadRecord(getActivity().getIntent().getLongExtra(RECORD_ID, -1L));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mActionListener.sendVote(
                        LogInActivity.USER_NAME,
                        mRecord.getRecordId(),
                        mRecord.getOptions().get(checkedId).getOptionName()
                );

                mRecord.getOptions()
                        .get(checkedId)
                        .getVotes()
                        .add(LogInActivity.USER_NAME);

                radioGroup.removeAllViews();
                int allVotesCount = mRecord.getAllVotes().size();
                for (Option o : mRecord.getOptions()) {
                    radioGroup.addView(QuizViewBuilder.createFinalOption(getContext(), o, allVotesCount));
                }
            }
        });

        return view;
    }

    @Override
    public void showRecord(Record record) {
        mRecord = record;
        mRecyclerViewAdapter.updateData(record.getImages());

        radioGroup.removeAllViews();
        if (!mRecord.getAllVotes().contains(LogInActivity.USER_NAME)) {
            for (int i = 0; i < record.getOptions().size(); i++) {
                radioGroup.addView(QuizViewBuilder.createBaseOption(getContext(), record.getOptions().get(i), i));
            }
        } else {
            int allVotesCount = mRecord.getAllVotes().size();
            for (Option o : mRecord.getOptions()) {
                radioGroup.addView(QuizViewBuilder.createFinalOption(getContext(), o, allVotesCount));
            }
        }
    }
}