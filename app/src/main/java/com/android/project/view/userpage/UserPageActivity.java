package com.android.project.view.userpage;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.User;
import com.android.project.util.ImageManager;
import com.android.project.view.main.MainPresenter;
import com.android.project.view.main.di.MainModule;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

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

    @Inject
    MainPresenter.ActionListener presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        ButterKnife.bind(this);
        collapsingToolbar.setTitle("");

        WhichOneApp.getUserComponent()
                .plus(new MainModule(this))
                .inject(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        presenter.loadUserInfo(getIntent().getExtras().getString(getString(R.string.user_name_opened_page)));
    }

    @Override
    public void showUserInfo(User user) {
        collapsingToolbar.setTitle(user.getUsername());

        updateAvatar(user);
        updateBackground(user);
    }

    @Override
    public void updateAvatar(User user) {
        WhichOneApp.createUserComponent(user);

        Glide.with(WhichOneApp.getContext())
                .load(ImageManager.IMAGE_URL + user.getAvatar())
                .asBitmap()
                .into(ImageManager.getInstance().createTarget(
                        Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL, avatar
                ));
    }

    @Override
    public void updateBackground(User user) {
        WhichOneApp.createUserComponent(user);

        Glide.with(WhichOneApp.getContext())
                .load(ImageManager.IMAGE_URL + user.getBackground())
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
        presenter.onStop();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void signOut() {

    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }
}
