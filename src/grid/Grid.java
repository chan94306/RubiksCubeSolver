package grid;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 3x3 Grid Model
 * @author andy
 *
 */
public class Grid {
	
	public final int length = 3;
	private Cell[][] cells;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param ds
	 * @param margin
	 */
	public Grid(int x, int y, int ds, int margin) {
		// TODO Auto-generated constructor stub
		
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				// Note that x paired with j, while y paired with i
				cells[i][j] = new Cell(x+j*ds, y+i*ds, ds, margin);
				cells[i][j].fillPaint = new Paint();
				cells[i][j].fillPaint.setStyle(Paint.Style.FILL);
			}
		}
	}
	
	public RectF getEdgeRect(int i, int j) {
		return cells[i][j].edge;
	}
	
	public RectF getFillRect(int i, int j) {
		return cells[i][j].fill;
	}
	
	public Paint getFillPaint(int i, int j) {
		return cells[i][j].fillPaint;
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
	}
	
	public void setFillColor(int i, int j, int color) {
		cells[i][j].fillPaint.setColor(color);
	}
}
