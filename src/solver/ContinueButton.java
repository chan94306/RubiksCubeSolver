package solver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import andy_andrew.rubiks.R;

/**
 * CaptureButton class so that perhaps one day, the bulk of its code won't be in the SolverActivity
 * @author Andy Zhang
 */
public class ContinueButton extends ImageButton{
	public enum States {Capture, Continue};
	private States state = States.Capture;
	private int face = 0;
	
	/**
	 * Initializes a new CaptureButton
	 * @param context Context of the button; for now, always SolverActivity
	 * @param x x-coordinate of the Button on screen
	 * @param y y-coordinate of the Button on screen
	 */
	@SuppressLint("NewApi")
	public ContinueButton(Context context, int x, int y) {
		super(context);
		setImageResource(R.drawable.continue_icon);
		setBackgroundColor(Color.WHITE);

//		setBackgroundColor(Color.TRANSPARENT);
//		setBackground(null);
//		setBackgroundResource(R.drawable.camera);
		setX(x);
		setY(y);
		setAdjustViewBounds(true);
		setMaxWidth(150);
		setMaxHeight(150);
	}
}
