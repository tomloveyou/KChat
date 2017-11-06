package com.mfc.appframe.components;

import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

public class PopupWindows extends PopupWindow {
	
	public PopupWindows(View _contentView, int _width, int _height, boolean _focusable) {
		super(_contentView, _width, _height, _focusable);
		setBackgroundDrawable(new ColorDrawable());
		setTouchable(true);
		setOutsideTouchable(true);
		setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					dismiss();
					return true;
				}
				return false;
			}
		});
	}
}
