package com.newgen.gfx;

import android.content.Context;
import android.view.Display;
import android.app.Activity;

public final class PM 
{
	private Context context;
	private Display display;
	public static float scrHght;
	public static float scrWdth;
	public PM(Context context)
	{
		this.context=context;
		display = ((Activity)context).getWindowManager().getDefaultDisplay();	
		scrHght =	display.getHeight();
		scrWdth=display.getWidth();

	}
	public  float pixelToDpi(float pixel)
	{
		return pixel /this.context.getResources().getDisplayMetrics().density;	
	}
	public float dpiToPixel(float dp)
	{
	    return dp * this.context.getResources().getDisplayMetrics().density;
	}
	public float getPercByWidth(float percentage)
	{
		return ((float)display.getWidth())/100.0f*percentage;
	}
	public float getPercByHeight(float percentage)
	{
		return ((float)display.getHeight())/100.0f*percentage;
	}
	public float getScreenWidth()
	{
		return this.display.getWidth();
	}
	public float getScreenHeight()
	{
		return this.display.getHeight();
	}
}
