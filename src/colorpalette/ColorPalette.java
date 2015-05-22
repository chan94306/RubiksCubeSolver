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
 * 
 * @author andy
 *
 */
public class ColorPalette extends View {
	
	private SolverActivity mSolverActivity;
	private PaletteButton[] palette = new PaletteButton[6];
	private int selectedColor = -1;
	private ImageView selector;
	
	private int squareLength;

	public ColorPalette(Context context) {
		super(context);
		this.mSolverActivity = (SolverActivity) context;

		palette[0] = new PaletteButton(context, this, UIValues.red);
		palette[1] = new PaletteButton(context, this, UIValues.orange);
		palette[2] = new PaletteButton(context, this, UIValues.yellow);
		palette[3] = new PaletteButton(context, this, UIValues.green);
		palette[4] = new PaletteButton(context, this, UIValues.blue);
		palette[5] = new PaletteButton(context, this, UIValues.white);
				
		int topMargin, leftMargin;
		
		if(UIValues.displayHeight < UIValues.displayWidth){ //landscape orientation
			topMargin = UIValues.displayHeight/7;
			squareLength = (int)(1.0/6*(UIValues.displayHeight - 2*topMargin));
			leftMargin = (int)(0.5*UIValues.leftBound-0.5*squareLength);
//			canvas.drawRect(leftMargin, topMargin, leftMargin+squareLength, topMargin+6*squareLength, mPaint);
		}else{
			leftMargin = UIValues.displayWidth/7;
			squareLength = (int)(1.0/6*(UIValues.displayWidth - 2*leftMargin));
			topMargin = (int)(0.5*UIValues.topBound-0.5*squareLength);
		}
				
		for(int i = 0; i < palette.length; i++){
			palette[i].setX(leftMargin);
			palette[i].setY(topMargin+squareLength*i);
			palette[i].setWidth(squareLength);
			palette[i].setHeight(squareLength);
			palette[i].setBackgroundColor(palette[i].getColor());
			
			// this better work
			final PaletteButton pb = palette[i];
			
			setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					selectedColor = pb.getColor();
					selector.setX(pb.getX());
					selector.setY(pb.getY());
				}
			});
		}
				
		selector = new ImageView(context);
		selector.setImageResource(R.drawable.selector);
		selector.setVisibility(ImageView.VISIBLE);
	}
	
	public void updateSelectedColor(int color){
		selectedColor = color;
	}
	
	public void updateSelectorImage(int color){
		selectedColor = color;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		Paint mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.RED);
		mPaint.setTextSize(25);

		super.onDraw(canvas);
	}

	public void addSelf() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for(int i = 0; i < palette.length; i++){
			mSolverActivity.addContentView(palette[i], lp);
		}
		
		lp = new LayoutParams(squareLength, squareLength);
		mSolverActivity.addContentView(selector, lp);
	}
	
}
