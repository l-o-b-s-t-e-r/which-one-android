package com.android.project.homewall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.project.R;
import com.android.project.model.Record;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallRecyclerViewAdapter extends RecyclerView.Adapter<HomeWallRecyclerViewAdapter.ViewHolder> {

    private Long MIN_ANIMATION_DURATION = 3000L;
    private int DELTA = 1000;

    private Context mContext;
    private List<Record> mRecords;
    private Random mAnimationRandom;
    private HomeWallPresenter.ActionListener mActionListener;

    public HomeWallRecyclerViewAdapter(Context context, HomeWallPresenter.ActionListener actionListener) {
        mContext = context;
        mRecords = new ArrayList<>();
        mAnimationRandom = new Random();
        mActionListener = actionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homewall_card, parent, false);

        return new ViewHolder(view);
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

    private Long animationDuration(int imageCount) {
        return MIN_ANIMATION_DURATION + MIN_ANIMATION_DURATION / imageCount + mAnimationRandom.nextInt(DELTA);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;

        private Record mRecord;
        private Animation mAnimation;
        private int currentAnimatedImage;
        private boolean changeImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.items_appearance);
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    if (changeImage) {
                        if (currentAnimatedImage < mRecord.getImages().size() - 1) {
                            currentAnimatedImage++;
                        } else {
                            currentAnimatedImage = 0;
                        }

                        Picasso.with(mContext)
                                .load(mContext.getString(R.string.base_uri) + mRecord.getImages().get(currentAnimatedImage).getImage())
                                .into(image);
                    }

                    changeImage = !changeImage;
                }
            });
        }

        @OnClick(R.id.image)
        public void onImageClick() {
            mActionListener.loadRecordDetail(mRecord.getRecordId());
        }

        public void setContent(Record record) {
            mRecord = record;
            mAnimation.setDuration(animationDuration(record.getImages().size()));
            //Log.i("ANIMATION DURATION", String.valueOf(mAnimation.getDuration()));

            Picasso.with(mContext)
                    .load(mContext.getString(R.string.base_uri) + mRecord.getImages().get(currentAnimatedImage).getImage())
                    .placeholder(R.drawable.background_top)
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            image.startAnimation(mAnimation);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }
}
