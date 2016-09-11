package com.android.project.userpage;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.project.R;
import com.android.project.main.MainPresenter;
import com.android.project.main.MainPresenterImpl;
import com.android.project.model.User;
import com.android.project.util.RecordServiceImpl;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserPageActivity extends AppCompatActivity implements MainPresenter.View {

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
        mActionListener.loadUserInfo(getIntent().getExtras().getString(getString(R.string.user_name_opened_page)));
    }

    @Override
    public void showUserInfo(User user) {
        collapsingToolbar.setTitle(user.getName());

        Picasso.with(this)
                .load(RecordServiceImpl.BASE_URL + user.getAvatar())
                .placeholder(R.mipmap.ic_launcher)
                .into(avatar);

        Picasso.with(this)
                .load(RecordServiceImpl.BASE_URL + user.getBackground())
                .placeholder(R.drawable.background_top)
                .into(background);
    }
}
