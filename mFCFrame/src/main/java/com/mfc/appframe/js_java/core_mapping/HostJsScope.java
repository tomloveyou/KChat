/**
 * Summary: js脚本所能执行的函数空间
 * Version 1.0
 * Date: 13-11-20
 * Time: 下午4:40
 * Copyright: Copyright (c) 2013
 */

package com.mfc.appframe.js_java.core_mapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.util.LogUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mfc.appframe.MainApplication;
import com.mfc.appframe.R;
import com.mfc.appframe.js_java.JsCallback;
import com.mfc.appframe.net.HttpNetWorkTask;
import com.mfc.appframe.net.HttpNetWorkTask.OnHttpResultListener;
import com.mfc.appframe.utils.DateUtil;
import com.mfc.appframe.utils.DebugUtil;
import com.mfc.appframe.utils.SPUtils;

//HostJsScope中需要被JS调用的函数，必须定义成public static，且必须包含WebView这个参数
public class HostJsScope {
	/**
	 * <b>方法描述：清除历史记录，清除当前页面之前的网页历史记录 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-6-8下午3:09:25
	 * @param view
	 * @since Met 1.0
	 * @see
	 */
	public static void clearWebHistory (WebView view) {
		view.clearHistory();
	}
	
	/**
	 * <b>方法描述： 长时间保存一个键值对，只要app不卸载，则值一直保存（基于android SharedPreferences存储）</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-6-8下午4:44:48
	 * @param view
	 * @param key 保存时对应的键key
	 * @param value 要保存的值
	 * @return 保存成功还是失败 true or false
	 * @since Met 1.0
	 * @see
	 */
	public static boolean saveStringToSP (WebView view,String key,String value) {
		return SPUtils.save(key, value);
	}
	
	/**
	 * <b>方法描述： 用于取出通过saveStringToSP保存的值，如果没有返回""。</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-6-8下午4:47:08
	 * @param view
	 * @param key
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public static String getStringFromSP (WebView view,String key) {
		return SPUtils.getString(key);
	}
	
	
	/**
	 * <b>方法描述： 用于暂时保存一些字符串变量，app退出后数据便会丢失</b>
	 * <dd>方法作用： 用于暂时保存一些字符串变量，app退出后数据便会丢失
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-5-25下午5:10:57
	 * @param view
	 * @param key 
	 * @param value
	 * @since Met 1.0
	 * @see
	 */
	public static void putString(WebView view,String key,String value) {
		MainApplication.putTempValue(key, value);
		DebugUtil.err("putString:key="+key+";value="+value);
	}
	
