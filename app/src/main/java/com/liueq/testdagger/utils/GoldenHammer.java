package com.liueq.testdagger.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.liueq.testdagger.TestApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liueq on 28/10/2015.
 */
public class GoldenHammer {

	public static void hideInputMethod(Context context, EditText view){
		view.clearFocus();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void showInputMethod(Context context, EditText view){
		view.setFocusable(true);
		view.requestFocus();

		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}

	public static Bitmap getBitmapFromDrawable(Drawable drawable){
		if(drawable == null){
			return null;
		}

		if(drawable instanceof BitmapDrawable){
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try{
			Bitmap bitmap;

			if(drawable instanceof ColorDrawable){
				bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);//TODO 有待查证
			}else{
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);//TODO 有待查证
			}

			Canvas canvas = new Canvas(bitmap);//将canvas关联到 bitmap
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas); //绘制，实际上就是对bitmap进行内容的写入
			return bitmap;
		} catch (OutOfMemoryError e){
			return null;
		}

	}

	/**
	 * Pixel To Dp
	 */
	public static int pixelToDp(int dp, Context context){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
		Float f = px;
		return f.intValue();
	}

	/**
	 * 在没有Context的情况下获取strings
	 * @param id
	 * @return
	 */
	public static String getString(int id){
		return TestApplication.getApplication().getString(id);
	}

	/**
	 * Get timestamp
	 * @return
	 */
	public static String getTimestamp(){
		long time = System.currentTimeMillis();
		return String.valueOf(time);
	}

	/**
	 * return format "yyyy-MM-dd HH-mm-SS"
	 * @param time
	 * @return
	 */
	public static String timeFormat(String time){
		long time_ms;
		try{
			time_ms = Long.valueOf(time);
		}catch (NumberFormatException e){
			e.printStackTrace();
			return "";
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time_ms);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		sdf.setCalendar(cal);
		return sdf.format(cal.getTime());
	}

	/**
	 * return format "yyyyMMddHHmmss"
	 * @param time
	 * @return
     */
	public static String timeFormatB(String time){
		long time_ms;
		try{
			time_ms = Long.valueOf(time);
		}catch (NumberFormatException e){
			e.printStackTrace();
			return "";
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time_ms);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setCalendar(cal);
		return sdf.format(cal.getTime());
	}

	public static boolean isAboveLollipop(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}
}
