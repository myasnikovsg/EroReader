package com.eroreader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class StoryList extends ListActivity {
	public int pages_loaded = 0;

	public static final int STORIES = 0;
	public static final int SEARCH = 1;
	public static final int LAST = 2;
	public static final int FAVORITE = 3;

	StoryAdapter adapter;
	static int type;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Settings.applyScreenSettings(this);

		adapter = new StoryAdapter(this);
		setListAdapter(adapter);

		switch (type) {
		case STORIES:
			setTitle("��������� / "
					+ Utils.cathegories[Utils.stories.get(0).prime]);
			break;
		case SEARCH:
			setTitle("�����");
			break;
		case LAST:
			setTitle("��������");
			break;
		case FAVORITE:
			setTitle("���������");
			break;
		default:
			break;
		}
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("�������");
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		int position = info.position;
		if (!Utils.END_REACHED && position == adapter.getCount() - 1)
			return;
		menu.add(Menu.NONE, position, 0, "�������");
		if (Utils.stories.get(position).favorite)
			menu.add(Menu.NONE, position, 1, "������� �� ����������");
		else
			menu.add(Menu.NONE, position, 1, "�������� � ���������");
		menu.add(Menu.NONE, position, 2, "�����������");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int position = item.getItemId();
		String title = (String) item.getTitle();
		if (title.startsWith("�������")) {
			Utils.base.open(Utils.stories.get(position).id);
			Intent intent = new Intent(StoryList.this, ReaderActivity.class);
			startActivity(intent);
		}
		if (title.startsWith("�������") || title.startsWith("��������")) {
			Utils.stories.get(position).favorite = !Utils.stories.get(position).favorite;
			Utils.base.setFavorite(Utils.stories.get(position).id,
					Utils.stories.get(position).favorite);
			adapter.notifyDataSetChanged();
		}
		if (title.startsWith("�����������")) {
			Intent intent = new Intent(StoryList.this, StoryProperties.class);
			Utils.cur_property_story = Utils.stories.get(position);
			startActivity(intent);
		}
		return true;
	}

	public void starClickHandler(View v) {
		int position = (Integer) v.getTag();
		Utils.stories.get(position).favorite = !Utils.stories.get(position).favorite;
		if (Utils.stories.get(position).favorite)
			((ImageView) v).setImageResource(R.drawable.star_filled);
		else
			((ImageView) v).setImageResource(R.drawable.star_empty);
		Utils.base.setFavorite(Utils.stories.get(position).id,
				Utils.stories.get(position).favorite);
	}

	public void tagClickHandler(View v) {
		int position = Utils.getCathegoryIndex(((TextView) v).getText()
				.toString());
		Utils.base.findByCathegory(position, 0);
		Intent intent = new Intent(StoryList.this, StoryList.class);
		startActivity(intent);
		finishFromChild(StoryList.this);
	}

	private void refresh(){
		//adapter = new StoryAdapter(this);
		//setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	boolean isRunning = true;
	Handler handler;
	Thread text_prepare;
	public void onListItemClick(ListView parent, View v, int position, long id) {

		if (!Utils.END_REACHED && position == adapter.getCount() - 1) {
			pages_loaded++;
			switch (type) {
			case STORIES:
				Utils.base.findByCathegory(Utils.stories.get(0).prime, pages_loaded);
				break;
			case LAST:
				Utils.base.findLastRead(pages_loaded);
				setTitle("��������");
				break;
			case FAVORITE:
				Utils.base.findFavorites(pages_loaded);
				setTitle("���������");
				break;
			default:
				break;
			}
			
			refresh(); 
			return;
		}

		Utils.base.open((Integer) v.getTag());
		Texter.refresh();
    	Intent intent = new Intent(StoryList.this, ReaderActivity.class);
		startActivity(intent);
	}

}
