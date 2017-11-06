package com.mfc.appframe.utils;

import com.mfc.appframe.MainApplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.util.Log;

/**
 * <dl>  Class Description
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：
 *  <dd> 类描述：获取设备相关信息（网络状态，设备编号等）和剪切板功能 
 *  <dd> 创建时间：2016-4-27上午11:41:37 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version
 */
public class DeviceUtil {
	
	/***
	 * 返回设备唯一物理id
	 * @param _context
	 * @return
	 */
	public static String getDeviceId(Context _context) {
		try {
			return Secure.getString(_context.getContentResolver(), Secure.ANDROID_ID);
		}
		catch (Exception e) {	
			 Log.d("Common", "cannot get DeviceId");
		}
		return null;
	}
	
	// 判断当前是否使用的是 WIFI网络
	public static boolean isWifiActive(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext  
				.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
		if (activeNetInfo != null  
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
			return true;  
		}  
		return false;
	}

	public static boolean isNetConnected(Context _context) {
		ConnectivityManager mConnectivity
		= (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		int netType = info.getType();
		if (netType == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else if (netType == ConnectivityManager.TYPE_MOBILE) {
			return info.isConnected();
		} else {
			return false;
		}
	}
	
	/**
	 * 实现文本复制功能 复制到剪切板
	 * <b>方法描述： 实现文本复制功能 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-27上午11:48:54
	 * @param content
	 * @param context
	 * @since Met 1.0
	 * @see
	 */
	public static void copy(String content, Context context)  
	{  
		// 得到剪贴板管理器  
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
		cmb.setPrimaryClip(ClipData.newPlainText(null, content.trim()));  
	}  
	/**
	 * 实现粘贴功能 获取剪切板内容
	 * <b>方法描述：实现粘贴功能  </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-27上午11:49:13
	 * @param context
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public static String paste(Context context)  
	{  
		// 得到剪贴板管理器  
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
		ClipData clip = cmb.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(context).toString();
        }
		return null;  
	}  
	
	/**
	 * <b>方法描述： 直接拨打电话</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-7-7下午3:44:37
	 * @param phoneNumnber
	 * @since Met 1.0
	 * @see
	 */
	public static void call (String phoneNumnber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNumnber));
		MainApplication.currentActivity.startActivity(intent);
	}
}
