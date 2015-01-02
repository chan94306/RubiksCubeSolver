package solver;

import android.content.Context;
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
public class ArrowManager {
	private SolverActivity mSolverActivity;
	private ImageView[] cubeArrows = new ImageView[4];
	private ImageView cubeUpArrow = cubeArrows[0];
	private ImageView cubeDownArrow = cubeArrows[1];
	private ImageView cubeLeftArrow = cubeArrows[2];
	private ImageView cubeRightArrow = cubeArrows[3];
	
	private ImageView upArrow;
	private ImageView downArrow;
	private ImageView leftArrow;
	private ImageView rightArrow;

	private ImageView CWArrow;
	private ImageView CCWArrow;
	
	public ArrowManager(Context context) {
		this.mSolverActivity = (SolverActivity) context;
	}

	public void initializeArrows() {
		upArrow = new ImageView(mSolverActivity);
		upArrow.setImageResource(R.drawable.arrow);
		upArrow.setY(0);
		LayoutParams arrowLayoutParams = new LayoutParams(100, Math.min(mSolverActivity.displayHeight, mSolverActivity.displayHeight) - 20);
		mSolverActivity.addContentView(upArrow, arrowLayoutParams);
	}

	/**
	 * Displays an arrow to specify that the user should rotate the entire cube
	 * @param direction direction of rotation, either left/CW (true) or right/CCW (false)
	 */
	private void displayArrow_Cube(boolean direction) {
		if(direction ==  LEFT){
			
		}else{
			
		}
	}

	/**
	 * Displays an arrow to specify that the user should rotate a face/slab
	 * @param face which face to rotate (0 to 6)
	 * @param direction direction of rotation, either CW (true) or CCW (false)
	 */
	private void displayArrow_Face(int face, boolean direction) {
		// if direction is true, then it is a 'clockwise' rotation
		switch(face){
		case 0: 
			if(direction){

			}else{

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
		case 3: // never rotate the back face -- double check with Andrew
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
