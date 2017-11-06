package com.mfc.appframe.utils;

import android.content.Context;
import android.widget.Toast;

import com.mfc.appframe.MainApplication;

public class ToastUtil {
	/***
	 * 显示吐司
	 * @param context 
	 * @param text 提示文字
	 */
	public static void showToast(Context context,String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	/***
	 * 显示吐司(注意，在activity的onCreate使用会报错，在onCreate中请使用 showToast(Context context,String text))
	 * @param text 提示文字
	 */
	public static void showToast(String text) {
		Toast.makeText(MainApplication.currentActivity, text, Toast.LENGTH_SHORT).show();
	}
	/***
	 * 显示吐司
	 * @param text 提示文字
	 * @param time 提示时长
	 */
	public static void showToast(String text,int time) {
		Toast.makeText(MainApplication.currentActivity, text, time).show();
	}
	/***
	 * 显示吐司
	 * @param textId 提示文字id
	 * @param time 显示时间
	 */
	public static void showToast(int textId,int time) {
		Toast.makeText(MainApplication.currentActivity, MainApplication.getContext().getString(textId), time).show();
	}
	/***
	 * 显示吐司
	 * @param textId 提示文字id
	 */
	public static void showToast(int textId) {
		Toast.makeText(MainApplication.currentActivity, MainApplication.getContext().getString(textId), Toast.LENGTH_SHORT).show();
	}
}
