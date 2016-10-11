package com.android.project.detail;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.adapter.RecordRecyclerViewAdapter;
import com.android.project.model.Image;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lobster on 25.06.16.
 */

public class RecordDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecordDetailRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecordRecyclerViewAdapter.class.getName();
    private Context mContext;
    private List<Image> mImages;

    public RecordDetailRecyclerViewAdapter(Context context) {
        mContext = context;
        mImages = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_image, parent, false);

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
        ImageView imageView;
        @BindView(R.id.progressBar)
        ProgressBar spinner;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setContent(final Image image) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(image.getImage()));
        }
    }
}
