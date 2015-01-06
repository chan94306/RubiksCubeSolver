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
	private ImageView cubeUpArrow = cubeArrows[0];
	private ImageView cubeDownArrow = cubeArrows[1];
	private ImageView cubeLeftArrow = cubeArrows[2];
	private ImageView cubeRightArrow = cubeArrows[3];
	
	private ImageView[] faceArrows = new ImageView[4];
	private ImageView upArrow;
	private ImageView downArrow;
	private ImageView leftArrow;
	private ImageView rightArrow;

	private ImageView CWArrow;
	private ImageView CCWArrow;
	
	private static final boolean LEFT = true, RIGHT = false;
	
	public ArrowManager(Context context) {
		this.mSolverActivity = (SolverActivity) context;
	}

	public void initializeArrows() {
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

		CCWArrow = new ImageView(mSolverActivity);
		CCWArrow.setImageResource(R.drawable.counterclockwise);
		mSolverActivity.addContentView(CCWArrow, new LayoutParams(UIValues.squareLength*2, UIValues.squareLength*2));

//		displayArrow_Face(0, true);
//		displayArrow_Face(1, true);
//		displayArrow_Face(2, true);
		clearArrows();


	}
	
	private void centerOnGrid(ImageView img, int i, int j) {
		centerImage(img, UIValues.displayWidth/2+i*UIValues.squareLength, UIValues.displayHeight/2+j*UIValues.squareLength);
	}
	
	/**
	 * Center an ImageView on a specified point
	 * (Sets the center of the img to (x, y))
	 * @param img ImageView to be centered
	 * @param x x-coordinate of the point to which img will be centered
	 * @param y y-coordinate of the point to which img will be centered
	 */
	public void centerImage(ImageView img, int x, int y){
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

	/**
	 * Displays an arrow to specify that the user should rotate the entire cube
	 * @param direction direction of rotation, either left/CW (true) or right/CCW (false)
	 */
	public void displayArrow_Cube(boolean direction) {
		if(direction ==  LEFT){
			
		}else{
			
		}
	}

	/**
	 * Displays an arrow to specify that the user should rotate a face/slab
	 * @param face which face to rotate (0 to 6)
	 * @param direction direction of rotation, either CW (true) or CCW (false)
	 */
	public void displayArrow_Face(int face, boolean direction) {
		// if direction is true, then it is a 'clockwise' rotation
		switch(face){
		case 0: 
			if(direction){
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
			if(direction){
				CWArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(CWArrow, 0, 0);
			}else{
				CCWArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(CCWArrow, 0, 0);
			}
			break;
		case 2: 
			if(direction){
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
			if(direction){
				rightArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(rightArrow, 0, 1);
			}else{
				leftArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(leftArrow, 0, 1);
			}
			break;
		case 5: 
			if(direction){
				leftArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(leftArrow, 0, -1);
			}else{
				rightArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(rightArrow, 0, -1);
			}
			break;
		case 6: 
			if(direction){
				upArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(upArrow, 0, 0);
			}else{
				downArrow.setVisibility(ImageView.VISIBLE);
				centerOnGrid(downArrow, 0, 0);
			}
			break;
		}				
	}
	
	public void clearArrows(){
//		for(ImageView arrow: cubeArrows){
//			arrow.setVisibility(ImageView.INVISIBLE);
//		}
		for(ImageView arrow: faceArrows){
			arrow.setVisibility(ImageView.INVISIBLE);
		}
		CWArrow.setVisibility(ImageView.INVISIBLE);
		CCWArrow.setVisibility(ImageView.INVISIBLE);
	}

}
