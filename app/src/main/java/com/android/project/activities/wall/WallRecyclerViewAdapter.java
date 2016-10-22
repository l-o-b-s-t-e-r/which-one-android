package com.android.project.activities.wall;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.ImageManager;
import com.android.project.util.QuizViewBuilder;

import java.io.File;
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
    private Context mContext;
    private List<Long> mRecordIds;
    private WallPresenter.ActionListener mActionListener;

    public WallRecyclerViewAdapter(Context context, WallPresenter.ActionListener actionListener, String username) {
        mUsername = username;
        mContext = context;
        mRecordIds = new ArrayList<>();
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
        holder.setContent(mActionListener.loadRecordById(mRecordIds.get(position)));

        if (!allRecordsLoaded && position == mRecordIds.size() - 1) {
            mActionListener.loadNextRecords(mRecordIds.get(position));
        }
    }

    public void updateData(List<Long> recordIds) {
        if (!recordIds.isEmpty()) {
            mRecordIds.addAll(recordIds);
            notifyItemChanged(mRecordIds.indexOf(recordIds.get(0)));
        } else {
            allRecordsLoaded = true;
        }
    }

    public void updateRecord(Long recordId) {
        Log.i(TAG, "updateRecord: record ID - " + recordId.toString());

        if (mRecordIds.contains(recordId)) {
            notifyItemChanged(mRecordIds.indexOf(recordId));
        } else {
            mRecordIds.add(recordId);
            notifyItemChanged(mRecordIds.size() - 1);
        }
    }

    public void cleanData() {
        mRecordIds.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRecordIds.size();
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

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
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
                                        mContext,
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
            username.setText(record.getUsername());
            title.setText(record.getTitle());

            if (new File(record.getAvatar()).exists()) {  //CHANGE
                RoundedBitmapDrawable imageBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), record.getAvatar());
                imageBitmapDrawable.setCornerRadius(50.0f);

                avatar.setImageDrawable(imageBitmapDrawable);
            } else {
                Subscriber<Bitmap> subscriber = new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        RoundedBitmapDrawable imageBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), bitmap);
                        imageBitmapDrawable.setCornerRadius(50.0f);

                        avatar.setImageDrawable(imageBitmapDrawable);
                    }
                };

                ImageManager.getInstance().addImageSubscriber(record.getAvatar(), subscriber);
            }


            buildOptions(mRecord.getAllVotes().contains(mUsername));

            createImageRecyclerAdapter();
        }

        private void buildOptions(boolean votedQuiz) {
            radioGroup.removeAllViews();

            if (votedQuiz) {
                for (Option option : mRecord.getOptions()) {
                    radioGroup.addView(
                            QuizViewBuilder.createFinalOption(mContext, option, mRecord.getAllVotes().size(), option.getVotes().contains(mUsername))
                    );
                }
            } else {
                for (Option option : mRecord.getOptions()) {
                    radioGroup.addView(
                            QuizViewBuilder.createBaseOption(mContext, option, radioGroup.getChildCount())
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
