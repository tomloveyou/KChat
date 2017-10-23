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
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yl.lenovo.kchat.bean.SplashAndLogin;
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
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        MyDatabaseStartImgHelper databaseStartImgHelper = new MyDatabaseStartImgHelper(this, "firstinit.db", null, 1);
        db = databaseStartImgHelper.getWritableDatabase("5123789");
        Cursor cursor = db.query("StartImg", new String[]{"local_splash_url","splash_url"}, null, null, null, null, null);

        if (cursor!=null){
            if (cursor.moveToNext()){
                Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(0));
                Drawable  drawable = new BitmapDrawable(null, bitmap);
                getWindow().setBackgroundDrawable(drawable);
            }
        }

        boolean isFirstOpen = SPUtils.getBoolean("FIRST_OPEN");

        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {

            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;


        }
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.image_splash_background);
        if (cursor.moveToNext()){
            Picasso.with(SplashActivity.this).load(cursor.getString(1)).into(imageView);
            BmobFile bmobfile = new BmobFile(cursor.getString(1), "", cursor.getString(1));
            downloadFile(bmobfile);

            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 2000);
        }else {
            getImgData();
        }


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
                    values.put("local_splash_url", savePath);
                    db.insert("StartImg", null, values);


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

                    ContentValues values = new ContentValues();
                    values.put("splash_url", object.getSplash_url().getFileUrl());
                    values.put("login_url", object.getLogin_url().getFileUrl());
                    db.insert("StartImg", null, values);
                    Picasso.with(SplashActivity.this).load(object.getSplash_url().getFileUrl()).into(imageView);
                    imageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }, 2000);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
    }


}
