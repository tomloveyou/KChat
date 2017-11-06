/**
 * System_name：gsmobile
 *
 * 文件名：DebugUtil.java
 *
 * 描述：
 * 
 * 日期：2016-4-7
 * 
 * 本系统是商用软件，未经授权擅自复制或传播本程序的部分或全部将是非法的
 *
 * Copyright(C) 足下科技有限公司YC48_第一组 Corporation 2005~2012 
 *
 *@version V1.0
 */
package com.mfc.appframe.utils;

import android.util.Log;

/**
 * <dl>  Class Description
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：调试时打印日志的类
 *  <dd> 类描述： 
 *  <dd> 创建时间：2016-4-7上午10:49:33 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version  
 */
public class DebugUtil {
	public static boolean DEBUG = true;
	public static final String TAG = "HSTC";
	
	public static void log (String str) {
		if (DEBUG) {
			Log.d(TAG, str);
		}
	}
	/**
	 * <b>方法描述：对Log.d的封装 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param tag
	 * @param str
	 * @since Met 1.0
	 * @see
	 */
	public static void log (String tag,String str) {
		if (DEBUG) {
			Log.d(tag, str);
		}
	}
	
	public static void err (String str) {
		if (DEBUG) {
			Log.e(TAG, str);
		}
	}
}
