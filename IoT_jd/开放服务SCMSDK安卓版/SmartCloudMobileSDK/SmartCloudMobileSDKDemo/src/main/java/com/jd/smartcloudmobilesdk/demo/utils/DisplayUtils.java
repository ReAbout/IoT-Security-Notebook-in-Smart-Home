package com.jd.smartcloudmobilesdk.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.jd.smartcloudmobilesdk.demo.JDApplication;

public class DisplayUtils {

	private static float mDensity = DisplayMetrics.DENSITY_DEFAULT;
	private static Display display = null;

	public static Display getDisplay() {
		if (display == null) {
			display = ((WindowManager) JDApplication.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		}
		return display;
	}
	
	public static int getDisplayWidth() {
		return getDisplay().getWidth();
	}
	
	public static int getDisplayHeight() {
		return getDisplay().getHeight();
	}
	
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dip2px(float dipValue) {
		return (int) (dipValue * mDensity + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	public static float getBaseline(Paint txtPaint, float txtRegionHeight) {
		FontMetrics fontMetrics = txtPaint.getFontMetrics();
		float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
		if(txtRegionHeight == 0)
			txtRegionHeight = fontTotalHeight;
		return (int) (txtRegionHeight / 2 + fontTotalHeight / 2 - fontMetrics.bottom);
	}

	/**
	 * 获得状态栏的高度
	 */
	public static int getStatusHeight(Context context) {

		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height").get(object)
					.toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	public static BitmapDrawable bitmapToDrawable(Bitmap bitmap,Context context){
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
//		try {
//			Method method = BitmapDrawable.class.getMethod("setTargetDensity", DisplayMetrics.class);
//			DisplayMetrics displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
//			displayMetrics.density = 1;
//			displayMetrics.scaledDensity = 1;
//			method.invoke(drawable,displayMetrics);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return drawable;
	}
	/***
	 * 获取屏幕朝向
	 * @param activity
	 * @return
	 */
	public static int getWindowRotation(Activity activity){
		int r = activity.getWindowManager().getDefaultDisplay().getRotation();
		return r;
	}
	public static int getTextWidth(Paint paint, String str) {
		int w = 0;
		if (str != null && str.length() > 0) {
			int len = str.length();
			float[] widths = new float[len];
			paint.getTextWidths(str, widths);
			for (int j = 0; j < len; j++) {
				w += (int) Math.ceil(widths[j]);
			}
		}
		return w;
	}
}
