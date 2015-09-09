package grid;

import colorpalette.ColorPalette;
import solver.CameraImageProcessor;
import solver.UIValues;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class CameraGridView extends GridView implements CameraImageProcessor{	
	// Height and width of the image provided by the camera, from CameraView
	private int imageWidth, imageHeight;
	// The entire image provided by the camera, in an integer array of RBG values
	private int[] imageRGB;
	// Raw YUV data, set by CameraView
	private byte[] imageYUV;
	
	private boolean updatePaints = true;
	
	public CameraGridView(Activity activity, final ColorPalette colorPalette) {
		super(activity);
//		super(context, UIValues.leftBound, UIValues.topBound, UIValues.squareLength, 10, 15, UIValues.squareLength/8, Color.BLACK);
		cells = new CameraCellView[3][3];
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = new CameraCellView(activity, 
						UIValues.leftBound+j*UIValues.squareLength, 
						UIValues.topBound+i*UIValues.squareLength, 
						UIValues.squareLength, 
						10, 						// Make this relative to UIValues.something
						15, 						// Make this relative to UIValues.something
						UIValues.squareLength/8, 
						Color.BLACK,
						colorPalette);
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (updatePaints) {
			// Do color grid logic, then draw grid by calling super's onDraw
			ImageUtilities.decodeYUV420SP(imageRGB, imageYUV, imageWidth, imageHeight);
			ImageUtilities.updateAverageGridPaints(this, imageRGB, 10, imageWidth, imageHeight);
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
		this.imageWidth = width;
		this.imageHeight = height;
		imageRGB = new int[width*height];
	}
	

	public void updateToIdealColors() {
		int idealColor;
		String debug = "";
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				idealColor = ImageUtilities.getIdealColor(getFillColor(i, j));
				setFillColor(i, j, idealColor);
				debug += idealColor + " ";
			}
			debug += "\n";
		}
		Log.e("", debug);
		invalidate();
	}
	
	public byte[] getImageYUV() {
		return imageYUV;
	}
	
	public void initImageYUV(int length) {
		imageYUV = new byte[length]; 
	}
	
	public void enableUpdatePaints() {
		updatePaints = true;
	}
	
	public void disableUpdatePaints() {
		updatePaints = false;
	}
}
