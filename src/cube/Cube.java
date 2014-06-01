package cube;

import java.util.HashMap;

import android.util.Log;



public class Cube implements Cloneable{
	//time to eat food
	// private int [][] Left = new int[3][3]; //face value = 0
	// private int [][] Front = new int[3][3]; //face value = 1
	// private int [][] Right = new int[3][3]; //face value = 2
	// private int [][] Back = new int[3][3]; //face value = 3
	// private int [][] Bottom = new int[3][3]; //face value = 4
	// private int [][] Top = new int[3][3]; //face value = 5
	// private int [][][] cube = {Left, Front, Right, Back, Bottom, Top};
	/*
	 *            Cube Layout
	 *
	 *                   555
	 *                   555
	 *                   555
	 *            000 111 222 333
	 *            000 111 222 333    
	 *            000 111 222 333
	 *                   444
	 *                   444
	 *                   444
	 */
	
	//Test #2
	private int [][][] cube = new int[6][3][3];

	private int leftColor , frontColor , rightColor , backColor , topColor , bottomColor = 0;
	private int [] RGBColors = {leftColor , frontColor , rightColor , backColor , topColor, bottomColor};
	private int algorithmCounter = 0;
	private final boolean CW = true; //Clockwise
	private final boolean CCW = false; //Counterclockwise
	//     boolean solved = false;
	private HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>();

	//Cube constructors and array manipulators

	/**
	 * Cube constructor. Calls resetCube() to populate a 6x3x3 array representation of a Rubik's Cube.
	 * 0 = Left Face;
	 * 1 = Front Face;
	 * 2 = Right Face;
	 * 3 = Back Face;
	 * 4 = Bottom Face;
	 * 5 = Top Face;
	 *
	 *
	 * @see resetCube
	 */
	public Cube(){
		resetCube();
	}

	public Cube(int[][][] cube, int[] RGBColors){
		this.cube = cube;
		this.RGBColors = RGBColors;
	}

	//        protected Cube clone(){
	//            Cube clone = null;
	//			try {
	//				clone = (Cube)super.clone();
	//			} catch (CloneNotSupportedException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//            return clone;
	//        }

	public Cube clone(){
		return new Cube(cube, RGBColors);
	}

	public void clone(Cube c){
		for(int f = 0; f<6; f++){
			for(int i = 0; i <3; i++){
				for(int j = 0; j<3; j++){
					cube[f][i][j] = c.getSquare(f, i, j);

				}
			}
			RGBColors[f] = c.getColorValue(f);
		}
	}


	/**
	 * Populates a 6x3x3 array of a completely solved Rubik's Cube.
	 *
	 *
	 * @see Cube
	 */
	public void resetCube(){
		for(int i = 0; i<6; i++){
			for(int j = 0; j<3; j++){
				for(int k = 0; k<3; k++){
					cube[i][j][k] = i;
				}
			}
		}

		// cube[3][0][0] = 7;
		// cube[3][1][0] = 8;
		// cube[3][2][0] = 9;

	}

	/**
	 * Physically rotates the entire cube in one of two directions. Shifts all values in the cube as appropriate so that
	 * the "1" face is always pointed towards the user.
	 *
	 *
	 * @param clockwise the direction the cube is rotating relative to a bird's eye view.
	 */
	public void rotateCube(boolean clockwise){

		int[][] storage = cube [0];
		int colorStorage = RGBColors [0];
		if(clockwise){
			faceShift( cube[4], false);
			faceShift( cube[5], true);
			cube[0] = cube[1];
			cube[1] = cube[2];
			cube[2] = cube[3];
			cube[3] = storage;

			RGBColors[0] = RGBColors [1];
			RGBColors[1] = RGBColors [2];
			RGBColors[2] = RGBColors [3];
			RGBColors[3] = colorStorage;



		}
		else{
			faceShift( cube[4], true);
			faceShift( cube[5], false);
			cube[0] = cube[3];
			cube[3] = cube[2];
			cube[2] = cube[1];
			cube[1] = storage;

			RGBColors[0] = RGBColors [3];
			RGBColors[3] = RGBColors [2];
			RGBColors[2] = RGBColors [1];
			RGBColors[1] = colorStorage;
		}
	}

