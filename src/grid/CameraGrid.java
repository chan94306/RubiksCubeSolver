package grid;

import android.util.Log;

@Deprecated
public class CameraGrid extends Grid{
	
	// Height and width of the image provided by the camera, from CameraView
	private int imageWidth, imageHeight;
	// The entire image provided by the camera, in an integer array of RBG values
	private int[] imageRGB;
	// Raw YUV data, set by CameraView
	public byte[] imageYUV;

	public CameraGrid(int x, int y, int ds, int margin) {
		super(x, y, ds, margin);
		// TODO Auto-generated constructor stub
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

	public void updatePaints() {
		// Do color grid logic, then draw grid by calling super's onDraw
		ImageUtilities.decodeYUV420SP(imageRGB, imageYUV, imageWidth, imageHeight);
//		ImageUtilities.updateAverageGridPaints(this, imageRGB, 10, imageWidth, imageHeight);
	}
	
	public void updateToIdealColors() {
		int idealColor;
		String debug = "";
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				idealColor = ImageUtilities.getIdealColor(getFillColor(i, j));
				setFillColor(i, j, idealColor);
				debug += idealColor + " ";
			}
			debug += "\n";
		}
		Log.e("", debug);
	}
}
