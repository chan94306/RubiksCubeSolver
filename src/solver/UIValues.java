package solver;

import android.util.Log;

public class UIValues {
	public static double GRID_PROPORTION;
	public static int displayWidth, displayHeight;
	public static int topBound, leftBound, squareLength;
	
	public static double getGridProportion() {
		return GRID_PROPORTION;
	}
	public static void setGridProportion(double gridProportion) {
		GRID_PROPORTION = gridProportion;
	}
	public static int getDisplayWidth() {
		return displayWidth;
	}
	public static void setDisplayWidth(int displayWidth) {
		UIValues.displayWidth = displayWidth;
	}
	public static int getDisplayHeight() {
		return displayHeight;
	}
	public static void setDisplayHeight(int displayHeight) {
		UIValues.displayHeight = displayHeight;
	}
	public static void init() {
		//And use it to set up margins
		if(displayWidth < displayHeight){
			squareLength = (int)(displayWidth/3.0*GRID_PROPORTION);
			leftBound = (int) (displayWidth/2.0 - 1.5*squareLength);
			topBound = (int)(displayHeight/2.0 - 1.5*squareLength);
		}
		// flush by height and offset width
		else{
			squareLength = (int)(displayHeight/3.0*GRID_PROPORTION);
			leftBound = (int)(displayWidth/2.0 - 1.5*squareLength);
			topBound = (int) (displayHeight/2.0 - 1.5*squareLength);
		}
//		Log.e("12/31", ""+squareLength + " " + displayWidth + " " + displayHeight + " " + leftBound + " " + topBound);
	}

}
