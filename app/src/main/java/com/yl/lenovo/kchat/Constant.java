package com.yl.lenovo.kchat;

import com.yl.lenovo.kchat.bean.MyUser;

/**
 * Created by lenovo on 2017/11/14.
 */

public class Constant {
    private static String user_avator="https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1553622011,3967717209&fm=27&gp=0.jpg";//美女
    private static String user_avator2="https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4104341815,495566484&fm=27&gp=0.jpg";//帅哥
    public static String getUser_avator(MyUser sextag){
        return (sextag==null?user_avator:(sextag.getUser_avator()==null?(sextag.getSex()==0?user_avator:user_avator2):sextag.getUser_avator()));
    }
}
