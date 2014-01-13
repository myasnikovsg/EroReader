package com.eroreader;

import java.util.ArrayList;

import org.apache.http.impl.conn.tsccm.WaitingThread;

import android.R.integer;
import android.content.Context;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import android.widget.TextView;

public class Texter {
	public static Context context;
	
	private static TextPaint paint;
	public static boolean etalon_refreshed;
	static String text;
	static boolean need_to_refresh;
	static Text etalon;
	static boolean done;
	static boolean l_done;
	static boolean p_done;
	static int average_line_height;
	static int screen_height;
	static int screen_width;
	static ArrayList<Integer> l_line; // breaks of lines in landscape orientation using 
	static ArrayList<Integer> p_line; // breaks of lines in portrait orientation using 
	private static ArrayList<Float> l_height; // heights of lines in landscape orientation 
	private static ArrayList<Float> p_height; // heights of lines in portrait orientation
	private static float l_all_height;
	private static float p_all_height;
	private static int l_max_page_processed;
	private static int p_max_page_processed;
	static int l_average_page_count;
	static int p_average_page_count;
	static int l_begin;
	static int p_begin;
	static char[] textArray;
	static int l_dist;
	static int p_dist; 
	static double lose;
	
	public static void refresh(){
		need_to_refresh = true;
		l_done = false;
		p_done = false;
	}
	
	private static synchronized void cookText(){
		int measure;
		int temp = 0;
		boolean flag;
		float fontSpacing = paint.getFontSpacing();
		float l_cap = screen_height - Utils.settings.getAtr(Settings.NAME_PADDING_BOTTOM) - Utils.settings.getAtr(Settings.NAME_PADDING_TOP);
		float p_cap = screen_width - Utils.settings.getAtr(Settings.NAME_PADDING_BOTTOM) - Utils.settings.getAtr(Settings.NAME_PADDING_TOP);
		p_cap = p_cap * 2f;
		l_cap = l_cap * 2f;
		float l_cooked_height = 0;
		float p_cooked_height = 0; 
		while (l_begin < text.length() || p_begin < text.length()){
			if (l_cooked_height <= l_cap && l_begin < text.length()){
				l_line.add(l_begin);
				measure = paint.breakText(textArray, l_begin, text.length() - l_begin, l_dist, null);
				flag = true;
				temp = text.indexOf('\n', l_begin);
				if (temp != -1 && temp < l_begin + measure){
					flag = false;
					temp++;
				}
				if (flag)
					temp = l_begin + measure;
				l_height.add(fontSpacing);
				l_cooked_height += fontSpacing;
				//Log.w("LLLLLL", "height = " + l_cooked_height + " l_begin = " + l_begin);
				l_begin = temp;			
			} else{
			//	Log.w("WARN", "l_cap = " + l_cap + " l_cook = " + l_cooked_height);
				l_cooked_height = l_cap + 0.1f;
			}
			if (p_cooked_height <= p_cap && p_begin < text.length()){
				p_line.add(p_begin);
				measure = paint.breakText(textArray, p_begin, text.length() - p_begin, p_dist, null);
				flag = true;
				temp = text.indexOf('\n', p_begin);
				if (temp != -1 && temp < p_begin + measure){
					flag = false;
					temp++;
				}
				if (flag)
					temp = p_begin + measure;
				p_height.add(fontSpacing);
				p_cooked_height += fontSpacing;
				p_begin = temp;			
			} else
				p_cooked_height = p_cap + 0.1f;
			if (l_cooked_height > l_cap && p_cooked_height > p_cap)
				break;
		}
		l_max_page_processed++;
		//Log.w("COOk", "l_cap = " + l_cap + " l_cook = " + l_cooked_height);
		//Log.w("COOk", "Endeded iteration. l_begin = " + l_begin + "p_begin = " + p_begin + "total = " + text.length());
		if (l_begin >= text.length()){
			l_line.add(text.length());
			l_done = true;
			l_max_page_processed = 10000;
		}
		p_max_page_processed++;
		if (p_begin >= text.length()) {
			p_line.add(text.length());
			p_done = true;
			p_max_page_processed = 10000;
		}
		
	}
	
