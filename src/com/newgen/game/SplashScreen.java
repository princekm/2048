package com.newgen.game;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashScreen extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		Handler handler=new Handler();
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run() 
			{
				Intent intent=new Intent(SplashScreen.this,PlayScreen.class);
				finish();
				startActivity(intent);
			}},1000);
	}

	@Override
	public void onBackPressed() 
	{
	}


}
