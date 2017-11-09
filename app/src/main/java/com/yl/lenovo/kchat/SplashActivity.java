package com.yl.lenovo.kchat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.yl.lenovo.kchat.bean.SplashAndLogin;
import com.yl.lenovo.kchat.utis.NetUtils;
import com.yl.lenovo.kchat.utis.SPUtils;
import com.yl.lenovo.kchat.utis.database.MyDatabaseStartImgHelper;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONObject;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.ProgressCallback;
import cn.bmob.v3.listener.QueryListener;

public class SplashActivity extends Activity {
    private ImageView imageView;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        path = (String) SPUtils.get(SplashActivity.this, "local_splash_url", "");
        Bitmap bitmap = null;
        if (path != null) {
            bitmap = BitmapFactory.decodeFile(path);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.splash);
        }
        Drawable drawable = new BitmapDrawable(null, bitmap);
        getWindow().setBackgroundDrawable(drawable);

        boolean isFirstOpen = (boolean) SPUtils.get(SplashActivity.this, "FIRST_OPEN", "");

        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {

            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;


        }
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.image_splash_background);
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

                    SPUtils.put(SplashActivity.this, "local_splash_url", savePath);


                    //toast("下载成功,保存路径:"+savePath);
                } else {
                    //toast("下载失败："+e.getErrorCode()+","+e.getMessage());
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

                    if (cursor.moveToNext()) {
                        if (!object.getSplash_url().getFileUrl().equals(cursor.getString(1))) {
                            ContentValues values = new ContentValues();
                            values.put("splash_url", object.getSplash_url().getFileUrl());
                            db.update("StartImg", values, null, null);
                            BmobFile bmobfile = new BmobFile("splash_img.jpg", "", object.getSplash_url().getFileUrl());
                            downloadFile(bmobfile);
                        }
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("splash_url", object.getSplash_url().getFileUrl());
                        db.insert("StartImg", null, values);
                        BmobFile bmobfile = new BmobFile("splash_img.jpg", "", object.getSplash_url().getFileUrl());
                        downloadFile(bmobfile);

                    }
                    Glide.with(SplashActivity.this).load(object.getSplash_url().getFileUrl()).into(imageView);


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
                        Glide.with(SplashActivity.this).load(img_path).into(imageView);
                    }


                }
                imageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        cursor.close();
                        db.close();
                        finish();
                    }
                }, 2000);

            }


        });
    }


}
