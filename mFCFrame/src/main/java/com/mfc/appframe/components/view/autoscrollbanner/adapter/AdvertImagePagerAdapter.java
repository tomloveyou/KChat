/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.mfc.appframe.components.view.autoscrollbanner.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mfc.appframe.components.view.autoscrollbanner.RecyclingPagerAdapter;
import com.mfc.appframe.utils.AppUtils;
import com.mfc.appframe.utils.DisplayUtils;

/**
 * AdvertImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class AdvertImagePagerAdapter extends RecyclingPagerAdapter {

	private Context 		context;
	private List<String> 	imageUrlList;
	private Integer[] 		imageIDs;
	private OnBannerClickListener listner;
	private int size;
	private Drawable onLoadingImage ;
	private Drawable onLoadErroImage;
	private boolean isInfiniteLoop;
	private boolean isOnline;
	private ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
	private List<String> 	strLists;//用于banner下方显示的文字
//	private DisplayImageOptions options ;

	public AdvertImagePagerAdapter(Context context, List<String> imageUrlList) {
		this.context = context;
		this.imageUrlList = imageUrlList;
		this.size = imageUrlList.size();
		isInfiniteLoop = false;
		isOnline = true;
		onLoadErroImage = onLoadingImage = AppUtils.getAppIcon(context);
	}
	
	public AdvertImagePagerAdapter(Context context, List<String> imageUrlList, List<String> strList) {
		this.context = context;
		this.imageUrlList = imageUrlList;
		this.size = imageUrlList.size();
		this.strLists = strList;
		isInfiniteLoop = false;
		isOnline = true;
		onLoadErroImage = onLoadingImage = AppUtils.getAppIcon(context);
	}
	
	public AdvertImagePagerAdapter(Context context, Integer[] imageIDs) {
		this.context = context;
		this.imageIDs = imageIDs;
		this.size = imageIDs.length;
		isInfiniteLoop = false;
		isOnline = false;
		onLoadErroImage = onLoadingImage = AppUtils.getAppIcon(context);
	}
	
	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : size;
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return position % size;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		if (view == null) {
			ImageView iv= new ImageView(context);
			iv.setScaleType(scaleType);
			view = iv;
		} 
		if (listner != null) {
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listner.onBannerClick(getPosition(position));
				}
			});
		}
		if (isOnline) {
			Glide.with(context)
	        .load(imageUrlList.get(getPosition(position)))
	        .error(onLoadErroImage)
	        .placeholder(onLoadingImage)
	        .crossFade()
	        .into((ImageView)view);
		}
		else {
			((ImageView)view).setImageResource(imageIDs[getPosition(position)]);
		}
		return view;
	}
	
	/*@Override
	public View getView(final int position, View view, ViewGroup container) {
		if (view == null) {//TODO 
			FrameLayout fl = new FrameLayout(context);
			ImageView iv= new ImageView(context);
			iv.setScaleType(scaleType);
			TextView tv = new TextView(context);
			tv.setTextColor(Color.parseColor("#ffffff"));
			tv.setTextSize(DisplayUtils.sp2px(context, 12));
			tv.setBackgroundColor(Color.parseColor("#88000000"));
			tv.setPadding(DisplayUtils.dip2px(context, 5), DisplayUtils.dip2px(context, 5), DisplayUtils.dip2px(context, 5), DisplayUtils.dip2px(context, 5));
			tv.setText("的手机发来的手机付款了几点睡了附近的开始接发的是龙卷风的 ");
			
			LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(context, 180));
			LayoutParams lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(context, 20));
			fl.addView(iv, lp1);
			fl.addView(tv,lp2);
			view = fl;
		} 
		if (listner != null) {
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listner.onBannerClick(getPosition(position));
				}
			});
		}
		if (isOnline) {
			Glide.with(context)
	        .load(imageUrlList.get(getPosition(position)))
	        .error(onLoadErroImage)
	        .placeholder(onLoadingImage)
	        .crossFade()
	        .into((ImageView)((FrameLayout)view).getChildAt(0));
		}
		else {
			((ImageView)((FrameLayout)view).getChildAt(0)).setImageResource(imageIDs[getPosition(position)]);
		}
		return view;
	}*/

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop the isInfiniteLoop to set
	 */
	public AdvertImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}
	
	public boolean isOnline () {
		return this.isOnline;
	}
	
	public void setOnBannerClickListener (OnBannerClickListener listner) {
		this.listner = listner;
	}
	
	public interface OnBannerClickListener {
		public void onBannerClick(int position);
	}
	
	/***
	 * 设置图片的缩放模式
	 * @param scaleType
	 */
	public void setImageScaleType (ScaleType scaleType) {
		this.scaleType = scaleType;
	}
	
	/***
	 * 设置正在加载图片时显示的图片，默认显示程序图标
	 * @param loadingImage
	 */
	public void setOnloadingImage (Drawable loadingImage) {
		this.onLoadingImage = loadingImage;
	}
	
	/***
	 * 设置图片加载失败的时候显示的图片，默认显示程序图标
	 * @param errorImage
	 */
	public void setOnloadErroImage (Drawable errorImage) {
		this.onLoadErroImage = errorImage;
	}
	
	public class ViewHolder{
		public ImageView iv;
		public TextView tv;
	}
}
