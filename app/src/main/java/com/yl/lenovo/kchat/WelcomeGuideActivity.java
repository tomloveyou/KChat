package com.yl.lenovo.kchat;

/**
 * Created by lenovo on 2017/7/1.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.squareup.picasso.Picasso;
import com.yl.lenovo.kchat.bean.leadimg;
import com.yl.lenovo.kchat.utis.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 欢迎页
 *
 * @author wwj_748
 */
public class WelcomeGuideActivity extends Activity implements View.OnClickListener {

    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button startBtn;
    private List<String> leader_data_urls = new ArrayList<>();


    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        views = new ArrayList<View>();

        // 初始化引导页视图列表

        startBtn = (Button) findViewById(R.id.btn_enter);
        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new PageChangeListener());
        getleadimg();

    }
private void  getleadimg(){
    BmobQuery<leadimg> query = new BmobQuery<leadimg>();

    query.findObjects(new FindListener<leadimg>() {
        @Override
        public void done(List<leadimg> object, BmobException e) {
            if(e==null){

                for (leadimg gameScore : object) {
                    leader_data_urls.add(gameScore.getImg_url().getFileUrl());
                }
                for (int i = 0; i < leader_data_urls.size(); i++) {
                    View view = LayoutInflater.from(WelcomeGuideActivity.this).inflate(R.layout.guid_view1, null);
                    ImageView guid_view = (ImageView) view.findViewById(R.id.guid_view_img);
                    Picasso.with(WelcomeGuideActivity.this).load(leader_data_urls.get(i)).into(guid_view);
                    if (i == leader_data_urls.size() - 1) {

                        startBtn.setTag("enter");
                        startBtn.setOnClickListener(WelcomeGuideActivity.this);
                    }

                    views.add(view);

                }
                adapter.notifyDataSetChanged();

                initDots();
            }else{
                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
            }
        }
    });
}
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        SPUtils.save("FIRST_OPEN", true);
        finish();
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        if (leader_data_urls.size() > 0) {
            dots = new ImageView[leader_data_urls.size()];
            // 循环取得小点图片
            for (int i = 0; i < leader_data_urls.size(); i++) {
                // 得到一个LinearLayout下面的每一个子元素
                dots[i] = (ImageView) ll.getChildAt(i);
                dots[i].setEnabled(false);// 都设为灰色
                dots[i].setOnClickListener(this);
                dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
            }

            currentIndex = 0;
            dots[currentIndex].setEnabled(true); // 设置为白色，即选中状态

        }

    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= leader_data_urls.size()) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > leader_data_urls.size() || currentIndex == position) {
            return;
        }
        startBtn.setVisibility(position==leader_data_urls.size()-1?View.VISIBLE:View.GONE);
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }

        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterMainActivity() {
        Intent intent = new Intent(WelcomeGuideActivity.this,
                SplashActivity.class);
        startActivity(intent);
        SPUtils.save("FIRST_OPEN", true);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int position) {
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。

        }

        // 当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            // arg0 :当前页面，及你点击滑动的页面
            // arg1:当前页面偏移的百分比
            // arg2:当前页面偏移的像素位置

        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurDot(position);
        }

    }
}