	/**
	 * <b>方法描述：用于取出已经保存的临时字符串值 </b>
	 * <dd>方法作用： 用于取出已经保存的临时字符串值
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-5-25下午5:12:03
	 * @param view
	 * @param key
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public static String getString(WebView view,String key) {
		return MainApplication.getTempValue(key);
	}
	
	
	//---------------- 界面切换类 ------------------
    /**
     * 后退
     * @param view 浏览器
     * */
    public static void goBack(WebView view) {
    	if (view.canGoBack()) {
//    		WebBackForwardList list = view.copyBackForwardList();
//    		for (int i = 0; i < list.getSize(); i++) {
//				DebugUtil.err("第"+i+"个页面："+list.getItemAtIndex(i).getUrl());
//			}
//    		view.goBack();
    		view.loadUrl("javascript:window.history.back();");
		}
    	else {
    		((Activity)view.getContext()).finish();
    	}
    }
    /**
     * 跳转到指定页面，并且清除此activity之上的所有activity
     * @param view 浏览器
     * */
    public static void hrefActivityWithClearTop (WebView view,String activityFullName) {
    	Intent intent = new Intent();  
    	try {
			intent.setClass(view.getContext(), Class.forName(activityFullName));
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			view.getContext().startActivity(intent);  
			((Activity)view.getContext()).finish();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
    }
    /**
     * 跳转到指定页面
     * @param view 浏览器
     * @param activityFullName 要跳转的activity的全路径名称
     * @param isFinishWebActivity 跳转后是否关闭网页界面（关闭WebView对应的Activity） true:关闭网页界面 ； false:不关闭网页界面
     * */
    public static void hrefActivity (WebView view, String activityFullName, boolean isFinishWebActivity) {
    	Intent intent = new Intent();
    	try {
			intent.setClass(view.getContext(), Class.forName(activityFullName));
			view.getContext().startActivity(intent);
			if (isFinishWebActivity) {
				((Activity)view.getContext()).finish();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
    }
    
    /**
     * 跳转到指定页面
     * @param view 浏览器
     * @param activityFullName 要跳转的activity的全路径名称
     * */
    public static void hrefActivity (WebView view, String activityFullName) {
    	hrefActivity(view, activityFullName, false);
    }
    
	
    /**
     * 短暂气泡提醒
     * @param webView 浏览器
     * @param message 提示信息
     * */
    public static void toast (WebView webView, String message) {
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 可选择时间长短的气泡提醒
     * @param webView 浏览器
     * @param message 提示信息
     * @param isShowLong 提醒时间方式
     * */
    public static void toast (WebView webView, String message, int isShowLong) {
        Toast.makeText(webView.getContext(), message, isShowLong).show();
    }

    /**
     * 弹出记录的测试JS层到Java层代码执行损耗时间差
     * @param webView 浏览器
     * @param timeStamp js层执行时的时间戳
     * */
    public static void testLossTime (WebView webView, long timeStamp) {
        timeStamp = System.currentTimeMillis() - timeStamp;
        alert(webView, String.valueOf(timeStamp));
    }

    /**
     * 系统弹出提示框
     * @param webView 浏览器
     * @param message 提示信息
     * */
    public static void alert (WebView webView, String message) {
        // 构建一个Builder来显示网页中的alert对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
        builder.setTitle(webView.getContext().getString(R.string.dialog_title_system_msg));
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    public static void alert (WebView webView, int msg) {
        alert(webView, String.valueOf(msg));
    }

    public static void alert (WebView webView, boolean msg) {
        alert(webView, String.valueOf(msg));
    }
    
    /**
     * <b>方法描述：用于让js调用安卓日期选择控件 </b>
     * <dd>方法作用： 弹出日期选择控件，并在选择完成后回调js的jsMethod函数
     * <dd>适用条件： 
     * <dd>执行流程： 
     * <dd>使用方法： 
     * <dd>注意事项： 
     * 2016-6-13上午10:48:57
     * @param webView
     * @param jsMethod 回调方法名称（注意，此js函数必须包含一个参数）
     * @since Met 1.0
     * @see
     */
    public static void pickDate (final WebView webView,final String jsMethod) {
    	final Calendar calendar = Calendar.getInstance();  
        final DatePickerDialog datePickerDialog = new DatePickerDialog(webView.getContext(), new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				String dateStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
				Date date=null;
				try {
					date = formatter.parse(dateStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String longTime = "("+date.getTime()+")";
				webView.loadUrl("javascript:"+jsMethod+longTime);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    
    /**
     * <b>方法描述：用于让js调用安卓日期选择控件 </b>
     * <dd>方法作用： 弹出日期选择控件，并在选择完成后回调js的jsMethod函数
     * <dd>适用条件： 
     * <dd>执行流程： 
     * <dd>使用方法： 
     * <dd>注意事项： 
     * 2016-6-13上午10:48:57
     * @param webView
     * @param jsMethod 回调方法名称（注意，此js函数必须包含两个参数，第一个参数是用户选择的时间long值，第二个是调用pickDate传入的id）
     * @param id 当页面上有多个控件调用此方法时，可以通过id判断是那个控件在调用此方法
     * @since Met 1.0
     * @see
     */
    public static void pickDate (final WebView webView,final String jsMethod, final String id) {
    	final Calendar calendar = Calendar.getInstance();  
    	final DatePickerDialog datePickerDialog = new DatePickerDialog(webView.getContext(), new OnDateSetListener() {
    		
    		@Override
    		public void onDateSet(DatePicker view, int year, int monthOfYear,
    				int dayOfMonth) {
    			String dateStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    			Date date=null;
    			try {
    				date = formatter.parse(dateStr);
    			} catch (ParseException e) {
    				e.printStackTrace();
    			}
    			String js = "javascript:"+jsMethod+"("+date.getTime()+",'"+id+"')";
    			DebugUtil.err("loadJs："+js);
    			webView.loadUrl(js);
    		}
    	}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    	datePickerDialog.show();
    }

    /**
     * 获取设备IMSI
     * @param webView 浏览器
     * @return 设备IMSI
     * */
    public static String getIMSI (WebView webView) {
        return ((TelephonyManager) webView.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * 获取用户系统版本大小
     * @param webView 浏览器
     * @return 安卓SDK版本
     * */
    public static int getOsSdk (WebView webView) {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 传入Json对象
     * @param view 浏览器
     * @param jo 传入的JSON对象
     * @return 返回对象的第一个键值对
     * */
    public static String passJson2Java (WebView view, JSONObject jo) {
        Iterator iterator = jo.keys();
        String res = null;
        if(iterator.hasNext()) {
            try {
                String keyW = (String)iterator.next();
                res = keyW + ": " + jo.getString(keyW);
            } catch (JSONException je) {

            }
        }
        return res;
    }

    /**
     * 将传入Json对象直接返回
     * @param view 浏览器
     * @param jo 传入的JSON对象
     * @return 返回对象的第一个键值对
     * */
    public static JSONObject retBackPassJson (WebView view, JSONObject jo) {
        return jo;
    }

    public static int overloadMethod(WebView view, int val) {
        return val;
    }

    public static String overloadMethod(WebView view, String val) {
        return val;
    }

    public static class RetJavaObj {
        public int intField;
        public String strField;
        public boolean boolField;
    }

    public static List<RetJavaObj> retJavaObject(WebView view) {
        RetJavaObj obj = new RetJavaObj();
        obj.intField = 1;
        obj.strField = "mine str";
        obj.boolField = true;
        List<RetJavaObj> rets = new ArrayList<RetJavaObj>();
        rets.add(obj);
        return rets;
    }

    public static void delayJsCallBack(WebView view, int ms, final String backMsg, final JsCallback jsCallback) {
        TaskExecutor.scheduleTaskOnUiThread(ms * 1000, new Runnable() {
            @Override
            public void run() {
                try {
                    jsCallback.apply(backMsg);
                } catch (JsCallback.JsCallbackException je) {
                    je.printStackTrace();
                }
            }
        });
    }

    public static long passLongType (WebView view, long i) {
        return i;
    }
    
    public static String text(WebView view, String str) {
    	return str;
    }
    
  //=================以下方法为企业工商app专用方法===============
  	/**
  	 * <b>方法描述： 此方法只给企业服务app使用</b>
  	 * <dd>方法作用： 此方法供企业服务web端调用，web端通过网页登录成功后，获取拿到的sessionID（两个）发送给安卓端，
  	 * 安卓端拿到sessionID后发送给服务器做校验，安卓端收到校验结果后通知js网页进行跳转。
  	 * <dd>适用条件： 
  	 * <dd>执行流程： 
  	 * <dd>使用方法： 
  	 * <dd>注意事项： 
  	 * 2016-6-17上午11:11:59
  	 * @param view
  	 * @since Met 1.0
  	 * @see
  	 */
  	@SuppressWarnings("unchecked")
  	public static void notifyAndroid (final WebView view,String data,final String jsMethod) {
  		DebugUtil.err("网页发送cookie：" + data+";时间："+DateUtil.getCurrDateTime("yyyy-MM-dd HH:mm:ss"));
  		final String[] sList = data.split(";");
  		if (sList.length < 1) {
  			String js = "javascript:"+jsMethod+"(false)";
			DebugUtil.err("loadJs："+js);
			view.loadUrl(js);
			return;
  		}
  		//TODO 注意 下面的地址有变动时记得修改
  		new HttpNetWorkTask("http://125.70.9.226:8081/qygs/tailor!getByCookie.action?value="+sList[0], "get", new OnHttpResultListener() {
  			
  			@Override
  			public void onSuccess(String result) {
  				DebugUtil.err("服务端校验sesisonId返回成功："+result+";时间："+DateUtil.getCurrDateTime("yyyy-MM-dd HH:mm:ss"));
  				if ("true".equals(result)) {
  					MainApplication.putTempValue("jinxin_session_id",sList[0]);
  				}
  				else {//服务器返回false
  					if (sList.length < 2) {//如果只有一个sessionid
  						MainApplication.putTempValue("jinxin_session_id",sList[0]);
  					}
  					else {//如果有两个sessionid
  						MainApplication.putTempValue("jinxin_session_id",sList[1]);
  					}
  				}
  				//通知网页，登录成功
  				/*String js = "javascript:"+jsMethod+"(true)";
  				DebugUtil.err("loadJs："+js);
  				DebugUtil.err("登录成功，通知网页跳转;时间："+DateUtil.getCurrDateTime("yyyy-MM-dd HH:mm:ss"));
  				view.loadUrl(js);*/
  				hrefActivity(view, "cn.cdgs.enterpriseservice.activity.MainActivity",true);
  			}
  			
  			@Override
  			public void onFail(int Code, String errMsg) {
  				//通知网页，登录失败
  				DebugUtil.err("服务端校验sesisonId返回失败："+errMsg+";时间："+DateUtil.getCurrDateTime("yyyy-MM-dd HH:mm:ss"));
  				String js = "javascript:"+jsMethod+"(false)";
  				DebugUtil.err("loadJs："+js);
  				view.loadUrl(js);
  			}
  		}).execute();
  		DebugUtil.err("向服务器发送sessionid做验证:http://125.70.9.226:8081/qygs/tailor!getByCookie.action?value="+sList[0]+";时间："+DateUtil.getCurrDateTime("yyyy-MM-dd HH:mm:ss"));
  		
  	}
  	//================================
    
}