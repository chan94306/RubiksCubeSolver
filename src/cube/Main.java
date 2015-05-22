package cube;

public class Main {

	final  boolean CW = true;
	final  boolean CCW = false;
	 Cube current = null;
	 Cube future = null;

	public  void main(String[] args) {
		Cube c = new Cube();
		//            printArray(current.getFace(4));
		printCube(c);
		System. out.print(current .isTopCornersPermutated ());
		System. out.print(current .isTopCornersPermutated2());
		current.rotateSide(1, true);
		printCube(c);
		System. out.print(current .isTopCornersPermutated ());
		System. out.print(current .isTopCornersPermutated2());

		solveTopFace();
		topLayerCorners();

	}

	/**
	 * Debugging method: prints out a 3x3 array of numbers
	 * @param a 3x3 integer array to be printed
	 */
	public  void printArray(int[][] a){
		for(int i = 0; i<3; i++){
			System. out.println(a[i][0] + "" + a[i][1] + "" + a[i][2]);
		}
		System. out.println("\n" );
	}

	
	public  void printCube(Cube c){
		for(int i = 0; i<3; i++){
			System. out.println("    " + current.getFace(5)[i][0]+"" +current.getFace(5)[i][1]+""+ current.getFace(5)[i][2]);
		}
		for(int i = 0; i<3; i++){
			System. out.println(current .getFace(0)[i][0]+""+ current.getFace(0)[i][1]+"" +current.getFace(0)[i][2]+" "+current.getFace(1)[i][0]+ ""+current .getFace(1)[i][1]+""+ current.getFace(1)[i][2]+" "+current.getFace(2)[i][0]+ ""+current .getFace(2)[i][1]+""+ current.getFace(2)[i][2]+" "+current.getFace(3)[i][0]+ ""+current .getFace(3)[i][1]+""+ current.getFace(3)[i][2]);
		}
		for(int i = 0; i<3; i++){
			System. out.println("    " + current.getFace(4)[i][0]+"" +current.getFace(4)[i][1]+""+ current.getFace(4)[i][2]);
		}
		System. out.println("\n" );
	}

	//TODO
	public  void displayInstructions(boolean CW){
		//display an arrow to rotate the entire cube

	}
	//TODO
	public  void displayInstructions(int face, boolean CW){
		//display an arrow on a specific row/column to rotate a side
	}

