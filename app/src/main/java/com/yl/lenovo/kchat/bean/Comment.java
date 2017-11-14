package com.yl.lenovo.kchat.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2017/11/14.
 */

public class Comment extends BmobObject {
    private MyUser user;
    private String commentContent;

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
