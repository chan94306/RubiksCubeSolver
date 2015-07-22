package colorpalette;

import solver.*;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import andy_andrew.rubiks.R;

/**
 * TODO: Refactor so that the PaletteButtons are actually PART of this view, instead of just initialized 
 * and manipulated in here.
 * @author Andy Zhang
 *
 */
public class ColorPalette extends View {
	
	private PaletteButton[] colors = new PaletteButton[6];
	private int selectedColor = 0;	// This is "clear" (alpha is 0). Cannot use -1 because that is Color.WHITE (0xffffffff)
	private ImageView selector;
	
	private int squareLength;

	public ColorPalette(Context context) {
		super(context);
		
		colors[0] = new PaletteButton(context, this, UIValues.RED);
		colors[1] = new PaletteButton(context, this, UIValues.ORANGE);
		colors[2] = new PaletteButton(context, this, UIValues.YELLOW);
		colors[3] = new PaletteButton(context, this, UIValues.GREEN);
		colors[4] = new PaletteButton(context, this, UIValues.BLUE);
		colors[5] = new PaletteButton(context, this, UIValues.WHITE);
		
		int topMargin, leftMargin;
		
		if(UIValues.displayHeight < UIValues.displayWidth){ //landscape orientation
			topMargin = UIValues.displayHeight/7;
			squareLength = (int)(1.0/6*(UIValues.displayHeight - 2*topMargin));
			leftMargin = (int)(0.5*UIValues.leftBound-0.5*squareLength);
		}else{
			leftMargin = UIValues.displayWidth/7;
			squareLength = (int)(1.0/6*(UIValues.displayWidth - 2*leftMargin));
			topMargin = (int)(0.5*UIValues.topBound-0.5*squareLength);
		}
				
		for(int i = 0; i < colors.length; i++){
			colors[i].setX(leftMargin);
			colors[i].setY(topMargin+squareLength*i);
			colors[i].setWidth(squareLength);
			colors[i].setHeight(squareLength);
			colors[i].setBackgroundColor(colors[i].getColor());
		}
				
		selector = new ImageView(context);
		selector.setImageResource(R.drawable.selector);
		selector.setVisibility(ImageView.VISIBLE);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO: draw what is necessary. selector? 
	}

	public void addSelf(SolverActivity mSolverActivity) {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for(int i = 0; i < colors.length; i++){
			mSolverActivity.addContentView(colors[i], lp);
		}
		
		lp = new LayoutParams(squareLength, squareLength);
		mSolverActivity.addContentView(selector, lp);
	}
	
	public void setPaletteVisibility(int visibility) {
		selector.setVisibility(visibility);
		for (int i = 0 ; i < colors.length; i++) {
			colors[i].setVisibility(visibility);
		}
	}

	public void notifyColorSelection(int color, float x, float y) {
		selectedColor = color;
		selector.setX(x);
		selector.setY(y);
	}
	
	public int getSelectedColor() {
		return selectedColor;
	}
}