	public synchronized static void prepareStory(){
		if (!need_to_refresh)
			return;
		need_to_refresh = false;
		text = Utils.curStory.text;
		etalon = createView(context, 0, Utils.ORIENTATION);
		paint = etalon.getPaint();
		l_line = new ArrayList<Integer>();
		p_line = new ArrayList<Integer>();
		l_height = new ArrayList<Float>();
		p_height = new ArrayList<Float>();
		l_all_height = 0;
		p_all_height = 0;
		l_average_page_count = 1;
		p_average_page_count = 1;
		l_begin = 0;
		p_begin = 0;
		lose = 0;
		textArray = text.toCharArray();
		l_dist = Utils.SCREEN_WIDTH - Utils.settings.getAtr(Settings.NAME_PADDING_LEFT) - Utils.settings.getAtr(Settings.NAME_PADDING_RIGHT);
		p_dist = Utils.SCREEN_HEIGHT - Utils.settings.getAtr(Settings.NAME_PADDING_LEFT) - Utils.settings.getAtr(Settings.NAME_PADDING_RIGHT);
		//average_line_height = etalon.getLineHeight();
		l_max_page_processed = -1;
		p_max_page_processed = -1;
		//cookText();
	}
	
	public static String countPagesAverage(){
		if (Utils.ORIENTATION){
			if (!l_done){
				l_average_page_count = Math.max(l_average_page_count, (int)(((l_max_page_processed + 1) * 2) * (text.length() * 1f / l_begin)));
				return "" +  l_average_page_count + "+";
			}
			else
				return "" + (getPageByLine(l_line.size() - 1, true) + 1);
		}
		else
			if (!p_done){
				p_average_page_count = Math.max(p_average_page_count, (int)(((p_max_page_processed + 1) * 2) * (text.length() * 1f / p_begin)));
				return "" + p_average_page_count + "+";
			}
			else
				return "" + (getPageByLine(p_line.size() - 1, false) + 1);
	}
	
	public static int countPages(){
		if (screen_height == 0)
			return 1;
		if (Utils.settings.VIEW_MODE == 0){
			return 1;
		} else {
			if (Utils.ORIENTATION){
				if (l_done)
					return getPageByLine(l_line.size() - 2, true) + 1;
				else
			//		return getPageByLine(l_line.size() - 1, true) + 2;
					return 10000;
			}
			else {
				if (p_done)
					return getPageByLine(p_line.size() - 2, false) + 1;
				else
				//	return getPageByLine(p_line.size() - 1, false) + 2;
					return 10000;
			}
			//return getPageByLine(Utils.ORIENTATION ? l_line.size() - 1 : p_line.size() - 1, Utils.ORIENTATION) ;
		}
	}
	
	public static Text createView(Context context, int position, boolean orientation) {
		Text view = new Text(context, position, orientation);
		view.setPadding(Utils.settings.getAtr(Settings.NAME_PADDING_LEFT), Utils.settings.getAtr(Settings.NAME_PADDING_TOP), Utils.settings.getAtr(Settings.NAME_PADDING_RIGHT), Utils.settings.getAtr(Settings.NAME_PADDING_BOTTOM));
		view.setTextSize(Utils.settings.getAtr(Settings.NAME_FONT_SIZE));
		//view.setLineSpacing(0, (Utils.settings.getAtr(Settings.NAME_LINE_SPACING)) / 100);
		int style;
		TextPaint paint;
		Typeface font;
		if (Utils.settings.BOLD)
			style = Typeface.BOLD; 
		else
			style = Typeface.NORMAL;
		switch (Utils.settings.FONT){
		case 0:
			font = Typeface.SANS_SERIF;
			break;
		case 1:
			font = Typeface.MONOSPACE;
			break;
		case 2:
			font = Typeface.SERIF;
			break;
		default:
			font = Typeface.DEFAULT;
			break;
		}
		view.setTypeface(font, style);
		view.setTextColor(Utils.settings.getFontColor());
		if (Utils.settings.getAtr(Settings.NAME_BACKGROUND_TEXTURE) == 0)
			view.setBackgroundColor(Utils.settings.getBackgroundColor());
		else{
			int id;
			switch (Utils.settings.getAtr(Settings.NAME_BACKGROUND_TEXTURE)){
			case 1 : 
				id = R.drawable.back1;
				break;
			case 2 :
				id = R.drawable.back2;
				break;
			case 3 :
				id = R.drawable.back3;
				break;
			case 4 :
				id = R.drawable.back4;
				break;
			case 5 :
				id = R.drawable.back5;
				break;
			case 6 :
				id = R.drawable.back6;
				break;
			default :
				id = 1;
				break;
			}
			Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
		    BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
		    bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			view.setBackgroundDrawable(bitmapDrawable);
		}
		return view;
	}
	//returns line number, from which (page) will start in (orientation)
	private static int getLineByPage(int page, boolean orientation){
		if (Utils.settings.VIEW_MODE == 0)
			return 0;
		float l_cap = screen_height - Utils.settings.getAtr(Settings.NAME_PADDING_BOTTOM) - Utils.settings.getAtr(Settings.NAME_PADDING_TOP);
		float p_cap = screen_width - Utils.settings.getAtr(Settings.NAME_PADDING_BOTTOM) - Utils.settings.getAtr(Settings.NAME_PADDING_TOP);
		if (page == 0)
			return 0;
		int cur_page = 0;
		float cur_height = 0;
		int i = 0;
		if (orientation){
			while (cur_page < page && i < l_height.size()){
				cur_height += l_height.get(i);
				if (cur_height > l_cap){
					cur_height = l_height.get(i);
					cur_page++;
				}
				i++;
			}
			return i - 1;
		} else{
			while (cur_page < page && i < p_height.size()){
				cur_height += p_height.get(i);
				if (cur_height > p_cap){
					cur_height = p_height.get(i);
					cur_page++;
				}
				i++;
			}
			return i - 1;
		}
		
	}
	
