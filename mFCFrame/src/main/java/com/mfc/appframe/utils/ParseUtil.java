/**
 * System_name：gsmobile
 *
 * 文件名：ParseUtil.java
 *
 * 描述：
 * 
 * 日期：2016-4-21
 * 
 * 本系统是商用软件，未经授权擅自复制或传播本程序的部分或全部将是非法的
 *
 *
 *@version V1.0
 */
package com.mfc.appframe.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <dl>  Class Description
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：
 *  <dd> 类描述： 
 *  <dd> 创建时间：2016-4-21上午11:32:34 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version  
 */
public class ParseUtil {
	/**
	 * <b>方法描述： 解析工商局企业二维码扫描结果信息</b>
	 * <dd>方法作用： 解析二维码扫描得到的字符串 返回map
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-4-21上午11:51:11
	 * @param codeResult
	 * @return
	 * @since Met 1.0
	 * @see
	 * 注册号：510100000010427 
	 * 名称：四川易利数字城市科技有限公司 
	 * 登记机关：成都市工商行政管理局 
	 * 登记日期：2014-08-29 
	 * 企业信用信息公示系统网址：http://gsxt.scaic.gov.cn http://gsxt.cdcredit.gov.cn
	 */
	public static Map<String,String> parseTDCodeResult(String codeResult) {
		Map<String,String> resultMap  = new HashMap<>();
		if (!codeResult.isEmpty()) {
			String[] strs = codeResult.split("\r\n");
			String[] items;
			for (String string : strs) {
				items = string.split("：");
				if (items.length > 1) {
					if (items[0].contains("注册号")) {
						resultMap.put("注册号", items[1].trim());
					}
					else {
						resultMap.put(items[0], items[1].trim());
					}
				}
			}
		}
		return resultMap;
	}
}
