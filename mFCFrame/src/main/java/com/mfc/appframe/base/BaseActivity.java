package com.mfc.appframe.base;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfc.appframe.MainApplication;
import com.mfc.appframe.R;
import com.mfc.appframe.adapter.CommonAdapter;
import com.mfc.appframe.adapter.ViewHolder;
import com.mfc.appframe.bean.CheckBoxListItemBean;
import com.mfc.appframe.components.ListDialog;
import com.mfc.appframe.components.MenuPopupUtil;
import com.mfc.appframe.components.MenuPopupUtil.OnMenuItemClickListener;
import com.mfc.appframe.utils.DisplayUtils;
import com.mfc.appframe.utils.PropertyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;


/***
 * 本项目所有activity的直接父类
 * <dl>
 * Class Description
 * <dd>项目名称：gsmobile
 * <dd>类名称：BaseActivity
 * <dd>类描述： 本项目所有activity的直接父类，此类抽取了所有activity的共性，并进行了一些初始化操作
 * <dd>创建时间：2016-3-28下午3:27:55 2016
 * <dd>修改人：无
 * <dd>修改时间：无
 * <dd>修改备注：无
 * </dl>
 * 
 * @author lirj
 * @see
 * @version
 */
public class BaseActivity extends FragmentActivity {
	private FrameLayout f_content;// 放置内容的frame布局
	private RelativeLayout rl_title;// 标题布局
	private Button btn_back;
	private Button btn_menu;
	private TextView tv_title;
	public ListDialog baseListDialog;
	public AlertDialog listCheckBoxDialog;
	private MenuPopupUtil baseMenu;// 菜单popwindow
	private CommonAdapter<CheckBoxListItemBean> checkboxListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ButterKnife.inject(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MainApplication.allActivitys.put(getClass().getName(),this);

	}

	/***
	 * 显示菜单popwindow
	 * 
	 * @param menuTexts
	 *            菜单项文字
	 * @param iconIds
	 *            菜单项图片id 可为null：表示不显示图标
	 * @param view
	 *            要将菜单显示在哪个view的下方
	 * @param onMenuItemClickListener
	 *            菜单点击事件
	 */
	protected void showPopmenuAsDropdown(String[] menuTexts, Integer[] iconIds, View view,
			OnMenuItemClickListener onMenuItemClickListener) {
		if (baseMenu == null) {
			baseMenu = new MenuPopupUtil(DisplayUtils.dip2px(this, 170), -2, this);
		}
		baseMenu.setItemTextsAndImgs(menuTexts, null);
		baseMenu.setOnMenuItemClickListener(onMenuItemClickListener);
		baseMenu.showDropDown(view);
	}

	/**
	 * 设置对话框标题下面的一个线的颜色
	 * 
	 * @param dialog
	 *            对话框
	 * @param color
	 *            颜色值
	 */
	public void dialogTitleLineColor(Dialog dialog, int color) {
		Context context = dialog.getContext();
		int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = dialog.findViewById(divierId);
		divider.setBackgroundColor(color);
	}

	/***
	 * <b>方法描述： 显示列表对话框</b>
	 * <dd>方法作用：
	 * <dd>适用条件：
	 * <dd>执行流程：
	 * <dd>使用方法：
	 * <dd>注意事项：
	 * 
	 * @param texts
	 *            列表文字
	 * @param title
	 * @param onItemClickListener
	 *            列表项点击回调
	 * @param onDismissListener
	 *            列表关闭监听
	 * @since Met 1.0
	 * @see
	 */
	public void showListDialog(String[] texts, String title, final OnItemClickListener onItemClickListener,
			OnDismissListener onDismissListener) {
		// 显示对话框
		if (baseListDialog == null) {
			baseListDialog = new ListDialog(this);
		}
		baseListDialog.setDialogTitle(title);
		baseListDialog.setTexts(texts);
		baseListDialog.setOnItemClickListener(onItemClickListener);
		baseListDialog.setOnDialogDismissListener(onDismissListener);
		baseListDialog.showListDialog();
	}

