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
import com.android.project.model.Image;
import com.android.project.model.Record;
import com.android.project.util.ImageManager;
import com.android.project.util.QuizViewBuilder;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        holder.setContent(mRecords.get(position), position);

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

    public void refreshRecords(Set<String> recordIds) {
        Log.i(TAG, "updateRecords: record IDs - " + recordIds.toString());

        Record record = new Record();
        Integer index;
        for (String recordId : recordIds) {
            record.setRecordId(Long.valueOf(recordId));
            if (mRecords.contains(record)) {
                index = mRecords.indexOf(record);
                mRecords.set(index, mActionListener.getRecordById(record.getRecordId()));
                notifyItemChanged(index);
            }
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.mQuizSubscriber != null) {
            holder.mQuizSubscriber.unsubscribe();
        }
    }

    public void updateRecord(Integer index, Record record) {
        mRecords.set(index, record);
    }

    public void cleanData() {
        mRecords.clear();
        allRecordsLoaded = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.username)
        TextView username;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.record_recycler)
        RecyclerView recyclerView;

        @BindView(R.id.radio_group)
        RadioGroup radioGroup;

        @BindView(R.id.avatar)
        ImageView avatar;

        private Record mRecord;
        private Integer mPosition;
        private Target mAvatarTarget;
        private Subscriber<Record> mQuizSubscriber;
        private RecordRecyclerViewAdapter mRecordRecyclerViewAdapter;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
            createIncludedRecyclerView();

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    radioGroup.removeAllViews();

                    mQuizSubscriber = createQuizSubscriber(
                            QuizViewBuilder.getInstance().createProgressOption(radioGroup, mRecord)
                    );

                    mActionListener.sendVote(
                            mRecord,
                            mRecord.getOptions().get(checkedId),
                            mUsername,
                            mQuizSubscriber,
                            mPosition
                    );
            });
        }

        @OnClick({R.id.avatar, R.id.username})
        public void onUserClick() {
            mActionListener.openUserPage(mRecord.getUsername());
        }

        public void setContent(Record record, Integer position) {
            Log.i(TAG, "setContent: record - " + record.toString());

            mRecord = record;
            mPosition = position;
            mAvatarTarget = ImageManager.getInstance().createTarget(avatar);
            username.setText(record.getUsername());
            description.setText(record.getDescription());

            WhichOneApp.getPicasso()
                    .load(ImageManager.IMAGE_URL + record.getAvatar())
                    .into(mAvatarTarget);

            buildOptions();
            setImages(record.getRecordId(), record.getImages());
        }

        private void buildOptions() {
            radioGroup.removeAllViews();

            if (mRecord.getSelectedOption() != null) {
                QuizViewBuilder.getInstance().createVotedOptions(radioGroup, mRecord);
            } else {
                QuizViewBuilder.getInstance().createRadioOptions(radioGroup, mRecord);
            }
        }

        private void createIncludedRecyclerView() {
            recyclerView.setLayoutManager(new LinearLayoutManager(WhichOneApp.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setNestedScrollingEnabled(false);

            mRecordRecyclerViewAdapter = new RecordRecyclerViewAdapter(mActionListener);
            recyclerView.setAdapter(mRecordRecyclerViewAdapter);
        }

        private void setImages(Long recordId, List<Image> images) {
            mRecordRecyclerViewAdapter.setContent(recordId, images);
        }

        private Subscriber<Record> createQuizSubscriber(final List<QuizViewBuilder.ViewHolder> viewHolderOptions) {
            return new Subscriber<Record>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Record record) {
                    for (QuizViewBuilder.ViewHolder viewHolder : viewHolderOptions) {
                        viewHolder.setContent(record);
                    }
                }
            };
        }
    }
}
