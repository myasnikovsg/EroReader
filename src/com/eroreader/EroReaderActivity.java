package com.eroreader;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EroReaderActivity extends Activity {  
    
    ProgressBar progress;
    boolean isRunning = false;
    Handler handler;
    int done = 0;
    int all = 100;
    TextView text;
	EditText pass;
	TextView incor;
	Button ok;
	Button canc;
    private final static int AUTHORIZE_DIALOG = 0;
    

    @Override
    public void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.main);
        
        getSettings();
    /*    
        if (Secutiry.checkCRC(this).length() > 2)
        	finish();
        if (Secutiry.checkD(this).length() > 2)
        	finish();*/
    	Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.back_start);
	    BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
	    bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
	    findViewById(R.id.progress).getRootView().setBackgroundDrawable(bitmapDrawable);
     
        if (Utils.settings.PASSWORD_SET){
        	showDialog(AUTHORIZE_DIALOG);
        } else
        	continueMaintainance();
    }
    
    boolean base_created;
    
    private synchronized void getSettings(){
        SharedPreferences settings = getSharedPreferences(Utils.PREF, 0);
        Utils.settings = new Settings();
        Utils.settings.readSettings(settings);
        Utils.LAST_FAVORITE_INDEX = settings.getInt("last_favorite_index", 1);
        Utils.LAST_READ_INDEX = settings.getInt("last_read_index", 1);
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        Utils.ORIENTATION = true;
        Utils.SCREEN_HEIGHT = Math.max(metrics.heightPixels, metrics.widthPixels);
        Utils.SCREEN_WIDTH = Math.min(metrics.heightPixels, metrics.widthPixels);
        Utils.DENSITY = metrics.scaledDensity;
        Utils.YDPI = metrics.ydpi;
    }
    
    float percent;
    float allmost_all = 98;
    float rate = 1;
    float time_to_slow = 78;
    float slow_rate = 0.00391f;
    
    private void addPercents(){
    	percent += rate;
    	if (percent > time_to_slow)
    		rate -= 0.00391;
    }
    
    private void changeText(){
    	text.setText("База открывается...");
    }
    
    private void continueMaintainance(){
    	text = (TextView)findViewById(R.id.label);
    	text.setText("База загружается...");
    	base_created = false;
    	percent = 0;
        progress = (ProgressBar)findViewById(R.id.progress);
        handler = new Handler() {
            @Override
            public synchronized void handleMessage(Message msg) {
            	if (!base_created && isRunning && !Utils.BASE_COMPLETELY_OPENED){
            		progress.setProgress((int)(100 * Utils.BASE_PROGRESS));
            	} else {
                	if (isRunning){
                		try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {}
                		progress.setProgress(100);
                		Intent intent = new Intent(EroReaderActivity.this, MainMenu.class);
                		startActivity(intent);
                	} 
                	isRunning = false;
                    EroReaderActivity.this.finish();
                }	
            }
        }; 
         
        progress.setProgress(0);
        
        Thread progressThread = new Thread(new Runnable() {
           public void run() {
               while (isRunning) {
                   try {
                       Thread.sleep(50);
                   } 
                   catch (InterruptedException e) {
                       Log.e("ERROR", "Thread Interrupted");
                   }
                   handler.sendMessage(handler.obtainMessage());
               }
            }
        });
        
        Thread baseOpen = new Thread(new Runnable() {
			
			public void run() {
				Utils.base = new DataBaseHelper(EroReaderActivity.this);
				try {
		        	Utils.base.createDataBase();	 
				} catch (IOException ioe) {
					throw new Error("Unable to create database");
					} 
				try {		 
					progress.setProgress((int)allmost_all);
					Utils.base.openDataBase();
					base_created = true;
				}catch(SQLException sqle){
					throw sqle;
		 	 	}
				Utils.base.readCathegories(); 
			}
		});
        isRunning = true;
        progressThread.start();
        baseOpen.start();
    }
    
    protected Dialog onCreateDialog(int id) {
		  
		LayoutInflater inflater;
		View layout;
		AlertDialog.Builder builder;
		
        switch (id) {
        case AUTHORIZE_DIALOG:
            inflater = getLayoutInflater();
            layout = inflater.inflate(
                R.layout.autoriz, (ViewGroup)findViewById(R.id.lin));

            pass = (EditText)layout.findViewById(R.id.pass_edit);
            incor = (TextView)layout.findViewById(R.id.incorrect);
            incor.setText("");
            
            pass.addTextChangedListener(new TextWatcher() {
				
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				
				public void afterTextChanged(Editable s) {
					if (s.length() < 4){
						ok.setEnabled(false);
						incor.setText("");
					} else
						ok.setEnabled(true);
				}
			});
            
            builder = new AlertDialog.Builder(this);
            builder.setView(layout);
            builder.setMessage("Введите пароль");
            
            ok = (Button)layout.findViewById(R.id.pass_ok);
            ok.setEnabled(false);
            canc = (Button)layout.findViewById(R.id.pass_canc);
            ok.setOnClickListener(new View.OnClickListener(){
				
				public void onClick(View v) {
					if (pass.getText().toString().equals(Integer.toString(Utils.settings.PASSWORD))){
						pass.setText("");
						incor.setText("");
						continueMaintainance();
						dismissDialog(AUTHORIZE_DIALOG);
					} else
					if (pass.getText().length() == 4){
						pass.setText(""); 
						incor.setText("Пароль введен неправильно.");
					} 
				}
            });
            canc.setOnClickListener(new View.OnClickListener(){
				
				public void onClick(View v) {
					pass.setText("");
					dismissDialog(AUTHORIZE_DIALOG);
				}
            });
            
            builder.setCancelable(false);
            return builder.create(); 
        default:
        return null;
        }
    }
    
}