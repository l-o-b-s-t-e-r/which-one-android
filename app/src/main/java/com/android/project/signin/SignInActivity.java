package com.android.project.signin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.cofig.DatabaseHelper;
import com.android.project.main.MainActivity;
import com.android.project.model.UserApp;
import com.android.project.signup.SignUpDialog;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements SignInPresenter.View {

    //public static final String USER_NAME = "tvShowBB";
    //public static final String USER_NAME = "tvShowGT";
    protected static final String TAG = SignInActivity.class.getSimpleName();
    //public static final String USER_NAME = "person";
    public static String USER_NAME = "sanfran";
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.forgot_password)
    TextView textViewForgot;

    private DatabaseHelper mDatabaseHelper = null;
    private SignInPresenter.ActionListener mActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        ButterKnife.bind(this);

        mActionListener = new SignInPresenterImpl(this);
    }

    @OnClick(R.id.forgot_password)
    void onForgotTextViewClick() {
        final View view = View.inflate(this, R.layout.remind_dialog, null);

        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppThemeDialogSignUp)
                .setView(view)
                .setTitle("REMIND INFO")
                .create();

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "SEND", new Message());

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextEmail = ButterKnife.findById(view, R.id.email);
                String email = editTextEmail.getText().toString().trim();
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mActionListener.remindInfo(email);
                    dialog.dismiss();
                } else {
                    editTextEmail.setError(getString(R.string.incorrect_email));
                }
            }
        });
    }

    @OnClick(R.id.btn_sign_in)
    void onSignInClick() {
        if (editTextName.getText().toString().trim().isEmpty()) {
            editTextName.setError(getString(R.string.empty_field));
            return;
        }

        if (editTextPassword.getText().toString().trim().isEmpty()) {
            editTextPassword.setError(getString(R.string.empty_field));
            return;
        }

        mActionListener.signIn(editTextName.getText().toString().trim(), editTextPassword.getText().toString().trim());
    }

    @OnClick(R.id.btn_sign_up)
    void onSignUpClick() {
        DialogFragment signUpDialogFragment = new SignUpDialog();
        signUpDialogFragment.show(getSupportFragmentManager(), "sign_up_dialog");
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
            USER_NAME = editTextName.getText().toString().trim();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USER_NAME, editTextName.getText().toString().trim());
            startActivity(intent);
        } else {
            Toast.makeText(SignInActivity.this, "Ups :(", Toast.LENGTH_SHORT).show();
            editTextName.setError(getString(R.string.wrong_password_or_name));
        }
    }

    @Override
    public void remindInfoResult(Integer requestCode) {
        if (requestCode == HttpURLConnection.HTTP_OK) {
            Log.i(TAG, "Info has sent successfully");
            Toast.makeText(this, "Info has sent successfully", Toast.LENGTH_LONG).show();
        } else {
            Log.i(TAG, "Info has not sent. Please check email and try again.");
            Toast.makeText(this, "Info has not sent. Please check email and try again.", Toast.LENGTH_LONG).show();
        }
    }
}
