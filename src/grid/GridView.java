package grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * 3x3 Grid
 * Cells labeled and displayed like this: (Row, Column) 
 * |-----------------|
 * | 0,0 | 0,1 | 0,2 |
 * |-----------------|
 * | 1,0 | 1,1 | 1,2 |
 * |-----------------|
 * | 2,0 | 2,1 | 2,2 |
 * |-----------------|
 * View
 * @author Andy Zhang
 *
 */
public class GridView extends View {
	private Paint edgePaint;
	private int rx;
	protected Grid grid;
	protected int margin = 0;

	/**
	 * TODO: document this!
	 * @param context
	 * @param x Horizontal coordinate of top left corner
	 * @param y Vertical coordinate of top left corner
	 * @param ds Side length of a cell in the grid 
	 * @param thickness Thickness of the line
	 * @param rx Radius of roundedness
	 * @param margin
	 */
	public GridView(Context context, int x, int y, int ds, int thickness, int rx, int margin, int color, boolean defaultGrid) {
		super(context);
		
		if (defaultGrid)
			this.grid = new Grid(x, y, ds, margin);
		
		this.rx = rx;
		this.margin = margin;
		
		edgePaint = new Paint();
		edgePaint.setStyle(Paint.Style.STROKE);
		edgePaint.setColor(color);
		edgePaint.setStrokeWidth(thickness);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				// Draw the outer edge, and then the inner fill
				canvas.drawRoundRect(grid.getEdgeRect(i, j), rx, rx, edgePaint);
				canvas.drawRoundRect(grid.getFillRect(i, j), rx, rx, grid.getFillPaint(i, j));
			}
		}
	}
	
	/**
	 * 
	 * @param i 
	 * @param j
	 * @param a
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setFillColor(int i, int j, int a, int r, int g, int b) {
		grid.setFillColor(i, j, a, r, g, b);
		// TODO: make sure this works, and test if invalidate() with arguments could be used instead 
		invalidate();
	}

//	public void setFillColor(int i, int j, int color) {
//		grid.setFillColor(i, j, color);
//		// TODO: make sure this works, and test if invalidate() with arguments could be used instead 
//		invalidate();
//	}
	
	/**
	 * Gets the color of the fill of cell at i, j
	 * @param i Row
	 * @param j Column
	 * @return
	 */
	public int getFillColor(int i, int j) {
		return grid.getFillColor(i, j);
	}
}
