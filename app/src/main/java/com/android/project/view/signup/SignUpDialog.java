package com.android.project.view.signup;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.view.signup.di.SignUpModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 05.09.16.
 */

public class SignUpDialog extends DialogFragment implements SignUpPresenter.View {

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password_repeat)
    EditText passwordRepeat;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.btn_sign_up)
    Button buttonSignUp;

    @Inject
    SignUpPresenter.ActionListener presenter;

    private Dialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WhichOneApp.getMainComponent()
                .plus(new SignUpModule(this))
                .inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.sign_up_fragment, null);
        ButterKnife.bind(this, view);

        mDialog = new AlertDialog.Builder(getActivity(), R.style.AppThemeDialogSignUp)
                .setView(view)
                .create();

        username.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                presenter.checkName(username.getText().toString().trim());
            }
        });

        email.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    presenter.checkEmail(email.getText().toString().trim());
                } else {
                    email.setError(getString(R.string.incorrect_email));
                }
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(225, 0, 86), PorterDuff.Mode.SRC_IN);
        }

        return mDialog;
    }

    @OnClick(R.id.btn_sign_up)
    public void onClick() {
        String username = this.username.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String passwordRepeat = this.passwordRepeat.getText().toString().trim();

        if (!username.matches("[a-z0-9]+")) {
            this.username.setError(getString(R.string.invalid_characters));
        }

        if (username.contains(" ")) {
            this.username.setError(getString(R.string.space_existence));
            return;
        }

        if (password.contains(" ")) {
            this.password.setError(getString(R.string.space_existence));
            return;
        }

        if (username.isEmpty()) {
            this.username.setError(getString(R.string.empty_field));
            return;
        }

        if (email.isEmpty()) {
            this.email.setError(getString(R.string.empty_field));
            return;
        }

        if (password.isEmpty()) {
            this.password.setError(getString(R.string.empty_field));
            return;
        }

        if (passwordRepeat.isEmpty()) {
            this.passwordRepeat.setError(getString(R.string.empty_field));
            return;
        }

        if (!password.equals(passwordRepeat)) {
            this.passwordRepeat.setError(getString(R.string.password_not_match));
            return;
        }

        presenter.signUp(username, password, email);
    }

    @Override
    public void showCheckNameResult(Boolean validName) {
        if (!validName) {
            username.setError(getString(R.string.existing_name));
        }
    }

    @Override
    public void showCheckEmailResult(Boolean validEmail) {
        if (!validEmail) {
            email.setError(getString(R.string.existing_email));
        }
    }

    @Override
    public void signUpResult(Boolean successResult) {
        if (successResult) {
            Toast.makeText(getContext(), getString(R.string.sign_up_success), Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            Toast.makeText(getContext(), getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        buttonSignUp.setEnabled(false);
        mDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        buttonSignUp.setEnabled(true);
        mDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(getContext(), getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }
}
