package cube;

import grid.CameraGridView;
import grid.GridView;
import grid.ImageUtilities;

import java.util.Arrays;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.util.Log;

public class CubeUtilities{

	/**
	 * Counts the number of squares with the color of the face (center piece).
	 *
	 *
	 * @param face the face of interest (0 through 5)
	 * @return the number of squares with the color of the center piece of the specified face.
	 */
	public static int countSquaresOnFace(Cube cube, int face){
		int counter = 0;
		for(int i = 0; i <3; i++){
			for(int j = 0; j<3; j++){
				if(cube.getSquare(face, i, j)  == cube.getSquare(face, 1, 1)){
                    counter++;
                }
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
	public static int countSquaresOnFace(Cube cube, int squareColor, int face){
		int counter = 0;
		for(int i = 0; i <3; i++){
			for(int j = 0; j<3; j++){
				if(cube.getSquare (face, i, j) == squareColor) counter++;
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
	public static boolean isFaceSolved(Cube cube, int face){
		if(countSquaresOnFace(cube, face) == 9) return true;
		else return false;
	}

	/**
	 * Checks if the entire cube is solved. Utilizes isFaceSolved on all 6 faces.
	 *
	 *
	 * @return true if the entire cube is solved, false otherwise.
	 */
	public static boolean isSolved(Cube cube){
		int counter = 0;
		for(int i = 0; i <6; i++){
			if(isFaceSolved(cube, i)) counter++;
		}
		if(counter == 6) return true;
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
	public static boolean isTopCornersPermutated2(Cube cube){
		int counter = 0;
		int sameColorVal = 88; //Shared color value
		int diffColorVal = 88;

        // TODO: do this with a set
		if(cube.getSquare (1, 0, 0) == cube.getSquare(0, 0, 0) && cube.getSquare(1, 0, 0) != 5){
			counter++;
			sameColorVal = cube.getSquare(1, 0, 0);
		}
		if(cube.getSquare (1, 0, 0) == cube.getSquare(5, 0, 0) &&
            cube.getSquare(1, 0, 0) != 5){
			counter++;
			sameColorVal = cube.getSquare(1, 0, 0);
		}
		if(cube.getSquare (1, 0, 0) == cube.getSquare(3, 0, 2) &&
            cube.getSquare(1, 0, 0) != 5){
			counter++;
			sameColorVal = cube.getSquare(1, 0, 0);
		}
		if(cube.getSquare (0, 0, 2) == cube.getSquare(0, 0, 0) &&
            cube.getSquare(0, 0, 2) != 5){
			counter++;
			sameColorVal = cube.getSquare(0, 0, 2);
		}
		if(cube.getSquare (0, 0, 2) == cube.getSquare(5, 0, 0) &&
            cube.getSquare(0, 0, 2) != 5){
			counter++;
			sameColorVal = cube.getSquare(0, 0, 2);
		}
		if(cube.getSquare (0, 0, 2) == cube.getSquare(3, 0, 2) &&
            cube.getSquare(0, 0, 2) != 5){
			counter++;
			sameColorVal = cube.getSquare(0, 0, 2);
		}
		if(cube.getSquare (5, 2, 0) == cube.getSquare(0, 0, 0) &&
            cube.getSquare(5, 2, 0) != 5){
			counter++;
			sameColorVal = cube.getSquare(5, 2, 0);
		}
		if(cube.getSquare (5, 2, 0) == cube.getSquare(5, 0, 0) &&
            cube.getSquare(5, 2, 0) != 5){
			counter++;
			sameColorVal = cube.getSquare(5, 2, 0);
		}
		if(cube.getSquare (5, 2, 0) == cube.getSquare(3, 0, 2) &&
            cube.getSquare(5, 2, 0) != 5){
			counter++;
			sameColorVal = cube.getSquare(5, 2, 0);
		}

		if(counter == 1){
            // change to just grab opposite value
			if(cube.getSquare (1, 0, 0) != 5 && cube.getSquare(1, 0, 0) !=
                sameColorVal) diffColorVal = cube.getSquare(1, 0, 0);
			else if (cube.getSquare (0, 0, 2) != 5 && cube.getSquare(0, 0, 2) !=
                sameColorVal) diffColorVal = cube.getSquare(0, 0, 2);
			else if (cube.getSquare (5, 2, 0) != 5 && cube.getSquare(5, 2, 0) !=
                sameColorVal) diffColorVal = cube.getSquare(5, 2, 0);

			if(sameColorVal+1 == diffColorVal || sameColorVal-3 == diffColorVal){ //Checks if diffColorVal is to the right of sameColorVal
				counter++;
				sameColorVal = diffColorVal;
				if(cube.getSquare (1, 0, 2) != 5 && cube.getSquare(1, 0, 2)
                    !=sameColorVal) diffColorVal=cube.getSquare(1, 0, 2);
				else if (cube.getSquare (5, 2, 2) != 5 && cube.getSquare(5, 2,
                    2) !=sameColorVal) diffColorVal=cube.getSquare (5, 2, 2);
				else if (cube.getSquare (2, 0, 0) != 5 && cube.getSquare(2, 0,
                    0) !=sameColorVal) diffColorVal=cube.getSquare (2, 0, 0);                          

				if(sameColorVal+1 == diffColorVal || sameColorVal-3 == diffColorVal){
					counter++;
					sameColorVal = diffColorVal;
					if(cube.getSquare (2, 0, 2) != 5 && cube.getSquare(2, 0, 2)
                        !=sameColorVal) diffColorVal=cube.getSquare (2, 0, 2);
					else if (cube.getSquare (5, 0, 2) != 5 && cube.getSquare(5,
                        0, 2) !=sameColorVal) diffColorVal=cube.getSquare (5, 0, 2);
					else if (cube.getSquare (3, 0, 0) != 5 && cube.getSquare(3,
                        0, 0) !=sameColorVal) diffColorVal=cube.getSquare (3, 0, 0);                          

					if(sameColorVal+1 == diffColorVal || sameColorVal-3 == diffColorVal){
						counter++;
						sameColorVal = diffColorVal;
						if(cube.getSquare (3, 0, 2) != 5 && cube.getSquare(3, 0,
                            2) !=sameColorVal) diffColorVal=cube.getSquare (3,
                            0, 2);
						else if (cube.getSquare (0, 0, 0) != 5 &&
                            cube.getSquare(0, 0, 0) !=sameColorVal)
                            diffColorVal=cube.getSquare (0, 0, 0);
						else if (cube.getSquare (5, 0, 0) != 5 &&
                            cube.getSquare(5, 0, 0) !=sameColorVal)
                            diffColorVal=cube.getSquare (5, 0, 0);

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
	public static boolean isTopCrossSolved(Cube cube){
		int counter = 0;
		if(cube.getSquare (5, 0, 1) == 5) counter++;
		if(cube.getSquare (5, 1, 0) == 5) counter++;
		if(cube.getSquare (5, 1, 2) == 5) counter++;
		if(cube.getSquare (5, 2, 1) == 5) counter++;
		return (counter==4);
	}

	/**
	 * Returns the number of edge pieces that are in the proper position in the second layer. Relies on center pieces only.
	 *
	 *
	 * @return an int value that should be between 0 and 4 of the number of edge pieces in the proper position in the second layer.
	 */
	public static int numSecondLayerCorrect(Cube cube){
		int counter = 0;
		if(cube.getSquare (0, 1, 0) == 0 && cube.getSquare (3, 1, 2) == 3) counter++;
		if(cube.getSquare (0, 1, 2) == 0 && cube.getSquare (1, 1, 0) == 1) counter++;
		if(cube.getSquare (1, 1, 2) == 1 && cube.getSquare (2, 1, 0) == 2) counter++;
		if(cube.getSquare (2, 1, 2) == 2 && cube.getSquare (3, 1, 0) == 3) counter++;
		return counter;
	}

	/**
	 * Checks if there are non -top face edge pieces on the top layer. For second layer solving usage
	 *
     * TODO: rename to, are second layer edges easily available on the top layer
	 * @return true if there are available edge pieces on the top layer, false otherwise.
	 */
	public static boolean isEdgesOnTopLayer(Cube cube){
		int counter = 0;
		if(cube.getSquare (5, 0, 1) !=5 && cube.getComplementaryEdgeColor(5,0,1) != 5) counter++;
		if(cube.getSquare (5, 1, 0) !=5 && cube.getComplementaryEdgeColor(5,1,0) != 5) counter++;
		if(cube.getSquare (5, 1, 2) !=5 && cube.getComplementaryEdgeColor(5,1,2) != 5) counter++;
		if(cube.getSquare (5, 2, 1) !=5 && cube.getComplementaryEdgeColor(5,2,1) != 5) counter++;
		return (counter > 0);
	}

	/**
	 * Checks if the left edge of the front face has its second layer edge piece in the correct position.
	 *
	 * @return true if the left edge is correctly positioned, false otherwise.
	 */
	public static boolean isLeftEdgeCorrect(Cube cube){
		return (cube.getSquare (0, 1, 2) == 0 && cube.getSquare(1, 1, 0) == 1);
	}

	//For usage in solving first layer corners
	public static boolean isLeftBottomCornerCorrect(Cube cube){
		return (cube.getSquare (0, 2, 2) == 0 && cube.getSquare(1, 2, 0) == 1 &&
        cube.getSquare(4, 0, 0) == 4);
	}

	public static int numFirstLayerCornersCorrect(Cube cube){
		int counter = 0;
		if(cube.getSquare (0, 2, 2) == cube.getSquare(0, 1, 1) &&
            cube.getSquare(1, 2, 0) == cube.getSquare(1, 1, 1) &&
            cube.getSquare(4, 0, 0) == 4) counter++;
		if(cube.getSquare (1, 2, 2) == cube.getSquare(1, 1, 1) &&
            cube.getSquare(2, 2, 0) == cube.getSquare(2, 1, 1) &&
            cube.getSquare(4, 0, 2) == 4) counter++;
		if(cube.getSquare (2, 2, 2) == cube.getSquare(2, 1, 1) &&
            cube.getSquare(3, 2, 0) == cube.getSquare(3, 1, 1) &&
            cube.getSquare(4, 2, 2) == 4) counter++;
		if(cube.getSquare (3, 2, 2) == cube.getSquare(3, 1, 1) &&
            cube.getSquare(0, 2, 0) == cube.getSquare(0, 1, 1) &&
            cube.getSquare(4, 2, 0) == 4) counter++;
		return counter;
	}

	//for first layer solving
	public static boolean isCornersOnTopLayer(Cube cube){
		if(countSquaresOnFace(cube, 4, 5) > 1) return true;
		for(int i = 0; i <3; i++){
			if(cube.getSquare(i, 0, 0) == 4 || cube.getSquare(i, 0, 2) == 4) return true;
		}
		return false;
	}

	public static boolean isFirstLayerCrossSolved(Cube cube){
		int counter = 0;
		if(cube.getSquare (4, 0, 1) == 4 && cube.getSquare(1, 2, 1) ==
            cube.getSquare(1, 1, 1)) counter++;
		if(cube.getSquare (4, 1, 0) == 4 && cube.getSquare(0, 2, 1) ==
            cube.getSquare(0, 1, 1)) counter++;
		if(cube.getSquare (4, 1, 2) == 4 && cube.getSquare(2, 2, 1) ==
            cube.getSquare(2, 1, 1)) counter++;
		if(cube.getSquare (4, 2, 1) == 4 && cube.getSquare(3, 2, 1) ==
            cube.getSquare(3, 1, 1)) counter++;
		return (counter==4);
	}

	public static boolean isFirstLayerEdgeCorrect(Cube cube){
		return (cube.getSquare(4, 0, 1) == 4 && cube.getSquare(1, 2, 1) == 1);
	}

	public static boolean isFirstLayerEdgesEasilyAvailable(Cube cube){
		if(cube.getSquare(5, 0, 1) == 4 || cube.getSquare(5, 1, 0) == 4 ||
            cube.getSquare(5, 1, 2) == 4 || cube.getSquare(5, 2, 1) == 4) return true;
		if(cube.getSquare(0, 1, 2) == 4 || cube.getSquare(2, 1, 0) == 4) return true;
		// 	for(int i = 0; i <3; i++){
		// 	if(cube[i][1][0] == 4 || cube[i][1][2] == 4) return true;
		// }
		else return false;		

	}



	/**
	 * Checks to see if two colors are on the same edge piece, when specified a specific square on the cube.
	 * Only works for the top layer (face 5) and edges on the front layer.
	 * @param face integer value 0 through 5 of the face the square is on
	 * @param i integer value, 0 through 2, [i][j] of a 2D array (y value)
	 * @param j integer value, 0 through 2, [i][j] of a 2D array (x value)
	 * @param colorA integer value 0 through 5 of one of the colors on the edge piece.
	 * @param colorB integer value 0 through 5 of the other color on the edge piece.
	 * @return true if the two specified colors are on the edge piece indicated, false otherwise.
	 */
	public static boolean areComplementaryCornerColors(Cube cube, int face, int i, int j, int colorA, int colorB){
		int[] Colors = new int[2];
		//Assuming face 4 on the top face
		if(face ==5 && i == 0 && j == 0){
			Colors[0] = cube.getSquare(0, 0, 0);
			Colors[1] = cube.getSquare(3, 0, 2);
		}
		else if(face ==5 && i == 0 && j == 2){
			Colors[0] = cube.getSquare(3, 0, 0);
			Colors[1] = cube.getSquare(2, 0, 2);
		}
		else if(face ==5 && i == 2 && j == 2){
			Colors[0] = cube.getSquare(2, 0, 0);
			Colors[1] = cube.getSquare(1, 0, 2);
		}
		else if(face ==5 && i == 2 && j == 0){
			Colors[0] = cube.getSquare(1, 0, 0);
			Colors[1] = cube.getSquare(0, 0, 2);
		}
		//Assuming face 4 is on the 0,0 face
		else if(face == 0 && i == 0 && j == 0){
			Colors[0] = cube.getSquare(3, 0, 2);
			Colors[1] = cube.getSquare(5, 0, 0);
		}
		else if(face == 1 && i == 0 && j == 0){
			Colors[0] = cube.getSquare(0, 0, 2);
			Colors[1] = cube.getSquare(5, 2, 0);
		}
		else if(face == 2 && i == 0 && j == 0){
			Colors[0] = cube.getSquare(1, 0, 2);
			Colors[1] = cube.getSquare(5, 2, 2);
		}
		else if(face == 3 && i == 0 && j == 0){
			Colors[0] = cube.getSquare(2, 0, 2);
			Colors[1] = cube.getSquare(5, 0, 2);
		}
		//Assuming face 4 is on the 0,2 face
		else if(face == 0 && i == 0 && j == 2){
			Colors[0] = cube.getSquare(1, 0, 0);
			Colors[1] = cube.getSquare(5, 2, 0);
		}
		else if(face == 1 && i == 0 && j == 2){
			Colors[0] = cube.getSquare(2, 0, 0);
			Colors[1] = cube.getSquare(5, 2, 2);
		}
		else if(face == 2 && i == 0 && j == 2){
			Colors[0] = cube.getSquare(5, 0, 2);
			Colors[1] = cube.getSquare(3, 0, 0);
		}
		else if(face == 3 && i == 0 && j == 2){
			Colors[0] = cube.getSquare(0, 0, 0);
			Colors[1] = cube.getSquare(5, 0, 0);
		}

		//for first layer
		else if(face == 0 && i == 2 && j ==2){
			Colors[0] = cube.getSquare(1, 2, 0);
			Colors[1] = cube.getSquare(4, 0, 0);
		}
		else if(face == 1 && i == 2 && j==0){
			Colors[0] = cube.getSquare(0, 2, 2);
			Colors[1] = cube.getSquare(4, 0, 0);
		}

		if((colorA == Colors[0] || colorA == Colors[1]) && (colorB == Colors[0] || colorB == Colors[1])) return true;
		else return false;

	}
	
	/**
	 * Checks if the newly read cube is valid (has 9 of each color)
	 * @return true if the cube has 9 of each color, false otherwise
	 */
	public static boolean isValidCube(Cube cube){
		int[] bins = new int[6];
		for(int f = 0; f<6; f++){
			for(int i = 0; i<3; i++){
				for(int j = 0 ; j<3; j++){
					bins[cube.getSquare(f, i, j)]++;
				}
			}
		}
		
		for(int i = 0; i < bins.length; i++){
			if(bins[i] != 9) return false;
		}
		return true;
		
	}
}
