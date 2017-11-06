package com.mfc.appframe.activity;
/*package com.mfc.appframe.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.mfc.appframe.R;
import com.mfc.appframe.base.BaseActivity;
import com.mfc.appframe.constant.Constant;
import com.mfc.appframe.net.NetWorkTask;
import com.mfc.appframe.net.NetWorkTask.OnResultListener;
import com.mfc.appframe.service.UpdateService;
import com.mfc.appframe.utils.AppUtils;
import com.mfc.appframe.utils.DeviceUtil;
import com.mfc.appframe.utils.HrefUtils;
import com.mfc.appframe.utils.ToastUtil;

*//***
 * app进入的欢迎页面Splash
 * @author lirj
 *
 *//*
public class SplashActivity extends BaseActivity implements OnResultListener, OnClickListener{
	private Handler handler = new Handler();
	private NetWorkTask netTask;
	private Runnable runnable;
	private String updateUrl;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		netTask = new NetWorkTask(Constant.TYPE_REQUEST_LOGIN, Constant.METHOD_APP_UPDATE, this);
		Map<String,String> params = new HashMap<String, String>();
		try {
			params.put("versionCode", AppUtils.getVersionCode(this)+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		netTask.execute(params);
		handler.postDelayed(runnable = new Runnable() {
			
			@Override
			public void run() {
				netTask.cancel(true);
				HrefUtils.hrefActivity(SplashActivity.this, LoginActivity.class , 3);
				finish();
			}
		}, 3000);
	}
	
	@Override
	public void onSuccess(String result, String method) {
		//TODO 如果有新版本移除handler任务
		if ("0".equals(result)) {
			ToastUtil.showToast("已经是最新版本");
		}
		else {//弹出更新提示对话框
			updateUrl = result;
			handler.removeCallbacks(runnable);//移除runnable任务
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.check_update)
			.setMessage(R.string.update_available_msg)
			.setCancelable(false)
			.setPositiveButton(R.string.sure, this)
			.setNegativeButton(R.string.cancel, this)
			.create().show();
		}
	}
	
	@Override
	public void onFail(int Code, String errMsg, String method) {
		// TODO Auto-generated method stub
		System.out.println();
		
	}

	//对话框点击事件回调
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			//检查网络环境，如果是非wifi提示会消耗流量
			checkNetEnvir();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			HrefUtils.hrefActivity(SplashActivity.this, LoginActivity.class , 3);
			finish();
			break;

		default:
			break;
		}
	}

	*//***
	 * 启动更新服务，启动登录页面
	 *//*
	private void startUpdateService() {
		Intent intent = new Intent(this, UpdateService.class);
		intent.putExtra("updatepath", updateUrl);
		startService(intent);
		HrefUtils.hrefActivity(SplashActivity.this, LoginActivity.class , 3);
		finish();
	}
	
	private void checkNetEnvir() {
		
		if (!DeviceUtil.isWifiActive(this)) {//非wifi提示消耗流量
			new AlertDialog.Builder(this)
			.setTitle(R.string.net_tip)
			.setMessage(R.string.not_wifi_tip)
			.setCancelable(false)
			.setPositiveButton(R.string.sure, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//继续下载
					startUpdateService();
				}
			})
			.setNegativeButton(R.string.cancel, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//取消下载
					HrefUtils.hrefActivity(SplashActivity.this, LoginActivity.class , 3);
					finish();
				}
			})
			.create().show();
		}
		else {
			startUpdateService();
		}
	}
}
*/