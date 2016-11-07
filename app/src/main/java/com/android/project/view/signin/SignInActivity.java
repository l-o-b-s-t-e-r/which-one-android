package com.android.project.view.signin;

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
import com.android.project.WhichOneApp;
import com.android.project.model.User;
import com.android.project.view.main.MainActivity;
import com.android.project.view.signin.di.SignInModule;
import com.android.project.view.signup.SignUpDialog;

import javax.inject.Inject;

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

    @Inject
    SignInPresenter.ActionListener presenter;

    private AlertDialog mRemindDialog;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        ButterKnife.bind(this);

        WhichOneApp.getMainComponent()
                .plus(new SignInModule(this))
                .inject(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = mSharedPreferences.getString(getString(R.string.user_name), null);
        if (username != null) {
            Log.i(TAG, "Application remembers this user. Start MainActivity");
            presenter.signIn(username);
        }
    }

    @OnClick(R.id.forgot_password)
    void onForgotTextViewClick() {
        final View view = View.inflate(this, R.layout.remind_dialog, null);

        mRemindDialog = new AlertDialog.Builder(this, R.style.AppThemeDialogSignUp)
                .setView(view)
                .setTitle("REMIND INFO")
                .create();

        mRemindDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SEND", new Message());
        mRemindDialog.show();
        mRemindDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                EditText editTextEmail = ButterKnife.findById(view, R.id.email);
                ProgressBar progressBar = ButterKnife.findById(view, R.id.progressBar);
                progressBar.setIndeterminate(true);

                String email = editTextEmail.getText().toString().trim();
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mRemindDialog.setCanceledOnTouchOutside(false);
                    presenter.remindInfo(email);
                } else {
                    editTextEmail.setError(getString(R.string.incorrect_email));
                }

            mRemindDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
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

        presenter.signIn(editTextName.getText().toString().trim(), editTextPassword.getText().toString().trim());
    }

    @OnClick(R.id.btn_sign_up)
    void onSignUpClick() {
        DialogFragment signUpDialogFragment = new SignUpDialog();
        signUpDialogFragment.show(getSupportFragmentManager(), "sign_up_dialog");
    }

    @Override
    public void openUserPage(User user) {
        Log.e(TAG, "openUserPage");
        WhichOneApp.createUserComponent(user);

        mSharedPreferences
                .edit()
                .putString(getString(R.string.user_name), user.getUsername())
                .apply();

        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onSuccessRemind(User user) {
        Toast.makeText(this, getString(R.string.remind_info_success), Toast.LENGTH_LONG).show();
        mRemindDialog.dismiss();
    }

    @Override
    public void onErrorRemind(Throwable throwable) {
        Toast.makeText(this, getString(R.string.remind_info_error), Toast.LENGTH_LONG).show();
        mRemindDialog.dismiss();
    }

    @Override
    public void onErrorSingIn(Throwable throwable) {
        Toast.makeText(SignInActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        editTextName.setError(getString(R.string.wrong_password_or_name));
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
        progressBar.setVisibility(View.GONE);
    }

}
