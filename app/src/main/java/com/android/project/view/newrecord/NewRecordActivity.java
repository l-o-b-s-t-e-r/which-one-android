package com.android.project.view.newrecord;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.util.ImageKeeper;
import com.android.project.util.QuizViewBuilder;
import com.android.project.view.main.MainActivity;
import com.android.project.view.newrecord.di.NewRecordModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewRecordActivity extends AppCompatActivity implements NewRecordPresenter.View {

    private final String TAG = NewRecordActivity.class.getSimpleName();
    private final int LOAD_IMAGE = 1;
    private final int MAX_IMAGES = 5;
    private final int MAX_OPTIONS = 9;

    @BindView(R.id.options_container)
    LinearLayout container;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.new_item_recycler)
    RecyclerView recyclerView;

    @Inject
    NewRecordPresenter.ActionListener presenter;
    @Inject
    NewRecordRecyclerViewAdapter recyclerViewAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        ButterKnife.bind(this);
        WhichOneApp.getUserComponent()
                .plus(new NewRecordModule(this))
                .inject(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @OnClick(R.id.done)
    void sendRecord() {
        if (recyclerViewAdapter.getItemCount() < 1 || container.getChildCount() < 1) {
            Toast.makeText(this, getString(R.string.new_record_empty_record), Toast.LENGTH_SHORT).show();
            return;
        }

        String optionName;
        List<String> allOptions = new ArrayList<>();

        for (int i = 0; i < container.getChildCount(); i++) {
            EditText option = (EditText) container.getChildAt(i).findViewById(R.id.option_name);
            optionName = option.getText().toString().trim();
            if (optionName.isEmpty() || allOptions.contains(optionName)) {
                Toast.makeText(this, getString(R.string.new_record_empty_or_duplicate_option), Toast.LENGTH_SHORT).show();
                return;
            } else {
                allOptions.add(optionName);
            }
        }

        presenter.sendRecord(
                recyclerViewAdapter.getAllImages(),
                allOptions,
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.user_name), ""),
                description.getText().toString().trim());
    }

    @Override
    public void loadMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void showImage(File imageFile) {
        recyclerViewAdapter.addItem(imageFile);
        mLinearLayoutManager.scrollToPosition(recyclerViewAdapter.getItemCount() - 1);
        container.addView(QuizViewBuilder.getInstance().createNewOption(container));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == LOAD_IMAGE) {
            if (data == null || data.getData() == null) {
                Log.i(TAG, "Image has been loaded from camera");
                presenter.loadImage(ImageKeeper.getInstance().getImageFile());
            } else {
                Log.i(TAG, "Image has been loaded from gallery");
                String[] picturePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), picturePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(picturePath[0]));
                presenter.loadImage(new File(imagePath));
                cursor.close();
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
            case R.id.action_add_image:
                if (recyclerViewAdapter.getItemCount() < MAX_IMAGES) {
                    startActivityForResult(ImageKeeper.getInstance().getChooserIntent(this), LOAD_IMAGE);
                } else {
                    Toast.makeText(this, getString(R.string.new_record_max_images), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_add_option:
                if (container.getChildCount() < MAX_OPTIONS) {
                    container.addView(QuizViewBuilder.getInstance().createNewOption(container));
                } else {
                    Toast.makeText(this, getString(R.string.new_record_max_options), Toast.LENGTH_SHORT).show();
                }
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }
}
