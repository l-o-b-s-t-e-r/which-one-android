package com.android.project.homewall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.model.Record;
import com.android.project.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallRecyclerViewAdapter extends RecyclerView.Adapter<HomeWallRecyclerViewAdapter.ViewHolder> {

    private Long MIN_ANIMATION_DURATION = 3000L;
    private int DELTA = 1000;
    private boolean allRecordsLoaded;

    private Context mContext;
    private List<Long> mRecordIds;
    private Random mAnimationRandom;
    private HomeWallPresenter.ActionListener mActionListener;

    public HomeWallRecyclerViewAdapter(Context context, HomeWallPresenter.ActionListener actionListener) {
        mContext = context;
        mRecordIds = new ArrayList<>();
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
        Record record = mActionListener.getRecordById(mRecordIds.get(position));
        holder.setContent(record);
        if (!allRecordsLoaded && position == mRecordIds.size() - 1) {
            mActionListener.loadNextRecords(record.getUsername(), mRecordIds.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mRecordIds.size();
    }

    public void updateData(List<Long> recordIds) {
        if (!recordIds.isEmpty()) {
            mRecordIds.addAll(recordIds);
            notifyDataSetChanged();
        } else {
            allRecordsLoaded = true;
        }
    }

    private Long animationDuration(int imageCount) {
        return MIN_ANIMATION_DURATION + MIN_ANIMATION_DURATION / imageCount + mAnimationRandom.nextInt(DELTA);
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

                        String imagePath = mRecord.getImages().get(currentAnimatedImage).getImage();
                        Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
                        if (bitmapImage != null) {
                            image.setImageBitmap(bitmapImage);
                        }
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

            String imagePath = mRecord.getImages().get(currentAnimatedImage).getImage();
            Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);

            if (bitmapImage == null) {
                Subscriber<Bitmap> subscriber = new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                        image.startAnimation(mAnimation);
                        spinner.setVisibility(View.GONE);
                    }
                };

                ImageLoader.getInstance().addImageSubscriber(imagePath, subscriber);
            } else {
                image.setImageBitmap(bitmapImage);
                image.startAnimation(mAnimation);
                spinner.setVisibility(View.GONE);
            }
        }
    }
}
