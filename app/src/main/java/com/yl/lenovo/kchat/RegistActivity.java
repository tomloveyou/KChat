package com.yl.lenovo.kchat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.yl.lenovo.kchat.server.network.http.HttpException;
import com.yl.lenovo.kchat.server.response.CheckPhoneResponse;
import com.yl.lenovo.kchat.server.response.RegisterResponse;
import com.yl.lenovo.kchat.server.response.SendCodeResponse;
import com.yl.lenovo.kchat.server.response.VerifyCodeResponse;
import com.yl.lenovo.kchat.server.utils.NToast;
import com.yl.lenovo.kchat.server.utils.downtime.DownTimer;
import com.yl.lenovo.kchat.server.utils.downtime.DownTimerListener;
import com.yl.lenovo.kchat.server.widget.ClearWriteEditText;
import com.yl.lenovo.kchat.server.widget.LoadDialog;
import com.yl.lenovo.kchat.ui.base.BaseActivity;
import com.yl.lenovo.kchat.utis.SPUtils;
import com.yl.lenovo.kchat.widget.dialog.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegistActivity extends BaseActivity implements UserContract.UserRegistView,DownTimerListener,OnClickListener {

    // UI references.
    private ClearWriteEditText mEmailView;
    private ClearWriteEditText mPasswordView;
    private ClearWriteEditText mPasswordView_comfirm,mreg_code;
    private Button mreg_getcode;

    private UserContract.UserPresenter presenter = new UserPresenter(this);
    private ImageView imageView;

    private static final int CHECK_PHONE = 1;
    private static final int SEND_CODE = 2;
    private static final int VERIFY_CODE = 3;
    private static final int REGISTER = 4;
    private static final int REGISTER_BACK = 1001;

    private String mPhone, mCode, mNickName, mPassword, mCodeToken;
    private boolean isRequestCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        setHeadVisibility(View.GONE);

        // Set up the login form.
        mreg_code=(ClearWriteEditText)findViewById(R.id.reg_code);
        mreg_getcode=(Button) findViewById(R.id.reg_getcode);
        mEmailView = (ClearWriteEditText) findViewById(R.id.email);
        imageView = (ImageView) findViewById(R.id.login_background_img);
        Picasso.with(RegistActivity.this).load(SPUtils.getString("login_background")).into(imageView);
        mPasswordView = (ClearWriteEditText) findViewById(R.id.password);
        mPasswordView_comfirm = (ClearWriteEditText) findViewById(R.id.password_comfirm);
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
        mEmailSignInButton.setOnClickListener(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(RegistActivity.this, R.anim.translate_anim);
                imageView.startAnimation(animation);
            }
        }, 200);
        mreg_getcode.setOnClickListener(this);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case CHECK_PHONE:
                return action.checkPhoneAvailable("86", mPhone);
            case SEND_CODE:
                return action.sendCode("86", mPhone);
            case VERIFY_CODE:
                return action.verifyCode("86", mPhone, mCode);
            case REGISTER:
                return action.register(mNickName, mPassword, mCodeToken);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        super.onSuccess(requestCode, result);
        if (result != null) {
            switch (requestCode) {
                case CHECK_PHONE:
                    CheckPhoneResponse cprres = (CheckPhoneResponse) result;
                    if (cprres.getCode() == 200) {
                        if (cprres.isResult()) {
                            mreg_getcode.setClickable(true);
                            mreg_getcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                            Toast.makeText(mContext, R.string.phone_number_available, Toast.LENGTH_SHORT).show();
                        } else {
                            mreg_getcode.setClickable(false);
                            mreg_getcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                            Toast.makeText(mContext, R.string.phone_number_has_been_registered, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case SEND_CODE:
                    SendCodeResponse scrres = (SendCodeResponse) result;
                    if (scrres.getCode() == 200) {
                        NToast.shortToast(mContext, R.string.messge_send);
                    } else if (scrres.getCode() == 5000) {
                        NToast.shortToast(mContext, R.string.message_frequency);
                    }
                    break;

                case VERIFY_CODE:
                    VerifyCodeResponse vcres = (VerifyCodeResponse) result;
                    switch (vcres.getCode()) {
                        case 200:
                            mCodeToken = vcres.getResult().getVerification_token();
                            if (!TextUtils.isEmpty(mCodeToken)) {
                                request(REGISTER);
                            } else {
                                NToast.shortToast(mContext, "code token is null");
                                LoadDialog.dismiss(mContext);
                            }
                            break;
                        case 1000:
                            //验证码错误
                            NToast.shortToast(mContext, R.string.verification_code_error);
                            LoadDialog.dismiss(mContext);
                            break;
                        case 2000:
                            //验证码过期
                            NToast.shortToast(mContext, R.string.captcha_overdue);
                            LoadDialog.dismiss(mContext);
                            break;
                    }
                    break;

                case REGISTER:
                    RegisterResponse rres = (RegisterResponse) result;
                    switch (rres.getCode()) {
                        case 200:
                            LoadDialog.dismiss(mContext);
                            NToast.shortToast(mContext, R.string.register_success);
                            Intent data = new Intent();
                            data.putExtra("phone", mPhone);
                            data.putExtra("password", mPassword);
                            data.putExtra("nickname", mNickName);
                            data.putExtra("id", rres.getResult().getId());
                            setResult(REGISTER_BACK, data);
                            this.finish();
                            break;
                        case 400:
                            // 错误的请求
                            break;
                        case 404:
                            //token 不存在
                            break;
                        case 500:
                            //应用服务端内部错误
                            break;
                    }
                    break;
            }
        }


    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        super.onFailure(requestCode, state, result);
        switch (requestCode) {
            case CHECK_PHONE:
                Toast.makeText(mContext, "手机号可用请求失败", Toast.LENGTH_SHORT).show();
                break;
            case SEND_CODE:
                NToast.shortToast(mContext, "获取验证码请求失败");
                break;
            case VERIFY_CODE:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "验证码是否可用请求失败");
                break;
            case REGISTER:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "注册请求失败");
                break;
        }
    }

    private boolean verify() {
        if (TextUtils.isEmpty(mEmailView.getText())) {
            mEmailView.setError("账户不能为空");
            mEmailView.setShakeAnimation();

            return false;
        }
        if (TextUtils.isEmpty(mPasswordView.getText())) {
            mPasswordView.setError("密码不能为空");
            mPasswordView.setShakeAnimation();
            return false;
        }
        if (TextUtils.isEmpty(mPasswordView_comfirm.getText())) {
            mPasswordView_comfirm.setError("确认密码不能为空");
            mPasswordView_comfirm.setShakeAnimation();
            return false;
        }
        if (!mPasswordView_comfirm.getText().toString().equals(mPasswordView.getText().toString())) {
            mPasswordView_comfirm.setError("密码不一致，请重新输入");
            mPasswordView_comfirm.setShakeAnimation();
            return false;
        }
        return true;
    }


    @Override
    public void registsuccess() {
        DialogUtils.dismiss();
       // finish();
    }



    @Override
    public void error(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_getcode:
                mPhone = mEmailView.getText().toString().trim();
                mCode = mreg_code.getText().toString().trim();
                mNickName = mEmailView.getText().toString().trim();
                mPassword = mPasswordView.getText().toString().trim();
                if (TextUtils.isEmpty(mEmailView.getText().toString().trim())) {
                    NToast.longToast(mContext, R.string.phone_number_is_null);
                } else {
                    isRequestCode = true;
                    DownTimer downTimer = new DownTimer();
                    downTimer.setListener(this);
                    downTimer.startDown(60 * 1000);
                    request(SEND_CODE);
                }
                break;
            case  R.id.email_sign_in_button:
                mPhone = mEmailView.getText().toString().trim();
                mCode = mreg_code.getText().toString().trim();
                mNickName = mEmailView.getText().toString().trim();
                mPassword = mPasswordView.getText().toString().trim();
                if (verify())
                    request(REGISTER);
                break;
        }
    }
}

