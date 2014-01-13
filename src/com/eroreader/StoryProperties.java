package com.eroreader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class StoryProperties extends Activity {
	
	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		
		Settings.applyScreenSettings(this);
		
		setContentView(R.layout.story_properties);
		TextView title = (TextView) findViewById(R.id.title);
		TextView cathegories = (TextView) findViewById(R.id.cathegories);
		TextView length = (TextView) findViewById(R.id.length);
		TextView paging = (TextView) findViewById(R.id.paging);
		TextView add_date = (TextView) findViewById(R.id.add_date);
		
		title.setText(Utils.cur_property_story.title);
		String temp = "";
		
		for (int i = 0; i < Utils.cathegories.length; i++)
			if (Utils.cur_property_story.cathegory.charAt(i) == '1')
				if (temp == "")
					temp += Utils.cathegories[i];
				else
					temp += ", " + Utils.cathegories[i];
		
		cathegories.setText(temp);
		int len = Utils.base.findLengthById(Utils.cur_property_story.id);
		length.setText("" + len + " знаков");
		
		paging.setText("" + Math.round(len / 1000) + " страниц");
		
		add_date.setText("25.10.2011");
	}
	
}
