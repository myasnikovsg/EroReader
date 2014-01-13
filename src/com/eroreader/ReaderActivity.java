package com.eroreader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Display;

public class ReaderActivity extends Activity {
   
	private ViewPager pager;
	TextView page_counter;
    private ReaderAdapter awesomeAdapter;
    DisplayMetrics metrics;
    private int cur_page;
    Display display;
    private TextView title;
    static boolean orient_locked = false;
    
    public void refreshAfterDims(){
    	awesomeAdapter.notifyDataSetChanged();
    }
    
    public void recreate(){
    	String temp = "1" + page_counter.getText().subSequence(page_counter.getText().toString().indexOf(' '), page_counter.getText().length());
    	Texter.refresh();
    	Texter.context = this;
        Texter.prepareStory();
        awesomeAdapter.notifyDataSetChanged();
        setContentView(R.layout.reader); 
        awesomeAdapter = new ReaderAdapter();
        pager = (ViewPager) findViewById(R.id.pager);
        page_counter = (TextView) findViewById(R.id.page_counter);
        title = (TextView) findViewById(R.id.title);
        title.setEllipsize(TruncateAt.END);
        if (!Utils.settings.SHOW_STATUS_BAR){
        	page_counter.setVisibility(View.GONE);
        	title.setVisibility(View.GONE);
        } else {
        	page_counter.setText(temp);
        	title.setText(Utils.curStory.title);
        }
        cur_page = 0;
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
        	 @Override
        	    public void onPageSelected(int position) {
        	        cur_page = position;
        	        if (Utils.settings.SHOW_STATUS_BAR)
        	        	page_counter.setText((position + 1) + " / " + Texter.countPagesAverage());
        	    }
		});
        pager.setAdapter(awesomeAdapter);
    }
    
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //if (Texter.need_to_refresh)
        //	text_prepare.start();
        Texter.context = this;
        Texter.prepareStory();
        Settings.applyScreenSettings(this);
        
        display = getWindowManager().getDefaultDisplay();
        int orientation = display.getRotation();
		Utils.ORIENTATION = ((orientation == Surface.ROTATION_0) | (orientation == Surface.ROTATION_180));
        
		OrientationEventListener orientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
			
			@Override
			public void onOrientationChanged(int orientation) {
				if (Utils.settings.ORIENTATION_LOCK != 0)
					return;
				if (orientation % 90 != 0)
					return;
				boolean f = Utils.ORIENTATION; 
				orientation = display.getRotation();
				Utils.ORIENTATION = ((orientation == Surface.ROTATION_0) | (orientation == Surface.ROTATION_180));
				if (f != Utils.ORIENTATION){
					pager.setCurrentItem(Texter.getPageIndex(cur_page));
					awesomeAdapter.notifyDataSetChanged();
				}
			}
		};
        orientationListener.enable();
        
        setContentView(R.layout.reader); 
       
        awesomeAdapter = new ReaderAdapter();
        pager = (ViewPager) findViewById(R.id.pager);
        page_counter = (TextView) findViewById(R.id.page_counter);
        title = (TextView) findViewById(R.id.title);
        title.setEllipsize(TruncateAt.END);
        if (!Utils.settings.SHOW_STATUS_BAR){
        	page_counter.setVisibility(View.GONE);
        	title.setVisibility(View.GONE);
        } else {
        	page_counter.setText("n/a");
        	title.setText(Utils.curStory.title);
        }
        cur_page = 0;
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
        	 @Override
        	    public void onPageSelected(int position) {
        	        cur_page = position;
        	        if (Utils.settings.SHOW_STATUS_BAR)
        	        	page_counter.setText((position + 1) + " / " + Texter.countPagesAverage());
        	    }
		});
        pager.setAdapter(awesomeAdapter);
    }
   
    public void refresh_page_counter(String str){
    	page_counter.setText(str);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = this.getMenuInflater();
    	inflater.inflate(R.layout.reader_menu, menu);
    	MenuItem star = (MenuItem) menu.findItem(R.id.menu_favorite);
    	if (Utils.curStory.favorite){
    		star.setTitle(R.string.remove_from_favorites);
    		star.setIcon(R.drawable.star_empty);
    	}
    	else{
    		star.setTitle(R.string.add_to_favorites);
    		star.setIcon(R.drawable.star_filled);
    	}
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	MenuItem star = (MenuItem) menu.findItem(R.id.menu_favorite);
    	if (Utils.curStory.favorite){
    		star.setTitle(R.string.remove_from_favorites);
    		star.setIcon(R.drawable.star_empty);
    	}
    	else{
    		star.setTitle(R.string.add_to_favorites);
    		star.setIcon(R.drawable.star_filled);
    	}
    	return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.menu_favorite){
    		Utils.curStory.favorite = !Utils.curStory.favorite;
			Utils.base.setFavorite(Utils.curStory.id,
					Utils.curStory.favorite);
    	}
    	if (item.getItemId() == R.id.menu_exit){
    		this.finish();
    	}
    	if (item.getItemId() == R.id.menu_prop){
    		Intent intent = new Intent(ReaderActivity.this, StoryProperties.class);
			Utils.cur_property_story = Utils.curStory;
			startActivity(intent);
    	}
    	if (item.getItemId() == R.id.menu_settings){
    		Intent intent = new Intent(ReaderActivity.this, SettingsView.class);
			SettingsView.FROM_TEXT = true;
			SettingsView.reader_context = this;
			startActivity(intent);
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    private class ReaderAdapter extends PagerAdapter{
               
    	@Override
        public int getCount() {
    		return Texter.countPages();
        }
    	
    	
    	
    	@Override
    	public int getItemPosition(Object object) {
    	    return POSITION_NONE;
    	}

        @Override
        public Object instantiateItem(View collection, int position) {
        	Text text = Texter.createView(Texter.context, position, Utils.ORIENTATION);
            ((ViewPager) collection).addView(text, 0);
            return text;
        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
        	((ViewPager) collection).removeView((TextView) view);
        }

               
               
                @Override
                public boolean isViewFromObject(View view, Object object) {
                        return view==((TextView)object);
                }

               
            /**
             * Called when the a change in the shown pages has been completed.  At this
             * point you must ensure that all of the pages have actually been added or
             * removed from the container as appropriate.
             * @param container The containing View which is displaying this adapter's
             * page views.
             */
                @Override
                public void finishUpdate(View arg0) {}
               

                @Override
                public void restoreState(Parcelable arg0, ClassLoader arg1) {}

                @Override
                public Parcelable saveState() {
                        return null;
                }

                @Override
                public void startUpdate(View arg0) {}
       
    }
}
   
    