package com.android.project.activities.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.activities.main.MainActivity;
import com.android.project.activities.signup.SignUpDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements SignInPresenter.View {

    private static final String TAG = SignInActivity.class.getSimpleName();

    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.forgot_password)
    TextView textViewForgot;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private AlertDialog remindDialog;
    private SignInPresenter.ActionListener mActionListener;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (mSharedPreferences.contains(getString(R.string.user_name))) {
            Log.i(TAG, "Application remembers this user. Start MainActivity");
            startActivity(new Intent(this, MainActivity.class));
        }

        mActionListener = new SignInPresenterImpl(this);
    }

    @OnClick(R.id.forgot_password)
    void onForgotTextViewClick() {
        final View view = View.inflate(this, R.layout.remind_dialog, null);

        remindDialog = new AlertDialog.Builder(this, R.style.AppThemeDialogSignUp)
                .setView(view)
                .setTitle("REMIND INFO")
                .create();

        remindDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SEND", new Message());
        remindDialog.show();
        remindDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextEmail = ButterKnife.findById(view, R.id.email);
                ProgressBar progressBar = ButterKnife.findById(view, R.id.progressBar);
                progressBar.setIndeterminate(true);

                String email = editTextEmail.getText().toString().trim();
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    remindDialog.setCanceledOnTouchOutside(false);
                    mActionListener.remindInfo(email);
                } else {
                    editTextEmail.setError(getString(R.string.incorrect_email));
                }

                remindDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
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

    @Override
    public void openUserPage(Boolean correctInfo) {
        if (correctInfo) {
            mSharedPreferences
                    .edit()
                    .putString(getString(R.string.user_name), editTextName.getText().toString().trim())
                    .apply();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } else {
            Toast.makeText(SignInActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            editTextName.setError(getString(R.string.wrong_password_or_name));
        }
    }

    @Override
    public void remindInfoResult(Boolean correctInfo) {
        if (correctInfo) {
            Toast.makeText(this, getString(R.string.remind_info_success), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.remind_info_error), Toast.LENGTH_LONG).show();
        }

        remindDialog.dismiss();
    }

    @Override
    protected void onStop() {
        mActionListener.onStop();
        super.onStop();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

}
