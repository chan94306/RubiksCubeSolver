package grid;

import android.graphics.Color;
import solver.UIValues;

/**
 * Contains static methods used to process the camera image input into
 * 1. Data to be displayed
 * 2. Data to be inputted into the Cube model
 * @author andy
 *
 */
public class ImageUtilities {
	
	private static final int GRIDSIZE = 3;

	/**
	 * & 0xFF truncates a value down to it's last 8 bits
	 * @param rgb Raw color value, ARGB
	 * @return Best guess for what the Rubiks color is for the raw value
	 */
	public static int getIdealColor(int rgb) {
		float hsv[] = new float[3];

		int r = (rgb >> 16) & 0xFF; // gets the red, green, blue components
		int g = (rgb >> 8) & 0xFF;
		int b = (rgb) & 0xFF;
		Color.RGBToHSV(r, g, b, hsv);

		// TUNE THESE JANKY VALUES
		// if value is close to 0, then black-ish, but there are no black squares!
		// if(val < 0.1) return Color.BLACK;

		// if saturation is close to 0 and value is close to 1, then white-ish
		if (hsv[1] < 0.4 && hsv[2] > 0.6) {
			// Log.e("ideal", "WHITE");
			return Color.WHITE;
		}
		// else use hue to determine spectrum color
		else {
			if (hsv[0] >= 0 && hsv[0] < 15) {
				// Log.e("ideal", "RED");
				return Color.RED;
			} else if (hsv[0] >= 15 && hsv[0] < 45) {
				// Log.e("ideal", "ORANGE_0xFF8800");
				return 0xFF8800; // ORANGE
			} else if (hsv[0] >= 45 && hsv[0] < 90) {
				// Log.e("ideal", "YELLOW");
				return Color.YELLOW;
			} else if (hsv[0] >= 90 && hsv[0] < 180) {
				// Log.e("ideal", "GREEN");
				return Color.GREEN;
			} else if (hsv[0] >= 180 && hsv[0] < 300) {
				// Log.e("ideal", "BLUE");
				return Color.BLUE;
			} else if (hsv[0] >= 300 && hsv[0] < 360) {
				// Log.e("ideal", "RED");
				return Color.RED;
			} else {
				// Log.e("ideal", "BLACK- something went wrong");
				return Color.BLACK; // something went wrong in this case
			}
		}
	}

	/**
	 * sets the squarePaints Paint array with the 9 average colors of the 9 squares of a face
	 * For Model
	 */
	public static void updateAverageGridPaints(CameraGridView gv, int[] RGBData, int margin, int mImageWidth, int mImageHeight) {
		// Bounds of the square, reset every loop of i for the 9 squares
		double prop = UIValues.GRID_PROPORTION;
		int leftBound, topBound;
		int squareLength;
		int redVal, greenVal, blueVal; // don't actually to explicitly state these
		int redMean = 0, greenMean = 0, blueMean = 0;

		for (int m = 0; m < GRIDSIZE; m++) {
			for (int n = 0; n < GRIDSIZE; n++) {

				// Assigns the top left corner of the square
				// if width is less than height, flush by width and offset
				// height
				// xyzz
				if (mImageWidth < mImageHeight) {
					squareLength = (int) (mImageWidth / 3.0 * prop);
					leftBound = (int) (mImageWidth / 2.0 - 1.5 * squareLength + squareLength * n);
					topBound = (int) (mImageHeight / 2.0 - 1.5 * squareLength + squareLength * m);
				}
				// else flush by height and offset width
				else {
					squareLength = (int) (mImageHeight / 3.0 * prop);
					leftBound = (int) (mImageWidth / 2.0 - 1.5 * squareLength + squareLength * n);
					topBound = (int) (mImageHeight / 2.0 - 1.5 * squareLength + squareLength * m);
				}
				// Log.e("", "" + squareLength);

				// Log.e("DOT 12/31", ""+ leftMargin + " " + topMargin + " " +
				// topMargin + " " + topMargin);
				for (int j = topBound + margin; j < topBound + squareLength
						- margin; j++) {
					int pix = j * mImageWidth + leftBound + margin; // check border conditions
					for (int i = leftBound + margin; i < leftBound
							+ squareLength - margin; i++, pix++) {
						// Log.e("DOT 12/31", "" + pix);
						redVal = (RGBData[pix] >> 16) & 0xff; // gets red component
						greenVal = (RGBData[pix] >> 8) & 0xff;
						blueVal = RGBData[pix] & 0xff;

						redMean += redVal;
						greenMean += greenVal;
						blueMean += blueVal;
					}

					// pix += mImageWidth - squareLength * 3;
				}
				// Number of pixels traversed should be the area of the square,
				// but make sure to double check
				int pixArea = (int) Math.pow(squareLength - 2 * margin, 2);

				redMean /= pixArea;
				greenMean /= pixArea;
				blueMean /= pixArea;

				gv.setFillColor(m, n, 255, (int) redMean, (int) greenMean, (int) blueMean);
//				paintGrid[m][n].setARGB((int) 255.0, (int) redMean, (int) greenMean, (int) blueMean);
			}
		}
	}
	
	/**
	 * Converts YUV to RGB
	 * For View
	 * @param rgb
	 * @param yuv420sp
	 * @param width
	 * @param height
	 */
	static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
		final int frameSize = width * height;

		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;

				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}
	}
}
