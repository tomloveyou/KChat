package com.yl.lenovo.kchat.mvp.contract;

import com.yl.lenovo.kchat.mvp.BasePresenter;

import java.util.List;

/**
 * Created by lenovo on 2017/6/30.
 */

public interface AppContract {
    interface AppView{
        void  getLeadData(List<String>imgs);
        void  getSplashData(String img_url);
    }
    interface AppPresenter extends BasePresenter{
        void loadSplashImag();
        void  getleader();
        void  getLoginAndRegistBackrround();
    }
}
