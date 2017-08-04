package com.yl.lenovo.kchat.mvp;

import java.util.List;

/**
 * Created by hefuyi on 16/8/19.
 */
public interface BaseListView<T> {
	/**
	 * 返回查询到的数据
	 * 
	 * @param jsonList
	 */
	void success(List<T> jsonList);

	void error(String msg);

	/**
	 * 获取当前分页的页数
	 * 
	 * @return
	 */
	int getcurrentPage();

	/**
	 * 通知页数 变化
	 */
	void notifypagechagnge();

}
