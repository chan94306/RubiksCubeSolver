package solver;

import cube.Cube;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import andy_andrew.rubiks.R;

/**
 * Launched by the start screen. This solves the cube
 * @author andy
 *
 */
public class SolverActivity extends Activity{    
	
//	public int displayWidth, displayHeight;
//	public static int topBound, leftBound, squareLength;
//	public static final double GRID_PROPORTION = 0.5;
	
	private CameraView mPreview;
	private DrawOnTop mDrawOnTop;
	private RubiksAlgorithm mRubiksAlgorithm;
	private ArrowManager mArrowManager;

	private final ColorToggleButton[][] colorToggles = new ColorToggleButton[3][3];
	private TextView dialog;
	private ImageView arrowImage;
	private Button captureButton, skipButton;
	private Handler mHandler;

	private int phase = 0;

	private Cube current = new Cube(), future = new Cube();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Hide the window title.
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		View mDecorView = getWindow().getDecorView();
		mDecorView.setSystemUiVisibility(
	            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
	            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
	            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//		
		// Get some information about the display
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		UIValues.displayWidth = size.x;
		UIValues.displayHeight = size.y;
		UIValues.GRID_PROPORTION = 0.7;
		UIValues.init();
		
		ColorPalette mColorPalette = new ColorPalette(this);
		

		// Initializes the general-purposes dialog box
		dialog = new TextView(this);
		dialog.setTextColor(Color.BLACK);
		dialog.setText("Follow rotation instructions. Tap 'Capture' to capture colors");
		dialog.setBackgroundColor(Color.WHITE);
		dialog.setAlpha((float) 0.7);
		dialog.setX(50);
		dialog.setY(50);
		dialog.setTextSize(UIValues.displayHeight/40);

		// Create our DrawOnTop view.
		// Create our Preview view and set it as the content of our activity.
		mDrawOnTop = new DrawOnTop(this, current);
		mPreview = new CameraView(this, mDrawOnTop);
		mRubiksAlgorithm = new RubiksAlgorithm(current, future, mDrawOnTop);
		mArrowManager = new ArrowManager(this);

		// Initializes the skip step button
		skipButton = new Button(this);
		skipButton.setText("I swear I did this step correctly");
		skipButton.setY(size.y - 100);
		skipButton.setX(size.x - 500);
		skipButton.setVisibility(Button.INVISIBLE);
		skipButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mRubiksAlgorithm.enableForceSkip();
			}
		});

		// Initializes the capture button
		captureButton = new Button(this);
		captureButton.setText("Capture");
		captureButton.setX(size.x - 200);
		captureButton.setY(50);
		captureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(captureButton.getText().equals("Capture")){
					//					new RubiksAlgorithm(dialog, current, future, mDrawOnTop, getApplicationContext()).execute();
					//					Log.e("phase" ,"" + phase;
					//					mDrawOnTop.notifyCapture();
					mDrawOnTop.readFace(phase);
					enableColorSelection(phase);
					captureButton.setText("Continue");
				}else{
					//					mDrawOnTop.notifyContinue();
					disableColorSelection(phase);

					if(phase == 5){
						//						mDrawOnTop.debugCubeColors();
						mDrawOnTop.recompileCubeColors();
						// if(!current.isValidCube()){
						// 	mDrawOnTop.setPhase(0);
						// 	captureButton.setText("Capture");
						// 	current = new Cube();
						// 	future = new Cube();
						// 	Toast.makeText(getApplicationContext(), "You screwed up. Reread cube faces.", Toast.LENGTH_SHORT).show();
						// 	return;
						// }
						//						mDrawOnTop.debugCubeInts();
						captureButton.setVisibility(Button.INVISIBLE);
						skipButton.setVisibility(Button.VISIBLE);
						dialog.setText("");
						dialog.setVisibility(View.INVISIBLE);

						mRubiksAlgorithm.execute();
					}
					displayInstructions(phase);
					phase++;
					captureButton.setText("Capture");

				}
				//				Log.e("button", "pressed");
			}

		});

		setContentView(mPreview);
		addContentView(mDrawOnTop, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(captureButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(skipButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(dialog, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(mColorPalette, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		mColorPalette.addSelf();
		mArrowManager.initializeArrows();

		for(int i = 0; i < colorToggles.length; i++){
			for(int j = 0; j < colorToggles[i].length; j++){
				colorToggles[i][j] = new ColorToggleButton(this);
				colorToggles[i][j].setVisibility(Button.INVISIBLE);
				colorToggles[i][j].setX(UIValues.leftBound + UIValues.squareLength*j);
				colorToggles[i][j].setY(UIValues.topBound + UIValues.squareLength*i);
				addContentView(colorToggles[i][j], new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
		}
		
		// Defines a Handler object that's attached to the UI thread
		mHandler = new SolverActivityHandler(this, Looper.getMainLooper(), mArrowManager);
		mRubiksAlgorithm.setHandler(mHandler);
	}
	
	public void enableColorSelection(int face){
		for(int i = 0; i < colorToggles.length; i++){
			for(int j = 0; j < colorToggles[i].length; j++){
				colorToggles[i][j].setState(current.getSquare(face, i, j));
				//				Log.e("cgs", "" +current.getSquare(face, i, j));
				colorToggles[i][j].setText(colorToggles[i][j].getColor());
				colorToggles[i][j].setVisibility(Button.VISIBLE);
			}
		}
	}

	public void disableColorSelection(int face){
		for(int i = 0; i < colorToggles.length; i++){
			for(int j = 0; j < colorToggles[i].length; j++){
				current.setSquare(face, i, j, colorToggles[i][j].getRawColorInt());				
				colorToggles[i][j].setVisibility(Button.INVISIBLE);
			}
		}

		current.setColorValue(face, colorToggles[1][1].getRawColorInt());
		//		Log.e("face color", "" + colorToggles[1][1].getRawColorInt());
	}
	
	public void displayInstructions(int face){
		mArrowManager.clearArrows();
		
		String s;
		if(face < 3){
			s = "Rotate whole cube LEFT";
			mArrowManager.displayArrow_Cube(ArrowManager.LEFT);
		}else if(face == 3){
			s = "Rotate whole cube LEFT TWICE, then UP";
			mArrowManager.displayArrow_Cube(ArrowManager.LEFT);
		}else if(face == 4){
			s = "Rotate whole cube DOWN TWICE";
			mArrowManager.displayArrow_Cube(ArrowManager.DOWN);
		}else if(face == 5){
			s = "Rotate whole cube UP. Ready to solve!";
			mArrowManager.displayArrow_Cube(ArrowManager.UP);
		}else{
			s = "WTF something went wrong";
		}
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}

}
