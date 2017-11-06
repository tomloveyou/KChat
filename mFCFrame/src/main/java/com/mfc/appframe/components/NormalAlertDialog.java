/**
 * System_name：gsmobile
 *
 * 文件名：NormalAlertDialog.java
 *
 * 描述：
 * 
 * 日期：2016-4-8
 * 
 * 本系统是商用软件，未经授权擅自复制或传播本程序的部分或全部将是非法的
 *
 * Copyright(C) 足下科技有限公司YC48_第一组 Corporation 2005~2012 
 *
 *@version V1.0
 */
package com.mfc.appframe.components;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mfc.appframe.MainApplication;
import com.mfc.appframe.R;

/**
 * <dl>  Class Description
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：普通消息提示框，对alertdialog简单封装
 *  <dd> 类描述： 
 *  <dd> 创建时间：2016-4-8下午2:44:15 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version  
 */
public class NormalAlertDialog{

	private String title;
	private String contentMsg;
	private AlertDialog.Builder builder; 
	private TextView tv_content;
	private TextView tv_title;
	private Button btn_ok;
	private Button btn_cancel;
	private View content;
	private AlertDialog dialog;
	private boolean cancelable = true;
	private boolean isAutoDismiss = true;
	private OnDismissListener dismissListener;
	private Context context;
	/**  
	 * 
	 * @Description: TODO   
	 * @param context    设定文件   
	 * @throws 
	 * 
	 */
	public NormalAlertDialog(Context context) {
		this.context = context;
		builder = new Builder(context);
		content = View.inflate(context, R.layout.dialog_download_finish, null);
		initView(content);
	}

	/**
	 * <b>方法描述： </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param content
	 * @since Met 1.0
	 * @see 
	 */
	 
	private void initView(View content) {
		tv_title = (TextView) content.findViewById(R.id.tv_title);
		tv_content = (TextView) content.findViewById(R.id.tv_content);
		btn_ok = (Button) content.findViewById(R.id.btn_ok);
		btn_cancel = (Button) content.findViewById(R.id.btn_cancel);
		tv_title.setBackgroundColor(Color.parseColor(MainApplication.getPropertyValue("mainColor")));
	}

	public String getTitle() {
		return title;
	}

	public NormalAlertDialog setTitle(String title) {
		this.title = title;
		tv_title.setVisibility(View.VISIBLE);
		tv_title.setText(title);
		return this;
	}

	public String getContentMsg() {
		return contentMsg;
	}

	/**
	 * <b>方法描述：设置对话框内容 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param contentMsg
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public NormalAlertDialog setContentMsg(String contentMsg) {
		this.contentMsg = contentMsg;
		tv_content.setText(contentMsg);
		return this;
	}
	
	/**
	 * <b>方法描述：设置取消按钮 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param text 按钮文字
	 * @param onClickListener 点击事件，null表示关闭对话框
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public NormalAlertDialog setNegativeButton(String text,final OnClickListener onClickListener) {
		btn_cancel.setVisibility(View.VISIBLE);
		btn_cancel.setText(text);
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					onClickListener.onClick(v);
					if (isAutoDismiss) {NormalAlertDialog.this.dismiss();}
				}
				else {
					NormalAlertDialog.this.dismiss();
				}
			}
		});
		return this;
	}
	
	/**
	 * <b>方法描述： 设置确定按钮</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param text 按钮文字
	 * @param onClickListener 点击事件，null表示关闭对话框
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public NormalAlertDialog setPositiveButton(String text,final OnClickListener onClickListener) {
		btn_ok.setVisibility(View.VISIBLE);
		btn_ok.setText(text);
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					onClickListener.onClick(v);
					if (isAutoDismiss) {NormalAlertDialog.this.dismiss();}
				}
				else {
					NormalAlertDialog.this.dismiss();
				}
			}
		});
		return this;
	}
	
	/**
	 * <b>方法描述：显示对话框 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @since Met 1.0
	 * @see
	 */
	public void show() {
		builder.setCancelable(cancelable);
		if (dialog == null) {
			dialog = builder.create();
			if (dismissListener != null) {
				dialog.setOnDismissListener(dismissListener);
			}
		}
		dialog.show();
		dialog.getWindow().setContentView(content);
	}
	
	/**
	 * <b>方法描述：关闭对话框 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @since Met 1.0
	 * @see
	 */
	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	
	/***
	 * <b>方法描述：设置对话框能否取消 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param cancelable
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public NormalAlertDialog setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
		return this;
	}
	
	/**
	 * <b>方法描述：设置按钮点击后是否自动关闭对话框，默认自动关闭 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param isAutodismiss
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public NormalAlertDialog setAutoDismiss (boolean isAutodismiss) {
		this.isAutoDismiss = isAutodismiss;
		return this;
	}
	
	public NormalAlertDialog setDismissListener(OnDismissListener dismissListener) {
		this.dismissListener = dismissListener;
		return this;
	}

}
