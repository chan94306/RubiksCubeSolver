package grid;

import solver.SolverActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author Andy Zhang
 */
public class CellView extends View{
	// Must be RectF instead of Rect to support Canvas.drawRoundRect()
	private RectF edgeRect;
	private Paint edgePaint;
	protected Button fillButton;

	private int rx;
	
	/**
	 * 
	 * @param x X-coordinate of edge rectangle
	 * @param y Y-coordinate of edge rectangle
	 * @param length Length of edge (not fill)
	 * @param fillMargin Distance between edge and fill
	 * @param rx Roundedness of rectangle corners
	 */
	public CellView(Context context, int x, int y, int length, int thickness, int rx, int margin, int edgeColor) {
		super(context);
		
		edgeRect = new RectF(x, y, x+length, y+length);

		edgePaint = new Paint();
		edgePaint.setStyle(Paint.Style.STROKE);
		edgePaint.setColor(edgeColor);
		edgePaint.setStrokeWidth(thickness);
		
		fillButton = new Button(context);
		fillButton.setX(x + margin);
		fillButton.setY(y + margin);
		fillButton.setBackgroundColor(Color.BLACK);
		fillButton.setHeight(length - 2*margin);
		fillButton.setWidth(length - 2*margin);
		
		this.rx = rx;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRoundRect(edgeRect, rx, rx, edgePaint);
		fillButton.draw(canvas);
	}
	
	/**
	 * Sets the fill color (color of the inner, filled rectangle) 
	 */
	public void setFillColor(int color) {
		fillButton.setBackgroundColor(color);
	}
	
	/**
	 * @return 
	 */
	public int getFillColor() {
		return ((ColorDrawable) fillButton.getBackground()).getColor();
	}
	
	public void addSelf(Activity activity) {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		activity.addContentView(fillButton, lp);
	}
}
