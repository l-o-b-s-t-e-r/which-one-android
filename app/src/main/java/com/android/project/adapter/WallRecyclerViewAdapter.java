package com.android.project.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.util.SquareImageView;
import com.android.project.login.LogInActivity;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.QuizViewBuilder;
import com.android.project.wall.WallPresenter;
import com.squareup.picasso.Picasso;

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
    private Context mContext;
    private List<Record> mCardContents;
    private WallPresenter.ActionListener mActionListener;

    public WallRecyclerViewAdapter(Context context, WallPresenter.ActionListener actionListener) {
        mContext = context;
        mCardContents = new ArrayList<>();
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
        holder.setContent(mCardContents.get(position));
        if (position == mCardContents.size()-1){
            mActionListener.loadNextRecords(mCardContents.get(position).getRecordId());
        }
    }

    public void updateData(List<Record> records) {
        if (!records.isEmpty()) {
            mCardContents.addAll(records);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mCardContents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView text;

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
                    for (Option o: mRecord.getOptions()){
                        radioGroup.addView(QuizViewBuilder.createFinalOption(mContext, o, allVotesCount));
                    }
                }
            });
        }

        @OnClick({R.id.avatar, R.id.title})
        void onUserClick(){
            mActionListener.openUserPage(mRecord.getTitle());
        }

        public void setContent(Record record) {
            mRecord = record;
            text.setText(record.getTitle());

            Picasso.with(mContext)
                    .load(mContext.getString(R.string.base_uri)+record.getAvatar())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(avatar);

            radioGroup.removeAllViews();
            if (!mRecord.getAllVotes().contains(LogInActivity.USER_NAME)) {
                for (int i = 0; i < record.getOptions().size(); i++) {
                    radioGroup.addView(QuizViewBuilder.createBaseOption(mContext, record.getOptions().get(i), i));
                }
            } else {
                int allVotesCount = mRecord.getAllVotes().size();
                for (Option o: mRecord.getOptions()){
                    radioGroup.addView(QuizViewBuilder.createFinalOption(mContext, o, allVotesCount));
                }
            }

            RecordRecyclerViewAdapter recordRecyclerViewAdapter = new RecordRecyclerViewAdapter(mContext, record.getImages(), mActionListener);
            recyclerView.setAdapter(recordRecyclerViewAdapter);
        }
    }
}
