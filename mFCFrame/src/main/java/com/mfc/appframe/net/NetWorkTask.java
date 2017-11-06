package com.mfc.appframe.net;

import java.util.Map;

import com.mfc.appframe.net.WebServiceTools.ServiceReturn;



import android.os.AsyncTask;
import android.text.TextUtils;

/***
 * 网络请求的task
 * @author lirj
 *	
 */
public class NetWorkTask extends AsyncTask<Map<String,String>,Integer,ServiceReturn> {
	private String url;
	private String method;
	private OnResultListener onResultListener;
	private int type;
	
	public NetWorkTask(String _url, String _method, OnResultListener _onResultListener) {
		this.url = _url;
		this.method = _method;
		this.onResultListener = _onResultListener;
	}
	public NetWorkTask(int type, String _method, OnResultListener _onResultListener) {
		this.method = _method;
		this.onResultListener = _onResultListener;
		this.type = type;
	}

	

	@Override
	protected ServiceReturn doInBackground(Map<String,String>... params) {
		if (TextUtils.isEmpty(url)) {
			return WebServiceTools.invokeService(type, method, params);
		}
		return WebServiceTools.invokeService(url, method, params);
	}
	
	@Override
	protected void onPostExecute(ServiceReturn result) {
		super.onPostExecute(result);
		if (this.onResultListener != null) {
			if (result.result == 0) {//访问成功
				onResultListener.onSuccess(result.msg, result.method);
			}
			else {
				onResultListener.onFail(result.result, result.msg, result.method);
			}
		}
	}

	/***
	 * 网络请求结果监听
	 * @author lirj
	 *
	 */
	public interface OnResultListener {
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
		public void onSuccess(String result,String method);
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
		public void onFail(int Code, String errMsg, String method);
	}

}