	/***
	 * <b>方法描述： 显示列表对话框</b>
	 * <dd>方法作用：
	 * <dd>适用条件：
	 * <dd>执行流程：
	 * <dd>使用方法：
	 * <dd>注意事项：
	 * 
	 * @param texts
	 *            列表文字
	 * @param title
	 * @param onItemClickListener
	 *            列表项点击回调
	 * @param onDismissListener
	 *            列表关闭监听
	 * @since Met 1.0
	 * @see
	 */
	public void showListDialog(List<String> texts, String title, final OnItemClickListener onItemClickListener,
			OnDismissListener onDismissListener) {
		// 显示对话框
		if (baseListDialog == null) {
			baseListDialog = new ListDialog(this);
		}
		baseListDialog.setDialogTitle(title);
		baseListDialog.setTexts(texts);
		baseListDialog.setOnItemClickListener(onItemClickListener);
		baseListDialog.setOnDialogDismissListener(onDismissListener);
		baseListDialog.showListDialog();
	}

	View checkbox_dialog_content;
	TextView tv_list_dialog_title;
	ListView lv_jump_list;

	public void showListDialogWithCheckBox(List<CheckBoxListItemBean> texts, String title,
			final OnItemClickListener onItemClickListener, OnDismissListener onDismissListener) {
		// 显示对话框
		if (listCheckBoxDialog == null) {
			listCheckBoxDialog = new AlertDialog.Builder(this).create();
			checkbox_dialog_content = View.inflate(this, R.layout.dialog_checkbox_list, null);
			tv_list_dialog_title = (TextView) checkbox_dialog_content.findViewById(R.id.tv_list_dialog_title);
			lv_jump_list = (ListView) checkbox_dialog_content.findViewById(R.id.lv_jump_list);
		}
		tv_list_dialog_title.setText(title);
		tv_list_dialog_title.setBackgroundColor(PropertyUtil.getMainColor());
		lv_jump_list.setAdapter(checkboxListAdapter = new CommonAdapter<CheckBoxListItemBean>(this, texts,
				R.layout.item_checkbox_list_dialog) {
			@Override
			public void convert(ViewHolder holder, CheckBoxListItemBean t) {
				holder.setText(R.id.tv_name, t.name);
				holder.setChecked(R.id.checkbox, t.isChecked);
			}
		});
		if (onItemClickListener != null) {
			lv_jump_list.setOnItemClickListener(onItemClickListener);
		}
		if (onDismissListener != null) {
			listCheckBoxDialog.setOnDismissListener(onDismissListener);
		}
	}

	/**
	 * 关闭列表对话框 <b>方法描述： </b>
	 * <dd>方法作用：
	 * <dd>适用条件：
	 * <dd>执行流程：
	 * <dd>使用方法：
	 * <dd>注意事项：
	 * 
	 * @since Met 1.0
	 * @see
	 */
	public void dismissListDialog() {
		if (baseListDialog != null) {
			baseListDialog.dismissListDialog();
		}
	}

	/***
	 * 设置布局文件
	 * 
	 * @param resId
	 */
	public void setContent(int resId) {
		setContentView(R.layout.activity_base);
		f_content = (FrameLayout) findViewById(R.id.f_content);
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		rl_title.setBackgroundColor(PropertyUtil.getMainColor());
		View contentView = View.inflate(this, resId, null);
		f_content.addView(contentView);
	}

