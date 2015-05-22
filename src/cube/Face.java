package cube;

/**
 * Not currently being used.
 *
 */
public class Face {
	public static final int NUM_ROWS = 3;
	public static final int NUM_COLUMNS = 3;
	private int[][] face;
	private int RGBValue;
	
	public Face(){
		this(0, 0);
	}
	
	public Face(int value, int RGBValue){
		this.RGBValue = RGBValue;
		for(int i = 0; i < NUM_COLUMNS; i++){
			for(int j = 0; j < NUM_ROWS; j++){
				setSquareValue(i, j, value);
			}
		}
	}
	
	public Face(Face f){
		setRGBValue(f.getRGBValue());
		for(int i = 0; i < NUM_COLUMNS; i++){
			for(int j = 0; j < NUM_ROWS; j++){
				setSquareValue(i, j, f.getSquareValue(i, j));
			}
		}
	}
	
	private void setSquareValue(int i, int j, int value){
		face[i][j] = value;
	}
	
	private void setRGBValue(int RGBValue){
		this.RGBValue = RGBValue;
	}
	
	public int getSquareValue(int i, int j){
		return face[i][j];
	}
	
	public int getRGBValue(){
		return RGBValue;
	}
	
	public int[][] getFace(){
		return face;
	}
}
