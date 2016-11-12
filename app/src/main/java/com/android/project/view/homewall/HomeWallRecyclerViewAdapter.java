package com.android.project.view.homewall;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.Record;
import com.android.project.model.User;
import com.android.project.util.ImageManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallRecyclerViewAdapter extends RecyclerView.Adapter<HomeWallRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = HomeWallRecyclerViewAdapter.class.getSimpleName();
    private final Long MIN_ANIMATION_DURATION = 3000L;
    private final int DELTA = 1000;
    private boolean allRecordsLoaded;

    private List<Record> mRecords = new ArrayList<>();
    private Random mAnimationRandom = new Random();
    private HomeWallPresenter.ActionListener mPresenter;
    private String mTargetUsername;

    @Inject
    public HomeWallRecyclerViewAdapter(HomeWallPresenter.ActionListener presenter, User user) {
        mPresenter = presenter;
        mTargetUsername = user.getUsername();
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
            mPresenter.loadNextRecords(record.getUsername(), record.getRecordId(), mTargetUsername);
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
        allRecordsLoaded = false;
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

                        Glide.with(WhichOneApp.getContext())
                                .load(ImageManager.IMAGE_URL + mRecord.getImages().get(currentAnimatedImage).getImage())
                                .into(image);
                    }

                    changeImage = !changeImage;
                }
            });
        }

        @OnClick(R.id.image)
        public void onImageClick() {
            mPresenter.loadRecordDetail(mRecord.getRecordId());
        }

        public void setContent(Record record) {
            Log.i(TAG, "setContent: record - " + record.toString());

            mRecord = record;
            currentAnimatedImage = 0;
            changeImage = false;

            mAnimation.setDuration(animationDuration(mRecord.getImages().size()));

            Glide.with(WhichOneApp.getContext())
                    .load(ImageManager.IMAGE_URL + mRecord.getImages().get(currentAnimatedImage).getImage())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            image.startAnimation(mAnimation);
                            return false;
                        }
                    })
                    .into(image);
        }
    }
}
