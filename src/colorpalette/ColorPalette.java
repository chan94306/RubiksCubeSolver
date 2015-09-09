package colorpalette;

import solver.*;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import andy_andrew.rubiks.R;

/**
 * TODO: Refactor so that the PaletteButtons are actually PART of this view, instead of just initialized 
 * and manipulated in here.
 * @author Andy Zhang
 *
 */
public class ColorPalette extends ViewGroup {
	public static final int NO_COLOR = 0;	// This is "clear" (alpha is 0). Cannot use -1 because that is Color.WHITE (0xffffffff)
	
	private PaletteButton[] colors = new PaletteButton[6];
	private int selectedColor = NO_COLOR;
	private ImageView selector;
	
	private int squareLength, topMargin, leftMargin;

	public ColorPalette(Context context) {
		super(context);
				
		if(UIValues.displayHeight < UIValues.displayWidth){ //landscape orientation
			topMargin = UIValues.displayHeight/7;
			squareLength = (int)(1.0/6*(UIValues.displayHeight - 2*topMargin));
			leftMargin = (int)(0.5*UIValues.leftBound-0.5*squareLength);
		}else{
			leftMargin = UIValues.displayWidth/7;
			squareLength = (int)(1.0/6*(UIValues.displayWidth - 2*leftMargin));
			topMargin = (int)(0.5*UIValues.topBound-0.5*squareLength);
		}
		setX(leftMargin);
		setY(topMargin);
		
		colors[0] = new PaletteButton(context, this, UIValues.RED);
		colors[1] = new PaletteButton(context, this, UIValues.ORANGE);
		colors[2] = new PaletteButton(context, this, UIValues.YELLOW);
		colors[3] = new PaletteButton(context, this, UIValues.GREEN);
		colors[4] = new PaletteButton(context, this, UIValues.BLUE);
		colors[5] = new PaletteButton(context, this, UIValues.WHITE);
		
		for(int i = 0; i < colors.length; i++){
			colors[i].setBackgroundColor(colors[i].getColor());
			colors[i].setVisibility(View.INVISIBLE);
			addView(colors[i]);
		}
		
		selector = new ImageView(context);
		selector.setImageResource(R.drawable.selector);
		selector.setVisibility(View.INVISIBLE);
		addView(selector, squareLength, squareLength);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO: draw what is necessary. selector? 
	}
	
	public void setPaletteVisibility(int visibility) {
		selector.setVisibility(visibility);
		for (int i = 0 ; i < colors.length; i++) {
			colors[i].setVisibility(visibility);
		}
	}

	public void notifyColorSelection(int color, float x, float y) {
		selectedColor = color;
		selector.layout((int)x, (int)y, (int) (x + squareLength), (int)(y + squareLength));
	}
	
	public int getSelectedColor() {
		return selectedColor;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.e("", "" + l + " " + t + " " + r + " " + b);
		for (int i = 0; i < colors.length; i++) {
			colors[i].layout(0, squareLength*i, squareLength, squareLength*(i+1));
		}
		selector.layout(0, 0, 0, 0);
	}
}
