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

		String toastMessage = bundle.getString("toastMessage");
		if(toastMessage != null){
			Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
			//					dialog.setText(bundle.getString("Instruction message"));
		}else{
			displayArrow(bundle.getInt("face"), bundle.getBoolean("direction"));
		}
	}

	private void displayArrow(int face, boolean direction) {
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
		case 3: // never rotate the back face
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
