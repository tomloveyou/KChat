package com.mfc.appframe.utils;

import java.text.DecimalFormat;

public class MathUtils {
	/**
	 * <b>方法描述： originalNum四舍五入保留小数点后decimalCount位</b>
	 *  <dd>方法作用： 四舍五入保留小数点后给定位数
	 *  <dd>适用条件： 
	 *  <dd>执行流程： 
	 *  <dd>使用方法： 
	 *  <dd>注意事项： 
	 *  <dd>日期：2016-11-25上午10:19:49
	 * @param originalNum 要操作的原始数
	 * @param decimalCount 保留小数点后多少位
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public static double keepDecimalWithRound(double originalNum,int decimalCount) {
		StringBuffer format = new StringBuffer(".");
		for (int i=0; i<decimalCount; i++) {
			format.append("#");
		}
		DecimalFormat df=new DecimalFormat(format.toString());
		String st=df.format(originalNum);
		return Double.parseDouble(st);
	}
}
