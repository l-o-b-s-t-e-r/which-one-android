package com.android.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.project.cofig.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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

        User user = new User();
        user.setmUserName("Vlad");
        user.setmUserPassword("password_" + r.nextInt(10));
        Log.d(user.getmUserName(), user.getmUserPassword());

        try {
            Dao<User, Integer> userDao = mGetHelper().getUserDao();
            Log.d("SIZE", String.valueOf(userDao.countOf()));
            userDao.create(user);
        }catch (SQLException e){
            e.printStackTrace();
        }


        Toast.makeText(this, view.getClass().getSimpleName(), Toast.LENGTH_SHORT)
                .show();
    }
}
