package solver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import andy_andrew.rubiks.R;

/**
 * 
 * @author andy
 * ArrowManager manages all the fucking arrows.
 * Arrow ImageViews are initialized and set up here.
 * Messages from SolverActivityHandler are processed here to change the arrows on display
 */
@SuppressLint("NewApi")
public class ArrowManager {
	private SolverActivity mSolverActivity;
	private ImageView[] cubeArrows = new ImageView[4];
	private ImageView cubeUpArrow;
	private ImageView cubeDownArrow;
	private ImageView cubeLeftArrow;
	private ImageView cubeRightArrow;
	
	private ImageView[] faceArrows = new ImageView[4];
	private ImageView upArrow;
	private ImageView downArrow;
	private ImageView leftArrow;
	private ImageView rightArrow;

	private ImageView CWArrow;
	private ImageView CCWArrow;
	
//	public static final boolean LEFT = true, RIGHT = false;
	
	// Used for cube rotations
	public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
	// Used for face rotations
	public static final int CW = 4, CCW = 5;

	public ArrowManager(Context context) {
		this.mSolverActivity = (SolverActivity) context;
	}

	public void initializeArrows() {
		// [0, 255]
		int alpha = 256*3/4;
		// This is stupid, but we can't seem to access it anywhere. We can toss this into dimens.xml OR find a smarter way to read it
		int oHeight = 936;
		int oWidth = 288;
		// The height of the arrow image, scaled down to match the size of the grid
		int height = (int) (UIValues.GRID_PROPORTION*Math.min(UIValues.displayHeight, UIValues.displayWidth));
		int width = (oWidth * height)/oHeight;
		
		LayoutParams p = new LayoutParams(width, height);

		for(int i = 0 ; i < faceArrows.length; i++){
			faceArrows[i] = new ImageView(mSolverActivity);
			faceArrows[i].setImageResource(R.drawable.arrow);
			mSolverActivity.addContentView(faceArrows[i], p);
			faceArrows[i].setImageAlpha(alpha);
			// This is cool shit to mess with
//			faceArrows[i].setRotationX(width/2);
//			faceArrows[i].setRotationY(width/2);
		}
		
		upArrow = faceArrows[0];
		rightArrow = faceArrows[1];
		downArrow = faceArrows[2];
		leftArrow = faceArrows[3];
		
		downArrow.setRotation(180);
				
		rightArrow.setPivotX(height/2);
		rightArrow.setPivotY(height/2);
		rightArrow.setRotation(90);
		
		leftArrow.setPivotX(width/2);
		leftArrow.setPivotY(width/2);
		leftArrow.setRotation(-90);
		
//		centerImage(leftArrow, UIValues.displayWidth/2, UIValues.displayHeight/2 - UIValues.squareLength);
//		centerImage(downArrow, UIValues.displayWidth/2 - UIValues.squareLength, UIValues.displayHeight/2);
//		centerImage(rightArrow, UIValues.displayWidth/2, UIValues.displayHeight/2 + UIValues.squareLength);
//		centerImage(upArrow, UIValues.displayWidth/2 + UIValues.squareLength, UIValues.displayHeight/2);
		
		CWArrow = new ImageView(mSolverActivity);
		CWArrow.setImageResource(R.drawable.clockwise);
		mSolverActivity.addContentView(CWArrow, new LayoutParams(UIValues.squareLength*2, UIValues.squareLength*2));
		CWArrow.setImageAlpha(alpha);

		CCWArrow = new ImageView(mSolverActivity);
		CCWArrow.setImageResource(R.drawable.counterclockwise);
		mSolverActivity.addContentView(CCWArrow, new LayoutParams(UIValues.squareLength*2, UIValues.squareLength*2));
		CCWArrow.setImageAlpha(alpha);

		// Length of one side of resized cube arrow (currently, cube arrow image is square)
		int length = (int) (UIValues.GRID_PROPORTION*Math.min(UIValues.displayWidth, UIValues.displayHeight)/1.5);
		p = new LayoutParams(length, length);
		
		for(int i = 0; i < cubeArrows.length; i++){
			cubeArrows[i] = new ImageView(mSolverActivity);
			cubeArrows[i].setImageResource(R.drawable.cubearrow);
			cubeArrows[i].setRotation(i*90);
			mSolverActivity.addContentView(cubeArrows[i], p);
			UIValues.centerImage(cubeArrows[i], UIValues.displayWidth/2, UIValues.displayHeight/2);
			cubeArrows[i].setImageAlpha(alpha);
		}
		
		cubeUpArrow = cubeArrows[0];
		cubeRightArrow = cubeArrows[1];
		cubeDownArrow = cubeArrows[2];
		cubeLeftArrow = cubeArrows[3];
		
//		displayArrow_Face(0, true);
//		displayArrow_Face(1, true);
//		displayArrow_Face(2, true);
		clearArrows();
	}
	
