package com.android.project.homewall;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.project.R;
import com.android.project.model.Record;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallRecyclerViewAdapter extends RecyclerView.Adapter<HomeWallRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Record> mRecords;
    private HomeWallPresenter.ActionListener mActionListener;

    public HomeWallRecyclerViewAdapter(Context context, HomeWallPresenter.ActionListener actionListener) {
        mContext = context;
        mRecords = new ArrayList<>();
        mActionListener = actionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homewall_card, parent, false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mRecords.get(position));
        if (position == mRecords.size() - 1) {
            mActionListener.loadNextRecords(mRecords.get(position).getTitle(), mRecords.get(position).getRecordId());
        }
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public void updateData(List<Record> records) {
        if (!records.isEmpty()) {
            mRecords.addAll(records);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;

        private Record mRecord;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }

        @OnClick(R.id.image)
        public void onImageClick() {
            mActionListener.loadRecordDetail(mRecord.getRecordId());
        }

        public void setContent(Record record) {
            mRecord = record;

            Picasso.with(mContext)
                    .load(mContext.getString(R.string.base_uri) + record.getImages().get(0).getImage())
                    .placeholder(R.drawable.background_top)
                    .into(image);
        }

    }
}
