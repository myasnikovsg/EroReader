package com.eroreader;

import android.app.Activity;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StoryAdapter extends BaseAdapter {
	private final Activity context;
	private int itemCount;

	public StoryAdapter(Activity context) {
		this.context = context;
	}
	
	public int getCount(){
		if (!Utils.END_REACHED)
			return Utils.stories.size() > 0 ? Utils.stories.size() + 1 : 0;
		else return Utils.stories.size();
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View storyView = inflater.inflate(R.layout.stories_list, null, true);
		TextView title = (TextView) storyView.findViewById(R.id.story_title);
		ImageView star = (ImageView) storyView.findViewById(R.id.star_fav1);
		TextView tag1 = (TextView) storyView.findViewById(R.id.tag1);
		TextView tag2 = (TextView) storyView.findViewById(R.id.tag2);
		TextView tag3 = (TextView) storyView.findViewById(R.id.tag3);
		TextView preview = (TextView) storyView.findViewById(R.id.text_preview);
		ImageView more = (ImageView) storyView.findViewById(R.id.more);
		
		if (!Utils.END_REACHED && position == getCount() - 1){
			title.setVisibility(View.GONE);
			star.setVisibility(View.GONE);
			tag1.setVisibility(View.GONE);
			tag2.setVisibility(View.GONE); 
			tag3.setVisibility(View.GONE);
			preview.setVisibility(View.GONE);
			more.setVisibility(View.VISIBLE);
			more.setClickable(false);
			return storyView;
		}
		
		storyView.setTag(Utils.stories.get(position).id);
		
		title.setText(Utils.stories.get(position).title);
		
		star.setTag(position);
		star.setClickable(true);
		
		if (Utils.stories.get(position).favorite)
			star.setImageResource(R.drawable.star_filled);
		else
			star.setImageResource(R.drawable.star_empty);
		
		SpannableString tagString = new SpannableString(Utils.getTag(Utils.stories.get(position).cathegory, 1));
		tagString.setSpan(new UnderlineSpan(), 0, tagString.length(), 0);
		tag1.setText(tagString);
		
		tagString = new SpannableString(Utils.getTag(Utils.stories.get(position).cathegory, 2));
		tagString.setSpan(new UnderlineSpan(), 0, tagString.length(), 0);
		tag2.setText(tagString);
		
		tagString = new SpannableString(Utils.getTag(Utils.stories.get(position).cathegory, 3));
		tagString.setSpan(new UnderlineSpan(), 0, tagString.length(), 0);
		tag3.setText(tagString);
		tag3.setEllipsize(TruncateAt.MARQUEE);
		
		preview.setText(Utils.ellipsize(Utils.stories.get(position).text));
		return storyView;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
}
