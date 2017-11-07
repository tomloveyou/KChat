package com.mfc.appframe.net;

import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.mfc.appframe.algorithm.DESede;
import com.mfc.appframe.constant.Constant;
import com.mfc.appframe.utils.DebugUtil;


public class WebServiceTools {
	
	public static final String NAMESPACE = "http://service.gongsj.surekam.com";
	
	public static class ServiceReturn{
		public int result;
		public String msg;
		public String method;
	}
	
	
	public static ServiceReturn invokeService(String url, String method, @SuppressWarnings("unchecked") Map<String,String>... params){
		// 声明一个新的自定义数据结构
		ServiceReturn serviceInfo = new ServiceReturn();
		serviceInfo.method = method;
		// 第1步：创建SoapObject对象，并指定WebService的命名空间和调用的方法名
		SoapObject request = new SoapObject(NAMESPACE, method);
		// 第2步：设置WebService方法的参数
		if (params != null){
			for (Map<String, String> map : params) {
				request.addProperty(map.keySet().iterator().next(), map.get(map.keySet().iterator().next()));
			}
		}
		// 第3步：创建SoapSerializationEnvelope对象，并指定WebService的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		// 设置bodyOut属性
		envelope.bodyOut = request;
		// 对于.net开发的WebService服务此属性必须为真
		envelope.dotNet = true;
		// 第4步：创建HttpTransportSE对象，并指定WSDL文档的URL
		HttpTransportSE ht = new HttpTransportSE(url);
		try{
			// 第5步：调用WebService
			ht.debug = true;
			ht.call(NAMESPACE + "/" + method, envelope);
			if (envelope.getResponse() != null){
				// 第6步：使用getResponse方法获得WebService方法的返回结果
				serviceInfo.result = 0;
				serviceInfo.msg = (envelope.getResponse()).toString();
			} else{
				serviceInfo.result = 1;
				serviceInfo.msg = "未返回任何结果";
			}
		} catch (Exception e){
			serviceInfo.result = 1;
			serviceInfo.msg = "访问服务器失败";
			e.printStackTrace();
		} finally{
			envelope = null;
			ht = null;
		}
		System.out.println("msg :"+serviceInfo.msg);
		return serviceInfo;
	}
	/**
	 * 
	 * @param type 0-登录 ;1-获取数据 ;2-修改数据 ; 3-12315;
	 * @param params 请求参数
	 * @param method 方法名称
	 * @return 封装了返回信息的类 ServiceReturn.result; .msg; .method;
	 */
	@SuppressWarnings("unchecked")
	public static ServiceReturn invokeService(int type, String method, Map<String,String>... params){
		String url=null;
		/*switch (type) {
		case Constant.TYPE_REQUEST_LOGIN:
			url = Constant.LOGIN;
			break;
		case Constant.TYPE_REQUEST_GET_DATA:
			url = Constant.GET_DATA;
			break;
		case Constant.TYPE_REQUEST_UPDATE_DATA:
			url = Constant.UPDATE_DATA;
			break;
		case Constant.TYPE_REQUEST_CUSTOMER_LINE://12315类型
			url = Constant.CUSTOMER_LINE_DATA;
			break;

		default:
			url = "";
			break;
		}*/
		// 声明一个新的自定义数据结构
		ServiceReturn serviceInfo = new ServiceReturn();
		serviceInfo.method = method;
		// 第1步：创建SoapObject对象，并指定WebService的命名空间和调用的方法名
		SoapObject request = new SoapObject(NAMESPACE, method);
		// 第2步：设置WebService方法的参数
		try {
			if (params != null){
				for (Map<String, String> map : params) {
					request.addProperty(map.keySet().iterator().next(), DESede.EncryptCode(map.get(map.keySet().iterator().next()), DESede.SKEY, DESede.SIV));
				}
			}
		} catch (Exception e) {
			serviceInfo.result = 1;
			serviceInfo.msg = "参数加密异常";
			e.printStackTrace();
			return serviceInfo;
		}
		
		// 第3步：创建SoapSerializationEnvelope对象，并指定WebService的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		// 设置bodyOut属性
		envelope.bodyOut = request;
		// 对于.net开发的WebService服务此属性必须为真
		envelope.dotNet = true;
		// 第4步：创建HttpTransportSE对象，并指定WSDL文档的URL
		HttpTransportSE ht = new HttpTransportSE(url);
		try{
			// 第5步：调用WebService
			ht.debug = true;
			ht.call(NAMESPACE + "/" + method, envelope);
			if (envelope.getResponse() != null){
				// 第6步：使用getResponse方法获得WebService方法的返回结果
				serviceInfo.result = 0;
				serviceInfo.msg = DESede.DecryptCodeUTF8((envelope.getResponse()).toString(), DESede.SKEY, DESede.SIV);
			} else{
				serviceInfo.result = 1;
				serviceInfo.msg = "未返回任何结果";
			}
		} catch (Exception e){
			serviceInfo.result = 1;
			serviceInfo.msg = "访问服务器失败";
			e.printStackTrace();
		} finally{
			envelope = null;
			ht = null;
		}
		DebugUtil.log("msg :"+serviceInfo.msg);
		return serviceInfo;
	}
}
