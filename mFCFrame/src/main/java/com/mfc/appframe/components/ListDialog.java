package com.mfc.appframe.components;

import java.util.ArrayList;
import java.util.List;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.mfc.appframe.R;
import com.mfc.appframe.adapter.CommonAdapter;
import com.mfc.appframe.adapter.ViewHolder;
import com.mfc.appframe.utils.PropertyUtil;

/***
 * 显示带listview的对话框
 * 
 * @author admin
 * 
 */
public class ListDialog {
	private AlertDialog dialog;
	private List<String> texts = new ArrayList<String>();
	private Context context;
	private View dialog_content;
	private ListView lv_jump_list;
	private TextView tv_list_dialog_title;
	private CommonAdapter<String> adapter;

	public ListDialog(final String[] texts, Context context) {
		this(arrayToList(texts), context);
	}

	public ListDialog(final List<String> texts, Context context) {
		this.texts = texts;
		this.context = context;
		dialog = new AlertDialog.Builder(context).create();
		dialog_content = View.inflate(context, R.layout.dialog_jump_list, null);
		tv_list_dialog_title = (TextView) dialog_content
				.findViewById(R.id.tv_list_dialog_title);
		tv_list_dialog_title.setBackgroundColor(PropertyUtil.getMainColor());
		lv_jump_list = (ListView) dialog_content
				.findViewById(R.id.lv_jump_list);
		List<String> list = new ArrayList<String>();
		list.addAll(texts);
		lv_jump_list.setAdapter(adapter = new CommonAdapter<String>(context,
				list, R.layout.item_jump_list) {

			@Override
			public void convert(ViewHolder holder, String t) {
				holder.setText(R.id.tv_name, texts.get(holder.getPosition()));
			}
		});
	}

	public ListDialog(Context context) {
		this.context = context;
		dialog = new AlertDialog.Builder(context).create();
		dialog_content = View.inflate(context, R.layout.dialog_jump_list, null);
		tv_list_dialog_title = (TextView) dialog_content
				.findViewById(R.id.tv_list_dialog_title);
		tv_list_dialog_title.setBackgroundColor(PropertyUtil.getMainColor());
		lv_jump_list = (ListView) dialog_content
				.findViewById(R.id.lv_jump_list);
		lv_jump_list.setAdapter(adapter = new CommonAdapter<String>(context,
				texts, R.layout.item_jump_list) {
			@Override
			public void convert(ViewHolder holder, String t) {
				holder.setText(R.id.tv_name, texts.get(holder.getPosition()));
			}
		});
	}

	public ListDialog setOnItemClickListener(
			OnItemClickListener onItemClickListener) {
		if (onItemClickListener != null) {
			lv_jump_list.setOnItemClickListener(onItemClickListener);
		}
		return this;
	}

	/***
	 * 设置菜单项文字数组
	 * 
	 * @param texts
	 * @return
	 */
	public ListDialog setTexts(String[] texts) {
		this.texts = arrayToList(texts);
		adapter.setData(this.texts);
		return this;
	}

	/***
	 * 设置菜单项文字数组
	 * 
	 * @param texts
	 * @return
	 */
	public ListDialog setTexts(List<String> texts) {
		this.texts = texts;
		adapter.setData(this.texts);
		return this;
	}

	/***
	 * 设置对话框标题
	 */
	public ListDialog setDialogTitle(String title) {
		tv_list_dialog_title.setText(title);
		return this;
	}

	/***
	 * 显示dialog
	 */
	public void showListDialog() {
		dialog.show();
		dialog.getWindow().setContentView(dialog_content);
	}

	/***
	 * 关闭dialog
	 */
	public void dismissListDialog() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/***
	 * 对话框关闭回调
	 */
	public void setOnDialogDismissListener(OnDismissListener onDismissListener) {
		dialog.setOnDismissListener(onDismissListener);
	}

	/***
	 * 获取dialog对象
	 * 
	 * @return
	 */
	public AlertDialog getAlertDialog() {
		return dialog;
	}

	private static List<String> arrayToList(String[] strs) {
		List<String> strList = new ArrayList<String>();
		for (String string : strs) {
			strList.add(string);
		}
		return strList;
	}
	
	public ListView getListView() {
		return lv_jump_list;
	}
}
