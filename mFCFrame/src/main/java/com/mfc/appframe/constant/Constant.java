package com.mfc.appframe.constant;
 

public class Constant {
	/**
	 * 二维码扫描请求码
	 */
	public static final int REQUEST_CODE_SCAN = 0x0000;
	/**
	 * 二维码扫描后解析得到内容返回值key
	 */
	public static final String DECODED_CONTENT_KEY = "codedContent";
	/**
	 * 二维码扫描后二维码bitmap返回值key
	 */
	public static final String DECODED_BITMAP_KEY = "codedBitmap";
	/**
	 * app目录名称
	 */
	public static String PROJECT_NAME = "hstc";//app目录名称
	public static final String ENTER_URL_KEY = "enter_url_key";
	//================分页查询 分页大小===========
	public static int PAGESIZE = 20;
	//=================请求码======================================
	/*
	 * 通用的请求码
	 * */
	public static final int COMMON_REQUEST_CODE = 9999;
	
	public static final String NOTIFY_ACTION = "NOTIFY_ACTION_TO_ANDROID";
	
}
