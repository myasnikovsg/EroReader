package com.eroreader;

import android.app.Activity;
import android.app.ListActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;

public class SettingsSimpleList extends ListActivity {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Settings.applyScreenSettings(this);
		
		String[] name = Utils.settings.getArray();
		setTitle(Utils.settings.title);
		SimpleSettingAdapter adapter = new SimpleSettingAdapter(this, name);
		setListAdapter(adapter);
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id){
		close(position);
	}
	
	public void close(int position){
		Utils.settings.setAtr(position);
		((SettingsView)Utils.context).refresh();
		((SettingsView)Utils.context).really_exit = true;
		finish();
	}
	
	private class SimpleSettingAdapter extends ArrayAdapter<String> {
		private final Activity context;
		private final String[] name;

		public SimpleSettingAdapter(Activity context, String[] name) {
			super(context, R.layout.settings_value_set, name);
			this.context = context;
			this.name = name;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View root = inflater.inflate(R.layout.settings_value_set, null, true);
			RadioButton item = (RadioButton)root.findViewById(R.id.item);
			root.setId(position);
			item.setText(name[position]);
			item.setId(position);
			item.setChecked(position == Utils.settings.selected);
			item.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					close(buttonView.getId());
				}
			});
			return root;
		}
		
	}
}