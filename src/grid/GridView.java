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
	private Paint gridPaint;
	private int rx;
	protected Cell[][] cells = new Cell[3][3];
	protected int margin = 0;

	/**
	 * TODO: document this!
	 * @param context
	 * @param x Horizontal coordinate of top left corner
	 * @param y Vertical coordinate of top left corner
	 * @param ds Side length of a cell in the grid 
	 * @param thickness Thickness of the line
	 * @param rx Radius of roundedness
	 */
	public GridView(Context context, int x, int y, int ds, int thickness, int rx, int margin, int color) {
		super(context);
		
		this.rx = rx;
		this.margin = margin;
		
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				// Note that x paired with j, while y paired with i
				cells[i][j] = new Cell(x+j*ds, y+i*ds, ds, margin);
			}
		}
		
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j].fillPaint = new Paint();
				cells[i][j].fillPaint.setStyle(Paint.Style.FILL);
			}
		}
		
		gridPaint = new Paint();
		gridPaint.setStyle(Paint.Style.STROKE);
		gridPaint.setColor(color);
		gridPaint.setStrokeWidth(thickness);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				// Draw the outer edge, and then the inner fill
				canvas.drawRoundRect(cells[i][j].edge, rx, rx, gridPaint);
				canvas.drawRoundRect(cells[i][j].fill, rx, rx, cells[i][j].fillPaint);
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
		cells[i][j].fillPaint.setARGB(a, r, g, b);
		// TODO: make sure this works, and test if invalidate() with arguments could be used instead 
		invalidate();
	}
	
	public void setFillColor(int i, int j, int color) {
		cells[i][j].fillPaint.setColor(color);
		// TODO: make sure this works, and test if invalidate() with arguments could be used instead 
		invalidate();
	}
	
	/**
	 * Gets the color of the fill of cell at i, j
	 * @param i Row
	 * @param j Column
	 * @return
	 */
	public int getFillColor(int i, int j) {
		return cells[i][j].fillPaint.getColor();
	}
}
