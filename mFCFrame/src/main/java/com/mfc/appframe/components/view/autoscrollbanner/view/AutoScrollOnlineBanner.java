package com.mfc.appframe.components.view.autoscrollbanner.view;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.mfc.appframe.R;
import com.mfc.appframe.components.view.autoscrollbanner.adapter.AdvertImagePagerAdapter;
import com.mfc.appframe.components.view.autoscrollbanner.adapter.AdvertImagePagerAdapter.OnBannerClickListener;

/***
 * 
 * @author LiRuijie
 * 用法：
 * 直接在布局中使用此类，然后在activity中通过findviewbyid实例化此类。
 * 1.直接调用setImageUrls（网络图片）或者setImageIds（本地图片）即可实现显示带指示器的banner图，不自动滚动
 * 2.调用startAutoScroll开始循环自动播放banner图
 * =========以上为最简单用法==========
 * 此外此类支持：
 * 1.设置播放间隔时间 setInterval
 * 2.设置是否显示圆点指示器 setIsShowIndicator
 * 3.设置圆点指示器的样式，各种颜色和边框大小和圆点大小 setIndicatorColor setIndicatorScale
 * 4.设置圆点指示器位置和方向 setIndicatorPosition setIndicatorOrientation
 * 5.设置图片显示的缩放模式 setImageScaleType
 * 6.设置图片点击事件 setOnBannerClickListener
 * 7.设置正在加载时显示的图片（当图片是来自于网络的时候）setOnLoadingImage
 * 8.设置图片加载出错时显示的图片（空的uri地址或者加载出错）setOnloadErrorImage
 * 9.在activity不可见时最好调用stopAutoScroll停止自动播放，并在activity可见时调用startAutoScroll恢复自动轮播
 */
public class AutoScrollOnlineBanner extends RelativeLayout {

	private AutoScrollViewPager 	mViewPager;
	private CirclePageIndicatorB 	indicator;
	private List<String> 			imageUrlList;
	private Integer[] 				imageIdList;
	private Context					context;
	private AdvertImagePagerAdapter mPagerAdapter;
	private ScaleType 				scaleType = ImageView.ScaleType.CENTER_CROP;
	private int 					intervalTime = 0;

