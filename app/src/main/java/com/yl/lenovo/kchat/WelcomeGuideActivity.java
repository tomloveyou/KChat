package com.yl.lenovo.kchat;

/**
 * Created by lenovo on 2017/7/1.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yl.lenovo.kchat.bean.leadimg;
import com.yl.lenovo.kchat.utis.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 欢迎页
 *
 * @author wwj_748
 */
public class WelcomeGuideActivity extends Activity {

    private static final String TAG = WelcomeGuideActivity.class.getSimpleName();
    private BGABanner mBackgroundBanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        setListener();
        processLogic();



    }
    private void initView() {
        setContentView(R.layout.activity_guide);
        mBackgroundBanner = (BGABanner) findViewById(R.id.banner_guide_background);

        mBackgroundBanner.setAdapter(new BGABanner.Adapter<ImageView, leadimg>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, leadimg model, int position) {
                Glide.with(WelcomeGuideActivity.this)
                        .load(model.getImg_url().getFileUrl())
                        .placeholder(R.mipmap.splash)
                        .error(R.mipmap.splash)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mBackgroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                SPUtils.put("FIRST_OPEN",false);
                startActivity(new Intent(WelcomeGuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void processLogic() {
        // 设置数据源
        BmobQuery<leadimg> query = new BmobQuery<leadimg>();

        query.findObjects(new FindListener<leadimg>() {
            @Override
            public void done(List<leadimg> object, BmobException e) {
                if (e == null) {
                    mBackgroundBanner.setData(object, null);

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }
}