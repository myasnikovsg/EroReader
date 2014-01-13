package com.eroreader;

import java.util.ArrayList;
import java.util.Arrays;
import android.content.Context;
import android.database.Cursor;

public class Utils {
	public static Context context;
	public static Context main_context;
	
	public static int LAST_FAVORITE_INDEX;
	public static int LAST_READ_INDEX;
	
	public static boolean ORIENTATION;
	
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static float DENSITY;
	
	public static DataBaseHelper base;
	public static ArrayList<Story> stories;
	public static String[] cathegories;
	final static String PREF = "PREF";
	public static String FILTER_CATH = "'111111111111111111111111111111111111'";
	public static Boolean FILTER_STATE = false; // or = false; and = true;
	public static Boolean SEARCH_PERFORMED = false;
	public static String SEARCH_STRING; 
	public static float YDPI;
	public static float BASE_PROGRESS = 0f;
	public static boolean BASE_COMPLETELY_OPENED = false; 
	
	public static boolean FLAG = false; 
	
	public static Story curStory;
	
	public static boolean END_REACHED;
	
	public static Settings settings;
	
	public static Story cur_property_story;
	
	public static int[] getColorArray(int width, int height, int color){
		int [] res = new int[width * height];
		for (int i = 0; i < width * height; i++)
			res[i] = color;
		return res;
	}
	
	public static int getCathegoryIndex(String cathegory){
		for (int i = 0; i < cathegories.length; i++){
			if (cathegories[i].startsWith(cathegory)) 
				return i;
		}
		return -1;
	} 
	
	public static int getStoryIndexById(int id){
		for (int i = 0; i < stories.size(); i++)
			if (stories.get(i).id == id)
				return i;
		return -1;
	}
	
	public static String generatePattern(int cath){
		String pattern = "";
		for (int i = 0; i < cathegories.length; i++){
			if (cath == i) 
				pattern += "1";
			else pattern += "_"; 
		}
		return "'" + pattern + "'";
	}
	
	public static String generatePattern(){
		if (FILTER_STATE)
			return FILTER_CATH;
		String res = "'";
		for (int i = 1; i < FILTER_CATH.length() - 1; i++)
			if (FILTER_CATH.charAt(i) == '1')
				res += '_'; 
			else
				res += '0';
		return res + "'";
	}
	//actually, is a letter or digit
	public static boolean isLetter(char c){
		return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= 'à') && (c <= 'ÿ')) || ((c >= 'À') && (c <= 'ß')) || ((c >= '0') && (c <= '9'));
	}
	
	public static String getTag(String tag, int num){
		int count = 0;
		int i = 0;
		while(i < cathegories.length && count < num){
			if (tag.charAt(i) == '1')
				count++;
			i++;
		}
		if (count == num)
			return cathegories[i - 1];
		else return "";
	}
	
	public static Story parseStory(Cursor cur, int cath, boolean isCurrent){
		Story story = new Story();
		story.cathegory = cur.getString(cur.getColumnIndex("cath"));
		story.weight = story.getWeight();
		story.prime = cath;
		story.id = (int) cur.getLong(cur.getColumnIndex("_id"));
		story.title = cur.getString(cur.getColumnIndex("title"));
		if (cur.getLong(cur.getColumnIndex("fav")) == 0)
			story.favorite = false;
		else
			story.favorite = true;
		story.pos = (int) cur.getLong(cur.getColumnIndex("pos"));
		if (!isCurrent)
			story.text = cur.getString(cur.getColumnIndex("text")).substring(0, Utils.settings.getAtr(Settings.NAME_PREVIEW_LENGTH));
		else
			story.text = cur.getString(cur.getColumnIndex("text"));
				
		story.last = (int) cur.getLong(cur.getColumnIndex("last"));
		return story;
	}
	
	public static void sortStories(){
		Story[] stub = new Story[stories.size()];
		for (int i = 0; i < stories.size(); i++)
			stub[i] = stories.get(i);
		Arrays.sort(stub);
		stories = new ArrayList<Story>();
		int t = 0;
		stories.add(stub[0]);
		for (int i = 0; i < stub.length; i++)
			if (stub[i].id != stub[t].id){
				t = i;
				stories.add(stub[t]);
			}
	}
	
	public static void changeFilter(int pos, boolean st){
		String temp;
		temp = st ? "1" : "_";
		Utils.FILTER_CATH = Utils.FILTER_CATH.substring(0, pos + 1) + temp + Utils.FILTER_CATH.substring(pos + 2); 
	}
	
	public static String ellipsize(String s){
		int i = s.length() - 1;
		while (s.charAt(i) != '.' && s.charAt(i) != ' ' && s.charAt(i) != '?' && s.charAt(i) != '!')
			i--;
		if (s.charAt(i) != ' ')
			while (s.charAt(i) == '.' || s.charAt(i) == '?' || s.charAt(i) == '!')
				i--;
		return s.substring(0, i - 1) + "...";
	}
	
}
