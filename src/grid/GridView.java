package grid;

import android.app.Activity;
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
public abstract class GridView extends View {	
	protected CellView[][] cells;

//	/**
//	 * Does not initialize cells, hence abstract!
//	 * @param context
//	 * @param x Horizontal coordinate of top left corner
//	 * @param y Vertical coordinate of top left corner
//	 * @param cellLength Side length of a cell in the grid 
//	 * @param thickness Thickness of the line
//	 * @param rx Radius of roundedness
//	 * @param margin
//	 */
//	public GridView(Context context, int x, int y, int cellLength, int thickness, int rx, int margin, int color) {
//		super(context);	
//	}
	
	public GridView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				cells[i][j].draw(canvas);
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
		setFillColor(i, j, (a << 24) | (r << 16) | (g << 8) | b);
	}

	public void setFillColor(int i, int j, int color) {
		cells[i][j].setFillColor(color);
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
		return cells[i][j].getFillColor();
	}
	
	public void addCellsToActivity(Activity activity) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				cells[i][j].addSelf(activity);
			}
		}
	}
}
