package com.yl.lenovo.kchat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yl.lenovo.kchat.bean.SplashAndLogin;
import com.yl.lenovo.kchat.mvp.contract.AppContract;
import com.yl.lenovo.kchat.mvp.contract.UserContract;
import com.yl.lenovo.kchat.mvp.presenter.AppPresenterImpl;
import com.yl.lenovo.kchat.mvp.presenter.UserPresenter;
import com.yl.lenovo.kchat.utis.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SplashActivity extends AppCompatActivity implements AppContract.AppView {
    private AppContract.AppPresenter presenter = new AppPresenterImpl(this);
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SPUtils.getBoolean("FIRST_OPEN");

        presenter.getleader();
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {

            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;


        }
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.image_splash_background);
        presenter.getLoginAndRegistBackrround();
        presenter.loadSplashImag();
    }


    @Override
    public void getLeadData(List<String> imgs) {

        SPUtils.save("leader_data", imgs);
    }

    @Override
    public void getSplashData(String img_url) {
        Picasso.with(SplashActivity.this).load(img_url).into(imageView);
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
