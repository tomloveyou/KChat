package com.yl.lenovo.kchat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
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
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yl.lenovo.kchat.bean.SplashAndLogin;
import com.yl.lenovo.kchat.mvp.contract.UserContract;
import com.yl.lenovo.kchat.mvp.presenter.UserPresenter;
import com.yl.lenovo.kchat.utis.SPUtils;
import com.yl.lenovo.kchat.utis.database.MyDatabaseStartImgHelper;
import com.yl.lenovo.kchat.widget.dialog.DialogUtils;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegistActivity extends AppCompatActivity implements UserContract.UserRegistView {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordView_comfirm;

    private UserContract.UserPresenter presenter = new UserPresenter(this);
    private SimpleDraweeView imageView;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        imageView = (SimpleDraweeView) findViewById(R.id.login_background_img);
        path = (String) SPUtils.get("local_login_url", "");
        Bitmap bitmap = null;
        if ("".equals(path)) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.splash);
        } else {
            bitmap = BitmapFactory.decodeFile(path);
        }
        imageView.setImageBitmap(bitmap);

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

        getImgData();

    }

    private void downloadFile(BmobFile file) {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                //toast("开始下载...");
            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {

                    SPUtils.put("local_login_url", savePath);



                    //toast("下载成功,保存路径:"+savePath);
                } else {
                    //toast("下载失败："+e.getErrorCode()+","+e.getMessage());
                }

            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob", "下载进度：" + value + "," + newworkSpeed);
            }

        });
    }


    public void getImgData() {
        BmobQuery<SplashAndLogin> query = new BmobQuery<SplashAndLogin>();
        query.getObject("7037c41db7", new QueryListener<SplashAndLogin>() {

            @Override
            public void done(SplashAndLogin object, BmobException e) {
                if (e == null) {
                    String origin = (String) SPUtils.get("login_url", null);
                    if (origin != null) {
                        if (!origin.equals(object.getLogin_url().getFileUrl())) {
                            SPUtils.put("login_url", object.getLogin_url().getFileUrl());
                            downloadFile(new BmobFile("local_login_url", null, object.getLogin_url().getFileUrl()));
                            imageView.setImageURI(Uri.parse(object.getLogin_url().getFileUrl()));
                        }
                    } else {
                        SPUtils.put("login_url", object.getLogin_url().getFileUrl());
                        imageView.setImageURI(Uri.parse(object.getLogin_url().getFileUrl()));
                        downloadFile(new BmobFile("local_login_url", null, object.getLogin_url().getFileUrl()));

                    }


                } else {



                }


            }


        });
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

