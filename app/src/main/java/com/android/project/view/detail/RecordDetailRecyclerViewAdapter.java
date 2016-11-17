package com.android.project.view.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.project.R;
import com.android.project.model.Image;
import com.android.project.util.ImageManager;
import com.android.project.view.wall.RecordRecyclerViewAdapter;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lobster on 25.06.16.
 */

public class RecordDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecordDetailRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecordRecyclerViewAdapter.class.getSimpleName();
    private List<Image> mImages;
    private RequestManager mGlide;

    @Inject
    public RecordDetailRecyclerViewAdapter(RequestManager glide) {
        mImages = new ArrayList<>();
        mGlide = glide;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_layout, parent, false);

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

    public void updateData(List<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.record_image)
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setContent(Image image) {
            mGlide.load(ImageManager.IMAGE_URL + image.getImage())
                    .into(this.image);
        }
    }
}
