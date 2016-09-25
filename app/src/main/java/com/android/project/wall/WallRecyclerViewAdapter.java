package com.android.project.wall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.adapter.RecordRecyclerViewAdapter;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.ImageLoader;
import com.android.project.util.QuizViewBuilder;
import com.android.project.util.SquareImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 12.05.16.
 */

public class WallRecyclerViewAdapter extends RecyclerView.Adapter<WallRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WallRecyclerViewAdapter.class.getName();
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
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record, parent, false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mActionListener.getRecordById(mRecordIds.get(position)));
        if (!allRecordsLoaded && position == mRecordIds.size() - 1) {
            mActionListener.loadNextRecords(mRecordIds.get(position));
        }
    }

    public void updateData(List<Long> recordIds) {
        if (!recordIds.isEmpty()) {
            mRecordIds.addAll(recordIds);
            notifyDataSetChanged();
        } else {
            allRecordsLoaded = true;
        }
    }

    public void updateRecord(Long recordId) {
        Log.i("INFO", String.valueOf(mRecordIds.indexOf(recordId)));
        notifyItemChanged(mRecordIds.indexOf(recordId));
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
        SquareImageView avatar;

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
                    mActionListener.sendVote(
                            mRecord.getRecordId(),
                            mRecord.getOptions().get(checkedId),
                            mUsername
                    );

                    mRecord.getOptions()
                            .get(checkedId)
                            .getVotes()
                            .add(mUsername);

                    radioGroup.removeAllViews();
                    int allVotesCount = mRecord.getAllVotes().size();
                    for (Option o: mRecord.getOptions()){
                        radioGroup.addView(QuizViewBuilder.createFinalOption(
                                mContext,
                                o,
                                allVotesCount,
                                o.getVotes().contains(mUsername)));
                    }
                }
            });
        }

        @OnClick({R.id.avatar, R.id.title})
        void onUserClick(){
            mActionListener.openUserPage(mRecord.getUsername());
        }

        public void setContent(Record record) {
            mRecord = record;
            username.setText(record.getUsername());
            title.setText(record.getTitle());

            Bitmap bitmapImage = BitmapFactory.decodeFile(record.getAvatar());
            if (bitmapImage == null) {
                avatar.setImageResource(R.mipmap.ic_launcher);
                ImageLoader.getInstance().pushImage(record.getAvatar(), avatar, null, null);
            } else {
                avatar.setImageBitmap(bitmapImage);
            }

            radioGroup.removeAllViews();
            if (!mRecord.getAllVotes().contains(mUsername)) {
                for (int i = 0; i < record.getOptions().size(); i++) {
                    radioGroup.addView(QuizViewBuilder.createBaseOption(mContext, record.getOptions().get(i), i));
                }
            } else {
                int allVotesCount = mRecord.getAllVotes().size();
                for (Option o: mRecord.getOptions()){
                    radioGroup.addView(QuizViewBuilder.createFinalOption(
                            mContext,
                            o,
                            allVotesCount,
                            o.getVotes().contains(mUsername)));
                }
            }

            RecordRecyclerViewAdapter recordRecyclerViewAdapter = new RecordRecyclerViewAdapter(record.getImages(), mActionListener);
            recyclerView.setAdapter(recordRecyclerViewAdapter);
        }
    }
}
