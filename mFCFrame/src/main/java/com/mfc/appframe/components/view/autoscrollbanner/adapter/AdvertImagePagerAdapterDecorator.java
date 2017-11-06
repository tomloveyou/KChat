package com.mfc.appframe.components.view.autoscrollbanner.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.mfc.appframe.components.view.autoscrollbanner.RecyclingPagerAdapter;

public class AdvertImagePagerAdapterDecorator extends RecyclingPagerAdapter {
	private AdvertImagePagerAdapter adapter;

	public AdvertImagePagerAdapterDecorator(AdvertImagePagerAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		return adapter.getView(position, convertView, container);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

}
