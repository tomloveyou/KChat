package com.yl.lenovo.kchat.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.yl.lenovo.kchat.R;


public class ProgressSpinnerDialog extends Dialog {
	
	private DialogDismissListener dialogDismissListener;
	private int countDownTime ;//自动关闭dialog的剩余时间
	private TextView tvMsg;//显示提示的文字
	private Handler mHandler = new Handler();
	private String mMessage;//显示的提示文字
	private Context mContext;
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			if (countDownTime > 1) {
				countDownTime--;
				if (!TextUtils.isEmpty(mMessage)) {
					tvMsg.setText(mMessage+"..."+countDownTime);
					indicator.startAnimation(rotateAnimation);
					show();
				}
				mHandler.postDelayed(this, 1000);
			}
			else {//如果剩余时间小于等于0
				dismiss();
			}
		}
	};
	private ImageView indicator;
	private RotateAnimation rotateAnimation;
	
	
	public ProgressSpinnerDialog(Context context) {
		super(context, R.style.custom_progress_dialog);
		this.mContext = context;
		setContentView(R.layout.progress_spinner_dialog);
		getWindow().getAttributes().gravity = Gravity.CENTER;//居中显示
		getWindow().getAttributes().width = LayoutParams.WRAP_CONTENT;//包裹内容
		getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
		
		tvMsg = (TextView) findViewById(R.id.id_tv_loadingmsg);
		indicator = (ImageView) findViewById(R.id.loadingImageView);


			indicator.setBackgroundResource(R.mipmap.img_loading);

		rotateAnimation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
		rotateAnimation.setRepeatCount(-1);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setRepeatMode(Animation.RESTART);
    	indicator.startAnimation(rotateAnimation);
	}

    public void setMessage (String strMessage) {
    	if (tvMsg != null) {
    		tvMsg.setText(strMessage);
    	}
    }
    
    /***
     * 显示提示对话框
     * @param _dismissTime 自动消失时间，当值<=0时表示不自动关闭，否则在对应时间后自动关闭对话框
     * @param dismissListener 对话框结束后的回调
     */
    public void show(final int _dismissTime, String _message,DialogDismissListener dismissListener) {
    	if (mContext == null) {
    		return;
    	}
    	this.mMessage = _message;
    	this.dialogDismissListener = dismissListener;
    	
    	if(_dismissTime > 0) {
    		this.countDownTime = _dismissTime;
    		mHandler.postDelayed(runnable, 1000);
    	}
    	else {
    		tvMsg.setText(mMessage);
    		indicator.startAnimation(rotateAnimation);
    		super.show();
    	}
    }
    
    
    
    @Override
	public void dismiss() {
		super.dismiss();
		if(dialogDismissListener != null) {
			dialogDismissListener.onDialogDismissListener();
		}
	    dialogDismissListener = null;
	}

	public void setOnDialogDismissListener (DialogDismissListener listener) {
    	this.dialogDismissListener = listener;
    }
    
    public interface DialogDismissListener {
    	void onDialogDismissListener();
    }
}

