package com.android.project.newitem;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 23.07.16.
 */

public class NewItemRecyclerViewAdapter extends RecyclerView.Adapter<NewItemRecyclerViewAdapter.ViewHolder> {

    private ArrayList<File> mImages;

    public NewItemRecyclerViewAdapter() {
        mImages = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_item_card, parent, false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mImages.get(position), position);
    }

    public void addItem(File picturePath) {
        mImages.add(picturePath);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mImages.remove(position);
        notifyDataSetChanged();
    }

    public List<File> getAllImages() {
        return mImages;
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;

        private int mPosition;

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