	/**
	 * Rotates one face of the cube in one of two directions. Calls faceShift to change the values of the rotating face, then
	 * shifts all values of other faces in the cube as appropriate.
	 *
	 *
	 * @param face an integer value of the face that is going to be rotated (0 through 5).
	 * @param clockwise  the direction the specified face is going to rotate, relative to that face.
	 */
	public void rotateSide(int face, boolean clockwise){
		if(face != 6) faceShift(cube [face], clockwise);
		int[] storagerow = new int[3];
		switch(face){
		case 0:
			//Store one of the columns
			for(int j = 0; j<3; j++){
				storagerow[j] = cube[3][j][2];
			}
			if(clockwise){
				//Rotate columns around
				for(int j= 0; j<3; j++){
					cube[3][2-j][2] = cube[4][j][0];
					cube[4][j][0] = cube[1][j][0];
					cube[1][j][0] = cube[5][j][0];
					cube[5][j][0] = storagerow[2-j];
				}
			}
			else{
				//Rotate columns around
				for(int j= 0; j<3; j++){
					cube[3][2-j][2] = cube[5][j][0];
					cube[5][j][0] = cube[1][j][0];
					cube[1][j][0] = cube[4][j][0];
					cube[4][j][0] = storagerow[2-j];
				}
			}
			break;
		case 1:
			//Store one of the rows
			for(int i = 0; i<3; i++){
				storagerow[i] = cube[5][2][i];
			}
			if(clockwise){
				//Rotate rows around
				for(int i=0; i<3; i++){
					cube[5][2][i] = cube[0][2-i][2]; }
				for(int i=0; i<3; i++){
					cube[0][i][2] = cube[4][0][i]; }
				for(int i=0; i<3; i++){
					cube[4][0][i] = cube[2][2-i][0]; }
				for(int i=0; i<3; i++){
					cube[2][i][0] = storagerow[i];
				}
			}
			else{
				//Rotate rows around
				for(int i=0; i<3; i++){
					cube[5][2][i] = cube[2][i][0]; }
				for(int i=0; i<3; i++){
					cube[2][i][0] = cube[4][0][2-i]; }
				for(int i=0; i<3; i++){
					cube[4][0][i] = cube[0][i][2]; }
				for(int i=0; i<3; i++){
					cube[0][i][2] = storagerow[2-i];
				}
			}
			break;
		case 2:
			//Store one of the columns
			for(int j = 0; j<3; j++){
				storagerow[j] = cube[3][j][0];
			}
			if(clockwise){
				//Rotate columns around
				for(int j= 0; j<3; j++){
					cube[3][2-j][0] = cube[5][j][2];
					cube[5][j][2] = cube[1][j][2];
					cube[1][j][2] = cube[4][j][2];
					cube[4][j][2] = storagerow[2-j];
				}
			}
			else{
				//Rotate columns around
				for(int j= 0; j<3; j++){
					cube[3][2-j][0] = cube[4][j][2];
					cube[4][j][2] = cube[1][j][2];
					cube[1][j][2] = cube[5][j][2];
					cube[5][j][2] = storagerow[2-j];
				}
			}
			break;
		case 3:
			//Store one of the rows
			for(int i = 0; i<3; i++){
				storagerow[i] = cube[5][0][i];
			}
			if(!clockwise){
				//Rotate rows around
				for(int i=0; i<3; i++){
					cube[5][0][i] = cube[0][2-i][0]; }
				for(int i=0; i<3; i++){
					cube[0][i][0] = cube[4][2][i]; }
				for(int i=0; i<3; i++){
					cube[4][2][i] = cube[2][2-i][2]; }
				for(int i=0; i<3; i++){
					cube[2][i][2] = storagerow[i];
				}
			}
			else{
				//Rotate rows around
				for(int i=0; i<3; i++){
					cube[5][0][i] = cube[2][i][2]; }
				for(int i=0; i<3; i++){
					cube[2][i][2] = cube[4][2][2-i]; }
				for(int i=0; i<3; i++){
					cube[4][2][i] = cube[0][i][0]; }
				for(int i=0; i<3; i++){
					cube[0][i][0] = storagerow[2-i];
				}
			}
			break;

		case 4:
			if(clockwise){
				//Store one of the bottom rows
				for(int i=0; i<3; i++){
					storagerow[i] = cube[3][2][i];
				}
				//Rotate the rows around
				for(int f=2; f>-1; f--){
					for(int i=0; i<3; i++){
						cube[f+1][2][i] = cube[f][2][i];
					}
				}
				//Slap the stored row in
				for(int i = 0; i<3; i++){
					cube[0][2][i] = storagerow[i];
				}
			}
			else{
				//Store one of the bottom rows
				for(int i=0; i<3; i++){
					storagerow[i] = cube[0][2][i];
				}
				//Rotate the rows around
				for(int f=0; f<3; f++){
					for(int i=0; i<3; i++){
						cube[f][2][i] = cube[f+1][2][i];
					}
				}
				//Slap the stored row in
				for(int i = 0; i<3; i++){
					cube[3][2][i] = storagerow[i];
				}
			}
			break;
		case 5:
			if(!clockwise){
				//Store one of the top rows
				for(int i=0; i<3; i++){
					storagerow[i] = cube[3][0][i];
				}
				//Rotate the rows around
				for(int f=2; f>-1; f--){
					for(int i=0; i<3; i++){
						cube[f+1][0][i] = cube[f][0][i];
					}
				}
				//Slap the stored row in
				for(int i = 0; i<3; i++){
					cube[0][0][i] = storagerow[i];
				}
			}
			else{
				//Store one of the top rows
				for(int i=0; i<3; i++){
					storagerow[i] = cube[0][0][i];
				}
				//Rotate the rows around
				for(int f=0; f<3; f++){
					for(int i=0; i<3; i++){
						cube[f][0][i] = cube[f+1][0][i];
					}
				}
				//Slap the stored row in
				for(int i = 0; i<3; i++){
					cube[3][0][i] = storagerow[i];
				}
			}
			break;
		case 6:
			//Store one of the columns
			for(int j = 0; j<3; j++){
				storagerow[j] = cube[3][j][1];
			}
			if(!clockwise){
				//Rotate columns around
				for(int j= 0; j<3; j++){
					cube[3][2-j][1] = cube[4][j][1];
					cube[4][j][1] = cube[1][j][1];
					cube[1][j][1] = cube[5][j][1];
					cube[5][j][1] = storagerow[2-j];
				}
			}
			else{
				//Rotate columns around
				for(int j= 0; j<3; j++){
					cube[3][2-j][1] = cube[5][j][1];
					cube[5][j][1] = cube[1][j][1];
					cube[1][j][1] = cube[4][j][1];
					cube[4][j][1] = storagerow[2-j];
				}
			}
			break;
		default: System.out .println("IMPROPER FACE VALUE");
		break;




		} //End switch statement
	}

