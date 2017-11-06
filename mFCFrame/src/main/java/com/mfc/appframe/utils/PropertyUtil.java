/**
 * System_name：HSTCFrame
 *
 * 文件名：PropertyUtil.java
 *
 * 描述：
 * 
 * 日期：2016-5-4
 * 
 * 本系统是商用软件，未经授权擅自复制或传播本程序的部分或全部将是非法的
 *
 *
 *@version V1.0
 */
package com.mfc.appframe.utils;

import android.graphics.Color;
import android.text.TextUtils;

import com.mfc.appframe.MainApplication;

/**
 * <dl>  Class Description
 *  <dd> 项目名称：HSTCFrame
 *  <dd> 类名称：获取settings.properties相关属性
 *  <dd> 类描述： 
 *  <dd> 创建时间：2016-5-4上午11:47:31 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version  
 */
public class PropertyUtil {
	/**
	 * <b>方法描述： 获取配置文件中配置的主色调</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-5-4上午11:50:28
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public static int getMainColor() {
		String mainColor = MainApplication.getPropertyValue("mainColor");
		if (!TextUtils.isEmpty(mainColor)) {
			return Color.parseColor(mainColor);
		}
		return Color.parseColor("#6dacfb");//默认蓝色
	}
	
	/**
	 * <b>方法描述： 获取文字主色调</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-5-4上午11:51:22
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public static int getMainTextColor() {
		String mainTextColor = MainApplication.getPropertyValue("mainTextColor");
		if (!TextUtils.isEmpty(mainTextColor)) {
			return Color.parseColor(mainTextColor);
		}
		return Color.parseColor("#666");//默认灰色
	}
}
