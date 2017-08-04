package com.yl.lenovo.kchat.mvp.presenter;

import android.util.Log;


import com.yl.lenovo.kchat.bean.leadimg;
import com.yl.lenovo.kchat.bean.SplashAndLogin;
import com.yl.lenovo.kchat.mvp.BasePresenterImpl;
import com.yl.lenovo.kchat.mvp.contract.AppContract;
import com.yl.lenovo.kchat.utis.SPUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by lenovo on 2017/6/30.
 */

public class AppPresenterImpl extends BasePresenterImpl<String> implements AppContract.AppPresenter {
    public AppPresenterImpl(AppContract.AppView appView) {
        this.appView = appView;
    }

    private AppContract.AppView appView;


    @Override
    public void loadSplashImag() {
        BmobQuery<SplashAndLogin> query = new BmobQuery<SplashAndLogin>();
        query.getObject("7037c41db7", new QueryListener<SplashAndLogin>() {

            @Override
            public void done(SplashAndLogin object, BmobException e) {
                if(e==null){
                    appView.getSplashData(object.getSplash_url().getFileUrl());
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

    }

    @Override
    public void getleader() {
        //只返回Person表的objectId这列的值
        BmobQuery<leadimg> bmobQuery = new BmobQuery<leadimg>();
        bmobQuery.addQueryKeys("img_url");
        bmobQuery.findObjects(new FindListener<leadimg>() {
            @Override
            public void done(List<leadimg> object, BmobException e) {
                if(e==null){
                    List<String> avdlist = new ArrayList<String>();
                    for (leadimg avObject : object) {
                        String title = avObject.getImg_url().getFileUrl();
                        avdlist.add(title);
                        Log.v("杨磊", title);
                    }
                    appView.getLeadData(avdlist);

                }
            }
        });

    }

    @Override
    public void getLoginAndRegistBackrround() {

        BmobQuery<SplashAndLogin> query = new BmobQuery<SplashAndLogin>();
        query.getObject("7037c41db7", new QueryListener<SplashAndLogin>() {

            @Override
            public void done(SplashAndLogin object, BmobException e) {
                if(e==null){
                    SPUtils.save("login_background",object.getLogin_url().getFileUrl());

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

    }
}