	/**
	 * Helper function for rotateCube and rotateSide, works with a 2D array and rotates the array either clockwise or counterclockwise.
	 *
	 *
	 * @param face an integer value of the face that is going to be rotated (0 through 5).
	 * @param clockwise  the direction the specified face is going to rotate, relative to that face.
	 * @see rotateCube
	 * @see rotateSide
	 */
	public void faceShift(int[][] face, boolean clockwise){
		int corner = face[0][0]; //Store the color of the left corner
		int edge = face[0][1]; //Store the top center edge color
		if(clockwise){
			//Shift corner data values
			face[0][0] = face[2][0];
			face[2][0] = face[2][2];
			face[2][2] = face[0][2];
			face[0][2] = corner;
			//Shift edge data values
			face[0][1] = face[1][0];
			face[1][0] = face[2][1];
			face[2][1] = face[1][2];
			face[1][2] = edge;
		}
		else{
			//Shift corner data values
			face[0][0] = face[0][2];
			face[0][2] = face[2][2];
			face[2][2] = face[2][0];
			face[2][0] = corner;
			//Shift edge data values
			face[0][1] = face[1][2];
			face[1][2] = face[2][1];
			face[2][1] = face[1][0];
			face[1][0] = edge;
		}
	}

	public void reMap() {

		colorMap = new HashMap<Integer, Integer>();

		for(int face = 0; face < 6; face++){
			colorMap.put(cube[face][1][1], face);
		}

		Log.e("tag", colorMap.toString());

		for(int face = 0; face < 6; face++){
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					cube[face][i][j] = colorMap.get(cube[face][i][j]);

				}
			}
		}

	}

