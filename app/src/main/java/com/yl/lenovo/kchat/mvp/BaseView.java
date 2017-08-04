package com.yl.lenovo.kchat.mvp;

/**
 * Created by hefuyi on 16/8/19.
 */
public interface BaseView<T> {
	void success(T t);

	void error(String msg);
}
