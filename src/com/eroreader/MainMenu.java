package com.eroreader;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;


public class MainMenu extends ListActivity {
	Display display;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		display = getWindowManager().getDefaultDisplay();
		Settings.applyScreenSettings(this);
		
		boolean f = false;
		if (f)
			name = new String[] {"Продолжить", "Последние прочтённые", "Библиотека", "Закладки", "Поиск", "Настройки", "Оставить отзыв", "Выход"}; else
				name = new String[] {"Последние прочтённые", "Библиотека", "Закладки", "Поиск", "Настройки", "Оставить отзыв", "Выход"};
		RowMenuAdapter adapter = new RowMenuAdapter(this, name);
		setListAdapter(adapter);
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id){
		if (name[position].startsWith("Выход"))
			finish();
		if (name[position].startsWith("Настройки")){
			Utils.main_context = this;
			Intent intent = new Intent(MainMenu.this, SettingsView.class);
            startActivity(intent);
		}
		if (name[position].startsWith("Оставить")){
		    String url = "http://google.com";  
		    Intent intent = new Intent(Intent.ACTION_VIEW);  
		    intent.setData(Uri.parse(url));  
		    startActivity(intent);  
		}
		if (name[position].startsWith("Библиотека")){
			Intent intent = new Intent(MainMenu.this, Cathegories.class);
			startActivity(intent);
		}
		if (name[position].startsWith("Закладки")){
			Utils.base.findFavorites(0);
			StoryList.type = StoryList.FAVORITE;
			Intent intent = new Intent(MainMenu.this, StoryList.class);
			startActivity(intent);
		}
		if (name[position].startsWith("Последние")){
			Utils.base.findLastRead(0);
			StoryList.type = StoryList.LAST;
			Intent intent = new Intent(MainMenu.this, StoryList.class);
			startActivity(intent);
		}
		if (name[position].startsWith("Поиск")){
			StoryList.type = StoryList.SEARCH;
			Utils.stories = new ArrayList<Story>();
			Intent intent = new Intent(MainMenu.this, Search.class);
			startActivity(intent);
		}
		if (name[position].startsWith("Продолжить")){
			if (Utils.curStory != null){
				Intent intent = new Intent(MainMenu.this, ReaderActivity.class);
				startActivity(intent);
			}
		}
	}
	
	public synchronized void refresh() {
		if (Utils.settings.change_atr == Settings.NAME_FULLSCREEN || Utils.settings.change_atr == Settings.NAME_ORIENTATION_LOCK){
			if (Utils.settings.FULLSCREEN)
				 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			else
				this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			if (Utils.settings.ORIENTATION_LOCK == 1)
				Utils.ORIENTATION = false;
			if (Utils.settings.ORIENTATION_LOCK == 2)
				Utils.ORIENTATION = true;
			if (Utils.settings.ORIENTATION_LOCK == 0){
			    int orientation = display.getRotation();
				Utils.ORIENTATION = ((orientation == Surface.ROTATION_0) | (orientation == Surface.ROTATION_180));
			}
			if (Utils.ORIENTATION){
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else {
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		} 
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		if (Utils.settings.BRIGHTNESS != 0)
			lp.screenBrightness = ((float)Settings.VALUE_BRIGHTNESS[Utils.settings.BRIGHTNESS] / 100);
		getWindow().setAttributes(lp);  
	}
	
    @Override
    protected void onStop(){
    	super.onStop();
    	
    	SharedPreferences settings = getSharedPreferences(Utils.PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        Utils.settings.writeSettings(editor);
        editor.putInt("last_favorite_index", Utils.LAST_FAVORITE_INDEX);
        editor.putInt("last_read_index", Utils.LAST_READ_INDEX);
        editor.commit();
    }
	
	private String[] name = new String[0];
}
