package solver;

import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

public class UIValues {
	public static double GRID_PROPORTION;
	public static int displayWidth, displayHeight;
	public static int topBound, leftBound, squareLength;
	
	public static int red = Color.RED;
	public static int orange = 0xFFFF9933;
	public static int yellow = Color.YELLOW;
	public static int green = 0xFF00CC00;
	public static int blue = Color.BLUE;
	public static int white = Color.WHITE;

	public static void init() {
		if(displayWidth < displayHeight){
			squareLength = (int)(displayWidth/3.0*GRID_PROPORTION);
			leftBound = (int)(displayWidth/2.0 - 1.5*squareLength);
			topBound = (int)(displayHeight/2.0 - 1.5*squareLength);
		}
		// flush by height and offset width
		else{
			squareLength = (int)(displayHeight/3.0*GRID_PROPORTION);
			leftBound = (int)(displayWidth/2.0 - 1.5*squareLength);
			topBound = (int)(displayHeight/2.0 - 1.5*squareLength);
		}
//		Log.e("12/31", ""+squareLength + " " + displayWidth + " " + displayHeight + " " + leftBound + " " + topBound);
		
	}
	
	/**
	 * Center an ImageView on a specified point
	 * (Sets the center of the img to (x, y))
	 * @param img ImageView to be centered
	 * @param x x-coordinate of the point to which img will be centered
	 * @param y y-coordinate of the point to which img will be centered
	 */
	public static void centerImage(ImageView img, int x, int y){
//		Log.e("img", "" + img.getLayoutParams().width + " " + img.getLayoutParams().height);
//		Log.e("img2", "" + img.getMeasuredHeight() + " " + img.getMeasuredHeight());
		int rot = (int) img.getRotation();
		
		// if it's 90 +/- 180
		if(rot%90 == 0 && rot%180 != 0){
			img.setX(x - img.getLayoutParams().height/2);
			img.setY(y - img.getLayoutParams().width/2);
		}else{
			img.setX(x - img.getLayoutParams().width/2);
			img.setY(y - img.getLayoutParams().height/2);
		}
	}

}
