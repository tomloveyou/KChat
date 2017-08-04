package com.yl.lenovo.kchat.mvp.contract;

import com.yl.lenovo.kchat.bean.MyUser;
import com.yl.lenovo.kchat.mvp.BasePresenter;

/**
 * Created by lenovo on 2017/6/30.
 */

public interface UserContract {
    interface UserRegistView {
        void registsuccess();
        void error(String msg);
    }
    interface UserLoginView {
        void loginsuccess();
        void error(String msg);
    }
    interface UserDataUpdateView{
        void updatesuccess();
        void error(String msg);
    }

    interface UserPresenter extends BasePresenter{
        void registUser(String username,String passwrod);
        void login(String username,String passwrod);
void  update(MyUser myUser);
    }
}
