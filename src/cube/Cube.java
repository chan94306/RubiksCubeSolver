package cube;

import grid.CameraGridView;
import grid.GridView;
import grid.ImageUtilities;

import java.util.Arrays;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.util.Log;

public class Cube implements Cloneable{
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
	
	private int [][][] cube = new int[6][3][3];

	private int leftColor , frontColor , rightColor , backColor , topColor , bottomColor = 0;
	private int [] RGBColors = {leftColor , frontColor , rightColor , backColor , topColor, bottomColor};
	public static final boolean CW = true; //Clockwise
	public static final boolean CCW = false; //Counterclockwise

	@SuppressLint("UseSparseArrays")
    // colorMap is a map for RGB value to 0-5.
	private HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
    // when there is a full cube rotation, because 0 is always on the left, 1
    // facing user, etc, all int values need to be remapped
	private HashMap<Integer, Integer> faceMap = new HashMap<Integer, Integer>();

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

	// Copy constructor
	public Cube(Cube c){
		for(int f = 0; f<6; f++){
			for(int i = 0; i <3; i++){
				for(int j = 0; j<3; j++){
					setSquare(f, i, j, c.getSquare(f, i, j));

				}
			}
			setColorValue(f, getColorValue(f));
		}
	}
	
	/**
	 * @author Andy
	 * Sets the colors of the specified face to to colors in the GridView
	 * Sets the the color of the face to be the color of the middle cell of grid
	 * TODO: Throw an error if the GridView has an improper color for a cell, eg. Color.BLACK
	 * Originally this was done in DrawOnTop.readFace((int) face, (Cube) current)
	 * TODO: make sure i, j are correct in for loops
	 * @param face Face of the cube [0, 5]
	 * @param grid GridView object, hopefully with ideal colors for all 9 cells
	 */
	public void setFaceColors(int face, GridView grid) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				setSquare(face, i, j, grid.getFillColor(i, j));
			}
		}
		setColorValue(face, grid.getFillColor(1, 1));
	}
	
	/**
	 * This is NOT the same as Cube.realignFaces();
	 * Turns the semi-raw Color values + orange into integers 0 to 5, 
	 * the final step before solving
	 * @param cube the Cube to set the color data
	 */
	@SuppressLint("UseSparseArrays")
	public void convertColorsToInts() {
		for (int face = 0; face < 6; face++) {
			colorMap.put(getColorValue(face), face);
		}

		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					setSquare(face, i, j, colorMap.get(getSquare(face, i, j)));
				}
			}
		}
	}
	
	// TODO: To be completed/Does this really belong in Cube?
	public int[][] update(CameraGridView cgv) {
		int[][] newFace = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				newFace[i][j] = colorMap.get(ImageUtilities.getIdealColor(cgv.getFillColor(i, j)));
			}
		}
		// return a 2D array of the face 1 (the face pointed at camera) based on
		// what's currently on the cube
		// the 2D array will consist of all numbers 0-6, not the RGB value
		// use getColorValue and cleverness to convert from approximate RGB
		// value to face int value
		return newFace;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Cube [cube=" + Arrays.toString(cube) + ", RGBColors="
				+ Arrays.toString(RGBColors) + ", colorMap=" + faceMap + "]";
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(RGBColors);
		result = prime * result
				+ ((faceMap == null) ? 0 : faceMap.hashCode());
		result = prime * result + Arrays.hashCode(cube);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Cube)) {
			return false;
		}
		Cube other = (Cube) obj;
		if (!Arrays.equals(RGBColors, other.RGBColors)) {
			return false;
		}
		if (faceMap == null) {
			if (other.faceMap != null) {
				return false;
			}
		} else if (!faceMap.equals(other.faceMap)) {
			return false;
		}
		if (!Arrays.deepEquals(cube, other.cube)) {
			return false;
		}
		return true;
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

	/**
	 * 
	 * 
	 * TODO:Needs a more descriptive name!
	 */
	public void realignFaces() {
		faceMap = new HashMap<Integer, Integer>();

		for(int face = 0; face < 6; face++){
			faceMap.put(cube[face][1][1], face);
		}

		Log.e("tag", faceMap.toString());

		for(int face = 0; face < 6; face++){
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					cube[face][i][j] = faceMap.get(cube[face][i][j]);

				}
			}
		}

	}

// Just Algorithm Things
	/*-------------------------------------------------------------------------------------------------------------------------*/

	


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
		case 0: return leftColor;
		case 1: return frontColor;
		case 2: return rightColor;
		case 3: return backColor;
		case 4: return bottomColor;
		case 5: return topColor;
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
	
	/**
	 * Returns the color of the other side of an edge piece. Only works for the top layer (face = 5)
	 * @param face The face of interest. For purposes of this function, face always = 5
	 * @param i The integer value 0 through 2 of [i][j] in a 2D array (y value)
	 * @param j The integer value 0 through 2 of [i][j] in a 2D array (x value)
	 * @return Returns the integer representation for the color value on the other side of the edge piece.
	 */
	public int getComplementaryEdgeColor(int face, int i, int j){
		if(face == 5 && i == 2 && j == 1) return getSquare(1, 0, 1);
		if(face == 5 && i == 1 && j == 0) return getSquare(0, 0, 1);
		if(face == 5 && i == 1 && j == 2) return getSquare(2, 0, 1);
		if(face == 5 && i == 0 && j == 1) return getSquare(3, 0, 1);

		//face 1
		if(face == 1 && i == 2 && j == 1) return getSquare(4, 0, 1); //technically not needed
		if(face == 1 && i == 1 && j == 0) return getSquare(0, 1, 1);
		if(face == 1 && i == 1 && j == 2) return getSquare(2, 1, 1);
		if(face == 1 && i == 0 && j == 1) return getSquare(5, 2, 1);

		



		else return 88;
	}
}
