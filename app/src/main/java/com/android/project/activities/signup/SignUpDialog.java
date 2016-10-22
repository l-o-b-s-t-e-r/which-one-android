package com.android.project.activities.signup;

import android.app.Dialog;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 05.09.16.
 */

public class SignUpDialog extends DialogFragment implements SignUpPresenter.View {

    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.editTextPasswordRepeat)
    EditText editTextPasswordRepeat;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_sign_up)
    Button buttonSignUp;

    private SignUpPresenter.ActionListener mActionListener;
    private Dialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionListener = new SignUpPresenterImpl(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_sign_up, null);

        mDialog = new AlertDialog.Builder(getActivity(), R.style.AppThemeDialogSignUp)
                .setView(view)
                .create();

        ButterKnife.bind(this, view);

        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mActionListener.checkName(editTextName.getText().toString().trim());
                }
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()) {
                        mActionListener.checkEmail(editTextEmail.getText().toString().trim());
                    } else {
                        editTextEmail.setError(getString(R.string.incorrect_email));
                    }
                }
            }
        });

        return mDialog;
    }

    @OnClick(R.id.btn_sign_up)
    public void onClick() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordRepeat = editTextPasswordRepeat.getText().toString().trim();

        if (!name.matches("[a-z0-9]+")) {
            editTextName.setError(getString(R.string.invalid_characters));
        }

        if (name.contains(" ")) {
            editTextName.setError(getString(R.string.space_existence));
            return;
        }

        if (password.contains(" ")) {
            editTextPassword.setError(getString(R.string.space_existence));
            return;
        }

        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.empty_field));
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.empty_field));
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.empty_field));
            return;
        }

        if (passwordRepeat.isEmpty()) {
            editTextPasswordRepeat.setError(getString(R.string.empty_field));
            return;
        }

        if (!password.equals(passwordRepeat)) {
            editTextPasswordRepeat.setError(getString(R.string.password_not_match));
            return;
        }

        buttonSignUp.setEnabled(false);
        mDialog.setCanceledOnTouchOutside(false);
        mActionListener.signUp(name, password, email);
    }

    @Override
    public void showCheckNameResult(Boolean validName) {
        if (!validName) {
            editTextName.setError(getString(R.string.existing_name));
        }
    }

    @Override
    public void showCheckEmailResult(Boolean validEmail) {
        if (!validEmail) {
            editTextEmail.setError(getString(R.string.existing_email));
        }
    }

    @Override
    public void signUpResult(Boolean successResult) {
        if (successResult) {
            Toast.makeText(getContext(), getString(R.string.sign_up_success), Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            buttonSignUp.setEnabled(true);
            mDialog.setCanceledOnTouchOutside(true);
            Toast.makeText(getContext(), getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
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
