package com.android.project.userpage;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.project.R;
import com.android.project.main.MainPresenter;
import com.android.project.main.MainPresenterImpl;
import com.android.project.model.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserPageActivity extends AppCompatActivity implements MainPresenter.View {

    public static final String USER_NAME = "USER_NAME";
    private static final String TAG = UserPageActivity.class.getName();
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    private MainPresenter.ActionListener mActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        ButterKnife.bind(this);
        collapsingToolbar.setTitle("");

        mActionListener = new MainPresenterImpl(this);
        mActionListener.loadUserInfo(getIntent().getExtras().getString(USER_NAME));
    }

    @Override
    public void showUserInfo(User user) {
        collapsingToolbar.setTitle(user.getName());

        Picasso.with(this)
                .load(getString(R.string.base_uri) + user.getAvatar())
                .placeholder(R.mipmap.ic_launcher)
                .into(avatar);

        Picasso.with(this)
                .load(getString(R.string.base_uri) + user.getBackground())
                .placeholder(R.drawable.background_top)
                .into(background);
    }
}
