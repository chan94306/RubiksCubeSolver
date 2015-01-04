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
	ArrowManager mArrowManager;
	// For face/slab rotations
	private static final boolean CW = true, CCW = false;
	// For whole cube rotations
	private static final boolean LEFT = true, RIGHT = false;

	public SolverActivityHandler(Context context, Looper mainLooper, ArrowManager am) {
		super(mainLooper);
		this.context = context;
		this.mArrowManager = am;
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
			mArrowManager.displayArrow_Face(face, direction);
		}
		// Else only the boolean exists, so it must be a whole cube rotation
		else{
			mArrowManager.displayArrow_Cube(direction);
		}
	}
}
