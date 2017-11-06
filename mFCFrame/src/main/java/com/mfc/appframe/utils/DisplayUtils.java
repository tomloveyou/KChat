package com.mfc.appframe.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class DisplayUtils {
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static StateListDrawable newSelector(Context context, int idNormal,
			int idPressed, int idFocused, int idUnable) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = idNormal == -1 ? null : context.getResources()
				.getDrawable(idNormal);
		Drawable pressed = idPressed == -1 ? null : context.getResources()
				.getDrawable(idPressed);
		Drawable focused = idFocused == -1 ? null : context.getResources()
				.getDrawable(idFocused);
		Drawable unable = idUnable == -1 ? null : context.getResources()
				.getDrawable(idUnable);
		// View.PRESSED_ENABLED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressed);
		// View.ENABLED_FOCUSED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_enabled,
				android.R.attr.state_focused }, focused);
		// View.ENABLED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_enabled }, normal);
		// View.FOCUSED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_focused }, focused);
		// View.WINDOW_FOCUSED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_window_focused }, unable);
		// View.EMPTY_STATE_SET
		bg.addState(new int[] {}, normal);
		return bg;
	}
	  /**
     * 验证手机号和座机号
     */
    public static boolean checkPhone(String phoneNumber) {
	    Pattern p = Pattern.compile("^(0\\d{2,3}-\\d{7,8}(-\\d{3,5}){0,1})|(((13[0-9])|(15([0-3]|[1-9]))|(18[0,1-9]))\\d{8})$");
       // Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * 验证经纬度
     */
    public static boolean checkLanLon(String LanLon) {
//	    Pattern p = Pattern.compile("^1[34578]{1}\\d{9}$");
        Pattern p = Pattern.compile("^(\\d{3}\\.\\d{4,}\\,\\d{2}\\.\\d{4,})|([E,W]{1}\\d{7}[N,S]{1}\\d{6})$");
        Matcher m = p.matcher(LanLon);
        return m.matches();
    }
    /**
     * 根据分辨率压缩图片比例
     *
     * @param imgPath
     * @param w
     * @param h
     * @return
     */
    private static Bitmap compressByResolution(String imgPath, int w, int h) {
     BitmapFactory.Options opts = new BitmapFactory.Options();
     opts.inJustDecodeBounds = true;
     BitmapFactory.decodeFile(imgPath, opts);
     
     int width = opts.outWidth;
     int height = opts.outHeight;
     int widthScale = width / w;
     int heightScale = height / h;
     
     int scale;
     if (widthScale < heightScale) { //保留压缩比例小的
      scale = widthScale;
     } else {
      scale = heightScale;
     }
     
     if (scale < 1) {
      scale = 1;
     }
 
     
     opts.inSampleSize = scale;
     
     opts.inJustDecodeBounds = false;
     
     Bitmap bitmap = BitmapFactory.decodeFile(imgPath, opts);
     
     return bitmap;
    }
}
