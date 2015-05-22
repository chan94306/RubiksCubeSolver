package colortogglebutton;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

@SuppressLint("UseSparseArrays")
public class ColorToggleButton extends Button{

	private static final HashMap<String, Integer> toState = new HashMap<String, Integer>();
	private static final HashMap<Integer, String> toColor = new HashMap<Integer, String>();
	private int state = 0;

	public ColorToggleButton(Context context) {
		super(context);

		setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// change the color string displayed, the state variable, and the color of the cubesquare
				incrementState();
				setText(getColor());
			}
		});

		toState.put("Red", 0);
		toState.put("Orange", 1);
		toState.put("Yellow", 2);
		toState.put("Green", 3);
		toState.put("Blue", 4);
		toState.put("White", 5);

		toColor.put(0, "Red");
		toColor.put(1, "Orange");
		toColor.put(2, "Yellow");
		toColor.put(3, "Green");
		toColor.put(4, "Blue");
		toColor.put(5, "White");
	}

	public void incrementState(){
		state = (state+1)%6;
	}

	public static int getState(String color){
		return toState.get(color);
	}

	public static String getColor(int state){
		return toColor.get(state);
	}

	public String getColor(){
		return toColor.get(state);
	}

	public void setState(int color){
		switch(color){
		case -65536: state = 0;			// red
		return;
		case 16746496: state = 1;		// orange
		return;
		case -256: state = 2;			// yellow
		return;
		case -16711936: state = 3;		// green
		return;
		case -16776961: state = 4;		// blue
		return;
		case -1: state = 5;				// white
		return;
		}
	}

	public int getRawColorInt(){
		switch(state){
		case 0: return -65536;
		case 1: return 16746496;
		case 2: return -256;
		case 3: return -16711936;
		case 4: return -16776961;
		case 5: return -1;
		default: return 11111;// should never happen
		}
	}

}
