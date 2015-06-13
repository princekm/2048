package com.newgen.game;

import java.util.Random;

import com.newgen.gfx.PM;
import com.newgen.swipe.OnSwipeTouchListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public final class PlayScreen extends Activity 
{
	private RelativeLayout screen;
	private final int BOXES=4;
	public boolean enable;
	private int score;
	private TextView box[][],header,scorePad,hsPad;
	private PM pm;
	private SharedPreferences sharedpreferences;
	private final String MyPREFERENCES = "MyPrefs" ;
	private final String MATRIX="Matrix"; 
	private final String UNDO1="Undo0";
	private final String UNDO2="Undo1";
	private final String UNDO3="Undo2";
	private final String UNDO4="Undo3";
	private final String UNDO5="Undo4";
	private final String SCORE="Score";
	private final String HSCORE="HScore";
	private final String AVAIL_UNDOS="Aundo";
	private int MAT[][]=new int[BOXES][BOXES];
	private final int MAX_UNDOS=5;
	private int avCount,next;
	private String undoList[];
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		init();
		setContentView(screen);
		loadUndoCount();
		if(sharedpreferences.contains(MATRIX))
		{
			String map=sharedpreferences.getString(MATRIX,"");
			if(!map.equals(""))
				loadFromDB();
			else
				newGame();
		}
		else
			newGame();
		loadHScore();
  	  
	}
	
	private void loadUndoCount()
	{
		if(!sharedpreferences.contains(AVAIL_UNDOS))
			resetUndoCount();
		else
			avCount=this.sharedpreferences.getInt(AVAIL_UNDOS,0);
	}
	private void resetUndoCount()
	{
		 Editor editor = sharedpreferences.edit();
		 editor.putInt(AVAIL_UNDOS, MAX_UNDOS);
		 editor.commit();
		 avCount=5;
	}
	private void redUndoCount()
	{
		if(avCount>0)
		{
			Editor editor = sharedpreferences.edit();
			avCount--;
			editor.putInt(AVAIL_UNDOS,avCount);
			editor.commit();
		}
	}
	@Override
	public void onDestroy()
	{
		updateDB(getMatrix());
		super.onDestroy();
	}
	public void show()
	{
		String s="";
		for(int i=0;i<BOXES;++i)
		{
			for(int j=0;j<BOXES;++j)
				s+=MAT[i][j];
		    s+='\n';
		}
		Toast.makeText(this,s,200).show();
	}
	public void performUndo()
	{
//		Handler handler=new Handler();
////		String s="";
//		handler.postDelayed(new Runnable(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				undoList[next]=getMatrix();
//				next=(next+1)%(avCount);
//			}
//			
//		},1);
//				
//////				for(int i=0;i<next;++i)
//////				{
//////					s+=i;
////				}
				Toast.makeText(this,next, 3).show();
	}
	private void saveUndo()
	{
		
			
	}
	private void loadFromDB()
	{
		int count=0;
		String map=sharedpreferences.getString(MATRIX,"");
		String id[]=map.split(",");
		for(int i=0;i<BOXES;++i)
			for(int j=0;j<BOXES;++j)
			{
				this.setBox(i, j,Integer.parseInt(""+id[count]));
				count++;
			}
		score=Integer.parseInt(sharedpreferences.getString(SCORE,""));
		updateScore();
	}
	private void loadHScore()
	{
		int hscore=0;
		if(sharedpreferences.contains(HSCORE))
			 hscore=Integer.parseInt(sharedpreferences.getString(HSCORE,""));
		else
		{
			 Editor editor = sharedpreferences.edit();
	    	  editor.putString(HSCORE,"0");
	    	  editor.commit();
		}
		updateHscore(hscore);

	}
	private void updateHscore(int hscore)
	{
		hsPad.setText("HighScore :"+hscore);
	}
	
	private String getMatrix()
	{
		String map="";
		for(int i=0;i<BOXES;++i)
			for(int j=0;j<BOXES;++j)
				if(blank(i,j))
					map+="0"+",";
				else
					map+=box[i][j].getText().toString()+",";
		return map.substring(0,map.length()-1);
	}
	public void updateDB(String map)
	{
	      Editor editor = sharedpreferences.edit();
	    	  editor.putString(MATRIX,map);
	    	  editor.putString(SCORE,""+score);
	    	  if(!sharedpreferences.contains(HSCORE))
	    		  editor.putString(HSCORE,""+score);
	    	  else
	    	  {
	    		  String hscore=sharedpreferences.getString(HSCORE,"");
	    		  if(Integer.parseInt(hscore)<score)
	    		  {
	    			  editor.remove(HSCORE);
		    		  editor.putString(HSCORE,""+score);
	    		  }
	    	  }
		      editor.commit(); 
		      Toast.makeText(this,"Game Saved",Toast.LENGTH_SHORT).show();
	}
	public void saveHs()
	{
	      Editor editor = sharedpreferences.edit();
		  editor.remove(HSCORE);
		  editor.putString(HSCORE,""+score);
	      editor.commit(); 

	}
	public void loadMAT()
	{
		for(int i=0;i<BOXES;++i)
			for(int j=0;j<BOXES;++j)
			{
				MAT[i][j]=Integer.parseInt(box[i][j].getText().toString());
			}
	}
	public boolean chkHscore()
	{
		 String s=sharedpreferences.getString(HSCORE,"");
		 int hscore=Integer.parseInt(s);
			 if(score>hscore)
			 {
				 updateHscore(score);
				 return true;
			 }
			 return false;
	}
	private void init()
	{
		pm=new PM(this);
		undoList=new String[5];
		for(int i=0;i<4;++i)
			for(int j=0;j<4;++j)
			{
			//	MAT[i]=new int[4];
			//	
			}
//		for(int i=0;i<4;++i)
//			for(int j=0;j<4;++j)
//			{
//				MAT[i][j]=0;
//			}
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		float dim=PM.scrWdth/4f*.9f;
		float left=(int)(PM.scrWdth/4f*.1f)*2;
		float top=(int)pm.getPercByHeight(10);
		screen=new RelativeLayout(this);
		screen.setBackgroundResource(R.color.belizehole);
		header=new TextView(this);
		header.setText("2048");
		enable=true;
		header.setTextColor(Color.WHITE);
		header.setTextSize(pm.dpiToPixel(25));
		screen.addView(header);
		header.setGravity(Gravity.CENTER);
		LayoutParams hparams=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		hparams.topMargin=(int)pm.getPercByHeight(3);
		header.setLayoutParams(hparams);
		scorePad=new TextView(this);
		scorePad.setText("Score :0");
		scorePad.setTextColor(Color.WHITE);
		scorePad.setTextSize(pm.dpiToPixel(20));
		
		screen.addView(scorePad);
		LayoutParams sparams=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		sparams.topMargin=(int)top+(int)(BOXES*dim)+(int)pm.getPercByHeight(4);
		scorePad.setLayoutParams(sparams);
		scorePad.setGravity(Gravity.CENTER);
		hsPad=new TextView(this);
		LayoutParams hsparams=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		hsparams.topMargin=2*(int)top+(int)(BOXES*dim)+(int)pm.getPercByHeight(4)+sparams.height;
		hsPad.setLayoutParams(hsparams);
		screen.addView(hsPad);
		hsPad.setTextColor(Color.WHITE);
		hsPad.setTextSize(pm.dpiToPixel(20));
		hsPad.setText("HighScore :0");
		hsPad.setGravity(Gravity.CENTER);
		box=new TextView[BOXES][BOXES];
		for(int i=0;i<BOXES;++i)
		{
			for(int j=0;j<BOXES;++j)
			{
				LayoutParams params=new RelativeLayout.LayoutParams((int)(dim),(int)(dim));
				params.leftMargin=j*(int)(dim)+(int)(left);
				params.topMargin=i*(int)(dim)+(int)(top);
				box[i][j]=new TextView(this);
				box[i][j].setTextColor(Color.WHITE);
				box[i][j].setTextSize(30);
				box[i][j].setGravity(Gravity.CENTER);
				box[i][j].setPadding(0,0,0,0);
				box[i][j].setBackgroundResource(R.drawable.blank);
				screen.addView(box[i][j],params);
			}
		}
		screen.setOnTouchListener(new OnSwipeTouchListener(this));
		
	}
	public int[] getRandomBox()
	{
		boolean matrix[][]=new boolean[BOXES][BOXES];
		int i,j,pos[],arr[][],count;
		count=0;
		for(i=0;i<BOXES;++i)
			for(j=0;j<BOXES;++j)
				if(blank(i,j))
				{
					matrix[i][j]=false;
					count++;
				}
		        else    
		            matrix[i][j]=true;
		if(count>0)
		{
			arr=new int[count][2];
			int cnt=0;
			for(i=0;i<BOXES;++i)
				for(j=0;j<BOXES;++j)
					if(!matrix[i][j])
					{
						arr[cnt][0]=i;
						arr[cnt][1]=j;
						cnt++;
					}
			Random random=new Random();
			int index=random.nextInt(cnt);
			return arr[index];
		}
		return null;
	}
	public void setBox(int i,int j,int value)
	{
			if(value==0)
				box[i][j].setText("");
			else
				box[i][j].setText(""+value);
			box[i][j].setBackgroundResource(this.getBackground(value));
	}
	
	@Override
	public void onBackPressed()
	{
		new AlertDialog.Builder(
				this)
		    .setTitle("Exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) 
					{
				           finish();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) 
					{
						dialog.cancel();
					}
				}).create().show();
		}
	public void shiftRight()
	{
		int cPtr,tPtr;
		boolean flag;
		for(int i=0;i<BOXES;++i)
		{
			cPtr=BOXES-1;
			tPtr=BOXES-2;
			while(tPtr>=0)
			{
				if(blank(i,tPtr))
					tPtr--;
				else if(equals(i,cPtr,i,tPtr))
				{
					String value=box[i][tPtr].getText().toString();
					setBlank(i,tPtr);
					box[i][tPtr].setBackgroundResource(this.getBackground(0));
					setBox(i,cPtr,2*Integer.parseInt(value));
					cPtr--;
					tPtr--;
					showAlert(2*Integer.parseInt(value));
					score+=2*Integer.parseInt(value);
				}
				else if(!equals(i,cPtr,i,tPtr))
				{
						String value=box[i][tPtr].getText().toString();
						if(!blank(i,cPtr))
							cPtr--;
						setBlank(i,tPtr);
						box[i][tPtr].setBackgroundResource(this.getBackground(0));
						setBox(i,cPtr,Integer.parseInt(value));
						tPtr--;
				}
			}
			
		}
	}
	public void shiftUp()
	{
		int cPtr,tPtr;
		for(int i=0;i<BOXES;++i)
		{
			cPtr=0;
			tPtr=1;
			while(tPtr<BOXES)
			{
				if(blank(tPtr,i))
					tPtr++;
				else if(equals(cPtr,i,tPtr,i))
				{
					String value=box[tPtr][i].getText().toString();
					setBlank(tPtr,i);
					box[tPtr][i].setBackgroundResource(this.getBackground(0));
					setBox(cPtr,i,2*Integer.parseInt(value));
					cPtr++;
					tPtr++;
					showAlert(2*Integer.parseInt(value));
					score+=2*Integer.parseInt(value);
					
				}
				else if(!equals(cPtr,i,tPtr,i))
				{
						String value=box[tPtr][i].getText().toString();
						if(!blank(cPtr,i))
							cPtr++;
						setBlank(tPtr,i);
						box[tPtr][i].setBackgroundResource(this.getBackground(0));
						setBox(cPtr,i,Integer.parseInt(value));
						tPtr++;
				}
			}
		}
	}
	
	public void shiftDown()
	{
		int cPtr,tPtr;
		for(int i=0;i<BOXES;++i)
		{
			cPtr=BOXES-1;
			tPtr=BOXES-2;
			while(tPtr>=0)
			{
				if(blank(tPtr,i))
					tPtr--;
				else if(equals(cPtr,i,tPtr,i))
				{
					String value=box[tPtr][i].getText().toString();
					setBlank(tPtr,i);
					box[tPtr][i].setBackgroundResource(this.getBackground(0));
					setBox(cPtr,i,2*Integer.parseInt(value));
					cPtr--;
					tPtr--;
					showAlert(2*Integer.parseInt(value));
					score+=2*Integer.parseInt(value);
						
				}
				else if(!equals(cPtr,i,tPtr,i))
				{
						String value=box[tPtr][i].getText().toString();
						if(!blank(cPtr,i))
							cPtr--;
						setBlank(tPtr,i);
						box[tPtr][i].setBackgroundResource(this.getBackground(0));
						setBox(cPtr,i,Integer.parseInt(value));
						tPtr--;
				}
			}
		}
	}
	public void shiftLeft()
	{
		int cPtr,tPtr;
		for(int i=0;i<BOXES;++i)
		{
			cPtr=0;
			tPtr=1;
			while(tPtr<BOXES)
			{
				if(blank(i,tPtr))
					tPtr++;
				else if(equals(i,cPtr,i,tPtr))
				{
					String value=box[i][tPtr].getText().toString();
					setBlank(i,tPtr);
					box[i][tPtr].setBackgroundResource(this.getBackground(0));
					setBox(i,cPtr,2*Integer.parseInt(value));
					cPtr++;
					tPtr++;
					showAlert(2*Integer.parseInt(value));
					score+=2*Integer.parseInt(value);
					
				}
				else if(!equals(i,cPtr,i,tPtr))
				{
						String value=box[i][tPtr].getText().toString();
						if(!blank(i,cPtr))
							cPtr++;
						setBlank(i,tPtr);
						box[i][tPtr].setBackgroundResource(this.getBackground(0));
						setBox(i,cPtr,Integer.parseInt(value));
						tPtr++;
				}
			}
		}
	}
	public void updateScore()
	{
		scorePad.setText("Score :"+score);
	//	checkHighScore();
	}
	private boolean blank(int i,int j)
	{
		if(box[i][j].getText().toString().equals(""))
			return true;
		return false;
	}
	private boolean equals(int i,int j,int k,int l)
	{
		if(box[i][j].getText().toString().equals(box[k][l].getText().toString()))
			return true;
		return false;
	}
	private void setBlank(int i,int j)
	{
		box[i][j].setText("");
	}
	
	private void newGame()
	{
		int p[]=getRandomBox();
    		setBox(p[0],p[1],2);
    		p=getRandomBox();
    		setBox(p[0],p[1],2);
	}
	public void genDelayed()
	{
		Handler handler=new Handler();
		handler.postDelayed(new Runnable(){

			@Override
			public void run() 
			{
				int p[]=getRandomBox();
				if(p!=null)
				{
					Random r=new Random();
					int i,j,k;
					i=r.nextInt(10);
					j=r.nextInt(10);
					k=r.nextInt(10);
					if(i+j+k>20)
						i=4;
					else
						i=2;
	    			setBox(p[0],p[1],i);
	    			Animation fade_in=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
	    			box[p[0]][p[1]].startAnimation(fade_in);
				}
	    		enable=true;
	    		chkHscore();
			}},2);
		
		
	}
	private void clearData()
	{
		Editor editor = sharedpreferences.edit();
		if(sharedpreferences.contains(MATRIX))
			editor.remove(MATRIX);
	      editor.commit(); 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.play_screen, menu);
		menu.getItem(1).setTitle("Undo"+avCount);
		if(avCount==0)
			menu.getItem(1).setEnabled(false);
		else
			menu.getItem(1).setEnabled(true);
		return true;
	}
	@Override  
    public boolean onOptionsItemSelected(MenuItem item) 
	{  
        switch (item.getItemId()) 
        {  
           case R.id.new_game: 
        	   clearData();
        	   if(chkHscore())
        		   saveHs();
        	   Intent intent=new Intent(this,PlayScreen.class);
        	   finish();
        	   startActivity(intent);
        	   resetUndoCount();
        	   return true;     
           case R.id.undo:
        	   redUndoCount();
        	   item.setTitle("Undo"+avCount);
       		   if(avCount==0)
       				item.setEnabled(false);
       		   else
       				item.setEnabled(true);
        	   return true;
           default:  
                return super.onOptionsItemSelected(item);  
        }  
    }  
	public void showAlert(int limit)
	{
		if(limit==2048)
		{
			new AlertDialog.Builder(
				this)
 			.setTitle("You Won!")
 			.setMessage("Start New Game?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog,int id) 
				{
					Intent intent=new Intent(PlayScreen.this,PlayScreen.class);
					clearData();
					finish();
					startActivity(intent);
				}
			})
			.setNegativeButton("No",new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog,int id) 
				{
					dialog.cancel();
				}
			}).create().show();;
		}
	}
	public int getBackground(int no)
	{
		switch(no)
		{
			case 0:return R.drawable.blank;
			case 2:return R.drawable.n2;
			case 4:return R.drawable.n4;
			case 8:return R.drawable.n8;
			case 16:return R.drawable.n16;
			case 32:return R.drawable.n32;
			case 64:return R.drawable.n64;
			case 128:return R.drawable.n128;
			case 256:return R.drawable.n256;
			case 512:return R.drawable.n512;
			case 1024:return R.drawable.n1024;
			case 2048:return R.drawable.n2048;
			case 4096:return R.drawable.n4096;
			default:return R.drawable.n4096;
		}
	}
}
