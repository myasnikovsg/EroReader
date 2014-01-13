package com.eroreader;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SearchFilter extends ListActivity implements RadioGroup.OnCheckedChangeListener{
	
	RadioButton and_radio;
	RadioButton or_radio;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Settings.applyScreenSettings(this);
		
		setContentView(R.layout.search_filter);
		and_radio = (RadioButton)findViewById(R.id.and_radio);
		or_radio = (RadioButton)findViewById(R.id.or_radio);
		RadioGroup radio = (RadioGroup)findViewById(R.id.radioGroup);
		radio.check(Utils.FILTER_STATE ? and_radio.getId() : or_radio.getId());
		radio.setOnCheckedChangeListener(this);
		String[] name = new String[Utils.cathegories.length];
		ListAdapter adapter = new CathAdapter(this, name);
		setListAdapter(adapter);  
	}
	
	public void checkClickHandler(View v){
		Utils.changeFilter(Utils.getCathegoryIndex(((CheckBox)v).getText().toString()), ((CheckBox)v).isChecked());
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id){
		
	}
 
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Utils.FILTER_STATE = checkedId == and_radio.getId() ? true : false;
	}
}
