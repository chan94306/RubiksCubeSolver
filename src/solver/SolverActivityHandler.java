package solver;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * @author andy
 * SolverActivityHandler is the Handler that SolverActivity uses to process Messages, mostly from RubiksAlgorithm, the side thread.
 * Messages contain information specifying a notification to the UI, in the form of arrows to direct the user in solving the cube. 
 * 
 * Takes data from Model and modifies View
 */
public class SolverActivityHandler extends Handler{
	Context context;
	// For face/slab rotations
	private static final boolean CW = true, CCW = false;
	// For whole cube rotations
	private static final boolean LEFT = true, RIGHT = false;

	public SolverActivityHandler(Looper mainLooper, Context context) {
		super(mainLooper);
		this.context = context;
	}
	
	/*
	 * handleMessage() defines the operations to perform when
	 * the Handler receives a new Message to process.
	 */
	@Override
	public void handleMessage(Message msg) {
		Bundle bundle = msg.getData();
		/*
		 * These three will never all be extracted, 
		 * but we get them all first, and then figure out which ones exist,
		 * to decide what type of message is specified to the user.
		 */
		String toastString = bundle.getString("toastString");
		boolean direction = bundle.getBoolean("direction");
		int face = bundle.getInt("face", -1);
		
		// Handle Strings by Toasting
		if(toastString != null){
			Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show();
		}
		// If the face value exist, display a face/slab rotation arrow
		else if(face != -1){
			displayArrow_Face(face, direction);
		}
		// Else only the boolean exists, so it must be a whole cube rotation
		else{
			displayArrow_Cube(direction);
		}
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
