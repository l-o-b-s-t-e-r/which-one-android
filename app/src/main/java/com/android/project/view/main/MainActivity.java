package com.android.project.view.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
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

import org.apache.commons.lang3.text.WordUtils;

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
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    User user;
    @Inject
    MainPresenter.ActionListener presenter;

    private WallFragment mWallFragment;
    private Animation mUpdateAnimation, fabAnimationShowRight, fabAnimationHideRight, fabAnimationShowLeft, fabAnimationHideLeft;
    private CoordinatorLayout.LayoutParams fabLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        WhichOneApp.getUserComponent()
                .plus(new MainModule(this))
                .inject(this);
        showUserInfo(user);

        setSupportActionBar(toolbar);
        collapsingToolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(getAssets(), "Lobster-Regular.ttf"));
        collapsingToolbar.setExpandedTitleTypeface(Typeface.createFromAsset(getAssets(), "Lobster-Regular.ttf"));

        setViewPager();
        setAnimations();

        fabLayoutParams = (CoordinatorLayout.LayoutParams)fab.getLayoutParams();

        fabAnimationHideRight.setAnimationListener(getHideRightAnimationListener());
        fabAnimationHideLeft.setAnimationListener(getHideLeftAnimationListener());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(225, 0, 86), PorterDuff.Mode.SRC_IN);
        }
    }

    @OnClick(R.id.avatar)
    void updateAvatar() {
        startActivityForResult(ImageKeeper.getInstance().getChooserIntent(this), UPDATE_AVATAR);
    }

    @OnClick(R.id.background)
    void updateBackground() {
        startActivityForResult(ImageKeeper.getInstance().getChooserIntent(this), UPDATE_BACKGROUND);
    }

    @OnClick(R.id.fab)
    void onFabClick(){
        startActivity(new Intent(MainActivity.this, NewRecordActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem updateItem = menu.findItem(R.id.action_update);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView updateActionView = (ImageView) inflater.inflate(R.layout.update_icon_layout, null);
        updateActionView.setOnClickListener(view -> {
                    view.startAnimation(mUpdateAnimation);
                    mWallFragment.showSwipeLayoutProgress();
                    mWallFragment.updateWall();
                }
        );

        updateItem.setActionView(updateActionView);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showUserInfo(User user) {
        collapsingToolbar.setTitle(WordUtils.capitalize(user.getUsername()));

        updateAvatar(user);
        updateBackground(user);
    }

    @Override
    public void updateAvatar(User user) {
        WhichOneApp.createUserComponent(user);

        Glide.with(this)
                .load(ImageManager.IMAGE_URL + user.getAvatar())
                .asBitmap()
                .into(ImageManager.getInstance().createTarget(
                        Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL, avatar
                ));
    }

    @Override
    public void updateBackground(User user) {
        WhichOneApp.createUserComponent(user);

        Glide.with(this)
                .load(ImageManager.IMAGE_URL + user.getBackground())
                .placeholder(R.mipmap.ic_background)
                .into(background);
    }

    @Override
    public void signOut() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
        WhichOneApp.releaseUserComponent();

        startActivity(new Intent(this, SignInActivity.class));
        finish();
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
    protected void onStop() {
        super.onStop();
        presenter.stop();
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

    private void setViewPager(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        mWallFragment = new WallFragment();
        HomeWallFragment homeWallFragment = new HomeWallFragment();
        List<Fragment> fragments = Arrays.asList(
                mWallFragment,
                homeWallFragment
        );

        FragmentStatePagerAdapter tabAdapter = new TabAdapter(fragmentManager, fragments);

        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(getOnPageChangeListener());

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_wall_page));

        TabLayout.Tab homeWallTab = tabLayout.newTab().setIcon(R.mipmap.ic_home_page);
        tabLayout.addTab(homeWallTab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == homeWallTab.getPosition()) {
                    homeWallFragment.loadRecords();
                    tabLayout.removeOnTabSelectedListener(this);
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setAnimations(){
        mUpdateAnimation = AnimationUtils.loadAnimation(this, R.anim.update);

        fabAnimationShowRight = AnimationUtils.loadAnimation(this, R.anim.fab_show_changing_right);
        fabAnimationHideRight = AnimationUtils.loadAnimation(this, R.anim.fab_hide_changing_right);
        fabAnimationShowLeft = AnimationUtils.loadAnimation(this, R.anim.fab_show_changing_left);
        fabAnimationHideLeft = AnimationUtils.loadAnimation(this, R.anim.fab_hide_changing_left);
    }

    private Animation.AnimationListener getHideRightAnimationListener(){
        return new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                fabLayoutParams.gravity = Gravity.BOTTOM | Gravity.START;
                fab.setLayoutParams(fabLayoutParams);
                fab.startAnimation(fabAnimationShowRight);
            }

            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}

        };
    }

    private Animation.AnimationListener getHideLeftAnimationListener(){
        return new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                fabLayoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                fab.setLayoutParams(fabLayoutParams);
                fab.startAnimation(fabAnimationShowLeft);
            }

            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}

        };
    }

    private ViewPager.OnPageChangeListener getOnPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (fab.getVisibility() == View.GONE) {
                    fab.setVisibility(View.VISIBLE);
                }

                switch (position) {
                    case 0:
                        fab.startAnimation(fabAnimationHideLeft);
                        break;
                    case 1:
                        fab.startAnimation(fabAnimationHideRight);
                        break;
                }
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageScrollStateChanged(int state) {}
        };
    }
}
