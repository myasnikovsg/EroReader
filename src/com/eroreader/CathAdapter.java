package com.eroreader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;


public class CathAdapter extends ArrayAdapter<String> {
	private final Activity context;

	public CathAdapter(Activity context, String[] name) {
		super(context, R.layout.cath_list, name);
		this.context = context;
	}
	
	@Override
	public boolean isEnabled(int position){
		return false;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = context.getLayoutInflater();
		View cathView = inflater.inflate(R.layout.cath_list, null, true);
		CheckBox box = (CheckBox) cathView.findViewById(R.id.checkBox);
		box.setChecked(Utils.FILTER_CATH.charAt(position + 1) == '1');
		box.setText(Utils.cathegories[position]);
		return cathView;
	}
	
}
