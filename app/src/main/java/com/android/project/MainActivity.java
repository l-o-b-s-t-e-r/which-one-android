package com.android.project;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseHelper mDatabaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        Button buttonSignIn = (Button) findViewById(R.id.btn_sign_in);
        buttonSignIn.setOnClickListener(this);

        //MyClass myClass = new MyClass();
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
        userApp.setmUserName("Vlad");
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
    public void onClick(View v) {
        new RegisterUser().execute();
    }

    private class RegisterUser extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String urlString = Uri.parse(getString(R.string.base_uri)).buildUpon()
                    .appendPath("get_user")
                    .build().toString();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return "null";
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return "Ups";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG)
                    .show();
        }
    }

}
