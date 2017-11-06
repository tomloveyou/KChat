package com.mfc.appframe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.mfc.appframe.MainApplication;

import java.io.File;
import java.util.List;

public class AppUtils {
	/***
	 * 获取程序版本名称
	 * @param _context
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context _context) throws Exception {  
	    //获取packagemanager的实例   
	    PackageManager packageManager = _context.getPackageManager();  
	    //getPackageName()是你当前类的包名，0代表是获取版本信息  
	    PackageInfo packInfo = packageManager.getPackageInfo(_context.getPackageName(), 0);  
	    return packInfo.versionName;   
	}
	/***
	 * 获取程序版本号
	 * @param _context
	 * @return
	 * @throws Exception
	 */
	public static int getVersionCode(Context _context) throws Exception {  
	    //获取packagemanager的实例   
	    PackageManager packageManager = _context.getPackageManager();  
	    //getPackageName()是你当前类的包名，0代表是获取版本信息  
	    PackageInfo packInfo = packageManager.getPackageInfo(_context.getPackageName(), 0);  
	    return packInfo.versionCode;   
	}
	
	/***
	 * 获取工程名
	 * @param _context
	 * @return
	 * @throws NameNotFoundException
	 */
	public static String getProjectName(Context _context) throws NameNotFoundException {
		PackageManager pm = _context.getPackageManager();
		PackageInfo packInfo = pm.getPackageInfo(_context.getPackageName(), 0);
		String packageName = packInfo.packageName;
		return packageName.substring(packageName.lastIndexOf(".")+1);
	}
	
	/***
	 * 退出应用
	 * 关闭所有activity
	 */
	public static void exitApp() {
		Log.e("exit","开始关闭activity");
		try{
		List<Activity> list = (List<Activity>) MainApplication.allActivitys.values();
		Activity activity = null;
		for (int i = 0; i < list.size(); i++) {
			if (null != list.get(i)) {
				activity = list.get(i);
				activity.finish();
			}
		}
		list.clear();
		MainApplication.allActivitys.clear();
		System.exit(0);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 关闭所有activity
	 */
	public static void clearActivitys() {
		DebugUtil.log("关闭所有activity");
		try{
			List<Activity> list = (List<Activity>) MainApplication.allActivitys;
			Activity activity = null;
			for (int i = 0; i < list.size(); i++) {
				if (null != list.get(i)) {
					activity = list.get(i);
					activity.finish();
				}
			}
			list.clear();
			MainApplication.allActivitys.clear();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 安装apk文件
	 * @param apkFile apk的file
	 */
	public static void installApk(File apkFile) {
		  // 核心是下面几句代码  
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.fromFile(apkFile),  
                "application/vnd.android.package-archive");  
        MainApplication.currentActivity.startActivity(intent);  
	}
	
	/***
	 * 获取应用程序的图标
	 * @param context
	 * @return
	 */
	public static Drawable getAppIcon (Context context) {
		try {
			return context.getPackageManager().getApplicationIcon(context.getPackageName());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("获取程序图标出错");
		}
	}
	

	
}
