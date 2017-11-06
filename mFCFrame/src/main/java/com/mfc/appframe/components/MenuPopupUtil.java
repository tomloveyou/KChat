package com.mfc.appframe.components;

import java.util.List;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.mfc.appframe.R;
import com.mfc.appframe.adapter.CommonAdapter;
import com.mfc.appframe.adapter.CommonAdapterForArray;
import com.mfc.appframe.adapter.ViewHolder;

public class MenuPopupUtil {
		private PopupWindows menuPopup;
		private ListView listView;
		private Context mContext;
		
		/***
		 * 显示popup菜单的工具类
		 * @param width 菜单宽度 -1表示fill_parent
		 * @param height 菜单高度 -2表示wrap_content, -1表示fill_parent,其他数值表示具体px
		 * @param context
		 */
		public MenuPopupUtil(int width,int height,Context context) {
			this.mContext = context;
			View content = View.inflate(context, R.layout.popup_menu_layout, null);
			menuPopup = new PopupWindows(content, width, height, true);
			//TODO 初始化listview
			listView = (ListView) content.findViewById(R.id.lv_popup);
		}
		
		/***
		 * 设置菜单文本和图标
		 * @param strIds 菜单文字，不可为null
		 * @param imgIds 菜单各文本对应图标，为null表示只显示文本
		 */
		public void setItemTextsAndImgs (final List<String> strList, final List<Integer> imgIds) {
			if (strList == null) {
				throw new IllegalArgumentException("文字不能为空");
			}
			if (imgIds != null && imgIds.size() < strList.size()) {
				throw new IllegalArgumentException("图标数不能小于文字数");
			}
			
			listView.setAdapter(new CommonAdapter<String>(mContext,strList,R.layout.item_menu) {

				@Override
				public void convert(ViewHolder holder, String s) {
					if (imgIds == null) {
						//TODO 设置imageview为gone
						holder.setVisible(R.id.iv_menu_item, false);
					}
					else  {
						//TODO 设置imageView 为visible，设置图片显示，并设置图片资源
						holder.setVisible(R.id.iv_menu_item, true);
						holder.setImageResource(R.id.iv_menu_item, imgIds.get(holder.getPosition()));
					}
					//TODO 设置文字显示
					holder.setText(R.id.tv_menu_item, strList.get(holder.getPosition()));
				}
			});
			
		}
		
		/***
		 * 设置菜单文本和图标
		 * @param strIds 菜单文字，不可为null
		 * @param imgIds 菜单各文本对应图标，为null表示只显示文本
		 */
		public void setItemTextsAndImgs (final String[] strList, final Integer[] imgIds) {
			if (strList == null) {
				throw new IllegalArgumentException("文字不能为空");
			}
			if (imgIds != null && imgIds.length < strList.length) {
				throw new IllegalArgumentException("图标数不能小于文字数");
			}
			
			listView.setAdapter(new CommonAdapterForArray<String>(mContext,strList,R.layout.item_menu) {
				
				@Override
				public void convert(ViewHolder holder, String s) {
					if (imgIds == null) {
						//TODO 设置imageview为gone
						holder.setVisible(R.id.iv_menu_item, false);
					}
					else  {
						//TODO 设置imageView 为visible，设置图片显示，并设置图片资源
						holder.setVisible(R.id.iv_menu_item, true);
						holder.setImageResource(R.id.iv_menu_item, imgIds[holder.getPosition()]);
					}
					//TODO 设置文字显示
					holder.setText(R.id.tv_menu_item, strList[holder.getPosition()]);
				}
			});
			
		}
		/***
		 * 设置菜单条目点击事件
		 * @param onMenuItemClickListener 点击菜单项回调
		 */
		public void setOnMenuItemClickListener (final OnMenuItemClickListener onMenuItemClickListener) {
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (onMenuItemClickListener != null)
					onMenuItemClickListener.OnMenuItemClick(view, position);
					menuPopup.dismiss();
				}
			});
		}
		
		/***
		 * 显示在给定view的下方
		 * @param v
		 */
		public void showDropDown(View v) {
			if (menuPopup.isShowing()) {
				menuPopup.dismiss();
			}
			else {
				menuPopup.showAsDropDown(v);
			}
		}
		
		public interface OnMenuItemClickListener {
			public void OnMenuItemClick(View view,int position);
		}
		
}
