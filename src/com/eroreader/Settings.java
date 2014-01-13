package com.eroreader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;

public class Settings {
	
	int change_atr;
	int selected;
	String title;
	
	static final int[] VALUE_SHOW_ITEMS_PER_PAGE = {5, 10, 20, 40, 80, 100};
	static int[]       VALUE_PREVIEW_LENGTH =      {50, 100, 200, 300};
	static String[]    VALUE_ORIENTATION_LOCK =    {"Сенсор", "Портрет", "Пейзаж"};
	static int[]       VALUE_BRIGHTNESS =          {5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	static String[]    VALUE_FONT =                {"Droid Sans", "Droid Sans Mono", "Droid Serif"};
	static int[]       VALUE_FONT_SIZE =           {10, 12, 15, 18, 24, 36, 45};
	static String[]    VALUE_VIEW_MODE =           {"Свиток", "Пэйджер"};
	static int[]       VALUE_LINE_SPACING =        {50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150};
	static String[]    VALUE_BACKGROUND_TEXTURE =  {"Заданный цвет", "Папирус", "Камень", "Береза", "Ткань", "Металл", "Дуб"};
	static int[]       VALUE_PADDING =             {1, 2, 3, 4, 5, 7, 9, 12};

	static final int NAME_SHOW_ITEMS_PER_PAGE = 0;
	static final int NAME_PREVIEW_LENGTH = 1;
	static final int NAME_ORIENTATION_LOCK = 2;
	static final int NAME_BRIGHTNESS = 3;
	static final int NAME_FULLSCREEN = 4;
	static final int NAME_USE_PASSWORD = 5;
	static final int NAME_EXIT_WITHOUT_PROMT = 6;
	static final int NAME_PASSWORD = 7;
	static final int NAME_SAVE_HISTORY = 8;
	static final int NAME_FONT = 9;
	static final int NAME_FONT_SIZE = 10;
	static final int NAME_BOLD = 11;
	static final int NAME_SHOW_STATUSBAR = 12;
	static final int NAME_VIEW_MODE = 13;
	static final int NAME_LINE_SPACING = 14;
	static final int NAME_NIGHT_MODE = 15;
	static final int NAME_FONT_COLOR_DAY = 16;
	static final int NAME_FONT_COLOR_NIGHT = 17;
	static final int NAME_BACKGROUND_COLOR_DAY = 18;
	static final int NAME_BACKGROUND_COLOR_NIGHT = 19;
	static final int NAME_BACKGROUND_TEXTURE = 20;
	static final int NAME_PADDING_LEFT = 21;
	static final int NAME_PADDING_RIGHT = 22;
	static final int NAME_PADDING_TOP = 23;
	static final int NAME_PADDING_BOTTOM = 24;
	static final int NAME_SHADOW = 25;
	
	int     SHOW_ITEMS_PER_PAGE;                                     // Applied  
	int     PREVIEW_LENGTH;                                          // Applied
	int     ORIENTATION_LOCK;                                        // Applied
	int     BRIGHTNESS; // ReaderActivity
	boolean FULLSCREEN; // ReaderActivity
	boolean PASSWORD_SET;                                            // Applied
	int     PASSWORD;                                                // Applied
	boolean EXIT_WITHOUT_PROMT;
	boolean SAVE_HISTORY;                                            // Applied
	int     FONT; // Texter                                          // Applied
	int     FONT_SIZE; // Texter                                     // Applied
	boolean BOLD; // Texter                                          // Applied
	boolean SHOW_STATUS_BAR; // ReaderActivity
	int     VIEW_MODE;                                               // Applied
	int     LINE_SPACING; // Texter                                  // Applied
	boolean NIGHT_MODE;                                              // Applied
	int     FONT_COLOR_DAY; // Texter                                // Applied
	int     FONT_COLOR_NIGHT; // Texter                              // Applied
	int     BACKGROUND_COLOR_DAY; // Texter
	int     BACKGROUND_COLOR_NIGHT; // Texter
	int     BACKGROUND_TEXTURE; // Texter
	int     PADDING_LEFT; // Texter                                  // Applied
	int     PADDING_RIGHT; // Texter                                 // Applied
	int     PADDING_TOP; // Texter                                   // Applied
	int     PADDING_BOTTOM; // Texter                                // Applied
	int     SHADOW;
	
	
	public void readSettings(SharedPreferences settings){
		SHOW_ITEMS_PER_PAGE = settings.getInt("show_items_per_page", 0);
		if (SHOW_ITEMS_PER_PAGE > 5)
			SHOW_ITEMS_PER_PAGE = 0;
		PREVIEW_LENGTH = settings.getInt("preview_length", 0);
		if (PREVIEW_LENGTH > 3)
			PREVIEW_LENGTH = 0;
		ORIENTATION_LOCK = settings.getInt("orientation", 0);
		BRIGHTNESS = settings.getInt("brightness", 0);
		FULLSCREEN = settings.getBoolean("fullscreen", false);
		PASSWORD_SET = settings.getBoolean("password_set", false);
		PASSWORD = settings.getInt("password", -1);
		EXIT_WITHOUT_PROMT = settings.getBoolean("exit_without_promt", false);
		SAVE_HISTORY = settings.getBoolean("save_history", true);
		FONT = settings.getInt("font", 0);
		FONT_SIZE = settings.getInt("font_size", 4);
		if (FONT_SIZE > 6)
			FONT_SIZE = 4;
		BOLD = settings.getBoolean("bold", false);
		SHOW_STATUS_BAR = settings.getBoolean("show_status_bar", true);
		VIEW_MODE = settings.getInt("view_mode", 1);
		LINE_SPACING = settings.getInt("line_spacing", 4);
		NIGHT_MODE = settings.getBoolean("night_mode", false);
		FONT_COLOR_DAY = settings.getInt("font_color_day", Color.BLACK);
		FONT_COLOR_NIGHT = settings.getInt("font_color_night", Color.WHITE);
		BACKGROUND_COLOR_DAY = settings.getInt("background_color_day", Color.WHITE);
		BACKGROUND_COLOR_NIGHT = settings.getInt("background_color_night", Color.BLACK);
		BACKGROUND_TEXTURE = settings.getInt("background_texture", 0);
		PADDING_LEFT = settings.getInt("padding_left", 3);
		PADDING_RIGHT = settings.getInt("padding_right", 3);
		PADDING_TOP = settings.getInt("padding_top", 3);
		PADDING_BOTTOM = settings.getInt("padding_botom", 3);
		SHADOW = settings.getInt("shadow", 0);
	}
	
	public void writeSettings(SharedPreferences.Editor editor){
		 editor.putInt("show_items_per_page", SHOW_ITEMS_PER_PAGE);
	     editor.putInt("preview_length", PREVIEW_LENGTH);
	     editor.putInt("orientation", ORIENTATION_LOCK);
	     editor.putInt("brightness", BRIGHTNESS);
	     editor.putBoolean("fullscreen", FULLSCREEN);
	     editor.putBoolean("password_set", PASSWORD_SET);
	     editor.putInt("password", PASSWORD);
	     editor.putBoolean("exit_without_promt", EXIT_WITHOUT_PROMT);
	     editor.putBoolean("save_history", SAVE_HISTORY);
	     editor.putInt("font", FONT);
	     editor.putInt("font_size", FONT_SIZE);
	     editor.putBoolean("bold", BOLD);
	     editor.putBoolean("show_status_bar", SHOW_STATUS_BAR);
	     editor.putInt("view_mode", VIEW_MODE);
	     editor.putInt("line_spacing", LINE_SPACING);
	     editor.putBoolean("night_mode", NIGHT_MODE);
	     editor.putInt("font_color_day", FONT_COLOR_DAY);
	     editor.putInt("font_color_night", FONT_COLOR_NIGHT);
	     editor.putInt("background_color_day", BACKGROUND_COLOR_DAY);
	     editor.putInt("background_color_night", BACKGROUND_COLOR_NIGHT);
	     editor.putInt("background_texture", BACKGROUND_TEXTURE);
	     editor.putInt("padding_left", PADDING_LEFT);
	     editor.putInt("padding_right", PADDING_RIGHT);
	     editor.putInt("padding_top", PADDING_TOP);
	     editor.putInt("padding_bottom", PADDING_BOTTOM);
	     editor.putInt("shadow", SHADOW);
	     editor.commit();
	}
	
	//get array of possible values for current attribute
	public String[] getArray(){
		switch (change_atr){
		case NAME_SHOW_ITEMS_PER_PAGE:
			return intArrayToStringArray(VALUE_SHOW_ITEMS_PER_PAGE, "");
		case NAME_PREVIEW_LENGTH:
			return intArrayToStringArray(VALUE_PREVIEW_LENGTH, "");
		case NAME_ORIENTATION_LOCK:
			return VALUE_ORIENTATION_LOCK;
		case NAME_BRIGHTNESS:
			String[] res = intArrayToStringArray(VALUE_BRIGHTNESS, "%");
			res[0] = "Автоопределение";
			return res;
		case NAME_FONT:
			return VALUE_FONT;
		case NAME_FONT_SIZE:
			return intArrayToStringArray(VALUE_FONT_SIZE, "");
		case NAME_VIEW_MODE:
			return VALUE_VIEW_MODE;
		case NAME_LINE_SPACING:
			return intArrayToStringArray(VALUE_LINE_SPACING, "%");
		case NAME_BACKGROUND_TEXTURE:
			return VALUE_BACKGROUND_TEXTURE;
		case NAME_PADDING_LEFT:
			return intArrayToStringArray(VALUE_PADDING, "");
		case NAME_PADDING_RIGHT:
			return intArrayToStringArray(VALUE_PADDING, "");
		case NAME_PADDING_TOP:
			return intArrayToStringArray(VALUE_PADDING, ""); 
		case NAME_PADDING_BOTTOM: 
			return intArrayToStringArray(VALUE_PADDING, "");
		default:
			return new String[0];
		}
	}
	
	public String[] intArrayToStringArray(int[] values, String add){
		String[] res = new String[values.length];
		for (int i = 0; i < values.length; i++)
			res[i] = Integer.toString(values[i]) + add;
		return res;
	}
	
	//set current attribute
	public void setAtr(int val){
		switch (change_atr){
		case NAME_SHOW_ITEMS_PER_PAGE:
			SHOW_ITEMS_PER_PAGE = val;
			break;
		case NAME_PREVIEW_LENGTH:
			PREVIEW_LENGTH = val;
			break;
		case NAME_ORIENTATION_LOCK:
			ORIENTATION_LOCK = val;
			break;
		case NAME_BRIGHTNESS:
			BRIGHTNESS = val;
			break;
		case NAME_FONT:
			FONT = val;
			break;
		case NAME_FONT_SIZE:
			FONT_SIZE = val;
			break;
		case NAME_VIEW_MODE:
			VIEW_MODE = val;
			break;
		case NAME_LINE_SPACING:
			LINE_SPACING = val;
			break;
		case NAME_FONT_COLOR_DAY:
			FONT_COLOR_DAY = val;
			break;
		case NAME_FONT_COLOR_NIGHT:
			FONT_COLOR_NIGHT = val;
			break;
		case NAME_BACKGROUND_COLOR_DAY:
			BACKGROUND_COLOR_DAY = val;
			break;
		case NAME_BACKGROUND_COLOR_NIGHT:
			BACKGROUND_COLOR_NIGHT = val;
			break;
		case NAME_BACKGROUND_TEXTURE:
			BACKGROUND_TEXTURE = val;
			break;
		case NAME_PADDING_LEFT:
			PADDING_LEFT = val;
			break;
		case NAME_PADDING_RIGHT:
			PADDING_RIGHT = val;
			break;
		case NAME_PADDING_TOP:
			PADDING_TOP = val;
			break;
		case NAME_PADDING_BOTTOM:
			PADDING_BOTTOM = val;
			break;
		}
	}
	//get current attribute
	public int getAtr(){
		return getAtr(change_atr);
	}
	//get current attribute
	public int getAtr(int atr){
		switch (atr){
		case NAME_SHOW_ITEMS_PER_PAGE:
			return VALUE_SHOW_ITEMS_PER_PAGE[SHOW_ITEMS_PER_PAGE];
		case NAME_PREVIEW_LENGTH:
			return VALUE_PREVIEW_LENGTH[PREVIEW_LENGTH];
		case NAME_ORIENTATION_LOCK:
			return ORIENTATION_LOCK;
		case NAME_BRIGHTNESS:
			return VALUE_BRIGHTNESS[BRIGHTNESS];
		case NAME_FONT:
			return FONT;
		case NAME_FONT_SIZE:
			return VALUE_FONT_SIZE[FONT_SIZE];
		case NAME_LINE_SPACING:
			return VALUE_LINE_SPACING[LINE_SPACING];
		case NAME_FONT_COLOR_DAY:
			return FONT_COLOR_DAY;
		case NAME_FONT_COLOR_NIGHT:
			return FONT_COLOR_NIGHT;
		case NAME_BACKGROUND_COLOR_DAY:
			return BACKGROUND_COLOR_DAY;
		case NAME_BACKGROUND_COLOR_NIGHT:
			return BACKGROUND_COLOR_NIGHT;
		case NAME_BACKGROUND_TEXTURE:
			return BACKGROUND_TEXTURE;
		case NAME_PADDING_LEFT:
			return VALUE_PADDING[PADDING_LEFT];
		case NAME_PADDING_RIGHT:
			return VALUE_PADDING[PADDING_RIGHT];
		case NAME_PADDING_TOP:
			return VALUE_PADDING[PADDING_TOP];
		case NAME_PADDING_BOTTOM:
			return VALUE_PADDING[PADDING_BOTTOM];
		default:
			return -1;
		}
	}
	
	public int getFontColor(){
		return NIGHT_MODE ? FONT_COLOR_NIGHT : FONT_COLOR_DAY;
	}
	
	public int getBackgroundColor(){
		return NIGHT_MODE ? BACKGROUND_COLOR_NIGHT : BACKGROUND_COLOR_DAY;
	} 
	
	public void setFontColor(int color){
		if (NIGHT_MODE)
			FONT_COLOR_NIGHT = color;
		else
			FONT_COLOR_DAY = color;
	}
	
	public void setBackgroundColor(int color){
		if (NIGHT_MODE)
			BACKGROUND_COLOR_NIGHT = color;
		else
			BACKGROUND_COLOR_DAY = color;
	}
	
	//All that have string values must have an entry here
	public String getAtrToStr(){
		return getAtrToStr(change_atr);
	}
	
	public static synchronized void applyScreenSettings(Activity context){
		if (Utils.settings == null)
			return;
		if (Utils.settings.ORIENTATION_LOCK == 1){
			context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			Utils.ORIENTATION = false;
		}
		if (Utils.settings.ORIENTATION_LOCK == 2){
			Utils.ORIENTATION = true;
			context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		if (Utils.settings.FULLSCREEN){
			 context.requestWindowFeature(Window.FEATURE_NO_TITLE);
			 context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else
		{
			context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		if (Utils.settings.BRIGHTNESS != 0)
			lp.screenBrightness = ((float)VALUE_BRIGHTNESS[Utils.settings.BRIGHTNESS] / 100);
		context.getWindow().setAttributes(lp);  
	}
	 
	public String getAtrToStr(int atr){
		switch (atr){
		case NAME_ORIENTATION_LOCK:
			return VALUE_ORIENTATION_LOCK[ORIENTATION_LOCK];
		case NAME_BRIGHTNESS:
			if (BRIGHTNESS == 0)
				return "Автоопределение";
			else
				return Integer.toString(VALUE_BRIGHTNESS[BRIGHTNESS]) + "%";
		case NAME_FONT:
			return VALUE_FONT[FONT];
		case NAME_VIEW_MODE:
			return VALUE_VIEW_MODE[VIEW_MODE];
		case NAME_BACKGROUND_TEXTURE:
			return VALUE_BACKGROUND_TEXTURE[BACKGROUND_TEXTURE];
		default:
			return "Error";
		}
	}
	
	
	public String memento(){  
		String MEMENTO =    "";
		MEMENTO +=   FONT; 
		MEMENTO +=	 FONT_SIZE; 
		MEMENTO +=	 BOLD; 
		MEMENTO +=   SHOW_STATUS_BAR;
		MEMENTO +=   NIGHT_MODE;    
		MEMENTO +=   FONT_COLOR_DAY; 
		MEMENTO +=   FONT_COLOR_NIGHT;
		MEMENTO +=   BACKGROUND_COLOR_DAY;
		MEMENTO +=   BACKGROUND_COLOR_NIGHT;
		MEMENTO +=   BACKGROUND_TEXTURE; 
		MEMENTO +=   PADDING_LEFT; 
		MEMENTO +=   PADDING_RIGHT;
		MEMENTO +=   PADDING_TOP; 
		MEMENTO +=   PADDING_BOTTOM; 
		return MEMENTO;
	}
	
	public boolean criticalChangesPerformed(String mem){
		return mem.compareTo(memento()) != 0;
	}
	
}

