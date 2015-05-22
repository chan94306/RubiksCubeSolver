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
 * View that displays 
 * @author Andy Zhang
 *
 */
public class DrawOnTop extends View {
	private Paint[][] paintGrid = new Paint[3][3];
	private Paint mPaintRed;

	private final int MARGIN = 10;

	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>();

	// These are initialized in CameraView
	public Bitmap mBitmap;
	public byte[] mYUVData;		// This gets data from CameraView
	public int[] mRGBData;
	public int mImageWidth, mImageHeight;

	public DrawOnTop(Context context, Cube current) {
		super(context);

		// Creates a bunch of paints that fill an area by default
		for (int i = 0; i < paintGrid.length; i++) {
			for (int j = 0; j < paintGrid[i].length; j++) {
				paintGrid[i][j] = new Paint();
				paintGrid[i][j].setStyle(Paint.Style.FILL);
			}
		}

		mPaintRed = new Paint();
		mPaintRed.setStyle(Paint.Style.STROKE);
		mPaintRed.setColor(Color.RED);
		mPaintRed.setTextSize(25);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mBitmap != null) {
			// Convert from YUV to RGB
			decodeYUV420SP(mRGBData, mYUVData, mImageWidth, mImageHeight);

			// Draw bitmap
			mBitmap.setPixels(mRGBData, 0, mImageWidth, 0, 0, mImageWidth, mImageHeight);

			getAverageGridPaints();
			drawGrid(canvas);
		}
		super.onDraw(canvas);
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

	public void readFace(int face, Cube cube) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cube.setSquare(face, i, j, getIdealColor(paintGrid[i][j].getColor()));
			}
		}
		cube.setColorValue(face, getIdealColor(paintGrid[1][1].getColor()));
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

	// Either assign color values first, and then comb though and turn them into
	// 0-5,
	// or use a smarter algorithm to group raw values and assign them 0-5

	// Input is a 32 bit int, the ARGB value
	// & 0xFF truncates a value down to it's last 8 bits
	public int getIdealColor(int rgb) {
		float hsv[] = new float[3];
		// hue = hsv[0], saturation = hsv[1], value = hsv[2];

		int r = (rgb >> 16) & 0xFF; // gets the red, green, blue components
		int g = (rgb >> 8) & 0xFF;
		int b = (rgb) & 0xFF;
		Color.RGBToHSV(r, g, b, hsv);

		// TUNE THESE JANKY VALUES
		// if value is close to 0, then black-ish
		// if(val < 0.1) return Color.BLACK; // no black squares doe

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
				return 0xFF8800; // ORANGE, this is legal usage after checking
									// that it's correctly interpreted as a hex
									// number
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
	 * Turns the semi-raw Color values + orange into integers 0 to 5, the final
	 * step before solving
	 * @param cube the Cube to set the color data
	 */
	@SuppressLint("UseSparseArrays")
	public void recompileCubeColors(Cube cube) {
		for (int face = 0; face < 6; face++) {
			colorMap.put(cube.getColorValue(face), face);
		}
//		Log.e("tag", colorMap.toString());

		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					cube.setSquare(face, i, j, colorMap.get(cube.getSquare(face, i, j)));
				}
			}
		}
	}

	/**
	 * Draws a 3x3 grid to show user how to align the cube
	 * @param canvas Canvas draw the grid on
	 */
	public void drawGrid(Canvas canvas) {
		int squareLength = UIValues.squareLength;
		int leftBound = UIValues.leftBound;
		int topBound = UIValues.topBound;

		// canvas.drawRect(1, 0, screenWidth-1, screenHeight-1, mPaintRed);
		canvas.drawRect(leftBound, topBound, leftBound + 3 * squareLength,
				topBound + 3 * squareLength, mPaintRed);
		canvas.drawLine(leftBound, topBound + 1 * squareLength, leftBound + 3
				* squareLength, topBound + 1 * squareLength, mPaintRed);
		canvas.drawLine(leftBound, topBound + 2 * squareLength, leftBound + 3
				* squareLength, topBound + 2 * squareLength, mPaintRed);
		canvas.drawLine(leftBound + 1 * squareLength, topBound, leftBound + 1
				* squareLength, topBound + 3 * squareLength, mPaintRed);
		canvas.drawLine(leftBound + 2 * squareLength, topBound, leftBound + 2
				* squareLength, topBound + 3 * squareLength, mPaintRed);

		int MARGIN = squareLength / 8;
		for (int i = 0; i < paintGrid.length; i++) {
			for (int j = 0; j < paintGrid[i].length; j++) {
				canvas.drawRect(leftBound + squareLength * j + MARGIN, topBound
						+ squareLength * i + MARGIN, leftBound + squareLength
						* (j + 1) - MARGIN, topBound + squareLength * (i + 1)
						- MARGIN, paintGrid[i][j]);
			}
		}
	}

	/**
	 * sets the squarePaints Paint array with the 9 average colors of the 9 squares of a face
	 */
	public void getAverageGridPaints() {
		// Bounds of the square, reset every loop of i for the 9 squares
		double prop = UIValues.GRID_PROPORTION;
		int leftBound, topBound;
		int squareLength;
		int redVal, greenVal, blueVal; // don't actually to explicitly state these
		int redMean = 0, greenMean = 0, blueMean = 0;

		for (int m = 0; m < paintGrid.length; m++) {
			for (int n = 0; n < paintGrid[m].length; n++) {

				// Assigns the top left corner of the square
				// if width is less than height, flush by width and offset
				// height
				// xyzz
				if (mImageWidth < mImageHeight) {
					squareLength = (int) (mImageWidth / 3.0 * prop);
					leftBound = (int) (mImageWidth / 2.0 - 1.5 * squareLength + squareLength
							* n);
					topBound = (int) (mImageHeight / 2.0 - 1.5 * squareLength + squareLength
							* m);
				}
				// else flush by height and offset width
				else {
					squareLength = (int) (mImageHeight / 3.0 * prop);
					leftBound = (int) (mImageWidth / 2.0 - 1.5 * squareLength + squareLength
							* n);
					topBound = (int) (mImageHeight / 2.0 - 1.5 * squareLength + squareLength
							* m);
				}
				// Log.e("", "" + squareLength);

				// Log.e("DOT 12/31", ""+ leftMargin + " " + topMargin + " " +
				// topMargin + " " + topMargin);
				for (int j = topBound + MARGIN; j < topBound + squareLength
						- MARGIN; j++) {
					int pix = j * mImageWidth + leftBound + MARGIN; // check border conditions
					for (int i = leftBound + MARGIN; i < leftBound
							+ squareLength - MARGIN; i++, pix++) {
						// Log.e("DOT 12/31", "" + pix);
						redVal = (mRGBData[pix] >> 16) & 0xff; // gets red component
						greenVal = (mRGBData[pix] >> 8) & 0xff;
						blueVal = mRGBData[pix] & 0xff;

						redMean += redVal;
						greenMean += greenVal;
						blueMean += blueVal;
					}

					// pix += mImageWidth - squareLength * 3;
				}
				// Number of pixels traversed should be the area of the square,
				// but make sure to double check
				int pixArea = (int) Math.pow(squareLength - 2 * MARGIN, 2);

				redMean /= pixArea;
				greenMean /= pixArea;
				blueMean /= pixArea;

				paintGrid[m][n].setARGB((int) 255.0, (int) redMean, (int) greenMean, (int) blueMean);
			}
		}
	}

	/**
	 * Converts YUV to RGB
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