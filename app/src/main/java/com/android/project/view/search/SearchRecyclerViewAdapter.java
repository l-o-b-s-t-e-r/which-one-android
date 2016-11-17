package com.android.project.view.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.model.User;
import com.android.project.util.ImageManager;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    private SearchPresenter.ActionListener mPresenter;
    private RequestManager mGlide;

    @Inject
    public SearchRecyclerViewAdapter(SearchPresenter.ActionListener actionListener, RequestManager glide) {
        mPresenter = actionListener;
        mGlide = glide;
        mUsers = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.short_user_info_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mUsers.get(position));
        if (position == mUsers.size() - 1) {
            mPresenter.loadNextUsers(mSearchQuery, mUsers.get(position).getUsername());
        }
    }

    public void setSearchQuery(String searchQuery) {
        mSearchQuery = searchQuery;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void updateData(List<User> users) {
        if (!users.isEmpty()) {
            mUsers.addAll(users);
            notifyItemRangeInserted(mUsers.size() - users.size(), users.size());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.avatar)
        ImageView avatar;

        private User mUser;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.avatar, R.id.username})
        void onUserClick() {
            mPresenter.loadUserPage(mUser);
        }

        public void setContent(User user) {
            mUser = user;

            username.setText(mUser.getUsername());

            mGlide.load(ImageManager.IMAGE_URL + user.getAvatar())
                    .asBitmap()
                    .into(ImageManager.getInstance().createTarget(
                            ImageManager.SMALL_AVATAR_SIZE, ImageManager.SMALL_AVATAR_SIZE, avatar
                    ));
        }
    }
}
