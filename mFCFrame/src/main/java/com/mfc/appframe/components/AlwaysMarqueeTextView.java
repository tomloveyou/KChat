package com.mfc.appframe.components;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class AlwaysMarqueeTextView extends TextView {
	 
	public AlwaysMarqueeTextView(Context context) {
	super(context);
	}
	 
	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
	super(context, attrs);
	}
	 
	public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	}
	 
	//始终返回true，即一直获得焦点
	@Override
	public boolean isFocused() {
	   return true;
	}
	
	@Override
	protected void onFocusChanged(boolean focused, int direction,Rect previouslyFocusedRect) {  
	}
}