package grid;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * A square cell with a border rectangle, an inner rectangle, and an inner fill color
 * @author Andy Zhang
 *
 */
@Deprecated
public class Cell {
	// Must be RectF instead of Rect to support Canvas.drawRoundRect()
	RectF edge;
	RectF fill;
	Paint fillPaint;
	
	/**
	 * 
	 * @param x 
	 * @param y
	 * @param length
	 * @param fillMargin
	 */
	public Cell(int x, int y, int length, int fillMargin) {
		
		edge = new RectF(x, y, x+length, y+length);
		
		fill = new RectF(x+fillMargin, y+fillMargin, x+length-fillMargin, y+length-fillMargin);
		
		fillPaint = new Paint();
		fillPaint.setStyle(Paint.Style.FILL);
	}
}
