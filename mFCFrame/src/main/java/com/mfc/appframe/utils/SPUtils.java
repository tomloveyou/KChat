package com.mfc.appframe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mfc.appframe.MainApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SPUtils {
	private static SharedPreferences sp = MainApplication.sp;
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
	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 *
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void put(Context context, String key, Object object)
	{

		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String)
		{
			editor.putString(key, (String) object);
		} else if (object instanceof Integer)
		{
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean)
		{
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float)
		{
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long)
		{
			editor.putLong(key, (Long) object);
		} else
		{
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 *
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject)
	{
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);

		if (defaultObject instanceof String)
		{
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer)
		{
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean)
		{
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float)
		{
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long)
		{
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key)
	{
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
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
	public static boolean getSettingBoolean(String key) {
		if (sp != null) {
			return sp.getBoolean(key, true);
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
	public static void clearData(String key,String value) {
		if (sp != null) {
		sp.edit().clear().commit();
		sp.edit().putString(key, value).commit();
		}
		
	}
	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 *
	 * @author zhy
	 *
	 */
	private static class SharedPreferencesCompat
	{
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 *
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod()
		{
			try
			{
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e)
			{
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 *
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor)
		{
			try
			{
				if (sApplyMethod != null)
				{
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e)
			{
			} catch (IllegalAccessException e)
			{
			} catch (InvocationTargetException e)
			{
			}
			editor.commit();
		}
	}
}
