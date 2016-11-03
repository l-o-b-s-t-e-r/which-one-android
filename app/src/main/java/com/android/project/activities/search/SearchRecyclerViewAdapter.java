package com.android.project.activities.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.User;
import com.android.project.util.ImageManager;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 01.08.16.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = SearchRecyclerViewAdapter.class.getSimpleName();

    private List<User> mUsers;
    private String mSearchQuery;
    private SearchPresenter.ActionListener mActionListener;

    public SearchRecyclerViewAdapter(String searchQuery, SearchPresenter.ActionListener actionListener) {
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
            mActionListener.loadNextUsers(mSearchQuery, mUsers.get(position).getUsername());
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

        @BindView(R.id.username)
        TextView username;

        @BindView(R.id.avatar)
        ImageView avatar;

        private User mUser;
        private Target mAvatarTarget;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mAvatarTarget = ImageManager.getInstance().createTarget(avatar);
        }

        @OnClick({R.id.avatar, R.id.username})
        void onUserClick() {
            mActionListener.loadUserPage(mUser);
        }

        public void setContent(User user) {
            mUser = user;

            username.setText(mUser.getUsername());
            WhichOneApp.getPicasso()
                    .load(ImageManager.IMAGE_URL + mUser.getAvatar())
                    .into(mAvatarTarget);
        }
    }
}
