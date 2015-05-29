package grid;

import solver.UIValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class CameraGridView extends GridView{

	// Height and width of the image provided by the camera, from CameraView
	private int imageWidth, imageHeight;
	// The entire image provided by the camera, in an integer array of RBG values
	private int[] imageRGB;
	// Raw YUV data, set by CameraView
	public byte[] imageYUV;
	
	public CameraGridView(Context context) {
		super(context, UIValues.leftBound, UIValues.topBound, UIValues.squareLength, 10, 15, UIValues.squareLength/8, Color.RED);
//		refresh = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// Do color grid logic, then draw grid by calling super's onDraw
//		if (refresh) {
			ImageUtilities.decodeYUV420SP(imageRGB, imageYUV, imageWidth, imageHeight);
			ImageUtilities.updateAverageGridPaints(this, imageRGB, 10, imageWidth, imageHeight);
//			refresh = false;
//		}
		
		super.onDraw(canvas);
	}
	
	/**
	 * Initialize imageWidth and imageHeight to argument values
	 * Initialize imageRBG array to width*height
	 * @param width
	 * @param height
	 */
	public void initImageDimensions(int width, int height) {
		this.imageWidth = width;
		this.imageHeight = height;
		imageRGB = new int[width*height];
	}
	

	public void updateToIdealColors() {
		int idealColor;
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				idealColor = ImageUtilities.getIdealColor(cells[i][j].fillPaint.getColor());
				setFillColor(i, j, idealColor);
			}
		}
	}
}
