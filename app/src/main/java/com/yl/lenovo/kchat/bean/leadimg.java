package com.yl.lenovo.kchat.bean;



import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lenovo on 2017/7/4.
 */

public class leadimg extends BmobObject {
    private BmobFile av_url;

    public BmobFile getImg_url() {
        return av_url;
    }

    public void setImg_url(BmobFile img_url) {
        this.av_url = img_url;
    }
}
