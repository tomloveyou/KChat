package com.yl.lenovo.kchat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import com.yl.lenovo.kchat.bean.SplashAndLogin;
import com.yl.lenovo.kchat.utis.NetUtils;
import com.yl.lenovo.kchat.utis.SPUtils;
import com.yl.lenovo.kchat.utis.database.MyDatabaseStartImgHelper;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.ProgressCallback;
import cn.bmob.v3.listener.QueryListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends Activity {
    private SimpleDraweeView imageView;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        path = (String) SPUtils.get("local_splash_url", "");
        boolean isFirstOpen = (boolean) SPUtils.get("FIRST_OPEN", true);

        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {

            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);

            finish();
            return;


        }
        setContentView(R.layout.activity_splash);
        imageView = (SimpleDraweeView) findViewById(R.id.image_splash_background);
        imageView.setImageURI(Uri.parse(path));
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

                    SPUtils.put("local_splash_url", savePath);

                    //toast("下载成功,保存路径:"+savePath);
                }
                imageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                        finish();
                    }
                }, 2000);
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
                    String origin = (String) SPUtils.get("splash_url", null);
                    if (origin != null) {
                        if (!origin.equals(object.getSplash_url().getFileUrl())) {
                            SPUtils.put("splash_url", object.getSplash_url().getFileUrl());
                            imageView.setImageURI(object.getSplash_url().getFileUrl());
                            downloadFile(new BmobFile("local_splash_url", null, object.getSplash_url().getFileUrl()));

                        }else {
                            imageView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }, 2000);
                        }
                    } else {
                        SPUtils.put("splash_url", object.getSplash_url().getFileUrl());
                        imageView.setImageURI(object.getSplash_url().getFileUrl());
                        downloadFile(new BmobFile("local_splash_url", null, object.getSplash_url().getFileUrl()));

                    }




                } else {
                    imageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }, 2000);
                }

            }


        });
    }

}
