package com.eroreader;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

public class SettingsTextureList extends ListActivity {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Settings.applyScreenSettings(this);
		
		String[] name = Utils.settings.getArray();
		setTitle(Utils.settings.title);
		TextureAdapter adapter = new TextureAdapter(this, name);
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
	
	private class TextureAdapter extends ArrayAdapter<String> {
		private final Activity context;
		private final String[] name;

		public TextureAdapter(Activity context, String[] name) {
			super(context, R.layout.settings_value_set, name);
			this.context = context;
			this.name = name;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View root = inflater.inflate(R.layout.settings_texture_set, null, true);
			RadioButton item = (RadioButton)root.findViewById(R.id.item);
			root.setId(position);
			ImageView image = (ImageView) root.findViewById(R.id.texture);
			switch (position){
			case 0 :
				int width = 300;
				int height = 300;
				image.setImageBitmap(Bitmap.createBitmap(Utils.getColorArray(width, height, Utils.settings.getBackgroundColor()), width, height, Bitmap.Config.ARGB_8888));
				break;
			case 1 :
				image.setImageResource(R.drawable.back1);
				break;
			case 2 :
				image.setImageResource(R.drawable.back2);
				break;
			case 3 :
				image.setImageResource(R.drawable.back3);
				break;
			case 4 :
				image.setImageResource(R.drawable.back4);
				break;
			case 5 :
				image.setImageResource(R.drawable.back5);
				break;
			case 6 :
				image.setImageResource(R.drawable.back6);
				break;
			default :
				break;
			}
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