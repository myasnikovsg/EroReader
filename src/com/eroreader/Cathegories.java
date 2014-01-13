package com.eroreader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Cathegories extends ListActivity {
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
		
		Settings.applyScreenSettings(this);
        setContentView(R.layout.cathegories);
        Utils.base.readCathegories();
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Utils.cathegories));
    }
    
    public void onListItemClick(ListView parent, View v, int position, long id) {
    	Utils.base.findByCathegory(position, 0);
    	StoryList.type = StoryList.STORIES;
    	Intent intent = new Intent(Cathegories.this, StoryList.class);
    	startActivity(intent);
    }
}
