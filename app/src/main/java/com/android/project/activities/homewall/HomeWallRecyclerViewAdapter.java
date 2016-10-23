package com.android.project.activities.homewall;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.activities.wall.WallRecyclerViewAdapter;
import com.android.project.model.Record;
import com.android.project.util.ImageManager;
import com.squareup.picasso.Callback;

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

    private static final String TAG = WallRecyclerViewAdapter.class.getSimpleName();
    private final Long MIN_ANIMATION_DURATION = 3000L;
    private final int DELTA = 1000;
    private boolean allRecordsLoaded;

    private List<Record> mRecords;
    private Random mAnimationRandom;
    private HomeWallPresenter.ActionListener mActionListener;

    public HomeWallRecyclerViewAdapter(HomeWallPresenter.ActionListener actionListener) {
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
        Record record = mRecords.get(position);
        holder.setContent(record);

        if (!allRecordsLoaded && position == mRecords.size() - 1) {
            mActionListener.loadNextRecords(record.getUsername(), record.getRecordId());
        }
    }

    public void updateData(List<Record> records) {
        if (!records.isEmpty()) {
            mRecords.addAll(records);
            notifyDataSetChanged();
        } else {
            allRecordsLoaded = true;
        }
    }

    public void cleanData() {
        mRecords.clear();
        notifyDataSetChanged();
    }

    private Long animationDuration(int imageCount) {
        return MIN_ANIMATION_DURATION + MIN_ANIMATION_DURATION / imageCount + mAnimationRandom.nextInt(DELTA);
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.progressBar)
        ProgressBar spinner;

        private Record mRecord;
        private Animation mAnimation;
        private int currentAnimatedImage;
        private boolean changeImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            mAnimation = AnimationUtils.loadAnimation(WhichOneApp.getContext(), R.anim.items_appearance);
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

                        WhichOneApp.getPicasso()
                                .load(ImageManager.IMAGE_URL + mRecord.getImages().get(currentAnimatedImage).getImage())
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
            Log.i(TAG, "setContent: record - " + record.toString());

            mRecord = record;
            currentAnimatedImage = 0;

            mAnimation.setDuration(animationDuration(mRecord.getImages().size()));

            WhichOneApp.getPicasso()
                    .load(ImageManager.IMAGE_URL + mRecord.getImages().get(currentAnimatedImage).getImage())
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            spinner.setVisibility(View.GONE);
                            image.startAnimation(mAnimation);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }
}