	/**
	 * 
	 * <b>方法描述：设置标题栏 </b>
	 * <dd>方法作用：
	 * <dd>适用条件：
	 * <dd>执行流程：
	 * <dd>使用方法：
	 * <dd>注意事项： 该方法必须在setContent方法之后调用
	 * 
	 * @param title
	 *            标题文字
	 * @param isShowBack
	 *            是否显示返回按钮
	 * @param menuText
	 *            传入空字符（null、""）表示隐藏， 传入一个字符表示显示图片 "1" "2" 分别对应
	 *            菜单图标和二维码图标，如需添加在getMenuIcon函数中添加 传入长度大于1的字符串表示显示这个字符串
	 * @param onClickListener
	 *            菜单按钮点击事件
	 * @since Met 1.0
	 * @see
	 */
	public void setTitle(String title, boolean isShowBack, String menuText, OnClickListener onClickListener) {
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_back = (Button) findViewById(R.id.btn_main_back);
		btn_menu = (Button) findViewById(R.id.btn_main_menu);
		tv_title.setText(title);
		if (isShowBack) {
			btn_back.setVisibility(View.VISIBLE);
			btn_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finishself();
				}
			});
		} else {
			btn_back.setVisibility(View.GONE);
		}
		setMenuIcon(menuText);
		if (onClickListener != null) {
			btn_menu.setOnClickListener(onClickListener);
		}
	}

	public void finishself() {
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	/***
	 * 设置标题文本
	 * 
	 * @param resId
	 *            文本资源id
	 */
	public void setTitleText(int resId) {
		tv_title.setText(getString(resId));
	}

	/***
	 * 设置标题文本
	 * 
	 * @param text
	 */
	public void setTitleText(String text) {
		tv_title.setText(text);
	}

	/***
	 * 获取标题栏中的菜单按钮
	 * 
	 * @return
	 */
	public Button getMenuButton() {
		return btn_menu;
	}

	@Override
	protected void onResume() {
		MainApplication.currentActivity = this;
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	public boolean checkPermission(@NonNull String permission) {
		return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.reset(this);
		MainApplication.allActivitys.remove(this);


	}

	// 点击键盘外区域自动隐藏键盘
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/***
	 * 判断app是否处在前台
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null) {
			return false;
		}
		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <b>方法描述： 设置标题栏右边图标</b>
	 * <dd>方法作用：
	 * <dd>适用条件：
	 * <dd>执行流程：
	 * <dd>使用方法：
	 * <dd>注意事项： 2016-4-21上午10:01:46
	 * 
	 * @param menuText
	 *            传入空字符（null、""）表示隐藏，
	 *            传入一个字符表示显示图片（图片：1->R.drawable.icon_btn_menu 2->二维码图标
	 *            详见getMenuIcon方法） 传入长度大于1的字符串表示显示这个字符串
	 * @since Met 1.0
	 * @see
	 */
	public void setMenuIcon(String menuText) {
		if (TextUtils.isEmpty(menuText)) {// 如果传空字符 表示隐藏
			btn_menu.setVisibility(View.INVISIBLE);
		} else if (menuText.length() == 1) {
			btn_menu.setVisibility(View.VISIBLE);
			Drawable drawable = getMenuIcon(menuText);
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			btn_menu.setCompoundDrawables(drawable, null, null, null);
		} else {
			btn_menu.setVisibility(View.VISIBLE);
			btn_menu.setCompoundDrawables(null, null, null, null);
			btn_menu.setText(menuText);
		}
	}

	/**
	 * <b>方法描述： 根据index获取图标</b>
	 * <dd>方法作用：
	 * <dd>适用条件：
	 * <dd>执行流程：
	 * <dd>使用方法：
	 * <dd>注意事项： 2016-4-21上午10:00:43
	 * 
	 * @param iconIndex
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	private Drawable getMenuIcon(String iconIndex) {
		Drawable drawable;
		switch (iconIndex) {
		case "1":// 菜单图标
			drawable = getResources().getDrawable(R.drawable.icon_btn_menu);
			break;
		case "3":// home图标
			drawable = getResources().getDrawable(R.drawable.home);
			break;
		/*
		 * case "2"://二维码图标 drawable = getResources().getDrawable(
		 * R.drawable.erweima); break;
		 */

		default:// 菜单图标
			drawable = getResources().getDrawable(R.drawable.icon_btn_menu);
			break;
		}
		return drawable;
	}

	public void showDatePicker(Context context, OnDateSetListener dateSetListener) {
		// TODO Auto-generated method stub
		DatePickerDialog datePickerDialog = null;

		Calendar calendar = Calendar.getInstance(Locale.CHINA);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		if (datePickerDialog == null) {

			datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, day);
		}
		datePickerDialog.show();
	}

	/**
	 * <b>方法描述： 显示选择日期的对话框</b>
	 * <dd>方法作用：
	 * <dd>适用条件：
	 * <dd>执行流程：
	 * <dd>使用方法：
	 * <dd>注意事项： 2016-4-14下午2:03:47
	 * 
	 * @param context
	 * @param dateSetListener
	 * @param date
	 *            字符串的日期格式（yyyy-MM-dd） null默认显示当前日期
	 * @since Met 1.0
	 * @see
	 */
	public void showDatePicker(Context context, OnDateSetListener dateSetListener, String date) {
		// TODO Auto-generated method stub
		DatePickerDialog datePickerDialog = null;
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (!TextUtils.isEmpty(date)) {
			String[] dates = date.split("-");
			if (dates.length > 2) {
				year = Integer.valueOf(dates[0]);
				month = Integer.valueOf(dates[1]);
				day = Integer.valueOf(dates[2]);
			}
		}

		if (datePickerDialog == null) {

			datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, day);
		}
		datePickerDialog.show();
	}

	/**
	 * 
	 * <b>方法描述： </b>
	 * <dd>方法作用：
	 * <dd>适用条件：
	 * <dd>执行流程：
	 * <dd>使用方法：
	 * <dd>注意事项： 2016-6-22上午10:14:03
	 * 
	 * @param context
	 * @param str
	 * @param minORMax
	 *            0表示最小值；1表示最大值
	 * @param dateSetListener
	 * @since Met 1.0
	 * @see
	 */
	public void showDatePicker(Context context, String str, int minORMax, OnDateSetListener dateSetListener) {
		// TODO Auto-generated method stub
		DatePickerDialog datePickerDialog = null;

		Calendar calendar = Calendar.getInstance(Locale.CHINA);

		if (str != null && !str.isEmpty()) {

			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
				Date date = df.parse(str);
				calendar.setTime(date);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("Date", "传入的日期字符串格式不正确");
			}
		}

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		if (datePickerDialog == null) {

			datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, day);
			if (str != null && !str.isEmpty() && minORMax == 0) {

				datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
			}
			if (str != null && !str.isEmpty() && minORMax == 1) {

				datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
			}
		}
		datePickerDialog.show();
	}

	/**
	 * 
	 * <b>方法描述： </b>
	 * <dd>方法作用： 时间选择
	 * <dd>适用条件：
	 * <dd>执行流程：
	 * <dd>使用方法：
	 * <dd>注意事项：
	 * <dd>日期：2016-6-29上午9:32:23
	 * 
	 * @param context
	 *            上下文对象
	 * @param str
	 *            参数时间
	 * @param minORMax
	 *            0表示最小值；1表示最大值
	 * @param defaultDate
	 *            默认时间
	 * @param dateSetListener
	 * @since Met 1.0
	 * @see
	 */
	public void showDatePicker(Context context, String str, int minORMax, String defaultDate,
			OnDateSetListener dateSetListener) {
		// TODO Auto-generated method stub
		DatePickerDialog datePickerDialog = null;

		Calendar calendar = Calendar.getInstance(Locale.CHINA);

		Date date_maxormin = null;

		if (defaultDate != null && !defaultDate.isEmpty()) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
			Date date;
			try {
				date = df.parse(defaultDate);
				calendar.setTime(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (str != null && !str.isEmpty()) {

			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
				date_maxormin = df.parse(str);
				// calendar.setTime(date_maxormin);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("Date", "传入的日期字符串格式不正确");
			}
		}

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		if (datePickerDialog == null) {

			datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, day);
			if (str != null && !str.isEmpty() && minORMax == 0 && date_maxormin != null) {

				datePickerDialog.getDatePicker().setMinDate(date_maxormin.getTime());
			}
			if (str != null && !str.isEmpty() && minORMax == 1 && date_maxormin != null) {

				datePickerDialog.getDatePicker().setMaxDate(date_maxormin.getTime());
			}
		}
		datePickerDialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		BaseActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public RelativeLayout getTitleBar() {
		return rl_title;
	}

}
