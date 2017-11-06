package com.mfc.appframe.components.view.wheelview.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mfc.appframe.R;

public class CommonTextAdapter extends AbstractWheelTextAdapter {
	
	
	List<String> list;
	
	public CommonTextAdapter(Context context, List<String> list,
			int currentItem) {
		super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
				24, 14);
		this.list = list;
		setItemTextResource(R.id.tempValue);
	}
	
	

	public void setData(List<String> data) {
		this.list = data;
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		return view;
	}

	@Override
	public int getItemsCount() {
		return list.size();
	}

	@Override
	protected CharSequence getItemText(int index) {
		return list.get(index) + "";
	}

}
