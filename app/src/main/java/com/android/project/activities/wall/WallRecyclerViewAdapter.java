package com.android.project.activities.wall;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.ImageManager;
import com.android.project.util.QuizViewBuilder;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Lobster on 12.05.16.
 */

public class WallRecyclerViewAdapter extends RecyclerView.Adapter<WallRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WallRecyclerViewAdapter.class.getSimpleName();
    private boolean allRecordsLoaded;
    private String mUsername;
    private List<Record> mRecords;
    private WallPresenter.ActionListener mActionListener;

    public WallRecyclerViewAdapter(WallPresenter.ActionListener actionListener, String username) {
        mUsername = username;
        mRecords = new ArrayList<>();
        mActionListener = actionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setContent(mRecords.get(position));

        if (!allRecordsLoaded && position == mRecords.size() - 1) {
            mActionListener.loadNextRecords(mRecords.get(position).getRecordId());
        }
    }

    public void updateData(List<Record> records) {
        if (!records.isEmpty()) {
            mRecords.addAll(records);
            notifyItemChanged(mRecords.size() - records.size());
        } else {
            allRecordsLoaded = true;
        }
    }

    public void updateRecord(Long recordId) {
        Log.i(TAG, "updateRecord: record ID - " + recordId.toString());

        Record record = mActionListener.getRecordById(recordId);
        if (mRecords.contains(record)) {
            notifyItemChanged(mRecords.indexOf(record));
        } else {
            mRecords.add(record);
            notifyItemChanged(mRecords.size() - 1);
        }
    }

    public void cleanData() {
        mRecords.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.username)
        TextView username;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.record_recycler)
        RecyclerView recyclerView;

        @BindView(R.id.radio_group)
        RadioGroup radioGroup;

        @BindView(R.id.avatar)
        ImageView avatar;

        private Record mRecord;
        private Target mAvatarTarget;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);

            recyclerView.setLayoutManager(new LinearLayoutManager(WhichOneApp.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setNestedScrollingEnabled(false);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
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
            });
        }

        @OnClick({R.id.avatar, R.id.title})
        public void onUserClick() {
            mActionListener.openUserPage(mRecord.getUsername());
        }

        public void setContent(Record record) {
            Log.i(TAG, "setContent: record - " + record.toString());

            mRecord = record;
            mAvatarTarget = ImageManager.getInstance().createTarget(avatar);
            username.setText(record.getUsername());
            title.setText(record.getTitle());

            WhichOneApp.getPicasso()
                    .load(ImageManager.IMAGE_URL + record.getAvatar())
                    .into(mAvatarTarget);

            buildOptions(mRecord.getAllVotes().contains(mUsername));

            createImageRecyclerAdapter();
        }

        private void buildOptions(boolean votedQuiz) {
            radioGroup.removeAllViews();

            if (votedQuiz) {
                for (Option option : mRecord.getOptions()) {
                    radioGroup.addView(
                            QuizViewBuilder.createFinalOption(option, mRecord.getAllVotes().size(), option.getVotes().contains(mUsername))
                    );
                }
            } else {
                for (Option option : mRecord.getOptions()) {
                    radioGroup.addView(
                            QuizViewBuilder.createBaseOption(option, radioGroup.getChildCount())
                    );
                }
            }
        }

        private void createImageRecyclerAdapter() {
            RecordRecyclerViewAdapter recordRecyclerViewAdapter = new RecordRecyclerViewAdapter(mRecord.getImages(), mActionListener);
            recyclerView.setAdapter(recordRecyclerViewAdapter);
        }
    }
}
