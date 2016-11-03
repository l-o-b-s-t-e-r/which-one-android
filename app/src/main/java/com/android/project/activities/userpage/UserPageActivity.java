package com.android.project.activities.userpage;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.activities.main.MainPresenter;
import com.android.project.activities.main.MainPresenterImpl;
import com.android.project.model.User;
import com.android.project.util.ImageManager;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserPageActivity extends AppCompatActivity implements MainPresenter.View {

    private static final String TAG = UserPageActivity.class.getSimpleName();

    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MainPresenter.ActionListener mActionListener;
    private Target mAvatarTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        ButterKnife.bind(this);
        collapsingToolbar.setTitle("");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mActionListener = new MainPresenterImpl(this);
        mActionListener.loadUserInfo(getIntent().getExtras().getString(getString(R.string.user_name_opened_page)));
    }

    @Override
    public void showUserInfo(User user) {
        collapsingToolbar.setTitle(user.getUsername());

        mAvatarTarget = ImageManager.getInstance().createTarget(avatar);

        WhichOneApp.getPicasso()
                .load(ImageManager.IMAGE_URL + user.getAvatar())
                .error(R.drawable.logo)
                .into(mAvatarTarget);

        WhichOneApp.getPicasso()
                .load(ImageManager.IMAGE_URL + user.getBackground())
                .error(R.drawable.background_top)
                .into(background);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActionListener.onStop();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
