package com.android.project.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.android.project.R;
import com.android.project.adapter.TabAdapter;
import com.android.project.homewall.HomeWallFragment;
import com.android.project.login.LogInActivity;
import com.android.project.model.User;
import com.android.project.newitem.NewItemActivity;
import com.android.project.util.PictureLoader;
import com.android.project.util.PictureLoaderImpl;
import com.android.project.util.RecordServiceImpl;
import com.android.project.wall.WallFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainPresenter.View{

    private static final String TAG = MainActivity.class.getName();
    private static final int UPDATE_AVATAR = 1;
    private static final int UPDATE_BACKGROUND = 2;

    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.avatar)
    ImageView avatar;

    private PictureLoader mPictureLoader;
    private MainPresenter.ActionListener mActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        mPictureLoader = new PictureLoaderImpl(getString(R.string.album_name));
        mActionListener = new MainPresenterImpl(new RecordServiceImpl(getString(R.string.base_uri)), this);
        mActionListener.loadUserInfo(LogInActivity.USER_NAME);


        FragmentManager fragmentManager = getSupportFragmentManager();

        ViewPager viewPager = ButterKnife.findById(this, R.id.viewPager);
        TabLayout tabLayout = ButterKnife.findById(this, R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        List<Fragment> fragments = Arrays.asList(
                                        WallFragment.newInstance(tabLayout),
                                        HomeWallFragment.newInstance(LogInActivity.USER_NAME, tabLayout)
                                    );

        FragmentStatePagerAdapter tabAdapter = new TabAdapter(fragmentManager, fragments, Arrays.asList("WALL","HOME"));
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = ButterKnife.findById(this, R.id.fab);
        if (fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, NewItemActivity.class));
                }
            });
        }
    }

    @OnClick(R.id.avatar)
    void updateAvatar(){
        startActivityForResult(mPictureLoader.getChooserIntent(), UPDATE_AVATAR);
    }

    @OnClick(R.id.load_background)
    void updateBackground(){
        startActivityForResult(mPictureLoader.getChooserIntent(), UPDATE_BACKGROUND);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getData() == null){
                Log.i(TAG, "IMAGE is LOADED (from camera)");
                sendBroadcast(mPictureLoader.addPictureToGallery());
                switch (requestCode){
                    case UPDATE_AVATAR:
                        mActionListener.updateAvatar(mPictureLoader.getPictureFile(), LogInActivity.USER_NAME); break;
                    case UPDATE_BACKGROUND:
                        mActionListener.updateBackground(mPictureLoader.getPictureFile(), LogInActivity.USER_NAME); break;

                    default: break;
                }

            } else {
                Log.i(TAG, "IMAGE is LOADED (from gallery)");
                String[] picturePath = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(data.getData(), picturePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(picturePath[0]));
                switch (requestCode){
                    case UPDATE_AVATAR:
                        mActionListener.updateAvatar(new File(imagePath), LogInActivity.USER_NAME); break;
                    case UPDATE_BACKGROUND:
                        mActionListener.updateBackground(new File(imagePath), LogInActivity.USER_NAME); break;

                    default: break;
                }
            }
        }
    }

    @Override
    public void showUserInfo(User user) {
        Picasso.with(this)
                .load(getString(R.string.base_uri)+user.getAvatar())
                .placeholder(R.mipmap.ic_launcher)
                .into(avatar);

        Picasso.with(this)
                .load(getString(R.string.base_uri)+user.getBackground())
                .placeholder(R.drawable.background_top)
                .into(background);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_record_detail, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

}
