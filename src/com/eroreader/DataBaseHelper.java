package com.eroreader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
    private static String DB_PATH = "/data/data/com.eroreader/databases/";
 
    //private static String DB_NAME = "esreader1_1tales_utf8_small.jpg";
    private static String DB_NAME = "base1";
   // private static String DB_NAME = "esreader1_10tales_utf8 (2).jpg";
    private static int BASE_PART_NUMBER = 67;
    private static String PART_T = "part";
    private SQLiteDatabase dataBase; 
 
    private final Context myContext;
 
    public DataBaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
    
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist && Utils.FLAG){
    		Utils.BASE_PROGRESS = 1f;
    	} else {
        	this.getWritableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String path = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
 
    	}catch(SQLiteException e){
    		e.printStackTrace();
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
 
    	return checkDB != null ? true : false;
    }

    private synchronized void copyDataBase() throws IOException{
       	String outFileName = DB_PATH + DB_NAME;
        
    	OutputStream output = new FileOutputStream(outFileName);
    	
    	Utils.BASE_PROGRESS = 0f;
 
    	byte[] buffer = new byte[1024];
    	int length;
    	for (int i = 1; i < BASE_PART_NUMBER + 1; i++){
    		InputStream input = myContext.getAssets().open(PART_T + i + ".db");
    		int t = 0;
    		Utils.BASE_PROGRESS += 1.0f / BASE_PART_NUMBER;
    		while ((length = input.read(buffer))>0){
    			output.write(buffer, 0, length);
    			t++;
    		}
    		System.out.println(t);
    		input.close();
    	}
    	output.flush();
    	output.close();
 
    }
 
    public void openDataBase() throws SQLException{
        String path = DB_PATH + DB_NAME;
    	dataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    	dataBase.setLocale(Locale.US);
    }
    
    @Override
	public synchronized void close() {
    	    if(dataBase != null)
    		    dataBase.close();
    	    super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
 	
	public void readCathegories(){
		boolean f = false;
		Cursor cur = null;
		while (!f)
			try {
				cur = dataBase.query("cath", null, null, null, null, null, null);
				f = true;
			} catch (Exception e) {
				Log.w("CATH", "fail");
			}
		Utils.BASE_COMPLETELY_OPENED = true;
		Utils.cathegories = new String[cur.getCount()];
		cur.moveToNext();
		while (cur.getPosition() < cur.getCount()){
			Utils.cathegories[cur.getShort(cur.getColumnIndex("_id")) - 1] = cur.getString(cur.getColumnIndex("name"));
			cur.moveToNext();
		}
		cur.close();
	}
	
	public int findLengthById(int id){
		Cursor cur = dataBase.query("stories", null, "_id=" + id, null, null, null, null);
		cur.moveToNext();
		return (cur.getString(cur.getColumnIndex("text"))).length();
	}
	
	public void findByCathegory(int cath, int page){
		int length = Utils.settings.getAtr(Settings.NAME_SHOW_ITEMS_PER_PAGE);
		if (page == 0){
			Utils.stories = new ArrayList<Story>();
			Utils.END_REACHED = false;
		}
		Cursor cur = dataBase.query("stories", null, "cath LIKE " + Utils.generatePattern(cath), null, null, null, null, "" + (page * length) + " , " + ((page + 1) * length + 1));
		if (cur.getCount() < length + 1)
			Utils.END_REACHED = true;
		int i = 0;
		cur.moveToNext();
		length = Math.min(length, cur.getCount());
		while (i < length){
			Utils.stories.add(Utils.parseStory(cur, cath, false));
			cur.moveToNext();
			i++;
		}
		cur.close();
		Utils.sortStories();
	}
	
	public void findFavorites(int page){
		int length = Utils.settings.getAtr(Settings.NAME_SHOW_ITEMS_PER_PAGE);
		if (page == 0){
			Utils.END_REACHED = false;
			Utils.stories = new ArrayList<Story>();
		}
		Cursor cur = dataBase.query("stories", null, "fav<>0", null, null, null, "fav DESC", "" + (page * length) + " , " + ((page + 1) * length + 1));
		int i = 0;
		if (cur.getCount() < length + 1)
			Utils.END_REACHED = true;
		cur.moveToNext();
		length = Math.min(length, cur.getCount());
		while (i < length){
			Utils.stories.add(Utils.parseStory(cur, -1, false));
			cur.moveToNext();
			i++;
		}
		cur.close();
	}
	
	public void findByText(int page){
		Cursor cur;
		int length = Utils.settings.getAtr(Settings.NAME_SHOW_ITEMS_PER_PAGE);
		if (page == 0){
			Utils.stories = new ArrayList<Story>();
			Utils.END_REACHED = false;
		}
		cur = dataBase.query("stories", null, "cath LIKE " + Utils.generatePattern() + " AND text LIKE " + "'%" + Utils.SEARCH_STRING + "%'", null, null, null, null, "" + (page * length) + " , " + ((page + 1) * length + 1));
		if (cur.getCount() < length + 1)
			Utils.END_REACHED = true;
		int i = 0;
		cur.moveToNext();
		length = Math.min(length, cur.getCount());
		while (i < length){
			Utils.stories.add(Utils.parseStory(cur, -1, false));
			cur.moveToNext();
			i++;
		}
		cur.close();
		//Utils.sortStories();	
	}
	
	public void findLastRead(int page){
		int length = Utils.settings.getAtr(Settings.NAME_SHOW_ITEMS_PER_PAGE);
		if (page == 0){
			Utils.END_REACHED = false;
			Utils.stories = new ArrayList<Story>();
		}
		Cursor cur = dataBase.query("stories", null, "last<>0", null, null, null, "last DESC", "" + (page * length) + " , " + ((page + 1) * length + 1));
		if (cur.getCount() < length + 1)
			Utils.END_REACHED = true;
		int i = 0;
		cur.moveToNext();
		length = Math.min(length, cur.getCount());
		while (i < length){
			Utils.stories.add(Utils.parseStory(cur, -1, false));
			cur.moveToNext();
			i++;
		}
		cur.close();
	}
	
	public void setFavorite(int id, boolean favorite){
		ContentValues content = new ContentValues();
		if (favorite)
			content.put("fav", Utils.LAST_FAVORITE_INDEX++); 
		else content.put("fav", 0);
		dataBase.update("stories", content, "_id=" + id, null);
	}
	
	public synchronized void open(int id){
		if (Utils.settings.SAVE_HISTORY){
			ContentValues content = new ContentValues();
			content.put("last", Utils.LAST_READ_INDEX++);
			dataBase.update("stories", content, "_id=" + id, null);
		}
		Cursor cur = dataBase.query("stories", null, "_id=" + id, null, null, null, null);
		cur.moveToNext();
		Utils.curStory = Utils.parseStory(cur, -1, true);
		cur.close();
	}
 
}
