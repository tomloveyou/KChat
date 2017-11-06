package com.mfc.appframe.receiver;

import com.mfc.appframe.utils.DeviceUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;

public class NetBroadCastReceiver extends BroadcastReceiver {

	private View mView;
	
	public NetBroadCastReceiver(View view){
		this.mView = view;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			//网络变化时，检查网络是否可用，不可用提示
			if (!DeviceUtil.isNetConnected(context)) {//网络不可用
				mView.setVisibility(View.VISIBLE);//显示提示
			}
			else {//网络可用
				if (mView.getVisibility() == View.VISIBLE) 
				mView.setVisibility(View.GONE);//隐藏提示
			}
		}
	}

}