// Just Algorithm Things
	/*-------------------------------------------------------------------------------------------------------------------------*/

	/**
	 * Returns the future/ideal cube after one step of the algorithm.
	 *
	 *
	 * @return the future cube after one step of the algorithm is implemented
	 * @deprecated
	 */
	public int [][][] nextStep (){
		boolean algStarted = false;

		//Rotate the cube so that the blank face is on the left
		if(cube [1][0][1] == 1 && !algStarted){ // CHANGE TO FACE IS EMPTY
			rotateCube( CW);
			return cube ;
		}
		else if (cube [3][0][1] == 3 && !algStarted){
			rotateCube( CCW);
			return cube ;
		}
		//Algorithm for edge switching
		else if (cube [2][0][1] == 1 || cube[2][0][1] == 0 || algStarted){
			boolean adjColMatch = true;
			if(cube [2][0][1] == 3) adjColMatch = false;
			algStarted = true;
			switch(algorithmCounter ){
			case 0: rotateSide(2, CCW ); break;
			case 1: rotateSide(2, CCW ); break;
			case 2:
				if(adjColMatch) rotateSide(5, CW );
				else rotateSide(5, CCW ); break;
			case 3: rotateCube(CCW ); break;
			case 4: rotateSide(6, CCW ); break;
			case 5: rotateSide(5, CW ); break;
			case 6: rotateSide(5, CW ); break;
			case 7: rotateSide(6, CW ); break;
			case 8:
				if(adjColMatch) rotateSide(5, CW );
				else rotateSide(5, CCW ); break;
			case 9: rotateCube(CW ); break;
			case 10: rotateSide(2, CW ); break;
			case 11: rotateSide(2, CW ); break;
			default: algorithmCounter = -1;
			algStarted = false;
			break;
			}
			algorithmCounter++;
			return cube ;
		}
		else return cube ;
	}

	/**
	 * Counts the number of squares with the color of the face (center piece).
	 *
	 *
	 * @param face the face of interest (0 through 5)
	 * @return the number of squares with the color of the center piece of the specified face.
	 */
	public int countSquaresOnFace(int face){
		int counter = 0;
		for(int i = 0; i <3; i++){
			for(int j = 0; j<3; j++){
				if(cube [face][i][j] == cube[face][1][1]) counter++;
			}
		}
		return counter;
	}

	/**
	 * Counts the number of squares of a specified color on a specified face.
	 *
	 *
	 * @param squareColor the color of the squares to be counted, (0 through 5)
	 * @param face the face of the cube where squares will be counted (0 through 5).
	 * @return the number of squares with the specified squareColor on the specified face.
	 */
	public int countSquaresOnFace(int squareColor, int face){
		int counter = 0;
		for(int i = 0; i <3; i++){
			for(int j = 0; j<3; j++){
				if(cube [face][i][j] == squareColor) counter++;
			}
		}
		return counter;
	}

	/**
	 * Checks if one particular face has all 9 of its colors.
	 *
	 *
	 * @param face the face of interest, 0 through 5.
	 * @return true if the face has all 9 of its colors, otherwise false
	 */
	public boolean isFaceSolved(int face){
		if(countSquaresOnFace(face) == 9) return true;
		else return false;
	}

	/**
	 * Checks if the entire cube is solved. Utilizes isFaceSolved on all 6 faces.
	 *
	 *
	 * @return true if the entire cube is solved, false otherwise.
	 */
	public boolean isSolved(){
		int counter = 0;
		for(int i = 0; i <6; i++){
			if(isFaceSolved(i)) counter++;
		}
		if(counter == 6) return true;
		else return false;
	}

	/**
	 * Checks if the top layer corners are in the proper position/order.
	 *
	 *
	 * @return true if the top layer corners are in the right order.
	 * @see isTopCornersPermutated2
	 * @deprecated
	 */
	public boolean isTopCornersPermutated (){
		int counter = 0;
		if(cube [5][2][0] == 0 || cube[5][2][0] == 1) counter++;
		if(cube [0][0][2] == 0 || cube[0][0][2] == 1) counter++;
		if(cube [1][0][0] == 0 || cube[1][0][0] == 1) counter++;

		if(cube [5][2][2] == 2 || cube[5][2][2] == 1) counter++;
		if(cube [2][0][0] == 2 || cube[2][0][0] == 1) counter++;
		if(cube [1][0][2] == 2 || cube[1][0][2] == 1) counter++;

		if(cube [5][0][0] == 0 || cube[5][0][0] == 3) counter++;
		if(cube [0][0][0] == 0 || cube[0][0][0] == 3) counter++;
		if(cube [3][0][2] == 0 || cube[3][0][2] == 3) counter++;

		if(cube [5][0][2] == 2 || cube[5][0][2] == 3) counter++;
		if(cube [3][0][0] == 2 || cube[3][0][0] == 3) counter++;
		if(cube [2][0][2] == 2 || cube[2][0][2] == 3) counter++;


		if(counter == 8) return true;
		else return false;
	}

	/**
	 * Checks if the top layer corners are in the proper position/order.
	 * First, the two left corners on the top layer are compared, and if they share a similar color, then check if the
	 * bottom left corner is properly ordered relative to the top left corner. Then proceed to the bottom right corner and
	 * check if it is properly ordered relative to the bottom left, then top right, then top left, etc.
	 *
	 *
	 * @return true if all 4 corners in the top layer are properly ordered. false otherwise.
	 */
	public boolean isTopCornersPermutated2(){
		int counter = 0;
		int sameColorVal = 88; //Shared color value
		int diffColorVal = 88;

		if(cube [1][0][0] == cube[0][0][0] && cube[1][0][0] != 5){
			counter++;
			sameColorVal = cube[1][0][0];
		}
		if(cube [1][0][0] == cube[5][0][0] && cube[1][0][0] != 5){
			counter++;
			sameColorVal = cube[1][0][0];
		}
		if(cube [1][0][0] == cube[3][0][2] && cube[1][0][0] != 5){
			counter++;
			sameColorVal = cube[1][0][0];
		}
		if(cube [0][0][2] == cube[0][0][0] && cube[0][0][2] != 5){
			counter++;
			sameColorVal = cube[0][0][2];
		}
		if(cube [0][0][2] == cube[5][0][0] && cube[0][0][2] != 5){
			counter++;
			sameColorVal = cube[0][0][2];
		}
		if(cube [0][0][2] == cube[3][0][2] && cube[0][0][2] != 5){
			counter++;
			sameColorVal = cube[0][0][2];
		}
		if(cube [5][2][0] == cube[0][0][0] && cube[5][2][0] != 5){
			counter++;
			sameColorVal = cube[5][2][0];
		}
		if(cube [5][2][0] == cube[5][0][0] && cube[5][2][0] != 5){
			counter++;
			sameColorVal = cube[5][2][0];
		}
		if(cube [5][2][0] == cube[3][0][2] && cube[5][2][0] != 5){
			counter++;
			sameColorVal = cube[5][2][0];
		}

		if(counter == 1){
			if(cube [1][0][0] != 5 && cube[1][0][0] != sameColorVal) diffColorVal = cube[1][0][0];
			else if (cube [0][0][2] != 5 && cube[0][0][2] != sameColorVal) diffColorVal = cube[0][0][2];
			else if (cube [5][2][0] != 5 && cube[5][2][0] != sameColorVal) diffColorVal = cube[5][2][0];

			if(sameColorVal+1 == diffColorVal || sameColorVal-3 == diffColorVal){ //Checks if diffColorVal is to the right of sameColorVal
				counter++;
				sameColorVal = diffColorVal;
				if(cube [1][0][2] != 5 && cube[1][0][2] !=sameColorVal) diffColorVal=cube[1][0][2];
				else if (cube [5][2][2] != 5 && cube[5][2][2] !=sameColorVal) diffColorVal=cube [5][2][2];
				else if (cube [2][0][0] != 5 && cube[2][0][0] !=sameColorVal) diffColorVal=cube [2][0][0];                          

				if(sameColorVal+1 == diffColorVal || sameColorVal-3 == diffColorVal){
					counter++;
					sameColorVal = diffColorVal;
					if(cube [2][0][2] != 5 && cube[2][0][2] !=sameColorVal) diffColorVal=cube [2][0][2];
					else if (cube [5][0][2] != 5 && cube[5][0][2] !=sameColorVal) diffColorVal=cube [5][0][2];
					else if (cube [3][0][0] != 5 && cube[3][0][0] !=sameColorVal) diffColorVal=cube [3][0][0];                          

					if(sameColorVal+1 == diffColorVal || sameColorVal-3 == diffColorVal){
						counter++;
						sameColorVal = diffColorVal;
						if(cube [3][0][2] != 5 && cube[3][0][2] !=sameColorVal) diffColorVal=cube [3][0][2];
						else if (cube [0][0][0] != 5 && cube[0][0][0] !=sameColorVal) diffColorVal=cube [0][0][0];
						else if (cube [5][0][0] != 5 && cube[5][0][0] !=sameColorVal) diffColorVal=cube [5][0][0];

						if(sameColorVal+1 == diffColorVal || sameColorVal-3 == diffColorVal){
							counter++;
						}
					}
				}
			}
		}
		return (counter == 5);
	}

	/**
	 * Checks to see if a "cross" is present on the top layer.
	 *
	 *
	 * @return true if a "cross" shape is present on the top face, false otherwise.
	 */
	public boolean isTopCrossSolved(){
		int counter = 0;
		if(cube [5][0][1] == 5) counter++;
		if(cube [5][1][0] == 5) counter++;
		if(cube [5][1][2] == 5) counter++;
		if(cube [5][2][1] == 5) counter++;
		return (counter==4);
	}

	/**
	 * Returns the number of edge pieces that are in the proper position in the second layer. Relies on center pieces only.
	 *
	 *
	 * @return an int value that should be between 0 and 4 of the number of edge pieces in the proper position in the second layer.
	 */
	public int numSecondLayerCorrect(){
		int counter = 0;
		if(cube [0][1][0] == 0 && cube [3][1][2] == 3) counter++;
		if(cube [0][1][2] == 0 && cube [1][1][0] == 1) counter++;
		if(cube [1][1][2] == 1 && cube [2][1][0] == 2) counter++;
		if(cube [2][1][2] == 2 && cube [3][1][0] == 3) counter++;
		return counter;
	}

	/**
	 * Checks if there are non -top face edge pieces on the top layer. For second layer solving usage
	 *
	 * @return true if there are available edge pieces on the top layer, false otherwise.
	 */
	public boolean isEdgesOnTopLayer(){
		int counter = 0;
		if(cube [5][0][1] !=5 && getComplementaryEdgeColor(5,0,1) != 5) counter++;
		if(cube [5][1][0] !=5 && getComplementaryEdgeColor(5,1,0) != 5) counter++;
		if(cube [5][1][2] !=5 && getComplementaryEdgeColor(5,1,2) != 5) counter++;
		if(cube [5][2][1] !=5 && getComplementaryEdgeColor(5,2,1) != 5) counter++;
		return (counter > 0);
	}

	/**
	 * Checks if the left edge of the front face has its second layer edge piece in the correct position.
	 *
	 * @return true if the left edge is correctly positioned, false otherwise.
	 */
	public boolean isLeftEdgeCorrect(){
		return (cube [0][1][2] == 0 && cube[1][1][0] == 1);
	}

	//For usage in solving first layer corners
	public boolean isLeftBottomCornerCorrect(){
		return (cube [0][2][2] == 0 && cube[1][2][0] == 1 && cube[4][0][0] == 4);
	}

	public int numFirstLayerCornersCorrect(){
		int counter = 0;
		if(cube [0][2][2] == 0 && cube[1][2][0] == 1 && cube[4][0][0] == 4) counter++;
		if(cube [1][2][2] == 1 && cube[2][2][0] == 2 && cube[4][0][2] == 4) counter++;
		if(cube [2][2][2] == 2 && cube[3][2][0] == 3 && cube[4][2][2] == 4) counter++;
		if(cube [3][2][2] == 3 && cube[0][2][0] == 0 && cube[4][2][0] == 4) counter++;
		return counter;
	}

	//for first layer solving
	public boolean isCornersOnTopLayer(){
		if(countSquaresOnFace(4, 5) > 1) return true;
		for(int i = 0; i <3; i++){
			if(cube[i][0][0] == 4 || cube[i][0][2] == 4) return true;
		}
		return false;
	}

	public boolean isFirstLayerCrossSolved(){
		int counter = 0;
		if(cube [4][0][1] == 4) counter++;
		if(cube [4][1][0] == 4) counter++;
		if(cube [4][1][2] == 4) counter++;
		if(cube [4][2][1] == 4) counter++;
		return (counter==4);
	}

	public boolean isFirstLayerEdgeCorrect(){
		return (cube[4][0][1] == 4);
	}

	public boolean isFirstLayerEdgesEasilyAvailable(){
		if(countSquaresOnFace(4, 5) > 1) return true;
		if(cube[0][1][2] == 4 || cube[2][1][0] == 4) return true;
		// 	for(int i = 0; i <3; i++){
		// 	if(cube[i][1][0] == 4 || cube[i][1][2] == 4) return true;
		// }
		return false;		

	}


	/*-------------------------------------------------------------------------------------------------------------------------*/

	/**
	 * Returns the main cube object.
	 *
	 * @return returns a Cube object.
	 */
	public int [][][] getCube(){
		return cube ;
	}

	/**
	 * Returns the specified face of the cube.
	 *
	 * @param face an integer value of the face of interest (0 through 5)
	 * @return returns a 2D array of the face of interest.
	 */
	public int [][] getFace(int face){
		return cube [face];
	}

	/**
	 * Returns the color (integer representation, 0 through 5) value of a specified square in the cube.
	 *
	 * @param face integer value, 0 through 5, of the face
	 * @param i integer value, 0 through 2, [i][j] of a 2D array (y value)
	 * @param j integer value, 0 through 2m [i][j] of a 2D array (x value)
	 * @return returns an integer value, 0 through 5, representing the color of that square.
	 */
	public int getSquare(int face, int i, int j){
		return cube [face][i][j];
	}

	/**
	 * Sets the square of a cube with a specific color.
	 *
	 * @param face integer value, 0 through 5, of the face
	 * @param i integer value, 0 through 2, [i][j] of a 2D array (y value)
	 * @param j integer value, 0 through 2, [i][j] of a 2D array (x value)
	 * @param Color integer value, 0 through 5. The color that the specified square should be.
	 */
	public void setSquare(int face, int i, int j, int Color){
		cube[face][i][j] = Color;
	}

	/**
	 * Sets the RGB color value of a specified face. Basically an RGB value is associated with an integer 0 through 5, one for each solved face.
	 *
	 * @param face The face color of interest (middle square). Face will be 0 through 5.
	 * @param colorValue The RGB integer for that color.
	 * @deprecated
	 */
	public void setColorValue2(int face, int colorValue){
		switch(face){
		case 0: leftColor = colorValue;
		break;
		case 1: frontColor = colorValue;
		break;
		case 2: rightColor = colorValue;
		break;
		case 3: backColor = colorValue;
		break;
		case 4: bottomColor = colorValue;
		break;
		case 5: topColor = colorValue;
		break;
		default: System.out .println("Improper Face Location Indicated");
		}
	}

	public void setColorValue(int face, int colorValue){
		RGBColors[face] = colorValue;
	}

	/**
	 * Retrieves the RGB color value for a specified face.
	 *
	 * @param face The face of interest, 0 through 5.
	 * @return Returns an RGB integer for the specified face.
	 * @deprecated
	 */
	public int getColorValue2 (int face){
		switch(face){
		case 0: return leftColor ;
		case 1: return frontColor ;
		case 2: return rightColor ;
		case 3: return backColor ;
		case 4: return bottomColor ;
		case 5: return topColor ;
		default: return 0;
		}
	}

	/**
	 * Retrieves the RGB color value for a specified face.
	 *
	 * @param face The face of interest, 0 through 5.
	 * @return Returns an RGB integer for the specified face.
	 */
	public int getColorValue(int face){
		return RGBColors [face];
	}

	/**
	 * Returns the color of the other side of an edge piece. Only works for the top layer (face = 5)
	 * @param face The face of interest. For purposes of this function, face always = 5
	 * @param i The integer value 0 through 2 of [i][j] in a 2D array (y value)
	 * @param j The integer value 0 through 2 of [i][j] in a 2D array (x value)
	 * @return Returns the integer representation for the color value on the other side of the edge piece.
	 */
	public int getComplementaryEdgeColor(int face, int i, int j){
		if(face == 5 && i == 2 && j == 1) return cube[1][0][1];
		if(face == 5 && i == 1 && j == 0) return cube[0][0][1];
		if(face == 5 && i == 1 && j == 2) return cube[2][0][1];
		if(face == 5 && i == 0 && j == 1) return cube[3][0][1];

		//face 1
		if(face == 1 && i == 2 && j == 1) return cube[4][0][1]; //technically not needed
		if(face == 1 && i == 1 && j == 0) return cube[0][1][1];
		if(face == 1 && i == 1 && j == 2) return cube[2][1][1];
		if(face == 1 && i == 0 && j == 1) return cube[5][2][1];



		else return 88;
	}

	//Only works for top layer
	public boolean areComplementaryCornerColors(int face, int i, int j, int colorA, int colorB){
		int[] Colors = new int[2];
		//Assuming face 4 on the top face
		if(face ==5 && i == 0 && j == 0){
			Colors[0] = cube[0][0][0];
			Colors[1] = cube[3][0][2];
		}
		else if(face ==5 && i == 0 && j == 2){
			Colors[0] = cube[3][0][0];
			Colors[1] = cube[2][0][2];
		}
		else if(face ==5 && i == 2 && j == 2){
			Colors[0] = cube[2][0][0];
			Colors[1] = cube[1][0][2];
		}
		else if(face ==5 && i == 2 && j == 0){
			Colors[0] = cube[1][0][0];
			Colors[1] = cube[0][0][2];
		}
		//Assuming face 4 is on the 0,0 face
		else if(face == 0 && i == 0 && j == 0){
			Colors[0] = cube[3][0][2];
			Colors[1] = cube[5][0][0];
		}
		else if(face == 1 && i == 0 && j == 0){
			Colors[0] = cube[0][0][2];
			Colors[1] = cube[5][2][0];
		}
		else if(face == 2 && i == 0 && j == 0){
			Colors[0] = cube[1][0][2];
			Colors[1] = cube[5][2][2];
		}
		else if(face == 3 && i == 0 && j == 0){
			Colors[0] = cube[2][0][2];
			Colors[1] = cube[5][0][2];
		}
		//Assuming face 4 is on the 0,2 face
		else if(face == 0 && i == 0 && j == 2){
			Colors[0] = cube[1][0][0];
			Colors[1] = cube[5][2][0];
		}
		else if(face == 1 && i == 0 && j == 2){
			Colors[0] = cube[2][0][0];
			Colors[1] = cube[5][2][2];
		}
		else if(face == 2 && i == 0 && j == 2){
			Colors[0] = cube[5][0][2];
			Colors[1] = cube[3][0][0];
		}
		else if(face == 3 && i == 0 && j == 2){
			Colors[0] = cube[0][0][0];
			Colors[1] = cube[5][0][0];
		}

		//for first layer
		else if(face == 0 && i == 2 && j ==2){
			Colors[0] = cube[1][2][0];
			Colors[1] = cube[4][0][0];
		}
		else if(face == 1 && i == 2 && j==0){
			Colors[0] = cube[0][2][2];
			Colors[1] = cube[4][0][0];
		}

		if((colorA == Colors[0] || colorA == Colors[1]) && (colorB == Colors[0] || colorB == Colors[1])) return true;
		else return false;

	}

	public boolean isValidCube(){
		int[] bins = new int[6];
		for(int f = 0; f<6; f++){
			for(int i = 0; i<3; i++){
				for(int j = 0 ; j<3; j++){
					bins[getSquare(f, i, j)]++;
				}
			}
		}
		
		for(int i = 0; i < bins.length; i++){
			if(bins[i] != 9) return false;
		}
		return true;
		
	}
	public void printCube(){
		for(int i = 0; i<3; i++){
			System. out.println(" " + cube[5][i][0]+"" +cube [5][i][1]+""+ cube[5][i][2]);
		}
		for(int i = 0; i<3; i++){
			System. out.println(cube [0][i][0]+""+ cube[0][i][1]+ ""+cube [0][i][2]+" "+cube[1][i][0]+ ""+cube [1][i][1]+""+ cube[1][i][2]+ " "+cube[2][i][0]+ ""+cube [2][i][1]+""+ cube[2][i][2]+ " "+cube[3][i][0]+ ""+cube [3][i][1]+""+ cube[3][i][2]);
		}
		for(int i = 0; i<3; i++){
			System. out.println(" " + cube[4][i][0]+"" +cube [4][i][1]+""+ cube[4][i][2]);
		}
		System. out.println("\n" );
	}
}
