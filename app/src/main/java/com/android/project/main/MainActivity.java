package com.android.project.main;

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

import com.android.project.R;
import com.android.project.adapter.TabAdapter;
import com.android.project.homewall.HomeWallFragment;
import com.android.project.model.User;
import com.android.project.newitem.NewItemActivity;
import com.android.project.signin.SignInActivity;
import com.android.project.util.ImageKeeper;
import com.android.project.util.RequestServiceImpl;
import com.android.project.wall.WallFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private static final String TAG = MainActivity.class.getName();
    private static final int UPDATE_AVATAR = 1;
    private static final int UPDATE_BACKGROUND = 2;

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    private String mUsername;
    private MainPresenter.ActionListener mActionListener;
    private WallFragment mWallFragment;
    private Animation mUpdateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsername = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.user_name), "");
        collapsingToolbar.setTitle(mUsername);

        mActionListener = new MainPresenterImpl(this);
        mActionListener.loadUserInfo(mUsername);


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
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, NewItemActivity.class));
                }
            });
        }

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
            if (data.getData() == null) {
                Log.i(TAG, "IMAGE is LOADED (from camera)");
                switch (requestCode) {
                    case UPDATE_AVATAR:
                        mActionListener.updateAvatar(ImageKeeper.getInstance().getImageFile(), mUsername);
                        break;
                    case UPDATE_BACKGROUND:
                        mActionListener.updateBackground(ImageKeeper.getInstance().getImageFile(), mUsername);
                        break;

                    default:
                        break;
                }

            } else {
                Log.i(TAG, "IMAGE is LOADED (from gallery)");
                String[] picturePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), picturePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(picturePath[0]));
                switch (requestCode) {
                    case UPDATE_AVATAR:
                        mActionListener.updateAvatar(new File(imagePath), mUsername);
                        break;
                    case UPDATE_BACKGROUND:
                        mActionListener.updateBackground(new File(imagePath), mUsername);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void showUserInfo(User user) {
        Picasso.with(this)
                .load(RequestServiceImpl.BASE_URL + RequestServiceImpl.IMAGE_FOLDER + user.getAvatar())
                .placeholder(R.mipmap.ic_launcher)
                .into(avatar);

        Picasso.with(this)
                .load(RequestServiceImpl.BASE_URL + RequestServiceImpl.IMAGE_FOLDER + user.getBackground())
                .placeholder(R.drawable.background_top)
                .into(background);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem updateItem = menu.findItem(R.id.action_update);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ImageView updateActionView = (ImageView) inflater.inflate(R.layout.update, null);
        updateActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(mUpdateAnimation);
                mWallFragment.updateWall();
            }
        });

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
                Intent intent = new Intent(this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .remove(getString(R.string.user_name))
                        .apply();

                startActivity(intent);
                break;
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
