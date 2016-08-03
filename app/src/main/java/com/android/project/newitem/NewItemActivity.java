package com.android.project.newitem;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.project.R;
import com.android.project.login.LogInActivity;
import com.android.project.main.MainActivity;
import com.android.project.util.PictureLoader;
import com.android.project.util.PictureLoaderImpl;
import com.android.project.util.QuizViewBuilder;
import com.android.project.util.RecordServiceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewItemActivity extends AppCompatActivity implements NewItemPresenter.View {

    private final String TAG = NewItemActivity.class.getName();
    private final int LOAD_IMAGE = 1;
    private final int MAX_IMAGES = 9;
    private final int MAX_OPTIONS = 9;
    private final String OPTION_TITLE = "Option #";
    @BindView(R.id.options_container)
    LinearLayout container;
    private PictureLoader mPictureLoader;
    private NewItemRecyclerViewAdapter mRecyclerViewAdapter;
    private NewItemPresenter.ActionListener mActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_activity);
        ButterKnife.bind(this);

        mPictureLoader = new PictureLoaderImpl(getString(R.string.album_name));
        mActionListener = new NewItemPresenterImpl(new RecordServiceImpl(getString(R.string.base_uri)), this);

        mRecyclerViewAdapter = new NewItemRecyclerViewAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.new_item_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(mRecyclerViewAdapter);

    }

    @OnClick(R.id.btn_post)
    void postRecord() {
        String optionTitle;
        List<String> allOptions = new ArrayList<>();

        for (int i = 0; i < container.getChildCount(); i++) {
            EditText option = (EditText) container.getChildAt(i).findViewById(R.id.option_title);
            optionTitle = option.getText().toString();
            if (!allOptions.contains(optionTitle)) {
                allOptions.add(optionTitle);
            } else {
                return;
            }
        }

        mActionListener.sendRecord(mRecyclerViewAdapter.getAllImages(), allOptions, LogInActivity.USER_NAME);
    }

    @OnClick(R.id.add_option)
    void addOption() {
        if (container.getChildCount() < MAX_OPTIONS) {
            container.addView(QuizViewBuilder.createNewOption(this, container, OPTION_TITLE + mRecyclerViewAdapter.getItemCount()));
        }
    }

    @OnClick(R.id.add_image)
    void addImage() {
        if (mRecyclerViewAdapter.getItemCount() < MAX_IMAGES) {
            startActivityForResult(mPictureLoader.getChooserIntent(), LOAD_IMAGE);
        }
    }

    @Override
    public void loadMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void setImage(File picturePath) {
        mRecyclerViewAdapter.addItem(picturePath);
        container.addView(QuizViewBuilder.createNewOption(this, container, OPTION_TITLE + (mRecyclerViewAdapter.getItemCount() - 1)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == LOAD_IMAGE) {
            if (data.getData() == null) {
                Log.i(TAG, "IMAGE is LOADED (from camera)");
                sendBroadcast(mPictureLoader.addPictureToGallery());
                setImage(mPictureLoader.getPictureFile());
            } else {
                Log.i(TAG, "IMAGE is LOADED (from gallery)");
                String[] picturePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), picturePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(picturePath[0]));
                setImage(new File(imagePath));
            }
        }
    }
}
