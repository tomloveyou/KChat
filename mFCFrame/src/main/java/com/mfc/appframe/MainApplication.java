package com.mfc.appframe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.mfc.appframe.constant.Constant;
import com.mfc.appframe.utils.AppUtils;
import com.mfc.appframe.utils.CrashHandler;
import com.mfc.appframe.utils.DebugUtil;
import com.mfc.appframe.utils.SPUtils;


import org.litepal.LitePalApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class MainApplication extends LitePalApplication {
	public static Activity currentActivity = null;
	public static boolean isOnForeground = true;
	public static long lastBackgroundTime = System.currentTimeMillis();
	public static Map<String,Activity> allActivitys =new HashMap<>();;// 应用启动的所有activity
	private static Map<String, String> propertiesMap = new HashMap<String, String>();// sets.properties文件
	private static Map<String, String> tempMap = new HashMap<String, String>();// 全局临时保存数据用的map
	public static SharedPreferences sp;// 保存一些需要持久化的小数据
	private static MainApplication sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		// 拷贝litepal配置文件到cache目录 TODO 注意这句话需要在自己项目中的application中调用
//		LitepalXmlUtils.getInstance().copyLitepalXmlToCache();
		// 初始化异常处理类，出现异常保存日志到sd卡上
		CrashHandler catchHandler = CrashHandler.getInstance();
		catchHandler.init(getApplicationContext());
		sInstance = this;
		sp = getSharedPreferences(SPUtils.SP_NAME, Context.MODE_PRIVATE);

		try {
			initAll();
		} catch (IOException e) {
			e.printStackTrace();
			DebugUtil.err("初始化未能完全成功");
		}
	}
	public static void destroyActivity(String key) {
		if (allActivitys.get(key)!=null){
			allActivitys.get(key).finish();
			allActivitys.remove(key);
		}

	}


	/**
	 * <b>方法描述： 所有的初始化操作在这里调用</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-27下午3:19:23
	 * @throws IOException 
	 * @since Met 1.0
	 * @see
	 */
	private void initAll() throws IOException {
		initProperties();//初始化配置文件中的数据到Map集合中
		try {//获取项目工程名字
			Constant.PROJECT_NAME = AppUtils.getProjectName(this);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			DebugUtil.err("工程名字获取失败");
		}
		String appFileName = propertiesMap.get("fileName");
		if (!TextUtils.isEmpty(appFileName)) {
			Constant.PROJECT_NAME = appFileName;
		}
		DebugUtil.log("app文件夹名称为："+Constant.PROJECT_NAME);
		
	}

	public static MainApplication getInstance() {
		return sInstance;
	}

	public static Context getContext() {
		return getInstance().getApplicationContext();
	}
	
	/**
	 * <b>方法描述：将settings.properties配置文件的数据封装到 propertiesMap中</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-27下午3:11:51
	 * @throws IOException
	 * @since Met 1.0
	 * @see
	 */
	public static void initProperties() throws IOException {
		Properties prop = new Properties();
		InputStream in = MainApplication.class.getResourceAsStream("/assets/settings.properties");
		// InputStream in = new BufferedInputStream (new
		// FileInputStream("sets.properties"));
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));
		prop.load(bf);
		Iterator<Entry<Object, Object>> it = prop.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			value = new String(value.toString().getBytes("utf-8"));
			propertiesMap.put(key.toString().trim(), value.toString().trim());
		}
	}
	
	/**
	 * <b>方法描述： 返回装载setting.properties配置文件信息的Map集合</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-27下午3:04:10
	 * @return propertiesMap
	 * @since Met 1.0
	 * @see
	 */
	public static Map<String, String> getProperties() {
		return propertiesMap;
	}
	
	/**
	 * <b>方法描述： 获取properties中key对应的vaule</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-27下午3:18:30
	 * @param key
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public static String getPropertyValue(String key) {
		String value = "";
		try {
			if (null != propertiesMap) {
				value = propertiesMap.get(key.trim()).trim();
			}
		} catch (Exception e) {
		}
		return value;
	}
	
	/**
	 * <b>方法描述： 获取临时保存在tempMap中的值</b>
	 * <dd>方法作用： 根据key，获取临时保存在tempMap中的值。
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-5-25下午5:03:08
	 * @param key 对应的key
	 * @return key所对应的value，如果没有返回"";
	 * @since Met 1.0
	 * @see
	 */
	public static String getTempValue (String key) {
		String value = "";
		try {
			if (null != tempMap) {
				value = tempMap.get(key.trim()).trim();
			}
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * <b>方法描述：往tempMap中存放值，用于保存一些全局要用的，占用内存小的string变量 </b>
	 * <dd>方法作用： 往tempMap中存放值
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-5-25下午5:06:36
	 * @param key 存放时对应的key
	 * @param value 要保存的value
	 * @since Met 1.0
	 * @see
	 */
	public static void putTempValue (String key,String value) {
		try {
			if (null != tempMap) {
				tempMap.put(key.trim(),value);
			}
		} catch (Exception e) {
		}
	}
}