	public AutoScrollOnlineBanner(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public AutoScrollOnlineBanner(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public AutoScrollOnlineBanner(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 初始化组合控件
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		View.inflate(context, R.layout.viewpager_layout, this);
		mViewPager = (AutoScrollViewPager) this.findViewById(R.id.view_pager);
		indicator = (CirclePageIndicatorB) this.findViewById(R.id.indicator);
		this.context = context;
	}
	
	private void initViewPager () {
		mPagerAdapter.setImageScaleType(scaleType);//设置默认图片缩放模式是CENTER_CROP
		mViewPager.setAdapter(mPagerAdapter);//设置adapter
		mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 %(mPagerAdapter.isOnline()? imageUrlList.size(): imageIdList.length));
		this.indicator.setViewPager(mViewPager);//绑定指示器
	}

	/***
	 * 设置网络图片的url地址集合，设置后就能显示，但不自动滚动
	 */
	public void setImageUrls(List<String> imageUrls) {
		this.imageUrlList = imageUrls;
		mPagerAdapter = new AdvertImagePagerAdapter(context, imageUrlList);
		initViewPager ();
	}

	/***
	 * 设置要显示的图片的id集合，设置后就能显示，但不自动滚动
	 * 
	 * @param imageIds
	 */
	public void setImageIds(Integer[] imageIds) {
		this.imageIdList = imageIds;
		mPagerAdapter = new AdvertImagePagerAdapter(context, imageIdList);
		initViewPager ();
	}

	/***
	 * 设置自动切换间隔时间
	 * @param time
	 */
	public void setInterval(int time) {
		this.intervalTime = time;
	}
	
	/***
	 * 开始滚动
	 */
	public void startAutoScroll () {
		if (intervalTime == 0) {
			intervalTime = 2500;
		}
		if (mPagerAdapter == null) {
			throw new RuntimeException("please call setImageIds or setImageUrls before call startAutoScroll");
		}
		this.mViewPager.setInterval(intervalTime);
		mViewPager.startAutoScroll();
	}
	
	/***
	 * 停止滚动
	 */
	public void stopAutoScroll () {
		mViewPager.stopAutoScroll();
	}
	
	/***
	 * 设置是否显示圆点指示器，默认不显示
	 * @param isShow
	 */
	public void setIsShowIndicator (boolean isShow) {
		
		if (isShow) {
			this.indicator.setVisibility(View.VISIBLE);
		}
		else {
			this.indicator.setVisibility(View.GONE);
		}
	}
	
	/***
	 * 获取展示图片的ViewPager
	 * 
	 * @return
	 */
	public AutoScrollViewPager getViewPager() {
		return this.mViewPager;
	}

	/***
	 * 获取Indicator
	 * 
	 * @return
	 */
	public CirclePageIndicatorB getIndicator() {
		return this.indicator;
	}
	
	/***
	 * 设置banner图的点击事件
	 * @param listener
	 */
	public void setOnBannerClickListener (OnBannerClickListener listener) {
		mPagerAdapter.setOnBannerClickListener(listener);
	}
	
	/***
	 * 设置图片的缩放模式ImageView.ScaleType.FIT_XY;FIT_CENTER;CENTER_CROP...
	 * @param scaleType
	 */
	public void setImageScaleType (ScaleType scaleType) {
		this.scaleType = scaleType;
		mPagerAdapter.setImageScaleType(this.scaleType);//设置默认图片缩放模式是CENTER_CROP
	}
	
	/***
	 * 设置indicator的颜色，传0表示使用默认样式
	 * @param strokeColor 边框颜色
	 * @param fillColor 焦点颜色
	 * @param pageColor 非焦点颜色
	 */
	public void setIndicatorColor (int strokeColor, int fillColor, int pageColor) {
		this.indicator.setStrokeColor(strokeColor);
		this.indicator.setFillColor(fillColor);
		this.indicator.setPageColor(pageColor);
	}
	
	/***
	 * 设置indicator边框宽度
	 * @param width
	 */
	public void setIndicatorStrokeWidth (float width) {
		this.indicator.setStrokeWidth(width);
	}
	
	/***
	 * 设置指示原点的大小
	 * @param scale
	 */
	public void setIndicatorScale (float scale) {
		this.indicator.setRadius(scale);
	}
	
	/***
	 * 设置indicator的方向，默认水平
	 *  @param oritation LinearLayout.VERTICAL/LinearLayout.HORIZENTAL
	 */
	public void setIndicatorOrientation (int oritation) {
		this.indicator.setOrientation(oritation);
	}
	
	/***
	 * 设置indicator的位置 默认居中，
	 * @param isCenter 如果是false则居左显示 true居中显示
	 */
	public void setIndicatorPosition(boolean isCenter) {
		this.indicator.setCentered(isCenter);
	}
	
	/***
	 * 表示我也不知道这是干嘛的。。。设置为true后indicator就不显示了。。。
	 * @param isSnap
	 */
	public void setSnap (boolean isSnap) {
		this.indicator.setSnap(isSnap);
	}
	
	/***
	 * 设置正在加载时显示的图标，默认是显示程序的icon
	 * @param onLoadingImage
	 */
	public void setOnLoadingImage (Drawable onLoadingImage) {
		this.mPagerAdapter.setOnloadingImage(onLoadingImage);
	}
	
	/***
	 * 设置加载失败时显示的图标，默认是显示程序的icon
	 * @param onLoaderrImage
	 */
	public void setOnloadErrorImage(Drawable onLoaderrImage) {
		this.mPagerAdapter.setOnloadErroImage(onLoaderrImage);
	}
}