	public  boolean sameFace(int[][] a, int[][] b){
		int counter = 0;
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(a[i][j] == b[i][j]) counter++;
			}
		}
		return (counter == 9);
	}
	
	//TODO
	public  int[][] update(){
		//return a 2D array of the face 1 (the face pointed at camera) based on what's currently on the cube
		//the 2D array will consist of all numbers 0-6, not the RGB value
		//use getColorValue and cleverness to convert from approximate RGB value to face int value
		return null ;
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
	public  void solveStep(int face, boolean direction){
		future.rotateSide(face, direction);
		int[][] cFace = current.getFace(1);
		int[][] fFace = future.getFace(1);
		while(!sameFace(cFace, fFace)){
			displayInstructions(face, direction);
			cFace = update();
		}
		current = future.clone();
	}

	/**
	 * Implements specified cube rotation instruction by altering a copy of the current cube (future cube) and displaying
	 * an on -screen instruction until the current cube face is the same as the future cube face.
	 *
	 *
	 * @param direction  The direction in which the cube is being rotated. True for clockwise, false for counterclockwise.
	 *                                Direction is relative to a top bird's eye view of the cube.
	 */
	public  void solveStep(boolean direction){
		future.rotateCube(direction);
		int[][] cFace = current.getFace(1);
		int[][] fFace = future.getFace(1);
		while(!sameFace(cFace, fFace)){
			displayInstructions(direction);
			cFace = update();
		}
		current = future.clone();
	}

	/**
	 * Step 7 (Final Step): Places the edge pieces on the top layer into the correct positions. Implements the algorithm
	 * until the cube is solved. Expected number of loops of the while loop executed is between 1 to 2.
	 *
	 */
	public  void topLayerEdges(){
		future = current.clone();

		while(!current .isSolved()){
			//Rotate the top face so it is positioned properly
			if(current.getSquare(1, 0, 0) == 0) solveStep(5, CW );
			else if (current.getSquare(0, 0, 0) == 1) solveStep(5, CCW);
			else if (current.getSquare(2, 0, 0) == 0){
				solveStep(5, CW);
				solveStep(5, CW);
			}

			//Rotate the cube so that the solved face is on the left
			else if(current .getSquare(1, 0, 1) == 1) solveStep (CW );
			else if (current.getSquare(3, 0, 1) == 3) solveStep(CCW);
			else if (current.getSquare(2, 0, 1) == 2){
				solveStep(CW);
				solveStep(CW);
			}

			//Perform Algorithm
			else{
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
	public  void solveTopFaceHelper(boolean direction){
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
	public  void solveTopFace(){
		future = current.clone();
		while (!current .isFaceSolved(5)){
			int numSquares = current.countSquaresOnFace(5); //Detect top face pattern
			if(numSquares == 6){ //"Fish" case pattern
				//Position face properly
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
		future = current.clone();
		while(!current .isTopCornersPermutated2()){
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
				if(current .getSquare(1,0,0) != 5 && current.getSquare(1,0,0) != sameColorVal) diffColorVal = current.getSquare(1,0,0);
				else if (current.getSquare(0,0,2) != 5 && current.getSquare(0,0,2) != sameColorVal) diffColorVal = current.getSquare(0,0,2);
				else if (current.getSquare(5,2,0) != 5 && current.getSquare(5,2,0) != sameColorVal) diffColorVal = current.getSquare(5,2,0);
				//Check if that corner is in the correct position or not, and correct its position
				if(sameColorVal+1 == diffColorVal || sameColorVal-3 == diffColorVal){ //Checks if diffColorVal is to the right of sameColorVal
					solveStep(CW);
					solveStep(CW);
				}
			}
			else{ //When there are no same colors on the left two corners
				//Find the similar color between the two corners in the front
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
	 * Step 4: Creates a "cross" on the top layer of the cube. First the top layer is rotated into a position where an algorithm
	 * can be executed. Then one of two algorithms are implemented depending on the presence of top face colors in certain
	 * areas of the top face.
	 *
	 *
	 */
	public  void topLayerCross(){
		future = current.clone();
		while(!current .isTopCrossSolved()){
			//Position top face for algorithm use
			if(current .getSquare(5,1,0) == 5 && current.getSquare(5,2,1) != 5) solveStep(5, CW);
			else if (current.getSquare(5,1,2) == current.getSquare(5,2,1)) solveStep(5, CCW);
			else if (current.getSquare(5,1,0) == current.getSquare(5,2,1)){
				solveStep(CW);
				solveStep(CW);
			}
			//Implement Algorithm
			if(current .getSquare(5,0,1) == current.getSquare(5,2,1)){
				solveStep(0, CW);
				solveStep(1, CW);
				solveStep(5, CW);
				solveStep(1, CCW);
				solveStep(5, CCW);
				solveStep(0, CCW);
			}
			else{
				solveStep(0, CW);
				solveStep(5, CW);
				solveStep(1, CW);
				solveStep(5, CCW);
				solveStep(1, CCW);
				solveStep(0, CCW);
			}
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
	public  void secondLayerHelperShortAlg(boolean direction){
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
	public  void secondLayerHelperLongAlg(boolean direction){ //Be careful about direction
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
	public  void secondLayer(){
		future.clone(current);
		//Setup, need to position sacrifice corner in the cube(1,2,0) area. Not necessary if firstLayer function does it.
		while(current .numSecondLayerCorrect() != 4){
			while(current .numSecondLayerCorrect() !=3){
				while(current .isEdgesOnTopLayer() && current.numSecondLayerCorrect() != 3){
					if(!current .isLeftEdgeCorrect()){ //Place the correct edge in if possible
						if(current .getSquare(5,0,1) !=5 && current.getComplementaryEdgeColor(5, 0, 1) !=5 && current.getSquare(5,0,1) != 1){
							solveStep(5, CCW);
							secondLayerHelperShortAlg(CW);
						}                                        
						else if (current.getSquare(5,0,1) !=5 && current.getComplementaryEdgeColor(5, 0, 1) !=5 && current.getSquare(5,0,1) == 1){
							solveStep(5, CW);
							solveStep(5, CW);
							secondLayerHelperShortAlg(CCW);
						}
						else if (current.getSquare(5,1,0) !=5 && current.getComplementaryEdgeColor(5, 1, 0) !=5 && current.getSquare(5,1,0) != 1){
							secondLayerHelperShortAlg(CW);
						}
						else if (current.getSquare(5,1,0) !=5 && current.getComplementaryEdgeColor(5, 1, 0) !=5 && current.getSquare(5,1,0) == 1){
							solveStep(5, CCW);
							secondLayerHelperShortAlg(CCW);
						}
						else if (current.getSquare(5,1,2) !=5 && current.getComplementaryEdgeColor(5, 1, 2) !=5 && current.getSquare(5,1,2) != 1){
							solveStep(5, CW);
							solveStep(5, CW);
							secondLayerHelperShortAlg(CW);
						}
						else if (current.getSquare(5,1,2) !=5 && current.getComplementaryEdgeColor(5, 1, 2) !=5 && current.getSquare(5,1,2) == 1){
							solveStep(5, CW);
							secondLayerHelperShortAlg(CCW);
						}
						else if (current.getSquare(5,2,1) !=5 && current.getComplementaryEdgeColor(5, 2, 1) !=5 && current.getSquare(5,2,1) != 1){
							solveStep(5, CW);
							secondLayerHelperShortAlg(CW);
						}
						else if (current.getSquare(5,2,1) !=5 && current.getComplementaryEdgeColor(5, 2, 1) !=5 && current.getSquare(5,2,1) == 1){
							secondLayerHelperShortAlg(CCW);
						}
					}
					solveStep(CW);
					solveStep(4, CW);
				} //end while EdgesOnTop loop
				if(!current .isLeftEdgeCorrect()){ //This is called when there are no edges on the top face. This pulls the edges out of the incorrect spot, enabling re-entrance of the above while loop.
					int colorA = current.getSquare(0,1,1);
					int colorB = current.getSquare(1,1,1);
					if((current .getSquare(1,1,2) == colorA || current.getSquare(1,1,2) == colorB) && (current.getSquare(2,1,0) == colorA || current.getSquare(2,1,0) == colorB)){
						solveStep(4, CW);
						solveStep(2, CW);
						solveStep(5, CW);
						solveStep(2, CCW);
						solveStep(4, CCW);
					}
					else if ((current.getSquare(3,1,2) == colorA || current.getSquare(3,1,2) == colorB) && (current.getSquare(0,1,0) == colorA || current.getSquare(0,1,0) == colorB)){
						solveStep(4, CCW);
						solveStep(CCW);
						secondLayerHelperShortAlg(CW);
						solveStep(CW);
						solveStep(4, CW);
					}
					else if ((current.getSquare(2,1,2) == colorA || current.getSquare(2,1,2) == colorB) && (current.getSquare(3,1,0) == colorA || current.getSquare(3,1,0) == colorB)){
						solveStep(4, CW);
						solveStep(4, CW);
						solveStep(CW);
						solveStep(2, CW);
						solveStep(5, CW);
						solveStep(2, CCW);
						solveStep(4, CCW);
						solveStep(4, CCW);
						solveStep(CCW);
					}
					else if ((current.getSquare(0,1,2) == colorA || current.getSquare(0,1,2) == colorB) && (current.getSquare(1,1,0) == colorA || current.getSquare(1,1,0) == colorB)){
						secondLayerHelperShortAlg(CW);
					}
				}
				else{
					solveStep(CW);
					solveStep(4, CW);
				}
			} //End while != 3 loop

			//Position the sacrifice corner with the unsolved edge
			while(current .isLeftEdgeCorrect()){
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

			//Place in the final edge
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
		}
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
		while(current.numFirstLayerCornersCorrect() < 3){
			while(current.isCornersOnTopLayer() && current.numFirstLayerCornersCorrect() < 3){
				if(!current.isLeftBottomCornerCorrect()){
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
					else if(current.getSquare(0,0,0) == 4 && current.areComplementaryCornerColors(3,0,0,colorA,colorB)){
						solveStep(5, CW);
						solveStep(5, CW);
						firstLayerCornersHelper(CCW);
					}

					else if(current.getSquare(0,2,2) == 4 && current.areComplementaryCornerColors(0,2,2,colorA,colorB)){
						solveStep(5, CW);
						firstLayerCornersHelper(CW);
					}
					else if(current.getSquare(1,2,2) == 4 && current.areComplementaryCornerColors(1,2,2,colorA,colorB)){
						solveStep(5, CW);
						solveStep(5, CW);
						firstLayerCornersHelper(CW);
					}
					else if(current.getSquare(2,2,2) == 4 && current.areComplementaryCornerColors(2,2,2,colorA,colorB)){
						solveStep(5, CCW);
						firstLayerCornersHelper(CW);
					}
					else if(current.getSquare(3,2,2) == 4 && current.areComplementaryCornerColors(3,2,2,colorA,colorB)){
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
				}
				solveStep(CW);
			}
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
		
		while(current.isLeftBottomCornerCorrect() && current.numFirstLayerCornersCorrect() == 3){
			solveStep(CW);
		}
	}

	public void firstLayerCross(){
		while(!current.isFirstLayerCrossSolved()){

			while(!current.isFirstLayerEdgesEasilyAvailable()){
				if(!current.isFirstLayerEdgeCorrect()){
					if(current.getSquare(1,0,1) == 1 && current.getComplementaryEdgeColor(1,0,1) == 4){
						solveStep(1, CW);
						solveStep(1, CW);
					}
					else if(current.getSquare(0,0,1) == 1 && current.getComplementaryEdgeColor(0,0,1) == 4){
						solveStep(5, CCW);
						solveStep(1, CW);
						solveStep(1, CW);
					}
					else if(current.getSquare(2,0,1) == 1 && current.getComplementaryEdgeColor(2,0,1) == 4){
						solveStep(5, CW);
						solveStep(1, CW);
						solveStep(1, CW);
					}
					else if(current.getSquare(3,0,1) == 1 && current.getComplementaryEdgeColor(3,0,1) == 4){
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
				}
				solveStep(CW);
			}


			//checks if there is a white on the outer edge piece, and moves it up to the top
			if(current.getSquare(0,1,2) == 4){
				solveStep(1, CW);
				if(current.getSquare(0,1,2) == 4){
					solveStep(5, CCW);
					solveStep(1, CCW);
				}
			}
			else if(current.getSquare(2,1,0) == 4){
				solveStep(1, CCW);
				if(current.getSquare(2,1,0) ==4){
					solveStep(5, CCW);
					solveStep(1, CW);
				}
			}
			//checks if there is a white on the inner edge piece, moves it to the top
			else if(current.getSquare(1,1,0) == 4){
				solveStep(0, CCW);
				if(current.getSquare(4,1,0) == 4){
					solveStep(5, CCW);
					solveStep(0, CW);
				}
			}
			else if(current.getSquare(1,1,2) == 4){
				solveStep(2, CW);
				if(current.getSquare(4,1,2) == 4){
					solveStep(5, CW);
					solveStep(2, CCW);
				}
			}
			//checks if there is a white on the bottom edge piece in wrong position, move to top
			else if(current.getSquare(1,2,1) == 4){
				solveStep(1, CW);
				solveStep(0, CCW);
				solveStep(5, CCW);
				solveStep(0, CW);
			}
			//checks if there is a white on the top edge piece, move to top
			else if(current.getSquare(1,0,1) ==4){
				solveStep(1, CCW);
				solveStep(0, CCW);
				solveStep(5, CCW);
				solveStep(0, CW);
			}
			else solveStep(CW);
		}

	}
}

