package com.mfc.appframe.activity;

import java.net.URLDecoder;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfc.appframe.R;
import com.mfc.appframe.base.BaseActivity;
import com.mfc.appframe.constant.Constant;
import com.mfc.appframe.js_java.core_mapping.HostJsScope;
import com.mfc.appframe.net.DownloadTask;
import com.mfc.appframe.receiver.NetBroadCastReceiver;
import com.mfc.appframe.utils.DebugUtil;
import com.mfc.appframe.utils.DeviceUtil;
import com.mfc.appframe.utils.HrefUtils;
import com.mfc.appframe.utils.ParseUtil;
import com.mfc.appframe.utils.ToastUtil;
import com.mfc.appframe.webchrome.CustomChromeClient;

/**
 * 显示html网页的activity 要使用时尽量拷贝到具体项目中使用
 * <dl>  Class Description
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：
 *  <dd> 类描述： 用法：bundle.putString(Constant.ENTER_URL_KEY, "url地址");
 *  			HrefUtils.hrefActivity(getActivity(), OAWebActivity.class, 0, bundle);
 *  <dd> 创建时间：2016-3-28下午3:38:55 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version
 */
public class WebActivity extends BaseActivity implements OnClickListener {


	protected WebView mWebView;
	private LinearLayout ll_error;// 出现错误时显示ad
	private Button btn_refresh;
	private Button btn_setting;
	private Button btn_net_setting;// 网络断开设置按钮
	protected TextView tv_error_msg;
	private BroadcastReceiver receiver;
	protected boolean isPageSuccess = true;
	private ValueCallback<Uri> mUploadMessage;
	private ValueCallback<Uri[]> mUploadMessages;//for android>=5.0
	protected FrameLayout fl_web_activity;
	protected WebView newWebView;//新窗口使用的webview
	protected String mUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		initView();
		initWebView(mWebView);// 初始化webview
		mUrl = getIntent().getExtras().getString(Constant.ENTER_URL_KEY);
//		synCookies(this, "http://171.221.172.20/", "JSESSIONID=119193a172506e57cdd090927adc; path=/; expires=Wednesday, 20 Nov 2099 23:12:40 GMT");//; 
		setListener();
		registeReceiver();
	}
	
	/* (non-Javadoc)
	 * @see com.mfc.appframe.base.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mWebView.loadUrl(mUrl);//将加载网页的操作延迟到onResume，这样可以在load之前的onCreate中进行一些操作
	}

	/***
	 * 注册网络监听
	 */
	private void registeReceiver() {
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetBroadCastReceiver(btn_net_setting);
		registerReceiver(receiver, filter);
	}

	private void setListener() {
		btn_refresh.setOnClickListener(this);
		btn_net_setting.setOnClickListener(this);
		// btn_setting.setOnClickListener(this);
	}

	private void initView() {
		ll_error = (LinearLayout) findViewById(R.id.ll_error);
		tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
		// btn_setting = (Button) findViewById(R.id.btn_setting);
		btn_refresh = (Button) findViewById(R.id.btn_refresh);
		btn_net_setting = (Button) findViewById(R.id.btn_net_setting);
		mWebView = (WebView) findViewById(R.id.wv_web_activity);
		fl_web_activity = (FrameLayout) findViewById(R.id.fl_web_activity);
	}

	/***
	 * 根据type判断显示什么视图
	 * 
	 * @param type
	 *            0：显示splash，1：显示webview，2：显示错误提示
	 */
	protected void showView(int type) {
		switch (type) {
		case 0:// 显示splash
			ll_error.setVisibility(View.GONE);
			break;
		case 1:// 显示webview
			if (ll_error.getVisibility() == View.VISIBLE)
				ll_error.setVisibility(View.GONE);
			break;
		case 2:// 显示错误提示
			ll_error.setVisibility(View.VISIBLE);
			btn_refresh.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView(WebView wv) {

		WebSettings webSettings = wv.getSettings();
		webSettings.setUseWideViewPort(true);// 自适应屏幕
		webSettings.setLoadWithOverviewMode(true);// 自适应屏幕
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持js调用window.open方法
		webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
		webSettings.setSupportMultipleWindows(true);// 设置允许开启多窗口
		webSettings.setDomStorageEnabled(true);// 
		webSettings.setJavaScriptEnabled(true);// 设置支持javascript
/*		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
	webSettings.setPluginState(PluginState.ON);*/
		// 此过程会将HostJsScope中的静态方法都封装到一个map中（HashMap<String, Method>()）
		// 初始化好需要注入webView的js字符串并注入
		// mWebView.setWebChromeClient(new CustomChromeClient("HostApp",
		// HostJsScope.class));
		wv.setWebChromeClient(new CustomChromeClient("HostApp",
				HostJsScope.class) {
			// For Android 3.0+ 文件选择
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType) {
				if (mUploadMessage != null)
					return;
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "文件选择"),
						Constant.COMMON_REQUEST_CODE);
			}
			
			// For Android < 3.0
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				openFileChooser(uploadMsg, "");
			}

			// For Android > 4.1.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				openFileChooser(uploadMsg, acceptType);
			}
			
			
			// For Android > 4.4 为了支持安卓5.0和6.0等高版本系统
			@Override
			public boolean onShowFileChooser(WebView webView,
					ValueCallback<Uri[]> filePathCallback,
					FileChooserParams fileChooserParams) {
				if (mUploadMessages != null
						)
					return false;
				mUploadMessages = filePathCallback;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "文件选择"),
						Constant.COMMON_REQUEST_CODE);
				return true;
			}

			/*
			 */
			@Override
			public void onCloseWindow(WebView window) {//html中，用js调用.close(),会回调此函数
				super.onCloseWindow(window);
				DebugUtil.err("关闭当前窗口");
				if (newWebView != null) {
					fl_web_activity.removeView(newWebView);
					newWebView.destroy();
					newWebView = null;
				}
			}

			/*
			 */
			@Override
			public boolean onCreateWindow(WebView view, boolean isDialog,
					boolean isUserGesture, Message resultMsg) {//html中调用window.open()，会回调此函数
				DebugUtil.err("创建新窗口：" + isDialog + ";" + isUserGesture + ";"
						+ resultMsg);
				newWebView = new WebView(WebActivity.this);
				initWebView(newWebView);
				fl_web_activity.addView(newWebView);
				WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
				transport.setWebView(newWebView);
				resultMsg.sendToTarget();
				return true;

				// return super.onCreateWindow(view, isDialog, isUserGesture,
				// resultMsg);
			}
		});
		wv.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				DebugUtil.log("PageStart:" + getCookies("http://171.221.172.20/"));
				if (!DeviceUtil.isNetConnected(WebActivity.this)) {
					showView(2);
				}
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					startActivity(intent);
					return true;
				}
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				DebugUtil.log("PageFinishCookie:"
						+ getCookies("http://171.221.172.20/"));
				WebBackForwardList list = view.copyBackForwardList();
				if (mUrl.equals(url)) {//此操作是为了解决返回到最后一个页面不能再返回的问题
					view.clearHistory();
				}
				DebugUtil.log("当前位置:"
						+ list.getCurrentIndex());
				if (isPageSuccess) {
					showView(1);
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				// 网页加载失败
				isPageSuccess = false;
				showView(2);
				tv_error_msg.setText(description + "\n错误代码：" + errorCode
						+ "；\n错误地址：" + failingUrl);
			}
		});
		// 设置下载监听
		wv.setDownloadListener(new DownloadListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				//http://172.27.242.168:1305/tailor/http://www.cdgs.gov.cn/mhwz/downloadfile.jsp?file_id=210016602
				if (url.contains("tailor")) {
					url = url.substring(url.lastIndexOf("http"));
				}
				final String fileName = URLDecoder.decode(url.substring(url
						.lastIndexOf("=") + 1));
				// 注释掉的代码是已下载检查的
				/*
				 * if (AppCacheDirUtil.isFileExist(fileName,
				 * AppCacheDirUtil.getDownloadPath())) { //如果已经存在弹出提示框 new
				 * NormalAlertDialog(OAWebActivity.this) .setTitle("提示")
				 * .setContentMsg(fileName+"已经存在，是否覆盖？") .setCancelable(false)
				 * .setPositiveButton("确定", new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { //覆盖下载 new
				 * DownloadTask(OAWebActivity.this,url,fileName).execute(); } })
				 * .setNegativeButton("取消", null) .show(); } else {
				 */
				// 直接下载
				new DownloadTask(WebActivity.this, url, fileName).execute();
				// }
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (R.id.btn_refresh == v.getId()) {
			isPageSuccess = true;
			mWebView.reload();
		}
		else if (R.id.btn_net_setting == v.getId()) {
			HrefUtils.hrefSettingActivity(this);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (newWebView != null) {//如果当前为新的窗口
				if (newWebView.canGoBack()) {
//					newWebView.goBack();//新窗口返回
					newWebView.loadUrl("javascript:window.history.back();");
				}
				else {//把新窗口移除界面，并销毁
					fl_web_activity.removeView(newWebView);
					newWebView.destroy();
					newWebView = null;
				}
				return true;
			}
			if (mWebView.canGoBack()) {
//				mWebView.goBack();
				mWebView.loadUrl("javascript:window.history.back();");
			}
			else {
				finish();
				WebActivity.this.overridePendingTransition(
						R.anim.push_right_in, R.anim.push_right_out);
			}
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {// 取消注册广播
			unregisterReceiver(receiver);
		}
	}

	/**
	 * 同步一下cookie 注:这里一定要注意一点，在调用设置Cookie之后不能再设置
	 * webView.getSettings().setBuiltInZoomControls(true);
	 * webView.getSettings().setJavaScriptEnabled(true); 这类属性，否则设置Cookie无效。
	 */
	public static void synCookies(Context context, String url, String cookies) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 移除
		DebugUtil.log("同步到webview的cookie" + cookies + ";***url:" + url);
		cookieManager.setCookie(url, cookies);//
		CookieSyncManager.getInstance().sync();
	}

	/***
	 * <b>方法描述：获取cookie </b> <dd>方法作用： <dd>适用条件： <dd>执行流程： <dd>使用方法： <dd>注意事项：
	 * 
	 * @param url
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public String getCookies(String url) {
		CookieManager cookieManager = CookieManager.getInstance();
		if (cookieManager.acceptCookie()) {
			return cookieManager.getCookie(url);
		}
		return null;
	}

	/**
	 * 二维码扫描结果回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描二维码/条码回传
		if (requestCode == Constant.REQUEST_CODE_SCAN
				&& resultCode == RESULT_OK) {
			if (data != null) {
				String content = data
						.getStringExtra(Constant.DECODED_CONTENT_KEY);
				Bitmap bitmap = data
						.getParcelableExtra(Constant.DECODED_BITMAP_KEY);
				Map<String, String> codeResultMap = ParseUtil
						.parseTDCodeResult(content);
				String reg_no = codeResultMap.get("注册号");
				String name = codeResultMap.get("名称");
				DebugUtil.log("解码结果： \n" + content);
				// TODO 复制内容到剪切板
				if (!TextUtils.isEmpty(reg_no)) {
					ToastUtil.showToast("注册号已复制剪切板");
					DeviceUtil.copy(reg_no, this);
				}
			}
		} else if (requestCode == Constant.COMMON_REQUEST_CODE) {// 文件选择结果回调
			if (null == mUploadMessage && null == mUploadMessages)
				return;
			Uri result = data == null || resultCode != RESULT_OK ? null : data
					.getData();
			if (android.os.Build.VERSION.SDK_INT < 20) {
				mUploadMessage.onReceiveValue(result);
			}
			else {
				if (result == null ) {
					mUploadMessages.onReceiveValue(null);
				}
				else {
					mUploadMessages.onReceiveValue(new Uri[]{result});
				}
			}
			mUploadMessage = null;
			mUploadMessages = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mfc.appframe.base.BaseActivity#onBackPressed()
	 */
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//	}
	
	protected void onPageFinished(WebView view, String url) {}
}
