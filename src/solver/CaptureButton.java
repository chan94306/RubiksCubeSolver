package solver;

import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * CaptureButton class so that perhaps one day, the bulk of its code won't be in the SolverActivity
 * @author Andy Zhang
 */
public class CaptureButton extends Button{

	private int face = 0;
	
	/**
	 * Initializes a new CaptureButton
	 * @param context Context of the button; for now, always SolverActivity
	 * @param x x-coordinate of the Button on screen
	 * @param y y-coordinate of the Button on screen
	 */
	public CaptureButton(Context context, int x, int y) {
		super(context);
		setText("Capture");
		setX(x);
		setY(y);

		// Ideally, the OnClickListener is in here too, but it references too many variables
	}
}
