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
public class CaptureButton extends ImageButton{
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
	public CaptureButton(Context context, int x, int y) {
		super(context);
		setImageResource(R.drawable.camera_icon);
//		setBackgroundColor(Color.TRANSPARENT);
//		setBackground(null);
//		setBackgroundResource(R.drawable.camera);
		setX(x);
		setY(y);
		setAdjustViewBounds(true);
		setMaxWidth(150);
		setMaxHeight(150);

		// Ideally, the OnClickListener is in here too, but it references too many variables
	}
	
	public CaptureButton.States getState() {
		return state;
	}
	
	public void toggleState() {
		if (state == States.Capture) {
			state = States.Continue;
			setImageResource(R.drawable.camera_icon);
		} else {
			state = States.Capture;
			setImageResource(R.drawable.continue_icon);
		}
//		invalidate();
//		forceLayout();
	}
}
