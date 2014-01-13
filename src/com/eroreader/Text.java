package com.eroreader;

import java.util.StringTokenizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.TextView;

public class Text extends TextView{
	
	public int view_width;
	public int view_height;
	public int position;
	public boolean orientation;
	public Context context;
	public String t;
	
	public Text(Context context, int position, boolean orientation){
		super(context);
		this.position = position;
		this.orientation = orientation;
		this.context = context;
	}
	
    @Override
   protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
       super.onSizeChanged(xNew, yNew, xOld, yOld);
       view_width = xNew;
       view_height = yNew;
       if (Texter.screen_height == 0){
    	   Texter.screen_height = Math.max(view_height, view_width);
       	   Texter.screen_width = Math.min(view_height, view_width);
       	   if (Texter.screen_height < Utils.SCREEN_HEIGHT)
       		   Texter.screen_width += Texter.screen_height - Utils.SCREEN_HEIGHT;
       	   else
       		   Texter.screen_height += Texter.screen_width - Utils.SCREEN_WIDTH;
    	   ((ReaderActivity) context).refreshAfterDims();
       }
       this.setText(Texter.getPage(position));
       //t = Texter.getPage(position);
       //Bitmap b = Bitmap.createBitmap(xNew, yNew, Bitmap.Config.ARGB_8888);
       //Canvas c = new Canvas(b);
       //onDraw(c);
       Path path = new Path();
       this.getPaint().getTextPath("Aasd", 0, 4, 0, 0, path);
       RectF rect = new RectF();
       path.computeBounds(rect, false);
       Log.w("asdasda", rect.toString());
      // this.setPadding(this.getPaddingLeft(), (int)(this.getPaddingTop() + Texter.lose / 2) , this.getPaddingRight(), (int)(this.getPaddingBottom() + Texter.lose / 2));
    }
}
