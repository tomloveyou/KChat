package com.mfc.appframe.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.mfc.appframe.MainApplication;
import com.mfc.appframe.R;

/***
 * <dl>  带数字的圆形进度条对话框
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：
 *  <dd> 类描述： 
 *  <dd> 创建时间：2016-4-5下午3:38:21 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version
 */
public class ProgressCircleNumberDialog extends Dialog {
	private RoundProgressBarWidthNumber mRoundProgressBar;
	private TextView		mProgressView;
	private String msg;
	private long max = 100;
	private long progress = 0;
	private Context mContext;
	public ProgressCircleNumberDialog(Context context,String msg) {
		super(context, R.style.custom_progress_dialog);
		mContext = context;
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		this.msg = msg;
		setContentView(R.layout.progress_dialog);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		getWindow().getAttributes().width = screenWidth - 200;
		getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
		initView();
	}

    private void initView() {
    	mProgressView = (TextView) findViewById(R.id.tv_progress);
    	mProgressView.setText(msg);
    	mRoundProgressBar = (RoundProgressBarWidthNumber) findViewById(R.id.pb_progress);
    	String mainColor = MainApplication.getPropertyValue("mainColor");
    	String focusColor = MainApplication.getPropertyValue("focusColor");
    	if (!TextUtils.isEmpty(mainColor)) {
    		mRoundProgressBar.mReachedBarColor = Color.parseColor(MainApplication.getPropertyValue("mainColor"));
    	}
    	if (!TextUtils.isEmpty(focusColor)) {
    		mRoundProgressBar.mUnReachedBarColor = Color.parseColor(MainApplication.getPropertyValue("focusColor"));
    	}
	}
    
    private Handler handler = new Handler() {
    	public void handleMessage(Message msg) {
    		if (msg.obj != null) {
    			mProgressView.setText(msg.obj.toString());
    		}
    		progress = msg.what;
    		if (mRoundProgressBar != null) {
    			mRoundProgressBar.setProgress((int) (progress * 100 / max));
    		}
    	};
    };

	public void setMessage(String _message) {
		Message msg = new Message();
		msg.obj = _message;
    	handler.sendMessage(msg);
    }
	
	public void setMax(int _max) {
		this.max = _max;
	}
	
	public int getMax() {
		return (int) max;
	}
	
	public int getProgress() {
		return (int) (progress * 100 / max);
	}
	
	public void setProgress(int _progress) {
		handler.sendEmptyMessage(_progress);
	}
    
    public void show() {
    	if (mContext instanceof Activity) {
    		if (((Activity)mContext).isFinishing()) {
    			return;
    		}
    	}
    	mRoundProgressBar.setMax(100);
    	mRoundProgressBar.setProgress((int) (progress * 100 / max));
    	super.show();
    }
    
    public void dismiss() {
    	try {
			super.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

