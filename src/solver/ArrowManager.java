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
		LayoutParams p;
		for(int i = 0 ; i < faceArrows.length; i++){
			faceArrows[i] = new ImageView(mSolverActivity);
			faceArrows[i].setImageResource(R.drawable.arrow);
			faceArrows[i].setX(0);
			
//			if(i%2 == 0){
				// up or down; 288 is the width of the PNG in pixels
				p = new LayoutParams((int) (288*UIValues.GRID_PROPORTION), (int) (UIValues.GRID_PROPORTION*Math.min(UIValues.displayHeight, UIValues.displayWidth)));
//			}else{
//				p = new LayoutParams((int) (UIValues.GRID_PROPORTION*Math.min(UIValues.displayHeight, UIValues.displayWidth)), (int) (936*UIValues.GRID_PROPORTION));
//			}
			Log.e("", "" + p.width + " " + p.height);
			mSolverActivity.addContentView(faceArrows[i], p);
			faceArrows[i].setRotation(i*90);

		}
		
		upArrow = faceArrows[0];
		rightArrow = faceArrows[1];
		downArrow = faceArrows[2];
		leftArrow = faceArrows[3];
		
		upArrow.setX(UIValues.displayWidth/2-upArrow.getWidth());
		upArrow.setY(UIValues.displayHeight/2-upArrow.getHeight());
//		Log.e("", "" + upArrow.getMaxWidth() + " " + upArrow.getMaxHeight());
		
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
//				upArrow.setY(UIValues.displayHeight/2);
			}else{
				// Left face up
			}
			break;
		case 1: 
			if(direction){

			}else{

			}
			break;
		case 2: 
			if(direction){

			}else{

			}
			break;
		case 3: // never rotate the back face -- confirmed by Andrew
			Log.e("ArrowManager:displayArrow_Face", "case 3 should never be invoked");
			break;
		case 4: 
			if(direction){

			}else{

			}
			break;
		case 5: 
			if(direction){

			}else{

			}
			break;
		case 6: 
			if(direction){

			}else{

			}
			break;
		}				
	}

}
