package solver;

import java.util.HashMap;
import cube.Cube;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * View that displays some random shit but isn't used anymore
 * @author Andy Zhang
 *
 */
@Deprecated
public class DrawOnTop extends View {

	public DrawOnTop(Context context) {
		super(context);

	}

	@Override
	protected void onDraw(Canvas canvas) {

	}

	public void debugCubeColors(Cube cube) {
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < 3; i++) {
				String msg = "";
				for (int j = 0; j < 3; j++) {
					msg += cube.getSquare(face, i, j) + ",";
				}
				Log.e("Face: " + face, msg);
			}
		}
	}

	public void debugCubeInts(Cube cube) {
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < 3; i++) {
				String msg = "";
				for (int j = 0; j < 3; j++) {
					msg += cube.getSquare(face, i, j) + ",";

				}
				Log.e("Face: " + face, msg);
			}
		}
	}
}