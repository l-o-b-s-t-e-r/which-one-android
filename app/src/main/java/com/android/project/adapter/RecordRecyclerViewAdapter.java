package com.android.project.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.model.Image;
import com.android.project.util.ImageLoader;
import com.android.project.wall.WallPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 21.05.16.
 */

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecordRecyclerViewAdapter.class.getName();
    private List<Image> mImages;
    private WallPresenter.ActionListener mActionListener;

    public RecordRecyclerViewAdapter(List<Image> images, WallPresenter.ActionListener actionListener) {
        mImages = images;
        mActionListener = actionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.record_image)
        ImageView imageView;
        @BindView(R.id.progressBar)
        ProgressBar spinner;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.record_image)
        public void onImageClick() {
            mActionListener.openRecordDetail(mImages.get(0).getRecordId());
        }

        public void setContent(Image image) {

            Bitmap bitmapImage = BitmapFactory.decodeFile(image.getImage());
            if (bitmapImage == null) {
                ImageLoader.getInstance().pushImage(image.getImage(), imageView, spinner, null);
            } else {
                imageView.setImageBitmap(bitmapImage);
                spinner.setVisibility(View.GONE);
            }
        }
    }
}
