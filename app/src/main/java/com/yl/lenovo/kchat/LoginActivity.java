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
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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


import com.facebook.drawee.view.SimpleDraweeView;
import com.yl.lenovo.kchat.bean.SplashAndLogin;
import com.yl.lenovo.kchat.mvp.contract.UserContract;
import com.yl.lenovo.kchat.mvp.presenter.UserPresenter;
import com.yl.lenovo.kchat.stick.TravelActivity;
import com.yl.lenovo.kchat.utis.SPUtils;
import com.yl.lenovo.kchat.utis.database.MyDatabaseStartImgHelper;
import com.yl.lenovo.kchat.widget.dialog.DialogUtils;


import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements UserContract.UserLoginView {
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView tv_regist;
    private SimpleDraweeView imageView;
    private UserContract.UserPresenter presenter = new UserPresenter(this);
    private SQLiteDatabase db;
    private  Cursor cursor;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyDatabaseStartImgHelper databaseStartImgHelper = new MyDatabaseStartImgHelper(this, "firstinit.db", null, 1);
        db = databaseStartImgHelper.getWritableDatabase("5123789");
       cursor = db.query("StartImg", new String[]{"local_login_url","login_url"}, null, null, null, null, null);

        if (SPUtils.getString("userinfo") != null && !"".equals(SPUtils.getString("userinfo"))) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        if (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},112);
        }
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
        imageView = (SimpleDraweeView) findViewById(R.id.login_background_img);
        mPasswordView = (EditText) findViewById(R.id.password);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                String path = cursor.getString(0);
                Bitmap bitmap = null;
                if (path != null) {
                    bitmap = BitmapFactory.decodeFile(path);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.splash);
                }
                Drawable drawable = new BitmapDrawable(null, bitmap);
                getWindow().setBackgroundDrawable(drawable);
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_btn_trans_round_n);
                Drawable drawable = new BitmapDrawable(null, bitmap);
                getWindow().setBackgroundDrawable(drawable);
            }
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_btn_trans_round_n);
            Drawable drawable = new BitmapDrawable(null, bitmap);
            getWindow().setBackgroundDrawable(drawable);
        }
        getImgData();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (verify()) {

                        DialogUtils.showProgressDialog(LoginActivity.this, "登录中，请稍后……");
                    }
                    presenter.login(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verify())
                    presenter.login(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });
        tv_regist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                    ContentValues values = new ContentValues();
                    values.put("local_login_url", savePath);

                    if (cursor.getString(0)!=null){
                        db.insert("StartImg", null,values);
                    }else {
                        db.update("StartImg", values,null ,null);
                    }



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

                    if (cursor.moveToNext()) {
                        if (!object.getLogin_url().getFileUrl().equals(cursor.getString(1))) {
                            ContentValues values = new ContentValues();
                            values.put("login_url", object.getLogin_url().getFileUrl());
                            db.update("StartImg", values,null,null);
                            BmobFile bmobfile = new BmobFile("login_img.jpg", "", object.getLogin_url().getFileUrl());
                            downloadFile(bmobfile);
                        }
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("login_url", object.getLogin_url().getFileUrl());
                        db.insert("StartImg", null, values);
                        BmobFile bmobfile = new BmobFile("login_img.jpg", "", object.getLogin_url().getFileUrl());
                        downloadFile(bmobfile);

                    }
                    imageView.setImageURI(Uri.parse(object.getLogin_url().getFileUrl()));



                } else {
                    String img_path = null;

                    try {
                        img_path = cursor.getString(1);
                    } catch (Exception e1) {
                        img_path = null;
                        Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(0));
                        imageView.setImageBitmap(bitmap);
                    }

                    if (img_path == null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(0));
                        imageView.setImageBitmap(bitmap);

                        // Picasso.with(SplashActivity.this).load(R.mipmap.smoothlistview_arrow).into(imageView);
                    } else {
                        imageView.setImageURI(Uri.parse(img_path));

                    }


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
        return true;
    }

    @Override
    public void loginsuccess() {
        DialogUtils.dismiss();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void error(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

