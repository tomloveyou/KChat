package com.yl.lenovo.kchat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yl.lenovo.kchat.mvp.contract.UserContract;
import com.yl.lenovo.kchat.mvp.presenter.UserPresenter;
import com.yl.lenovo.kchat.utis.SPUtils;
import com.yl.lenovo.kchat.widget.dialog.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegistActivity extends AppCompatActivity implements UserContract.UserRegistView {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordView_comfirm;
    private View mProgressView;
    private View mLoginFormView;
    private UserContract.UserPresenter presenter = new UserPresenter(this);
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        imageView = (ImageView) findViewById(R.id.login_background_img);
        Picasso.with(RegistActivity.this).load(SPUtils.getString("login_background")).into(imageView);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView_comfirm = (EditText) findViewById(R.id.password_comfirm);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (verify()){
                        presenter.registUser(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    }

                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verify()){
                    DialogUtils.showProgressDialog(RegistActivity.this,"注册中，请稍后……");
                    presenter.registUser(mEmailView.getText().toString(), mPasswordView.getText().toString());
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private boolean verify() {
        if (TextUtils.isEmpty(mEmailView.getText())) {
            mEmailView.setError("账户不能为空");
            mEmailView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mPasswordView.getText())) {
            mPasswordView.setError("密码不能为空");
            mPasswordView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mPasswordView_comfirm.getText())) {
            mPasswordView_comfirm.setError("确认密码不能为空");
            mPasswordView_comfirm.requestFocus();
            return false;
        }
        if (!mPasswordView_comfirm.getText().toString().equals(mPasswordView.getText().toString())) {
            mPasswordView_comfirm.setError("密码不一致，请重新输入");
            mPasswordView_comfirm.requestFocus();
            return false;
        }
        return true;
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    public void registsuccess() {
        DialogUtils.dismiss();
        finish();
    }



    @Override
    public void error(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

