package solver;

import java.util.HashMap;
import cube.Cube;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * View that displays some random shit ugh
 * @author Andy Zhang
 *
 */
@Deprecated
public class DrawOnTop extends View {
	private Paint[][] paintGrid = new Paint[3][3];
	private Paint mPaintRed;

	private final int MARGIN = 10;

	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>();

	// These are initialized in CameraView
//	public Bitmap mBitmap;
	public byte[] mYUVData;		// This gets data from CameraView
	public int[] mRGBData;		// YUV data is converted to RGB
	public int mImageWidth, mImageHeight;	
	// The above ints are necessary because mRGBData is a 1D array, so we must keep track of dimensions separately

	public DrawOnTop(Context context) {
		super(context);

		// Creates a bunch of paints that fill an area by default
//		for (int i = 0; i < paintGrid.length; i++) {
//			for (int j = 0; j < paintGrid[i].length; j++) {
//				paintGrid[i][j] = new Paint();
//				paintGrid[i][j].setStyle(Paint.Style.FILL);
//			}
//		}
//
//		mPaintRed = new Paint();
//		mPaintRed.setStyle(Paint.Style.STROKE);
//		mPaintRed.setColor(Color.RED);
//		mPaintRed.setTextSize(25);
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		if (mBitmap != null) {
			// Convert from YUV to RGB
//			decodeYUV420SP(mRGBData, mYUVData, mImageWidth, mImageHeight);

//			getAverageGridPaints();
//			drawGrid(canvas);
//		}
	}

	public void debugCubeColors(Cube cube) {
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < 3; i++) {
				String msg = "";
				for (int j = 0; j < 3; j++) {
					msg += cube.getSquare(face, i, j) + ",";
				}
				Log.e("Face: " + face, msg);
			}
		}
	}

	public void debugCubeInts(Cube cube) {
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < 3; i++) {
				String msg = "";
				for (int j = 0; j < 3; j++) {
					msg += cube.getSquare(face, i, j) + ",";

				}
				Log.e("Face: " + face, msg);
			}
		}
	}

	// TODO: To be completed
	public int[][] update() {
		int[][] newFace = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				newFace[i][j] = colorMap.get(getIdealColor(paintGrid[i][j].getColor()));
			}
		}
		// return a 2D array of the face 1 (the face pointed at camera) based on
		// what's currently on the cube
		// the 2D array will consist of all numbers 0-6, not the RGB value
		// use getColorValue and cleverness to convert from approximate RGB
		// value to face int value
		return newFace;
	}

	/**
	 * Input is a 32 bit int, the ARGB value
	 * & 0xFF truncates a value down to it's last 8 bits
	 * @param rgb
	 * @return
	 */
	public int getIdealColor(int rgb) {
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
	 * 5/27/15: this is the same as Cube.remap?
	 * Turns the semi-raw Color values + orange into integers 0 to 5, 
	 * the final step before solving
	 * @param cube the Cube to set the color data
	 */
	@SuppressLint("UseSparseArrays")
	public void recompileCubeColors(Cube cube) {
		for (int face = 0; face < 6; face++) {
			colorMap.put(cube.getColorValue(face), face);
		}

		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					cube.setSquare(face, i, j, colorMap.get(cube.getSquare(face, i, j)));
				}
			}
		}
	}
}