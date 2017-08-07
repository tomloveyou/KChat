package com.yl.lenovo.kchat.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yl.lenovo.kchat.KChatApp;
import com.yl.lenovo.kchat.MainActivity;
import com.yl.lenovo.kchat.bean.Dynamic;
import com.yl.lenovo.kchat.bean.Message;
import com.yl.lenovo.kchat.utis.SPUtils;
import com.yl.lenovo.kchat.widget.dialog.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lenovo on 2017/8/7.
 */

public class MyPushMessageReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Message message=new Message();
            try {
                JSONObject jsonObject=new JSONObject(intent.getStringExtra("msg"));
                message.setAlert(jsonObject.optString("alert"));
                message.setContent(jsonObject.optString("content"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            message.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {

                }
            });
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
        }
    }
}
