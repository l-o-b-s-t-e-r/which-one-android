package com.android.project.view.newrecord;

import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.project.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 23.07.16.
 */

public class NewRecordRecyclerViewAdapter extends RecyclerView.Adapter<NewRecordRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = NewRecordRecyclerViewAdapter.class.getSimpleName();

    private ArrayList<File> mImageFiles;

    @Inject
    public NewRecordRecyclerViewAdapter() {
        mImageFiles = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_item_card, parent, false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mImageFiles.get(position), position);
    }

    public void addItem(File imageFile) {
        mImageFiles.add(imageFile);
        notifyItemInserted(mImageFiles.size());

    }

    public void removeItem(int position) {
        mImageFiles.remove(position);
        notifyItemRemoved(position);
    }

    public List<File> getAllImages() {
        return mImageFiles;
    }

    @Override
    public int getItemCount() {
        return mImageFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;

        private Integer mPosition;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }

        public void setContent(File imageFile, int position) {
            mPosition = position;
            image.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        }

        @OnClick(R.id.delete_image)
        public void onClick() {
            removeItem(mPosition);
        }

    }
}
