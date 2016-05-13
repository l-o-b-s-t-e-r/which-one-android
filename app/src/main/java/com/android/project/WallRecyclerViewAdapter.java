package com.android.project;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by macos on 12.05.16.
 */
public class WallRecyclerViewAdapter extends RecyclerView.Adapter<WallRecyclerViewAdapter.ViewHolder> {

    private List<String> mCardContents;
    private Context mContext;

    public WallRecyclerViewAdapter(List<String> cardContents, Context context) {
        mCardContents = cardContents;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mCardContents.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mCardContents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView mText;

        public ViewHolder(CardView cardView) {
            super(cardView);

            mCardView = cardView;
            mText = (TextView) cardView.findViewById(R.id.text);
        }

        public void setContent(String cardContent, final int position) {
            mText.setText(cardContent);
        }
    }
}
