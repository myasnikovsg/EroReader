package com.eroreader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RowMenuAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] name;

	public RowMenuAdapter(Activity context, String[] name) {
		super(context, R.layout.main_menu, name);
		this.context = context;
		this.name = name;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.main_menu, null, true);
		TextView text = (TextView) rowView.findViewById(R.id.label);
		ImageView image = (ImageView) rowView.findViewById(R.id.icon);
		text.setText(name[position]);
		String s = name[position];
		
		if (s.startsWith("Продолжить"))
			image.setImageResource(R.drawable.continue_read);
		if (s.startsWith("Последние прочтённые"))
			image.setImageResource(R.drawable.last_readen);
		if (s.startsWith("Библиотека"))
			image.setImageResource(R.drawable.library);
		if (s.startsWith("Закладки"))
			image.setImageResource(R.drawable.favorites);
		if (s.startsWith("Поиск"))
			image.setImageResource(R.drawable.search);
		if (s.startsWith("Настройки"))
			image.setImageResource(R.drawable.settings);
		if (s.startsWith("Оставить отзыв"))
			image.setImageResource(R.drawable.comment);
		if (s.startsWith("Выход"))
			image.setImageResource(R.drawable.exit);	
		return rowView;
	}
}