	private void centerOnGrid(ImageView img, int i, int j) {
		UIValues.centerImage(img, UIValues.displayWidth/2+i*UIValues.squareLength, UIValues.displayHeight/2+j*UIValues.squareLength);
	}

	/**
	 * @deprecated
	 * Displays an arrow to specify that the user should rotate the entire cube
	 * @param direction direction of rotation, either left/CW (true) or right/CCW (false)
	 */
	public void displayArrow_Cube(boolean direction) {
//		int GAP = 10;
		boolean LEFT = true;
		if(direction == LEFT){
//			int x = (int) (UIValues.displayWidth/2 - UIValues.squareLength*1.5 - cubeLeftArrow.getLayoutParams().width/2 - GAP);
//			centerImage(cubeLeftArrow, x, UIValues.displayHeight/2);
			cubeLeftArrow.setVisibility(ImageView.VISIBLE);
		}else{
//			int x = (int) (UIValues.displayWidth/2 + UIValues.squareLength*1.5 + cubeRightArrow.getLayoutParams().width/2 + GAP);
//			centerImage(cubeRightArrow, x, UIValues.displayHeight/2);
			cubeRightArrow.setVisibility(ImageView.VISIBLE);
		}
	}
	
	/**
	 * Displays an arrow to specify that the user should rotate the entire cube
	 * @param direction direction of rotation
	 */
	public void displayArrow_Cube(int direction) {
		cubeArrows[direction].setVisibility(ImageView.VISIBLE);
	}

	/**
	 * Displays an arrow to specify that the user should rotate a face/slab
	 * @param face which face to rotate (0 to 6)
	 * @param direction direction of rotation
	 */
	public void displayArrow_Face(int face, int direction) {
		// if direction is true, then it is a 'clockwise' rotation
		switch(face){
		case 0: 
			if(direction == CW){
				// Left face down
				downArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(downArrow, -1, 0);
			}else{
				// Left face up
				upArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(upArrow, -1, 0);
			}
			break;
		case 1: 
			if(direction == CW){
				CWArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(CWArrow, 0, 0);
			}else{
				CCWArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(CCWArrow, 0, 0);
			}
			break;
		case 2: 
			if(direction == CW){
				upArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(upArrow, 1, 0);
			}else{
				downArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(downArrow, 1, 0);
			}
			break;
		case 3: // never rotate the back face -- confirmed by Andrew
			Log.e("ArrowManager:displayArrow_Face", "case 3 should never be invoked");
			break;
		case 4: 
			if(direction == CW){
				rightArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(rightArrow, 0, 1);
			}else{
				leftArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(leftArrow, 0, 1);
			}
			break;
		case 5: 
			if(direction == CW){
				leftArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(leftArrow, 0, -1);
			}else{
				rightArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(rightArrow, 0, -1);
			}
			break;
		case 6: 
			if(direction == CW){
				upArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(upArrow, 0, 0);
			}else{
				downArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(downArrow, 0, 0);
			}
			break;
		}				
	}
	
	/**
	 * Sets all arrows invisible
	 * Remember to uncomment the part for cubeArrows
	 */
	public void clearArrows(){
		for(ImageView arrow: cubeArrows){
			arrow.setVisibility(ImageView.INVISIBLE);
		}
		for(ImageView arrow: faceArrows){
			arrow.setVisibility(ImageView.INVISIBLE);
		}
		CWArrow.setVisibility(ImageView.INVISIBLE);
		CCWArrow.setVisibility(ImageView.INVISIBLE);
	}

}
