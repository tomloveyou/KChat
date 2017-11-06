package com.mfc.appframe.utils;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mfc.appframe.R;

public class HrefUtils {
	
	/***
	 * 跳转到网络设置页面
	 * @param context
	 */
	public static void hrefSettingActivity(Activity context) {
		// 跳转到系统的网络设置界面
		Intent intent = null;
		// 先判断当前系统版本
		if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
			intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		}else{
			intent = new Intent();
			intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
		}
		context.startActivity(intent);
	}
	/***
	 * 跳转到拨号页面
	 * @param context
	 * @param url
	 */
	public static void hrefCall(Activity context,String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url)); 
		context.startActivity(intent);
	}
	
	/**
	 * <b>方法描述：启动activity并带activity关闭回调 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-15上午10:04:21
	 * @param context
	 * @param targetcls
	 * @param requestCode
	 * @param type
	 * @param bundle
	 * @since Met 1.0
	 * @see
	 */
	public static <T>void hrefActivityForResult(Activity context, Class<T> targetcls,int requestCode,
			int type, Bundle bundle) {
		Intent intent = new Intent(context, targetcls);
		intent.putExtras(bundle);
		context.startActivityForResult(intent, requestCode);
		setTexiao(context, type);
	}
	
	/**
	 * <b>方法描述： </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-15上午10:05:10
	 * @param context
	 * @param targetcls
	 * @param requestCode
	 * @param type
	 * @since Met 1.0
	 * @see
	 */
	public static <T>void hrefActivityForResult(Activity context, Class<T> targetcls,int requestCode,
			int type) {
		Intent intent = new Intent(context, targetcls);
		context.startActivityForResult(intent, requestCode);
		setTexiao(context, type);
	}
	/**
	 * <b>方法描述：启动activity并带activity关闭回调 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-15上午10:04:21
	 * @param context
	 * @param targetcls
	 * @param requestCode
	 * @param type
	 * @param bundle
	 * @since Met 1.0
	 * @see
	 */
	public static <T>void hrefActivityForResult(Fragment fragment, Class<T> targetcls,int requestCode,
			int type, Bundle bundle) {
		Intent intent = new Intent(fragment.getActivity(), targetcls);
		intent.putExtras(bundle);
		fragment.startActivityForResult(intent, requestCode);
		setTexiao(fragment.getActivity(), type);
	}
	
	/**
	 * <b>方法描述： </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-15上午10:05:10
	 * @param context
	 * @param targetcls
	 * @param requestCode
	 * @param type
	 * @since Met 1.0
	 * @see
	 */
	public static <T>void hrefActivityForResult(Fragment fragment, Class<T> targetcls,int requestCode,
			int type) {
		Intent intent = new Intent(fragment.getActivity(), targetcls);
		fragment.startActivityForResult(intent, requestCode);
		setTexiao(fragment.getActivity(), type);
	}
	
	public static <T>void hrefActivityForResult(Activity context, Class<T> targetClass,int requestCode,
			String bundleKey, Object bundleValue, int type) {
		Intent intent = new Intent(context, targetClass);
		Bundle bundle = new Bundle();
		bundle.putSerializable(bundleKey, (Serializable) bundleValue);
		intent.putExtras(bundle);
		context.startActivityForResult(intent, requestCode);
		setTexiao(context, type);
	}
	
	public static void hrefActivity(Activity context, Class targetcls,
			int type, Bundle bundle) {
		Intent intent = new Intent(context, targetcls);
		intent.putExtras(bundle);
		context.startActivity(intent);
		setTexiao(context, type);
	}
	
	/**
	 * Activity跳转
	 * 
	 * @param context
	 *            上下文环境变量
	 * @param targetActivity
	 *            目标页面
	 * @param bundleKey
	 *            参数Key
	 * @param bundleValue
	 *            参数值
	 */
	public static void hrefActivity(Activity context, Class targetClass,
			String bundleKey, Object bundleValue, int type) {
		Intent intent = new Intent(context, targetClass);
		Bundle bundle = new Bundle();
		bundle.putSerializable(bundleKey, (Serializable) bundleValue);
		intent.putExtras(bundle);
		context.startActivity(intent);
		setTexiao(context, type);
	}

	/**
	 * Activity跳转
	 * 
	 * @param context
	 *            上下文环境变量
	 * @param targetActivity
	 *            的实例对象class.newinstance() 目标页面
	 * @param type
	 *            页面跳转特效 。 0：标准左右效果， 1：无效果，2：放大和缩小 目标页面
	 */
	public static void hrefActivity(Activity context, Class targetCls,
			int type) {
		Intent intent = new Intent(context, targetCls);
		context.startActivity(intent);
		setTexiao(context, type);
	}
	
	public static void finish(Activity context,
			int type) {
		context.finish();
		setTexiao(context, type);
	}

	/**
	 * Activity跳转
	 * 
	 * @param context
	 *            上下文环境变量
	 * @param targetActivity
	 *            目标页面
	 * @param type
	 *            页面跳转特效 。 0：标准左右效果， 1：无效果，2：放大和缩小 目标页面
	 */
	public static void hrefActivity(Context context, Class targetcls,
			int type) {
		Intent intent = new Intent(context, targetcls);
		context.startActivity(intent);
		setTexiao(context, type);
	}

	public static void setTexiao(Activity context, int type) {
		switch (type) {
		case 0:
			context.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		case 1:
//			 context.overridePendingTransition(R.anim.z_zoom_no
//			 ,R.anim.z_zoom_no);
			context.overridePendingTransition(0, 0);
			break;
		case 2:
			context.overridePendingTransition(R.anim.z_zoomin_in,
					R.anim.z_zoomout_out);
			break;
		case 3:
			context.overridePendingTransition(R.anim.zoom_in_login,
					R.anim.zoom_out_splash);
			break;
		default:
			break;
		}
	}

	public static void setTexiao(Context contextp, int type) {
		Activity context = (Activity) contextp;
		switch (type) {
		case 0:
			context.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		case 1:
			// context.overridePendingTransition(R.anim.z_zoom_no
			// ,R.anim.z_zoom_no);
			context.overridePendingTransition(0, 0);
			break;
		case 2:
			context.overridePendingTransition(R.anim.z_zoomin_in,
					R.anim.z_zoomout_out);
			break;
		default:
			break;
		}
	}
}
