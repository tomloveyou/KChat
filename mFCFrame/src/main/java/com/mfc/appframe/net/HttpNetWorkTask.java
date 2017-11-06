package com.mfc.appframe.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.mfc.appframe.net.HttpNetWorkTask.HttpResult;


/***
 * 网络请求的task
 * @author lirj
 *	
 */
public class HttpNetWorkTask extends AsyncTask<Map<String,Object>,Integer,HttpResult> {
	private String url;
	private String method;
	private OnHttpResultListener onResultListener;
	
	public HttpNetWorkTask(String _url, String _method, OnHttpResultListener _onResultListener) {
		this.url = _url;
		this.method = _method;
		this.onResultListener = _onResultListener;
	}

	

	@Override
	protected HttpResult doInBackground(Map<String,Object>... params) {
		HttpResult httpResult = new HttpResult();
		HttpUriRequest httpRequest;
		if ("get".equals(method.toLowerCase())) {
			httpRequest = new HttpGet(url);// 建立http get联机
		}
		else {
	        httpRequest = new HttpPost(url);   //建立HTTP POST联机
	        List <NameValuePair> paramList = new ArrayList <NameValuePair>();   //Post运作传送变量必须用NameValuePair[]数组储存 
	        for (Map<String, Object> map : params) {
	        	paramList.add(new BasicNameValuePair(map.keySet().iterator().next(),
						map.get(map.keySet().iterator().next()).toString()));
	        }
	        try {
				((HttpPost)httpRequest).setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}   
		}
		HttpResponse httpResponse;
		try {
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if ((httpResult.statusCode = httpResponse.getStatusLine().getStatusCode()) == 200) {
				httpResult.result = EntityUtils.toString(httpResponse.getEntity());// 获取相应的字符串
			}
			else {
				httpResult.result = "获取数据失败";
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} // 发出http请求
		return httpResult;
	}
	
	@Override
	protected void onPostExecute(HttpResult result) {
		super.onPostExecute(result);
		if (this.onResultListener != null) {
			if (result.statusCode == 200) {//访问成功
				onResultListener.onSuccess(result.result);
			}
			else {
				onResultListener.onFail(result.statusCode, result.errMsg);
			}
		}
	}

	/***
	 * 网络请求结果监听
	 * @author lirj
	 *
	 */
	public interface OnHttpResultListener {
		/**
		 * <b>方法描述：请求成功 </b>
		 * <dd>方法作用： 
		 * <dd>适用条件： 
		 * <dd>执行流程： 
		 * <dd>使用方法： 
		 * <dd>注意事项： 
		 * 2016-4-11下午2:35:54
		 * @param result 请求成功返回的json结果
		 * @param method ws方法名称
		 * @since Met 1.0
		 * @see
		 */
		public void onSuccess(String result);
		/**
		 * <b>方法描述：请求失败回调 </b>
		 * <dd>方法作用： 
		 * <dd>适用条件： 
		 * <dd>执行流程： 
		 * <dd>使用方法： 
		 * <dd>注意事项： 
		 * 2016-4-11下午2:36:55
		 * @param Code 错误代码
		 * @param errMsg 错误信息
		 * @param method ws方法名
		 * @since Met 1.0
		 * @see
		 */
		public void onFail(int Code, String errMsg);
	}
	
	public class HttpResult {
		public int statusCode;
		public String result;
		public String errMsg;
	}

}
