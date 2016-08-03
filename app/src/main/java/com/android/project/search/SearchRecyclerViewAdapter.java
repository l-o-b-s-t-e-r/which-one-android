package com.android.project.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.model.User;
import com.android.project.util.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 01.08.16.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private String mSearchQuery;
    private SearchPresenter.ActionListener mActionListener;

    public SearchRecyclerViewAdapter(Context context, String searchQuery, SearchPresenter.ActionListener actionListener) {
        mContext = context;
        mSearchQuery = searchQuery;
        mActionListener = actionListener;
        mUsers = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_header, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mUsers.get(position));
        if (position == mUsers.size() - 1) {
            mActionListener.loadNextUsers(mSearchQuery, mUsers.get(position).getId());
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void updateData(List<User> users) {
        if (!users.isEmpty()) {
            mUsers.addAll(users);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView mText;

        @BindView(R.id.avatar)
        SquareImageView avatar;

        private User mUser;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.avatar, R.id.title})
        void onUserClick() {
            mActionListener.loadUserPage(mUser);
        }

        public void setContent(User user) {
            mUser = user;

            mText.setText(mUser.getName());
            Picasso.with(mContext)
                    .load(mContext.getString(R.string.base_uri) + mUser.getAvatar())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(avatar);
        }


    }
}
