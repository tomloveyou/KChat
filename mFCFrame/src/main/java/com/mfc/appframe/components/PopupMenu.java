package com.mfc.appframe.components;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import com.mfc.appframe.R;


public class PopupMenu {
	
	private PopupWindows 	menuPop;
	private ListView	listView;
	private View		view;
	
	/***
	 * 创建菜单
	 * @param view
	 * @param _width
	 * @param _height
	 * @param _focusable
	 * @param context
	 */
	public PopupMenu(View view,int _width, int _height, boolean _focusable,Context context) {
		this.view = view;
		View viewContent = View.inflate(context, R.layout.popup_menu_layout, null);
		listView = (ListView) viewContent.findViewById(R.id.lv_popup);
		menuPop = new PopupWindows(viewContent, _width, _height, _focusable);
	}
	
	public ListView getListView() {
		return listView;
	}
	
	public void show() {
		if (menuPop.isShowing()) {
			menuPop.dismiss();
		}
		else {
			menuPop.showAsDropDown(view);
		}
	}
	
}