	private static int getPageByLine(int line, boolean orientation){
		int cur_page = 0;
		float cur_height = 0;
		int i = 0;
		float l_cap = screen_height - Utils.settings.getAtr(Settings.NAME_PADDING_BOTTOM) - Utils.settings.getAtr(Settings.NAME_PADDING_TOP);
		float p_cap = screen_width - Utils.settings.getAtr(Settings.NAME_PADDING_BOTTOM) - Utils.settings.getAtr(Settings.NAME_PADDING_TOP);  
		if (orientation){
			while (i < line){
				cur_height += l_height.get(i);
				if (cur_height > l_cap){
					cur_height = l_height.get(i);
					cur_page++;
				}
				i++;
			}
			return cur_page;
		} else{
			while (i < line){
				cur_height += p_height.get(i);
				if (cur_height > p_cap){
					cur_height = p_height.get(i);
					cur_page++;
				}
				i++;
			}
			return cur_page;
		}
	}
	
	// returns page number of (page) in other orientation
	public static int getPageIndex(int page){
		if (screen_height == 0)
			return 0;
		return getPageByLine(getLineByPage(page, !Utils.ORIENTATION), Utils.ORIENTATION);
	}
	
	public static String getPage(int page){
		int i;
		String str = "";
		float height = 0;
		String temp;
		float l_cap = screen_height - Utils.settings.getAtr(Settings.NAME_PADDING_BOTTOM) - Utils.settings.getAtr(Settings.NAME_PADDING_TOP);
		float p_cap = screen_width - Utils.settings.getAtr(Settings.NAME_PADDING_BOTTOM) - Utils.settings.getAtr(Settings.NAME_PADDING_TOP); 
		if (Utils.ORIENTATION){
			i = getLineByPage(page, Utils.ORIENTATION);
			if (Utils.settings.VIEW_MODE == 0){
				while (i < l_line.size() - 1){
					temp = text.substring(l_line.get(i), l_line.get(i + 1)).trim();
					if (temp.charAt(temp.length() - 1) != '\n')
						temp = temp.concat("\n");
					str = str.concat(temp);
					i++;
				}
			} else { 
				if (page > l_max_page_processed){
					cookText();
					i = getLineByPage(page, Utils.ORIENTATION);
				}
				while (i < l_line.size() - 1 && (height + l_height.get(i)) < l_cap){
				//	Log.w("WARN", "" + l_line.get(i) + " " + l_line.get(i + 1) + " length = " + text.length() + "page = " + page + "cur_height = " + height);
					temp = text.substring(l_line.get(i), l_line.get(i + 1));
					height += l_height.get(i);
					if (temp.charAt(temp.length() - 1) != '\n')
						temp = temp.concat("\n");
					str = str.concat(temp);
					i++;
				} 
				if (i < l_line.size() - 1)
					lose = l_cap - height;
				else
					lose = 0;
			}
		} else {
			i = getLineByPage(page, Utils.ORIENTATION);
			if (Utils.settings.VIEW_MODE == 0){
				while (i < p_line.size() - 1){
					temp = text.substring(p_line.get(i), p_line.get(i + 1)).trim();
					if (temp.charAt(temp.length() - 1) != '\n')
						temp = temp.concat("\n");
					str = str.concat(temp);
					i++;
				}
			} else {
				if (page > p_max_page_processed){
					cookText();
					i = getLineByPage(page, Utils.ORIENTATION);
				}
				while (i < p_line.size() - 1 && (height + p_height.get(i)) < p_cap){
					temp = text.substring(p_line.get(i), p_line.get(i + 1));
					height += p_height.get(i);
					if (temp.charAt(temp.length() - 1) != '\n')
						temp = temp.concat("\n");
					str = str.concat(temp);
					i++;
				}
				if (i < p_line.size() - 1)  
					lose = p_cap - height;
				else
					lose = 0;
			}
		}
		if (page == 0)
			((ReaderActivity)context).refresh_page_counter("1 / " + Texter.countPagesAverage());
		return str;
	} 
}
