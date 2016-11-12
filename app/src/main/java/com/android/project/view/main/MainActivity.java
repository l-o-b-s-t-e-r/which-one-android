package com.android.project.view.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.User;
import com.android.project.util.ImageKeeper;
import com.android.project.util.ImageManager;
import com.android.project.view.TabAdapter;
import com.android.project.view.homewall.HomeWallFragment;
import com.android.project.view.main.di.MainModule;
import com.android.project.view.newrecord.NewRecordActivity;
import com.android.project.view.signin.SignInActivity;
import com.android.project.view.wall.WallFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int UPDATE_AVATAR = 1;
    private static final int UPDATE_BACKGROUND = 2;

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Inject
    User user;
    @Inject
    MainPresenter.ActionListener presenter;

    private WallFragment mWallFragment;
    private Animation mUpdateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        ButterKnife.bind(this);

        WhichOneApp.getUserComponent()
                .plus(new MainModule(this))
                .inject(this);
        showUserInfo(user);

        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();

        ViewPager viewPager = ButterKnife.findById(this, R.id.viewPager);

        TabLayout tabLayout = ButterKnife.findById(this, R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mWallFragment = (WallFragment) WallFragment.newInstance(tabLayout);
        List<Fragment> fragments = Arrays.asList(
                mWallFragment,
                HomeWallFragment.newInstance(tabLayout)
        );

        FragmentStatePagerAdapter tabAdapter = new TabAdapter(fragmentManager, fragments, Arrays.asList("WALL", "HOME"));
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        final FloatingActionButton fab = ButterKnife.findById(this, R.id.fab);
        final CoordinatorLayout.LayoutParams fabLayoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();

        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, NewRecordActivity.class)));

        mUpdateAnimation = AnimationUtils.loadAnimation(this, R.anim.update);
        final Animation fabAnimationShowRight = AnimationUtils.loadAnimation(this, R.anim.fab_show_changing_right);
        final Animation fabAnimationHideRight = AnimationUtils.loadAnimation(this, R.anim.fab_hide_changing_right);
        final Animation fabAnimationShowLeft = AnimationUtils.loadAnimation(this, R.anim.fab_show_changing_left);
        final Animation fabAnimationHideLeft = AnimationUtils.loadAnimation(this, R.anim.fab_hide_changing_left);

        fabAnimationHideRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fabLayoutParams.gravity = Gravity.BOTTOM | Gravity.START;
                fab.setLayoutParams(fabLayoutParams);
                fab.startAnimation(fabAnimationShowRight);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        fabAnimationHideLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fabLayoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                fab.setLayoutParams(fabLayoutParams);
                fab.startAnimation(fabAnimationShowLeft);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (fab.getVisibility() == View.GONE) {
                    fab.setVisibility(View.VISIBLE);
                }

                switch (position) {
                    case 0:
                        fab.startAnimation(fabAnimationHideLeft);
                        return;
                    case 1:
                        fab.startAnimation(fabAnimationHideRight);
                        return;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick(R.id.avatar)
    void updateAvatar() {
        startActivityForResult(ImageKeeper.getInstance().getChooserIntent(this), UPDATE_AVATAR);
    }

    @OnClick(R.id.background)
    void updateBackground() {
        startActivityForResult(ImageKeeper.getInstance().getChooserIntent(this), UPDATE_BACKGROUND);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
                Log.i(TAG, "Image has been loaded from camera");
                switch (requestCode) {
                    case UPDATE_AVATAR:
                        presenter.updateAvatar(ImageKeeper.getInstance().getImageFile(), user.getUsername());
                        break;
                    case UPDATE_BACKGROUND:
                        presenter.updateBackground(ImageKeeper.getInstance().getImageFile(), user.getUsername());
                        break;

                    default:
                        break;
                }

            } else {
                Log.i(TAG, "Image has been loaded from gallery");
                String[] picturePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), picturePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(picturePath[0]));
                cursor.close();
                switch (requestCode) {
                    case UPDATE_AVATAR:
                        presenter.updateAvatar(new File(imagePath), user.getUsername());
                        break;
                    case UPDATE_BACKGROUND:
                        presenter.updateBackground(new File(imagePath), user.getUsername());
                        break;

                    default:
                        break;
                }
            }
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem updateItem = menu.findItem(R.id.action_update);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView updateActionView = (ImageView) inflater.inflate(R.layout.update, null);
        updateActionView.setOnClickListener(view -> {
                    view.startAnimation(mUpdateAnimation);
                    mWallFragment.showSwipeLayoutProgress();
                    mWallFragment.updateWall();
                }
        );

        updateItem.setActionView(updateActionView);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                presenter.clearDatabase();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void signOut() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
        WhichOneApp.releaseUserComponent();

        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }
}
