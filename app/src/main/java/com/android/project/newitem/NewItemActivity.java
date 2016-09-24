package com.android.project.newitem;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.project.R;
import com.android.project.cofig.WhichOneApp;
import com.android.project.main.MainActivity;
import com.android.project.util.ImageKeeper;
import com.android.project.util.QuizViewBuilder;

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

    @BindView(R.id.options_container)
    LinearLayout container;

    private NewItemRecyclerViewAdapter mRecyclerViewAdapter;
    private NewItemPresenter.ActionListener mActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActionListener = new NewItemPresenterImpl(this, ((WhichOneApp) getApplication()).getMainComponent());

        mRecyclerViewAdapter = new NewItemRecyclerViewAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.new_item_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(mRecyclerViewAdapter);

    }

    @OnClick(R.id.done)
    void postRecord() {
        if (mRecyclerViewAdapter.getItemCount() < 1 || container.getChildCount() < 1) {
            return; //show message
        }

        String optionTitle;
        List<String> allOptions = new ArrayList<>();

        for (int i = 0; i < container.getChildCount(); i++) {
            EditText option = (EditText) container.getChildAt(i).findViewById(R.id.option_title);
            optionTitle = option.getText().toString().trim();
            if (!optionTitle.isEmpty() && !allOptions.contains(optionTitle)) {
                allOptions.add(optionTitle);
            } else {
                return; //show message
            }
        }

        mActionListener.sendRecord(
                mRecyclerViewAdapter.getAllImages(),
                allOptions,
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.user_name), ""));
    }

    @Override
    public void loadMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void setImage(File picturePath) {
        mRecyclerViewAdapter.addItem(picturePath);
        container.addView(QuizViewBuilder.createNewOption(this, container));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == LOAD_IMAGE) {
            if (data.getData() == null) {
                Log.i(TAG, "IMAGE is LOADED (from camera)");
                setImage(ImageKeeper.getInstance().getImageFile());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_record, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_image:
                if (mRecyclerViewAdapter.getItemCount() < MAX_IMAGES) {
                    startActivityForResult(ImageKeeper.getInstance().getChooserIntent(this), LOAD_IMAGE);
                }
                break;
            case R.id.action_add_option:
                if (container.getChildCount() < MAX_OPTIONS) {
                    container.addView(QuizViewBuilder.createNewOption(this, container));
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
