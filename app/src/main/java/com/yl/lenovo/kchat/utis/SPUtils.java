package com.yl.lenovo.kchat.utis;

import android.content.SharedPreferences;


import com.yl.lenovo.kchat.KChatApp;

import org.json.JSONArray;

import java.util.List;

public class SPUtils {
	private static SharedPreferences sp = KChatApp.sp;
	public static final String SP_NAME = "hstc_sp_name";
	/***
	 * 保存简单字符串数据到sharedpreference中
	 * @param key 值对应的key
	 * @param value 要保存的值
	 */
	public static boolean save(String key,String value) {
		if (sp != null) {
			return sp.edit().putString(key, value).commit();
		}
		return false;
	}
	public static boolean save(String key,List<String> value) {
		if (sp != null) {
			JSONArray jsonArray=new JSONArray();
			for (int i=0;i<value.size();i++){
             jsonArray.put(value.get(i));
			}
			return sp.edit().putString(key, jsonArray.toString()).commit();
		}
		return false;
	}
	public static boolean save(String key,boolean value) {
		if (sp != null) {
			return sp.edit().putBoolean(key, value).commit();
		}
		return false;
	}
	
	public static boolean save(String key,int value) {
		if (sp != null) {
			return sp.edit().putInt(key, value).commit();
		}
		return false;
	}
	
	public static String getString(String key) {
		if (sp != null) {
			return sp.getString(key, "");
		}
		return "";
	}
	
	public static boolean getBoolean(String key) {
		if (sp != null) {
			return sp.getBoolean(key, false);
		}
		return false;
	}
	
	public static int getInt(String key) {
		if (sp != null) {
			return sp.getInt(key, 0);
		}
		return 0;
	}
	public static boolean remove(String key) {
		if (sp != null) {
			return sp.edit().remove(key).commit();
		}
		return false;
	}
}
