package grid;

import solver.UIValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class CameraGridView extends GridView {
	
	public CameraGridView(Context context) {
		// TODO: WHY IS IT RED?
		super(context, UIValues.leftBound, UIValues.topBound, UIValues.squareLength, 10, 15, UIValues.squareLength/8, Color.RED, false);
		grid = new CameraGrid(UIValues.leftBound, UIValues.topBound, UIValues.squareLength, margin);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		((CameraGrid) grid).updatePaints();
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
	}
	
	public byte[] getImageYUV() {
		return ((CameraGrid) grid).imageYUV;
	}
	
	public void initImageYUV(int length) {
		((CameraGrid) grid).imageYUV = new byte[length]; 
	}
}
