package com.eroreader;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


public class Search extends StoryList {

	EditText searchText;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.search);
		searchText = (EditText)findViewById(R.id.search_string);
		Utils.SEARCH_STRING = "";
		searchText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			public void afterTextChanged(Editable s) {
				Utils.SEARCH_STRING = s.toString();
				Utils.base.findByText(0);
				adapter.notifyDataSetChanged();
			}
		});
		registerForContextMenu(getListView());
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Менюшка");
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		int position = info.position;
		if (!Utils.END_REACHED && position == adapter.getCount() - 1)
			return;
		menu.add(Menu.NONE, position, 0, "Открыть");
		if (Utils.stories.get(position).favorite)
			menu.add(Menu.NONE, position, 1, "Удалить из избранного");
		else
			menu.add(Menu.NONE, position, 1, "Добавить в избранное");
		menu.add(Menu.NONE, position, 2, "Подробности");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int position = item.getItemId();
		String title = (String) item.getTitle();
		if (title.startsWith("Открыть")) {
			Utils.base.open(Utils.stories.get(position).id);
			Intent intent = new Intent(Search.this, ReaderActivity.class);
			startActivity(intent);
		}
		if (title.startsWith("Удалить") || title.startsWith("Добавить")) {
			Utils.stories.get(position).favorite = !Utils.stories.get(position).favorite;
			Utils.base.setFavorite(Utils.stories.get(position).id,
					Utils.stories.get(position).favorite);
			adapter.notifyDataSetChanged();
		}
		if (title.startsWith("Подробности")) {
			Intent intent = new Intent(Search.this, StoryProperties.class);
			Utils.cur_property_story = Utils.stories.get(position);
			startActivity(intent);
		}
		return true;
	}
	
	public void searchClickHandler(View v){
		Utils.base.findByText(0);
		adapter.notifyDataSetChanged();
	}
	
	public void filterClickHandler(View v){
		Intent intent = new Intent(Search.this, SearchFilter.class);
		startActivity(intent); 
	}
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		super.onListItemClick(parent, v, position, id);
		if (position == adapter.getCount() - 1){
			pages_loaded++;
			Utils.base.findByText(pages_loaded);
			adapter.notifyDataSetChanged();
		}
	}
}
