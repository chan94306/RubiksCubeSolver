package solver;


import cube.Cube;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class RubiksAlgorithm extends AsyncTask<String, Integer, Void> {

	TextView dialog;
	private Cube current, future;
	private Context applicationContext;
	DrawOnTop mDrawOnTop;
	private Handler mHandler;
	private boolean forceSkip = false;

	private static final boolean CW = true, CCW = false;
	private static final int REFRESH_RATE = 500;

	public RubiksAlgorithm(TextView dialog, Cube current, Cube future, 
			DrawOnTop mDrawOnTop, Handler mHandler, Context applicationContext){
		this.dialog = dialog;
		this.current = current;
		this.future = future;
		this.mDrawOnTop = mDrawOnTop;
		this.mHandler = mHandler;
	}

	// can use UI thread here
	// STEP 1: onPreExecute
	@Override
	protected void onPreExecute() {
		//TODO : Initialize shit

	}

	// automatically done on worker thread (separated from UI thread)
	//protected Void doInBackground(final String... args) {						// <---- original
	// Step 2: doInBackground

	@Override
	protected Void doInBackground(String... args) {
		/* 			 sample test stuff to do

		try {
			for (Integer i = 0; i < 10; i++) {
				Thread.sleep(1000);
				publishProgress(i);
			}
		} catch (InterruptedException e) {
			Log.v("try-catch shit", e.getMessage());
		}
		return null;
		 */
		//~~~~~~ Double check this codeblock to make sure notifies are working properly ~~~~~//
		
		notifyMainThreadDialog("first layer cross starting");
		firstLayerCross();
		notifyMainThreadDialog("first layer cross completed");
		notifyMainThreadDialog("first layer corners starting");
		firstLayerCorners();
		notifyMainThreadDialog("first layer corners completed");
		notifyMainThreadDialog("second layer starting");
		secondLayer();
		notifyMainThreadDialog("second layer completed!");
		topLayerCross();
		Log.e("doInBackGround" ,"topLayerCross finished");
		notifyMainThreadDialog("Top Layer Cross Completed");
		topLayerCorners();
		Log.e("doInBackGround" ,"topLayerCorners finished");
		notifyMainThreadDialog("Top Layer Corners Permutated");
		solveTopFace();
		Log.e("doInBackGround" ,"solveTopFace finished");
		notifyMainThreadDialog("Top Face Solved");
		topLayerEdges();

		Log.e("doInBackGround" ,"topLayerEdges finished");

		return null;
	}

	// periodic updates - it is OK to change UI
	// Intermission: onProgressUpdate is called when publishProgress is
	@Override
	public void onProgressUpdate(Integer... progress){
		//		dialog.setText("background thread: " + progress[0]);
	}

	// can use UI thread here
	// Step 3: onPostExecute, done after doInBackground finishes
	@Override
	protected void onPostExecute(Void nothing) {
		Log.e("onPostExecute" ,"done");
	}

	public void enableForceSkip(){
		forceSkip = true;
		Log.e("forceSkip", "" + forceSkip);
	}



	//To be completed
	public void toastInstructions(boolean CW){

		//		Log.e("displayInstructions", "1");

		//display an arrow to rotate the entire cube
		if(CW)
			//			Toast.makeText(applicationContext, "Rotate whole cube clockwise", Toast.LENGTH_LONG).show();
			//			dialog.setText("Rotate whole cube clockwise");
			notifyMainThreadToast("Rotate whole cube clockwise");
		else 
			//			Toast.makeText(applicationContext, "Rotate whole cube counterclockwise", Toast.LENGTH_LONG).show();
			//			dialog.setText("Rotate whole cube counterclockwise");
			notifyMainThreadToast("Rotate whole cube counterclockwise");
	}


	//To be completed    
	public void toastInstructions(int face, boolean CW){

		//		Log.e("displayInstructions", "2");

		//display an arrow on a specific row/column to rotate a side
		String s = "Rotate ";

		switch(face){
		case 0: s+="left face";break;
		case 1: s+="front face";break;
		case 2: s+="right face";break;
		case 3: s+="back face";break;
		case 4: s+="bottom face";break;
		case 5: s+="top face";break;
		case 6: s+="middle column";break;
		}

		if(face < 6){
			if(CW) s += " clockwise";
			else s += " counterclockwise";
		}else{
			if(CW) s += " up";
			else s += " down";
		}
		//		Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show();
		notifyMainThreadToast(s);
	}

	/**
 * Does some thing in old style.
 *
 * @deprecated no longer using Toasts. Refer to notifyMainThreadImageView
 */
	@deprecated
	private void notifyMainThreadToast(String s) {
		Message msg =  new Message();
		Bundle bundle = new Bundle();

		bundle.putString("Instruction message", s);
		msg.setData(bundle);

		mHandler.sendMessage(msg);

	}

	private void notifyMainThreadImageView(int face, boolean direction){
		Message msg =  new Message();
		Bundle bundle = new Bundle();

		bundle.putInt("face", face);
		bundle.putBoolean("direction", direction);
		msg.setData(bundle);

		mHandler.sendMessage(msg);
	}

	public boolean sameFace(int[][] a, int[][] b){
		int counter = 0;



		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(a[i][j] == b[i][j]) counter++;
			}
		}
		return (counter == 9);
	}




	/**
	 * Implements specified cube face rotation instruction by altering a copy of the current cube (future cube) and displaying
	 * an on -screen instruction until the current cube face is the same as the future cube face.
	 *
	 *
	 * @param face             The face of the cube that is being rotated (0 through 5)
	 * @param direction  The direction in which the face is being rotated. True for clockwise, false for counterclockwise.
	 *                                Direction is relative to the face that is being rotated.
	 */
	public void solveStep(int face, boolean direction){
		//		Log.e("solveStep", "started");

		future.rotateSide(face, direction);
		int[][] cFace = current.getFace(1);
		int[][] fFace = future.getFace(1);
		//		int i = 0;

		//		Log.e("solveStep", "" + !sameFace(cFace, fFace));
		toastInstructions(face, direction);

		//		Log.e("sameFace", "current");
		//		printArray(cFace);
		Log.e("sameFace", "future");
		for(int i = 0; i<6; i++) printArray(future.getFace(i));

		while(!sameFace(cFace, fFace) && !forceSkip){
			cFace = mDrawOnTop.update();
			try {
				Thread.sleep(REFRESH_RATE);

			} catch (InterruptedException e) {
				Log.v("Thread.sleep() got try-catched", e.getMessage());
			}

			//			Log.e("solveStep", "while loop end" + i);
			//			i++;
		}
		forceSkip = false;
		current.clone(future);
		if(face == 6) current.reMap();
		//		Log.e("solveStep", "ended");
	}

	/**
	 * Implements specified cube rotation instruction by altering a copy of the current cube (future cube) and displaying
	 * an on -screen instruction until the current cube face is the same as the future cube face.
	 *
	 *
	 * @param direction  The direction in which the cube is being rotated. True for clockwise, false for counterclockwise.
	 *                                Direction is relative to a top bird's eye view of the cube.
	 */
	public void solveStep(boolean direction){
		//		Log.e("solveStep2", "started");

		future.rotateCube(direction);
		int[][] cFace = current.getFace(1);
		int[][] fFace = future.getFace(1);
		//		int i = 0;
		//		Log.e("solveStep2", "" + !sameFace(cFace, fFace));

		toastInstructions(direction);

		//		Log.e("sameFace", "current");
		//		printArray(cFace);
		Log.e("sameFace", "future");
		//for(int i = 0; i<6; i++) printArray(future.getFace(i));
		printArray(future.getFace(1));

		while(!sameFace(cFace, fFace) && !forceSkip){
			cFace = mDrawOnTop.update();
			Log.e("sameFace", "current");
			printArray(current.getFace(1));
			try {
				Thread.sleep(REFRESH_RATE);

			} catch (InterruptedException e) {
				Log.v("Thread.sleep() got try-catched", e.getMessage());
			}
			//			Log.e("solveStep2", "while loop end" + i);
			//			i++;

		}
		forceSkip = false;
		current.clone(future);
		current.reMap();
		future.reMap();
		Log.e("sameFace", "current remapped");
		printArray(current.getFace(1));
		Log.e("sameFace", "future remapped");
		printArray(current.getFace(1));
		//		Log.e("solveStep2", "ended");

	}



	/**
	 * Helper function for both the firstLayerCorners and secondLayer. Calls a three step algorithm to fix a corner into
	 * the first layer.
	 *
	 *
	 * @param direction  The direction in which the corner piece is moved into place. true for clockwise, false for counterclockwise.
	 * The direction is relative to a bird's eye view of the cube. It is also the direction of the first step in the algorithm.
	 * @see secondLayer
	 */
	public  void firstLayerCornersHelper(boolean direction){
		if(direction){
			solveStep(1, CW);
			solveStep(5, CCW);
			solveStep(1, CCW);
		}
		else{
			solveStep(0, CCW);
			solveStep(5, CW);
			solveStep(0, CW);
		}
	}

	public  void firstLayerCorners(){
		future.clone(current);
		while(current.numFirstLayerCornersCorrect() < 3){
			if(!current.isLeftBottomCornerCorrect() && current.isCornersOnTopLayer()){
				notifyMainThreadDialog("corners on top layer");
				int colorA = current.getSquare(0,1,1);
				int colorB = current.getSquare(1,1,1);
				if(current.getSquare(2,0,0) == 4 && current.areComplementaryCornerColors(2,0,0,colorA,colorB)) firstLayerCornersHelper(CCW);
				else if(current.getSquare(3,0,0) == 4 && current.areComplementaryCornerColors(3,0,0,colorA,colorB)){
					solveStep(5, CW);
					firstLayerCornersHelper(CCW);
				}
				else if(current.getSquare(1,0,0) == 4 && current.areComplementaryCornerColors(1,0,0,colorA,colorB)){
					solveStep(5, CCW);
					firstLayerCornersHelper(CCW);
				}
				else if(current.getSquare(0,0,0) == 4 && current.areComplementaryCornerColors(0,0,0,colorA,colorB)){
					solveStep(5, CW);
					solveStep(5, CW);
					firstLayerCornersHelper(CCW);
				}

				else if(current.getSquare(0,0,2) == 4 && current.areComplementaryCornerColors(0,0,2,colorA,colorB)){
					solveStep(5, CW);
					firstLayerCornersHelper(CW);
				}
				else if(current.getSquare(1,0,2) == 4 && current.areComplementaryCornerColors(1,0,2,colorA,colorB)){
					solveStep(5, CW);
					solveStep(5, CW);
					firstLayerCornersHelper(CW);
				}
				else if(current.getSquare(2,0,2) == 4 && current.areComplementaryCornerColors(2,0,2,colorA,colorB)){
					solveStep(5, CCW);
					firstLayerCornersHelper(CW);
				}
				else if(current.getSquare(3,0,2) == 4 && current.areComplementaryCornerColors(3,0,2,colorA,colorB)){
					firstLayerCornersHelper(CW);
				}

				else if (current.getSquare(5,2,0) == 4 && current.areComplementaryCornerColors(5,2,0,colorA,colorB)){
					solveStep(0, CCW);
					solveStep(5, CW);
					solveStep(0, CW);
					solveStep(5, CCW);
					firstLayerCornersHelper(CW);
				}      
				else if (current.getSquare(5,0,0) == 4 && current.areComplementaryCornerColors(5,0,0,colorA,colorB)){
					solveStep(5, CCW);
					solveStep(0, CCW);
					solveStep(5, CW);
					solveStep(0, CW);
					solveStep(5, CCW);
					firstLayerCornersHelper(CW);
				}
				else if (current.getSquare(5,2,2) == 4 && current.areComplementaryCornerColors(5,2,2,colorA,colorB)){
					solveStep(5, CW);
					solveStep(0, CCW);
					solveStep(5, CW);
					solveStep(0, CW);
					solveStep(5, CCW);
					firstLayerCornersHelper(CW);
				}
				else if (current.getSquare(5,0,2) == 4 && current.areComplementaryCornerColors(5,0,2,colorA,colorB)){
					solveStep(5, CW);
					solveStep(5, CW);
					solveStep(0, CCW);
					solveStep(5, CW);
					solveStep(0, CW);
					solveStep(5, CCW);
					firstLayerCornersHelper(CW);
				} 
				else notifyMainThreadDialog("none of the if statements called, error");
			}
			solveStep(CW);

			if(!current.isCornersOnTopLayer() && !current.isLeftBottomCornerCorrect() && current.numFirstLayerCornersCorrect() < 3){
				notifyMainThreadDialog("corners not on top layer");
				int colorA = current.getSquare(0,1,1);
				int colorB = current.getSquare(1,1,1);
				//if the corner piece is in first layer but not in the right position
				if (current.getSquare(0,2,2) == 4 && current.areComplementaryCornerColors(0,2,2,colorA,colorB)){
					solveStep(1, CW);
					solveStep(5, CCW);
					solveStep(1, CCW);
					solveStep(5, CW);
					firstLayerCornersHelper(CW);
				}
				else if (current.getSquare(1,2,0) == 4 && current.areComplementaryCornerColors(1,2,0,colorA,colorB)){
					solveStep(0, CCW);
					solveStep(5, CW);
					solveStep(0, CW);
					solveStep(5, CCW);
					firstLayerCornersHelper(CCW);
				}

				//if some other corner piece is in the first layer corner spot
				else if (current.getSquare(0,2,2) == 4){
					solveStep(1, CW);
					solveStep(5, CCW);
					solveStep(1, CCW);
				}
				else if (current.getSquare(1,2,0) == 4){
					solveStep(0, CCW);
					solveStep(5, CW);
					solveStep(0, CW);
				}   
			}
		}
		notifyMainThreadDialog("positioning sacrifice corner");
		if(current.numFirstLayerCornersCorrect() == 3)
		while(current.isLeftBottomCornerCorrect()){
			solveStep(CW);
		}
	}
	/**
	 * Step 1: Creates a "cross" shape in the first/bottom layer of the cube. An edge piece is defined to be "easily available"
	 * if it is on the top layer or in the front face, and if such edge pieces exist, they are placed into the first layer. If
	 * none are available, then if edge pieces are in the front face but not in an "easily available" state, they are moved to
	 * the top layer into an "easily available" state. This way, when the while loop is run again, the easily available edge
	 * piece should be recognized and placed into the cross. 
	 */
	public void firstLayerCross(){
		future.clone(current);
		while(!current.isFirstLayerCrossSolved()){
			if(!current.isFirstLayerEdgeCorrect() && current.isFirstLayerEdgesEasilyAvailable()){
				notifyMainThreadDialog("first layer edges easily available");
				if(current.getSquare(5,2,1) == 4 && current.getComplementaryEdgeColor(5,2,1) == current.getSquare(1,1,1)){
					solveStep(1, CW);
					solveStep(1, CW);
				}
				else if(current.getSquare(5,1,0) == 4 && current.getComplementaryEdgeColor(5,1,0) == current.getSquare(1,1,1)){
					solveStep(5, CCW);
					solveStep(1, CW);
					solveStep(1, CW);
				}
				else if(current.getSquare(5,1,2) == 4 && current.getComplementaryEdgeColor(5,1,2) == current.getSquare(1,1,1)){
					solveStep(5, CW);
					solveStep(1, CW);
					solveStep(1, CW);
				}
				else if(current.getSquare(5,0,1) == 4 && current.getComplementaryEdgeColor(5,0,1) == current.getSquare(1,1,1)		){
					solveStep(5, CW);
					solveStep(5, CW);
					solveStep(1, CW);
					solveStep(1, CW);
				}
				//if the white piece can be moved in because it's on face 1, move it in now.
				else if(current.getSquare(1,1,0) == 1 && current.getComplementaryEdgeColor(1,1,1) == 4){
					solveStep(1, CCW);
				}
				else if(current.getSquare(1,1,2) == 1 && current.getComplementaryEdgeColor(1,1,2) == 4){
					solveStep(1, CW);
				}
				//else notifyMainThreadDialog("edges easily available but nothing moved in");
				//else solveStep(CW);
			} //end isEasilyAvailable
			

			if(!current.isFirstLayerEdgesEasilyAvailable() && !current.isFirstLayerCrossSolved()){
				notifyMainThreadDialog("no first layer cross pieces easily available");
				//checks if there is a white on the outer edge piece, and moves it up to the top
				if(current.getSquare(0,1,2) == 4){
					solveStep(1, CW);
					if(current.getSquare(0,1,2) == 4 || current.getSquare(2,1,0) ==4){ //if a solved cross piece was disconnected, then reconnect it
						solveStep(5, CCW);
						solveStep(1, CCW);
					}
				}
				if(current.getSquare(2,1,0) == 4){
					solveStep(1, CCW);
					if(current.getSquare(2,1,0) ==4 || current.getSquare(0,1,2) ==4){
						solveStep(5, CCW);
						solveStep(1, CW);
					}
				}
				//checks if there is a white on the inner edge piece, moves it to the top
				if(current.getSquare(1,1,0) == 4){
					solveStep(0, CCW);
					if(current.getSquare(1,1,0) == 4 || current.getSquare(3,1,2) == 4){
						solveStep(5, CCW);
						solveStep(0, CW);
					}
				}
				if(current.getSquare(1,1,2) == 4){
					solveStep(2, CW);
					if(current.getSquare(1,1,2) == 4 || current.getSquare(3,1,0) == 4){
						solveStep(5, CW);
						solveStep(2, CCW);
					}
				}
				//checks if there is a white on the bottom edge piece in wrong position, move to top
				if(current.getSquare(1,2,1) == 4){
					solveStep(1, CW);
					solveStep(0, CCW);
					solveStep(5, CCW);
					solveStep(0, CW);
				}
				//checks if there is a white on the top edge piece, move to top
				if(current.getSquare(1,0,1) ==4){
					solveStep(1, CCW);
					solveStep(0, CCW);
					solveStep(5, CCW);
					solveStep(0, CW);
					if(current.getSquare(2,1,0) ==4){
						solveStep(5, CW);
						solveStep(1, CW);
					}
				}
				if(current.getSquare(4,0,1) ==4 && current.getSquare(1,2,1) != current.getSquare(1,1,1)){
					solveStep(1, CW);
					solveStep(1, CW);
				}
			}
			solveStep(CW);
		}
		notifyMainThreadDialog("cross solved, exit while loop");
		//rotate first and second layers so that they are aligned
		notifyMainThreadDialog("aligning first and second layer");
		if(current.getSquare(0,2,1) == current.getSquare(2, 1, 1)){
			solveStep(4, CW);
			solveStep(4, CW);
		}
		else if(current.getSquare(0,2,1) == current.getSquare(1,1,1)){
			solveStep(4, CW);
		}
		else if(current.getSquare(1, 2, 1) == current.getSquare(0,1,1)){
			solveStep(4, CCW);
		}
	}


	/**
	 * Helper function for secondLayer, implements a 3 step algorithm to place the proper edge piece into the second layer. Requires
	 * a sacrificed/unsolved first layer corner piece.
	 *
	 *
	 * @param direction  The direction in which the edge piece is moved into place. true for clockwise, false for counterclockwise.
	 * The direction is relative to the face the edge is currently on, and it is also the direction of rotation for the first
	 * step in the algorithm.
	 * @see secondLayer
	 */
	public void secondLayerHelperShortAlg(boolean direction){
		int face = direction? 1 : 0;
		solveStep(face, direction);
		solveStep(5, !direction);
		solveStep(face, !direction);
	}

	/**
	 * Helper function for secondLayer, implements a 7 step algorithm to place the proper edge piece into the second layer when there
	 * is no sacrificed/unsolved first layer corner. Algorithm is implemented as the last step in solving the second layer.
	 *
	 * @param direction  The OPPOSITE direction in which the edge piece needs to move into place. true for clockwise, false for counterclockwise.
	 * It is also the direction of rotation for the first step in the algorithm.
	 */
	public void secondLayerHelperLongAlg(boolean direction){ //Be careful about direction
		int face = direction? 1 : 0;
		int oppositeface = !direction? 1: 0;
		solveStep(face, direction);
		solveStep(5, !direction);
		solveStep(face, !direction);
		solveStep(5, !direction);
		solveStep(oppositeface, !direction);
		solveStep(5, direction);
		solveStep(oppositeface, direction);
	}

	/**
	 * Step 3: Solves the second layer.
	 * First edges are placed in with the short algorithm. The short algorithm can only be used to fix 3 of the 4 edge pieces in,
	 * and only when there are both a sacrifice/unsolved first layer corner piece and available edge pieces on the top layer. When
	 * there are no available top edge pieces, the while loop for placing edges in via the short algorithm is exited, and a series
	 * of if statements below place edge pieces back into the top layer for re-entrance into the while loop.
	 * Once 3 of the 4 edge pieces are fixed into place, the first layer is completed by solving for the sacrificed corner, and then
	 * one of two long algorithms are used to fix the final edge piece to complete the second layer.
	 *
	 *
	 * @see secondLayerHelperLongAlg
	 * @see secondLayerHelperShortAlg
	 * @see firstLayerCornersHelper
	 */
	public void secondLayer(){
		Log.e("secondLayer", "first line");
		future.clone(current);
		Log.e("secondLayer", "future cloned");
		notifyMainThreadDialog("starting second layer");
		Log.e("secondLayer", "starting second layer");
		//Setup, need to position sacrifice corner in the cube(1,2,0) area. Not necessary if firstLayer function does it.
		notifyMainThreadDialog("second layer not completed");
		while(current.numSecondLayerCorrect() < 3){

			if(!current.isLeftEdgeCorrect() && current.isEdgesOnTopLayer()){ //Place the correct edge in if possible
				notifyMainThreadDialog("edges on top layer, putting them in");
				Log.e("secondLayer", "edges on top layer, putting them in");
				if(current .getSquare(5,0,1) == 0 && current.getComplementaryEdgeColor(5, 0, 1) ==1){
					solveStep(5, CCW);
					secondLayerHelperShortAlg(CW);
				}                                        
				else if (current.getSquare(5,0,1) == 1 && current.getComplementaryEdgeColor(5, 0, 1) == 0){
					solveStep(5, CW);
					solveStep(5, CW);
					secondLayerHelperShortAlg(CCW);
				}
				else if (current.getSquare(5,1,0) ==0 && current.getComplementaryEdgeColor(5, 1, 0) == 1){
					secondLayerHelperShortAlg(CW);
				}
				else if (current.getSquare(5,1,0) ==1 && current.getComplementaryEdgeColor(5, 1, 0) == 0){
					solveStep(5, CCW);
					secondLayerHelperShortAlg(CCW);
				}
				else if (current.getSquare(5,1,2) ==0 && current.getComplementaryEdgeColor(5, 1, 2) ==1){
					solveStep(5, CW);
					solveStep(5, CW);
					secondLayerHelperShortAlg(CW);
				}
				else if (current.getSquare(5,1,2) ==1 && current.getComplementaryEdgeColor(5, 1, 2) ==0){
					solveStep(5, CW);
					secondLayerHelperShortAlg(CCW);
				}
				else if (current.getSquare(5,2,1) ==0 && current.getComplementaryEdgeColor(5, 2, 1) ==1){
					solveStep(5, CW);
					secondLayerHelperShortAlg(CW);
				}
				else if (current.getSquare(5,2,1) ==1 && current.getComplementaryEdgeColor(5, 2, 1) ==0 ){
					secondLayerHelperShortAlg(CCW);
				}
			}
			notifyMainThreadDialog("finished first if loop");
			solveStep(CW);
			if(!current.isFaceSolved(4)) solveStep(4, CW);
			//end while EdgesOnTop loop

			if(!current.isLeftEdgeCorrect() && !current.isEdgesOnTopLayer()){ //This is called when there are no edges on the top face. This pulls the edges out of the incorrect spot, enabling re-entrance of the above while loop.
				notifyMainThreadDialog("no edges on top face as of now");
				//					int colorA = current.getSquare(0,1,1);
				//					int colorB = current.getSquare(1,1,1);
				//					if((current .getSquare(1,1,2) == colorA || current.getSquare(1,1,2) == colorB) && (current.getSquare(2,1,0) == colorA || current.getSquare(2,1,0) == colorB)){
				//						solveStep(4, CW);
				//						solveStep(2, CW);
				//						solveStep(5, CW);
				//						solveStep(2, CCW);
				//						solveStep(4, CCW);
				//					}
				//					else if ((current.getSquare(3,1,2) == colorA || current.getSquare(3,1,2) == colorB) && (current.getSquare(0,1,0) == colorA || current.getSquare(0,1,0) == colorB)){
				//						solveStep(4, CCW);
				//						solveStep(CCW);
				//						secondLayerHelperShortAlg(CW);
				//						solveStep(CW);
				//						solveStep(4, CW);
				//					}
				//					else if ((current.getSquare(2,1,2) == colorA || current.getSquare(2,1,2) == colorB) && (current.getSquare(3,1,0) == colorA || current.getSquare(3,1,0) == colorB)){
				//						solveStep(4, CW);
				//						solveStep(4, CW);
				//						solveStep(CW);
				//						solveStep(2, CW);
				//						solveStep(5, CW);
				//						solveStep(2, CCW);
				//						solveStep(4, CCW);
				//						solveStep(4, CCW);
				//						solveStep(CCW);
				//					}//can be an else?
				//					else if ((current.getSquare(0,1,2) == colorA || current.getSquare(0,1,2) == colorB) && (current.getSquare(1,1,0) == colorA || current.getSquare(1,1,0) == colorB)){
				//						secondLayerHelperShortAlg(CW);
				//					}

				if(current.getSquare(0,1,2) !=5 && current.getSquare(1,1,0) != 5){
					secondLayerHelperShortAlg(CW);
				}

			}
			else if(!current.isEdgesOnTopLayer()){
				notifyMainThreadDialog("bottom else if being called");
				solveStep(CW);
				solveStep(4, CW);
			}
		} //End while != 3 loop
		notifyMainThreadDialog("three edges in");

		if(!current.isFaceSolved(4)){
			//Position the sacrifice corner with the unsolved edge
			while(current.isLeftEdgeCorrect()){
				solveStep(CW);
				solveStep(4, CW);
			}
			//Correct the sacrifice corner- place it in to complete the first layer
			if(current .getSquare(2,0,0) == 4) firstLayerCornersHelper(CCW);
			else if (current.getSquare(1,0,0) == 4){
				solveStep(5, CCW);
				firstLayerCornersHelper(CCW);
			}
			else if (current.getSquare(3,0,0) == 4){
				solveStep(5, CW);
				firstLayerCornersHelper(CCW);
			}                    
			else if (current.getSquare(0,0,0) == 4){
				solveStep(5, CW);
				solveStep(5, CW);
				firstLayerCornersHelper(CCW);
			}

			else if (current.getSquare(3,0,2) == 4){
				firstLayerCornersHelper(CW);
			}                    
			else if (current.getSquare(0,0,2) == 4){
				solveStep(5, CW);
				firstLayerCornersHelper(CW);
			}      
			else if (current.getSquare(2,0,2) == 4){
				solveStep(5, CCW);
				firstLayerCornersHelper(CW);
			}                          
			else if (current.getSquare(1,0,2) == 4){
				solveStep(5, CW);
				solveStep(5, CW);
				firstLayerCornersHelper(CW);
			}

			else if (current.getSquare(5,2,0) == 4){
				solveStep(0, CCW);
				solveStep(5, CW);
				solveStep(0, CW);
				solveStep(5, CCW);
				firstLayerCornersHelper(CW);
			}      
			else if (current.getSquare(5,0,0) == 4){
				solveStep(5, CCW);
				solveStep(0, CCW);
				solveStep(5, CW);
				solveStep(0, CW);
				solveStep(5, CCW);
				firstLayerCornersHelper(CW);
			}
			else if (current.getSquare(5,2,2) == 4){
				solveStep(5, CW);
				solveStep(0, CCW);
				solveStep(5, CW);
				solveStep(0, CW);
				solveStep(5, CCW);
				firstLayerCornersHelper(CW);
			}
			else if (current.getSquare(5,0,2) == 4){
				solveStep(5, CW);
				solveStep(5, CW);
				solveStep(0, CCW);
				solveStep(5, CW);
				solveStep(0, CW);
				solveStep(5, CCW);
				firstLayerCornersHelper(CW);
			}

			else if (current.getSquare(0,2,2) == 4){
				solveStep(1, CW);
				solveStep(5, CCW);
				solveStep(1, CCW);
				solveStep(5, CW);
				firstLayerCornersHelper(CW);
			}
			else if (current.getSquare(1,2,0) == 4){
				solveStep(0, CCW);
				solveStep(5, CW);
				solveStep(0, CW);
				solveStep(5, CCW);
				firstLayerCornersHelper(CCW);
			}
			else notifyMainThreadDialog("error in correcting sacrifice corner");
		}
		//Place in the final edge
		if(current.numSecondLayerCorrect() <= 3){
			notifyMainThreadDialog("placing in final edge");
			int colorA = current.getSquare(0,1,1);
			int colorB = current.getSquare(1,1,1);
			if(current .getSquare(5,1,2) == colorA && current.getComplementaryEdgeColor(5,1,2) == colorB) secondLayerHelperLongAlg (CCW );
			else if (current.getSquare(5,2,1) == colorA && current.getComplementaryEdgeColor(5,2,1) == colorB){
				solveStep(5, CCW);
				secondLayerHelperLongAlg(CCW);
			}
			else if (current.getSquare(5,1,0) == colorA && current.getComplementaryEdgeColor(5,1,0) == colorB){
				solveStep(5, CW);
				solveStep(5, CW);
				secondLayerHelperLongAlg(CCW);
			}
			else if (current.getSquare(5,0,1) == colorA && current.getComplementaryEdgeColor(5,0,1) == colorB){
				solveStep(5, CW);
				secondLayerHelperLongAlg(CCW);
			}

			else if (current.getSquare(5,1,2) == colorB && current.getComplementaryEdgeColor(5,1,2) == colorA){
				solveStep(5, CCW);
				secondLayerHelperLongAlg(CW);
			}
			else if (current.getSquare(5,2,1) == colorB && current.getComplementaryEdgeColor(5,2,1) == colorA){
				solveStep(5, CW);
				solveStep(5, CW);
				secondLayerHelperLongAlg(CW);
			}
			else if (current.getSquare(5,1,0) == colorB && current.getComplementaryEdgeColor(5,1,0) == colorA){
				solveStep(5, CW);
				secondLayerHelperLongAlg(CW);
			}
			else if (current.getSquare(5,0,1) == colorB && current.getComplementaryEdgeColor(5,0,1) == colorA){
				secondLayerHelperLongAlg(CW);
			}
			else{
				secondLayerHelperLongAlg(CW);
				solveStep(5, CCW);
				secondLayerHelperLongAlg(CW);
			}
		}

		//rotate first and second layers so that they are aligned
		if(current.getSquare(0,2,1) == current.getSquare(2, 1, 1)){
			solveStep(4, CW);
			solveStep(4, CW);
		}
		else if(current.getSquare(0,2,1) == current.getSquare(1,1,1)){
			solveStep(4, CW);
		}
		else if(current.getSquare(1, 2, 1) == current.getSquare(0,1,1)){
			solveStep(4, CCW);
		}

	}

	/**
	 * Step 4: Creates a "cross" on the top layer of the cube. First the top layer is rotated into a position where an algorithm
	 * can be executed. Then one of two algorithms are implemented depending on the presence of top face colors in certain
	 * areas of the top face.
	 *
	 *
	 */
	public  void topLayerCross(){
		future.clone(current);
		while(!current .isTopCrossSolved()){
			notifyMainThreadDialog("Top Layer Cross not complete");
			Log.e("topLayerCross", "top cross not solved, enter while loop");
			//Position top face for algorithm use
			if(current .getSquare(5,1,0) == 5 && current.getSquare(5,2,1) != 5){
				Log.e("topLayerCross", "step 1");
				solveStep(5, CW);
			}
			else if (current.getSquare(5,1,2) == current.getSquare(5,2,1)){
				Log.e("topLayerCross", "step 2");
				solveStep(5, CCW);
			}
			else if (current.getSquare(5,1,0) == current.getSquare(5,2,1)){
				Log.e("topLayerCross", "step 3");
				solveStep(CW);
				solveStep(CW);
			}
			//Implement Algorithm
			notifyMainThreadDialog("Starting Algorithm for top layer cross");
			if(current .getSquare(5,0,1) == current.getSquare(5,2,1)){
				Log.e("topLayerCross", "step 4");
				solveStep(0, CW);
				solveStep(1, CW);
				solveStep(5, CW);
				solveStep(1, CCW);
				solveStep(5, CCW);
				solveStep(0, CCW);
			}
			else{
				Log.e("topLayerCross", "step 5");
				solveStep(0, CW);
				solveStep(5, CW);
				solveStep(1, CW);
				solveStep(5, CCW);
				solveStep(1, CCW);
				solveStep(0, CCW);
			}
			Log.e("topLayerCross", "end of while loop");
		}
		Log.e("topLayerCross", "exit while loop, top layer done");
	}

	/**
	 * Step 5: Properly positions the top face corner pieces so that their colors matches the order of colors on the actual cube.
	 * First, the two corners on the left side of the top face are compared. If one of the colors between the two corners are
	 * shared other than the color of the top face, then check if those two corners are positioned correctly by comparing the
	 * order of the cube colors on the bottom left corner with the face colors of the theoretically completed cube.
	 * If there are no shared colors between the two corners, rotate the top face so that two shared colors are on the left
	 * edge of the top face. Finally, implement the algorithm.
	 *
	 *
	 */
	public  void topLayerCorners(){
		future.clone(current);
		while(!current .isTopCornersPermutated2()){
			Log.e("topLayerCorners" ,"top corners not permutated, inside while loop");
			notifyMainThreadDialog("Top Layer Corners Not Permutated");
			int sameColorVal = 88; //Shared color value
			int diffColorVal = 88;
			int counter = 0;
			//Figures out which color is the same between the two corners, if there is one
			if(current .getSquare(1,0,0) == current.getSquare(0,0,0) && current.getSquare(1,0,0) != 5){
				counter++;
				sameColorVal = current.getSquare(1,0,0);
			}
			if(current .getSquare(1,0,0) == current.getSquare(5,0,0) && current.getSquare(1,0,0) != 5){
				counter++;
				sameColorVal = current.getSquare(1,0,0);
			}
			if(current .getSquare(1,0,0) == current.getSquare(3,0,2) && current.getSquare(1,0,0) != 5){
				counter++;
				sameColorVal = current.getSquare(1,0,0);
			}
			if(current .getSquare(0,0,2) == current.getSquare(0,0,0) && current.getSquare(0,0,2) != 5){
				counter++;
				sameColorVal = current.getSquare(0,0,2);
			}
			if(current .getSquare(0,0,2) == current.getSquare(5,0,0) && current.getSquare(0,0,2) != 5){
				counter++;
				sameColorVal = current.getSquare(0,0,2);
			}
			if(current .getSquare(0,0,2) == current.getSquare(3,0,2) && current.getSquare(0,0,2) != 5){
				counter++;
				sameColorVal = current.getSquare(0,0,2);
			}
			if(current .getSquare(5,2,0) == current.getSquare(0,0,0) && current.getSquare(5,2,0) != 5){
				counter++;
				sameColorVal = current.getSquare(5,2,0);
			}
			if(current .getSquare(5,2,0) == current.getSquare(5,0,0) && current.getSquare(5,2,0) != 5){
				counter++;
				sameColorVal = current.getSquare(5,2,0);
			}
			if(current .getSquare(5,2,0) == current.getSquare(3,0,2) && current.getSquare(5,2,0) != 5){
				counter++;
				sameColorVal = current.getSquare(5,2,0);
			}

			if(counter == 1){ //counter = 1 means there is one same color
				//Figure out the second color on the corner
				Log.e("topLayerCorners" ,"counter == 1");
				if(current .getSquare(1,0,0) != 5 && current.getSquare(1,0,0) != sameColorVal) diffColorVal = current.getSquare(1,0,0);
				else if (current.getSquare(0,0,2) != 5 && current.getSquare(0,0,2) != sameColorVal) diffColorVal = current.getSquare(0,0,2);
				else if (current.getSquare(5,2,0) != 5 && current.getSquare(5,2,0) != sameColorVal) diffColorVal = current.getSquare(5,2,0);
				//Check if that corner is in the correct position or not, and correct its position
				if(sameColorVal+1 != diffColorVal || sameColorVal-3 != diffColorVal){ //Checks if diffColorVal is to the right of sameColorVal
					solveStep(CW);
					solveStep(CW);
				}
			}
			else{ //When there are no same colors on the left two corners
				//Find the similar color between the two corners in the front
				Log.e("topLayerCorners" ,"counter != 1");
				if(current .getSquare(1,0,2) == current.getSquare(5,2,0) && current.getSquare(1,0,2) !=5) sameColorVal = current.getSquare(1,0,2);
				else if (current.getSquare(1,0,2) == current.getSquare(1,0,0) && current.getSquare(1,0,2) !=5) sameColorVal = current.getSquare(1,0,2);
				else if (current.getSquare(1,0,2) == current.getSquare(0,0,2) && current.getSquare(1,0,2) !=5) sameColorVal = current.getSquare(1,0,2);
				else if (current.getSquare(5,2,2) == current.getSquare(5,2,0) && current.getSquare(5,2,2) !=5) sameColorVal = current.getSquare(5,2,2);
				else if (current.getSquare(5,2,2) == current.getSquare(1,0,0) && current.getSquare(5,2,2) !=5) sameColorVal = current.getSquare(5,2,2);
				else if (current.getSquare(5,2,2) == current.getSquare(0,0,2) && current.getSquare(5,2,2) !=5) sameColorVal = current.getSquare(5,2,2);
				else if (current.getSquare(2,0,0) == current.getSquare(5,2,0) && current.getSquare(2,0,0) !=5) sameColorVal = current.getSquare(2,0,0);
				else if (current.getSquare(2,0,0) == current.getSquare(1,0,0) && current.getSquare(2,0,0) !=5) sameColorVal = current.getSquare(2,0,0);
				else if (current.getSquare(2,0,0) == current.getSquare(0,0,2) && current.getSquare(2,0,0) !=5) sameColorVal = current.getSquare(2,0,0);
				//Figure out the second color on the corner
				if(current .getSquare(1,0,2) != 5 && current.getSquare(1,0,2) !=sameColorVal) diffColorVal=current.getSquare(1,0,2);
				else if (current.getSquare(5,2,2) != 5 && current.getSquare(5,2,2) !=sameColorVal) diffColorVal=current.getSquare(5,2,2);
				else if (current.getSquare(2,0,0) != 5 && current.getSquare(2,0,0) !=sameColorVal) diffColorVal=current.getSquare(2,0,0);

				//Figure out if the corner is positioned correctly, and correct its position
				if(sameColorVal+1 == diffColorVal || sameColorVal-3 == diffColorVal) solveStep(5, CW);
				else solveStep(5, CCW);
			}
			//Implement Algorithm
			Log.e("topLayerCorners" ,"starting Algorithm");
			notifyMainThreadDialog("Starting Algorithm for top layer corner permutation");
			solveStep(0, CW);
			solveStep(5, CCW);
			solveStep(2, CCW);
			solveStep(5, CW);
			solveStep(0, CCW);
			solveStep(5, CCW);
			solveStep(2, CW);                        
		}

	}

	/**
	 * Helper function for solveTopFace(), implements an algorithm to correct the orientation of corner faces on the top layer.
	 *
	 *
	 * @param direction        Indicates which algorithm is used. The pair of algorithms only differ by rotation direction
	 *                                       thus the boolean direction can be used to easily switch between the two algorithms.
	 * @see solveTopFace
	 */
	public void solveTopFaceHelper(boolean direction){
		notifyMainThreadDialog("Implementing Top Face Algorithm");
		solveStep(2, direction);
		solveStep(5, direction);
		solveStep(2, !direction);
		solveStep(5, direction);
		solveStep(2, direction);
		solveStep(5, direction);
		solveStep(5, direction);
		solveStep(2, !direction);
	}

	/**
	 * Step 6 ( Penultimate Step): Positions corner pieces of the top layer so that the top face is solved.
	 *
	 * Different algorithms are used for cases where there are a specific number of correct faces on the top layer.
	 * numSquares is used to detect the number of correct faces, and consequent steps rotate the top layer into
	 * the proper orientation so that an algorithm can be used.
	 *
	 *
	 * @see solveTopFaceHelper
	 */
	public void solveTopFace(){
		future.clone(current);
		while (!current .isFaceSolved(5)){
			notifyMainThreadDialog("Top Face Not Solved");
			int numSquares = current.countSquaresOnFace(5); //Detect top face pattern
			if(numSquares == 6){ //"Fish" case pattern
				//Position face properly
				Log.e("solveTopFace", "6 squares on top");
				if(current .countSquaresOnFace(5, 1) == 0) solveStep (5, CW);
				else if (current.countSquaresOnFace(5, 3) == 0) solveStep(5, CCW);
				else if (current.countSquaresOnFace(5, 2) == 0){
					solveStep(CW);
					solveStep(CW);
				}

				//Perform Algorithm
				if(current .getSquare(1, 0, 0) == 5) solveTopFaceHelper(CCW);
				else solveTopFaceHelper( CW);
			}
			else if (numSquares == 5){
				Log.e("solveTopFace", "5 squares on top");
				//Position face properly
				if(current .countSquaresOnFace(5, 1) == 2) solveStep (5, CW);
				else if (current.countSquaresOnFace(5, 3) == 2) solveStep(5, CCW);
				else if (current.countSquaresOnFace(5, 2) == 2){
					solveStep(CW);
					solveStep(CW);
				}
				//Perform Algorithm
				if(current .getSquare(1, 0, 2) == 5) solveTopFaceHelper(CCW);
				else solveTopFaceHelper( CW);
			}
			else if (numSquares == 7){
				Log.e("solveTopFace", "7 squares on top");
				//Position face properly
				if(current .getSquare(0, 0, 0) == 5) solveStep (5, CCW );
				else if (current.getSquare(2, 0, 0) == 5) solveStep(5, CW);
				else if (current.getSquare(3, 0, 0) == 5){
					solveStep(CW);
					solveStep(CW);
				}
				//Perform Algorithm
				if(current .getSquare(3, 0, 2) == 5) solveTopFaceHelper(CCW);
				else solveTopFaceHelper( CW);
			}
		}
	}

	/**
	 * Step 7 (Final Step): Places the edge pieces on the top layer into the correct positions. Implements the algorithm
	 * until the cube is solved. Expected number of loops of the while loop executed is between 1 to 2.
	 *
	 */
	public void topLayerEdges(){
		future.clone(current) ;


		while(!current.isSolved()){
			Log.e("current isn't solved", "while loop - started");
			notifyMainThreadDialog("Top Layer Edges Not Solved");

			//Rotate the top face so it is positioned properly
			if(current .getSquare(1, 0, 0) == 0){
				Log.e("topLayerEdges", "step 1");
				solveStep (5, CW );
			}
			else if (current.getSquare(0, 0, 0) == 1){
				Log.e("topLayerEdges", "step 2");
				solveStep(5, CCW);
			}
			else if (current.getSquare(2, 0, 0) == 0){
				Log.e("topLayerEdges", "step 3");
				solveStep(5, CW);
				solveStep(5, CW);
			}

			//Rotate the cube so that the solved face is on the left
			else if(current .getSquare(1, 0, 1) == 1){
				Log.e("topLayerEdges", "step 4");
				solveStep (CW );
			}
			else if (current.getSquare(3, 0, 1) == 3){
				Log.e("topLayerEdges", "step 5");
				solveStep(CCW);
			}
			else if (current.getSquare(2, 0, 1) == 2){
				Log.e("topLayerEdges", "step 6");
				solveStep(CW);
				solveStep(CW);
			}


			//Perform Algorithm
			else{
				Log.e("topLayerEdges", "starts real algorithm");
				notifyMainThreadDialog("Implementing Top Layer Edges Algorithm");
				boolean adjColMatch = false;
				solveStep(2, CCW);
				solveStep(2, CCW);
				if(current .getSquare(1, 0, 1) == current.getSquare(1, 0, 2)) adjColMatch = true ;
				if(adjColMatch) solveStep(5, CW);
				else solveStep(5, CCW);
				solveStep(CCW);
				solveStep(6, CCW);
				solveStep(5, CW);
				solveStep(5, CW);
				solveStep(6, CW);
				if(adjColMatch) solveStep(5, CW);
				else solveStep(5, CCW);
				solveStep(CW);
				solveStep(2, CW);
				solveStep(2, CW);
			}
			Log.e("finished one algorithm", "while loop - finished");
		}
		notifyMainThreadDialog("Cube completed!!!");
	}

	public static void printArray(int[][] a){
		for(int i = 0; i<3; i++){
			Log.e("array line", a[i][0] + ", " + a[i][1] + ", " + a[i][2]);
		}
	}
}