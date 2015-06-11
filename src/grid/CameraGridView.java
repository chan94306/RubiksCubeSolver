package grid;

import solver.UIValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class CameraGridView extends GridView {
	private boolean updatePaints = true;
	
	public CameraGridView(Context context) {
		super(context, UIValues.leftBound, UIValues.topBound, UIValues.squareLength, 10, 15, UIValues.squareLength/8, Color.BLACK, false);
		grid = new CameraGrid(UIValues.leftBound, UIValues.topBound, UIValues.squareLength, margin);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (updatePaints) {
			((CameraGrid) grid).updatePaints();
		}
		super.onDraw(canvas);
	}
	
	/**
	 * Initialize imageWidth and imageHeight to argument values
	 * Initialize imageRBG array to width*height
	 * @param width
	 * @param height
	 */
	public void initImageDimensions(int width, int height) {
		((CameraGrid) grid).initImageDimensions(width, height);
	}
	

	public void updateToIdealColors() {
		((CameraGrid) grid).updateToIdealColors();
		invalidate();
	}
	
	public byte[] getImageYUV() {
		return ((CameraGrid) grid).imageYUV;
	}
	
	public void initImageYUV(int length) {
		((CameraGrid) grid).imageYUV = new byte[length]; 
	}
	
	public void enableUpdatePaints() {
		updatePaints = true;
	}
	
	public void disableUpdatePaints() {
		updatePaints = false;
	}
}
