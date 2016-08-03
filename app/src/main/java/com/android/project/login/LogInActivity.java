package com.android.project.login;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.model.UserApp;
import com.android.project.cofig.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;

import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {

    //public static final String USER_NAME = "person";
    public static final String USER_NAME = "sanfran";
    //public static final String USER_NAME = "tvShowBB";
    //public static final String USER_NAME = "tvShowGT";


    protected static final String TAG = LogInActivity.class.getSimpleName();
    private DatabaseHelper mDatabaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
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

}
