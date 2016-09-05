package com.android.project.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.cofig.DatabaseHelper;
import com.android.project.main.MainActivity;
import com.android.project.model.UserApp;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements SignInPresenter.View {

    //public static final String USER_NAME = "person";
    public static final String USER_NAME = "sanfran";
    //public static final String USER_NAME = "tvShowBB";
    //public static final String USER_NAME = "tvShowGT";
    protected static final String TAG = SignInActivity.class.getSimpleName();
    @BindView(R.id.editTextName)
    EditText name;
    @BindView(R.id.editTextPassword)
    EditText password;
    private DatabaseHelper mDatabaseHelper = null;
    private SignInPresenter.ActionListener mActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        ButterKnife.bind(this);

        mActionListener = new SignInPresenterImpl(this);
    }

    @OnClick(R.id.btn_sign_in)
    void onClick() {
        mActionListener.signIn(name.getText().toString(), password.getText().toString());
    }

    private DatabaseHelper mGetHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return mDatabaseHelper;
    }

    public void addUser(View view) {
        Random r = new Random();

        UserApp userApp = new UserApp();
        userApp.setmUserName("user_name");
        userApp.setmUserPassword("password_" + r.nextInt(10));
        Log.d(userApp.getmUserName(), userApp.getmUserPassword());

        try {
            Dao<UserApp, Integer> userDao = mGetHelper().getUserDao();
            Log.d("SIZE", String.valueOf(userDao.countOf()));
            userDao.create(userApp);
        }catch (SQLException e){
            e.printStackTrace();
        }


        Toast.makeText(this, view.getClass().getSimpleName(), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void openUserPage(Integer requestCode) {
        if (requestCode == HttpURLConnection.HTTP_OK) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USER_NAME, name.getText().toString());
            startActivity(intent);
        } else {
            Log.i(TAG, "BAD");
        }
    }
}
