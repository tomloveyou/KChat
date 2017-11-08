package com.yl.lenovo.kchat.mvp.presenter;

import android.content.Intent;
import android.util.Log;


import com.google.gson.Gson;
import com.yl.lenovo.kchat.KChatApp;
import com.yl.lenovo.kchat.R;
import com.yl.lenovo.kchat.bean.MyUser;
import com.yl.lenovo.kchat.mvp.BasePresenter;
import com.yl.lenovo.kchat.mvp.BasePresenterImpl;
import com.yl.lenovo.kchat.mvp.contract.UserContract;
import com.yl.lenovo.kchat.utis.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by lenovo on 2017/6/30.
 */

public class UserPresenter extends BasePresenterImpl implements UserContract.UserPresenter {
    private UserContract.UserRegistView userView;
    private UserContract.UserLoginView userLoginView;

    private UserContract.UserDataUpdateView userDataUpdateViews;

    public UserPresenter(UserContract.UserDataUpdateView userDataUpdateViews) {
        this.userDataUpdateViews = userDataUpdateViews;
    }

    public UserPresenter(UserContract.UserRegistView userView) {
        this.userView = userView;
    }

    public UserPresenter(UserContract.UserLoginView userLoginView) {
        this.userLoginView = userLoginView;
    }

    @Override
    public void registUser(String username,String passwrod) {
        MyUser bu = new MyUser();
        bu.setUsername(username);
        bu.setPassword(passwrod);

        bu.setEmail(username+"@163.com");
//注意：不能用save方法进行注册
        bu.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser s, BmobException e) {
                if(e==null){
                    userView.registsuccess();
                }else{
                    userView.error(e.getMessage());
                }
            }
        });

    }

    @Override
    public void login(String username, String passwrod) {
        MyUser bu2 = new MyUser();
        bu2.setUsername(username);
        bu2.setPassword(passwrod);
        bu2.login(new SaveListener<MyUser>() {

            @Override
            public void done(MyUser bmobUser, BmobException e) {
                if(e==null){
                    KChatApp.getInstance().setBmobUser(bmobUser);
                    ;
                    SPUtils.put("userinfo",new Gson().toJson(bmobUser));
                    userLoginView.loginsuccess();
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                }else if (e.getErrorCode()==101){
                    userLoginView.error("用户名或则密码错误");
                }else {
                    userLoginView.error(e.getMessage());
                }
            }
        });

    }

    @Override
    public void update(MyUser myUser) {
        myUser.update(myUser.getObjectId(), new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    userDataUpdateViews.updatesuccess();
                }else{
                    userDataUpdateViews.error("更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

}
