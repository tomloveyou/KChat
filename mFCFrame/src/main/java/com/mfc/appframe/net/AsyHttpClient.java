package com.mfc.appframe.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 框架地址：http://loopj.com/android-async-http/
 * @author Lrj
 *
 */
public class AsyHttpClient {

	  private static AsyncHttpClient client = new AsyncHttpClient(); //http
//	  private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443); //https

	  /**
	   * <b>方法描述：http get请求 </b>
	   *  <dd>方法作用： 通过get方式请求
	   *  <dd>适用条件： 
	   *  <dd>执行流程： 
	   *  <dd>使用方法： 
	   *  <dd>注意事项： 
	   *  <dd>日期：2016-10-24下午5:31:39
	   * @param context 具体哪个context发起的请求（请求发起者）
	   * @param url 请求地址
	   * @param params 请求参数，用法和Map类似
	   * @param responseHandler 结果回调，常用的有JsonHttpResponseHandler，FileAsyncHttpResponseHandler，
	   * 	BaseJsonHttpResponseHandler（自己实现json解析过程），TextHttpResponseHandler
	   * @since Met 1.0
	   * @see
	   */
	  public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.get(context, url, params, responseHandler);
	  }
	  
	  /**
	   * <b>方法描述：http post请求 </b>
	   *  <dd>方法作用： 通过post方式请求
	   *  <dd>适用条件： 
	   *  <dd>执行流程： 
	   *  <dd>使用方法： 
	   *  <dd>注意事项： 
	   *  <dd>日期：2016-10-24下午5:31:39
	   * @param context 具体哪个context发起的请求（请求发起者）
	   * @param url 请求地址
	   * @param params 请求参数，用法和Map类似
	   * @param responseHandler 结果回调，常用的有JsonHttpResponseHandler，FileAsyncHttpResponseHandler，
	   * 	BaseJsonHttpResponseHandler（自己实现json解析过程），TextHttpResponseHandler
	   * @since Met 1.0
	   * @see
	   */
	  public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.post(context, url, params, responseHandler);
	  }
	  
	  /**
	   * <b>方法描述：http get请求 </b>
	   *  <dd>方法作用： 通过get方式请求
	   *  <dd>适用条件： 
	   *  <dd>执行流程： 
	   *  <dd>使用方法： 
	   *  <dd>注意事项： 
	   *  <dd>日期：2016-10-24下午5:31:39
	   * @param url 请求地址
	   * @param params 请求参数，用法和Map类似
	   * @param responseHandler 结果回调，常用的有JsonHttpResponseHandler，FileAsyncHttpResponseHandler，
	   * 	BaseJsonHttpResponseHandler（自己实现json解析过程），TextHttpResponseHandler
	   * @since Met 1.0
	   * @see
	   */
	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(url, params, responseHandler);
	  }

	  /**
	   * <b>方法描述：http post请求 </b>
	   *  <dd>方法作用： 通过post方式请求
	   *  <dd>适用条件： 
	   *  <dd>执行流程： 
	   *  <dd>使用方法： 
	   *  <dd>注意事项： 
	   *  <dd>日期：2016-10-24下午5:31:39
	   * @param url 请求地址
	   * @param params 请求参数，用法和Map类似
	   * @param responseHandler 结果回调，常用的有JsonHttpResponseHandler，FileAsyncHttpResponseHandler，
	   * 	BaseJsonHttpResponseHandler（自己实现json解析过程），TextHttpResponseHandler
	   * @since Met 1.0
	   * @see
	   */
	  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(url, params, responseHandler);
	  }
	  
	  /**
	   * <b>方法描述：下载数据使用，会返回byte数据 </b>
	   *  <dd>方法作用： 
	   *  <dd>适用条件： 
	   *  <dd>执行流程： 
	   *  <dd>使用方法： 
	   *  <dd>注意事项： 
	   *  <dd>日期：2016-12-10上午11:04:33
	   * @param url 文件地址
	   * @param bHandler 下载回调
	   * @since Met 1.0
	   * @see
	   */
	  public static void downloadFile(String url, BinaryHttpResponseHandler bHandler) {
	      client.get(url, bHandler);
	  }
	  
	  /**
	   * <b>方法描述： 取消请求</b>
	   *  <dd>方法作用： 取消有context发起的所有请求
	   *  <dd>适用条件： 
	   *  <dd>执行流程： 
	   *  <dd>使用方法： 
	   *  <dd>注意事项： 
	   *  <dd>日期：2016-10-24下午5:35:43
	   * @param context 取消context发起的所有请求
	   * @param mayInterruptIfRunning 是否取消正在运行的请求
	   * @since Met 1.0
	   * @see
	   */
	  public static void cancelRequest(Context context, boolean mayInterruptIfRunning) {
		  client.cancelRequests(context, mayInterruptIfRunning);
	  }
	  
	  public static AsyncHttpClient getClient() {
		  return client;
	  }

}
