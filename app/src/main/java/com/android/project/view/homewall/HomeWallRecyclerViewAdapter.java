package com.android.project.view.homewall;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.Record;
import com.android.project.model.User;
import com.android.project.util.ImageManager;
import com.bumptech.glide.RequestManager;
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

    private final long MIN_ANIMATION_DURATION = 3000L;
    private final int DELTA = 1000;
    private boolean allRecordsLoaded;

    private List<Record> mRecords = new ArrayList<>();
    private Random mAnimationRandom = new Random();
    private HomeWallPresenter.ActionListener mPresenter;
    private String mTargetUsername;
    private RequestManager mGlide;

    @Inject
    public HomeWallRecyclerViewAdapter(HomeWallPresenter.ActionListener presenter, User user, RequestManager glide) {
        mPresenter = presenter;
        mTargetUsername = user.getUsername();
        mGlide = glide;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homewall_record_layout, parent, false);

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

        @BindView(R.id.record_image)
        ImageView image;

        private Record mRecord;
        private Animator mAnimation;
        private int mCurrentAnimatedImage;
        private boolean mChangeImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            mAnimation = AnimatorInflater.loadAnimator(WhichOneApp.getContext(), R.animator.image_appearance);
            mAnimation.setTarget(image);
            mAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    if (mChangeImage) {
                        if (mCurrentAnimatedImage < mRecord.getImages().size() - 1) {
                            mCurrentAnimatedImage++;
                        } else {
                            mCurrentAnimatedImage = 0;
                        }

                        mGlide.load(ImageManager.IMAGE_URL + mRecord.getImages().get(mCurrentAnimatedImage).getImage())
                                .into(image);
                    }

                    mChangeImage = !mChangeImage;
                }

                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                }

                public void onAnimationCancel(Animator animation) {
                }
            });
        }

        @OnClick(R.id.record_image)
        public void onImageClick() {
            mPresenter.loadRecordDetail(mRecord.getRecordId());
        }

        public void setContent(Record record) {
            Log.i(TAG, "setContent: record - " + record.toString());

            mRecord = record;
            mCurrentAnimatedImage = 0;
            mChangeImage = true;

            mAnimation.setDuration(animationDuration(mRecord.getImages().size()));

            mGlide.load(ImageManager.IMAGE_URL + mRecord.getImages().get(mCurrentAnimatedImage).getImage())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mAnimation.start();
                            return false;
                        }
                    })
                    .into(image);
        }
    }
}
