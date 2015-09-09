package solver;

import grid.CameraGridView;
import cube.Cube;
import colorpalette.*;
import colortogglebutton.ColorToggleButton;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Launched by the start screen. This solves the cube.
 * Models:
 * Views:
 * Controllers:
 * @author Andy Zhang
 *
 */
public class SolverActivity extends Activity{    
	
	private CameraView mCameraView;
	private CameraGridView mCameraGridView;
	private RubiksAlgorithm mRubiksAlgorithm;
	private ArrowManager mArrowManager;

	private final ColorToggleButton[][] colorToggles = new ColorToggleButton[3][3];
	private ColorPalette mColorPalette;
	private TextView dialog;
	private Button captureButton, skipButton;
	private Handler mHandler;

	private int face = 0;

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
//		mDrawOnTop = new DrawOnTop(this);
		mColorPalette = new ColorPalette(this);
		mCameraGridView = new CameraGridView(this, mColorPalette);
		mCameraView = new CameraView(this, mCameraGridView);
		mRubiksAlgorithm = new RubiksAlgorithm(current, future, mCameraGridView);
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
		captureButton = new CaptureButton(this, size.x - 200, 50);
		captureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(captureButton.getText().equals("Capture")){
					mCameraView.disableData();
					mCameraGridView.disableUpdatePaints();
					mCameraGridView.updateToIdealColors();
//					mCameraGridView.readFace(face, current);
					
					enableColorPalette(face);
					captureButton.setText("Confirm");
				}else{ // User taps "Confirm"
					// 
					current.setFaceColors(face, mCameraGridView);
					disableColorPalette(face);
					mCameraView.enableData();
					mCameraGridView.enableUpdatePaints();

					if(face == 5){
						//						mDrawOnTop.debugCubeColors();
						
//						current.reMap();	// Incorrectly replaces the next line. See DrawOnTop.recompileCubeColors and Cube.reMap
						current.convertColorsToInts();
						
						/*
						 //TODO: Re-enable this error check for user! Double check to make sure everything is reset
						 if(!current.isValidCube()){
						 	mDrawOnTop.setPhase(0);
						 	captureButton.setText("Capture");
						 	current = new Cube();
						 	future = new Cube();
						 	Toast.makeText(getApplicationContext(), "You screwed up. Reread cube faces.", Toast.LENGTH_SHORT).show();
						 	return;
						 }
//						mDrawOnTop.debugCubeInts();
						 */
						captureButton.setVisibility(Button.INVISIBLE);
						skipButton.setVisibility(Button.VISIBLE);
						dialog.setText("");
						dialog.setVisibility(View.INVISIBLE);

						mRubiksAlgorithm.execute();
					}
					displayInstructions(face);
					face++;
					captureButton.setText("Capture");

				}
			}

		});

		setContentView(mCameraView);
		
		LayoutParams wrapContent = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		addContentView(captureButton, wrapContent);
		addContentView(skipButton, wrapContent);
		addContentView(dialog, wrapContent);
		addContentView(mColorPalette, wrapContent);
		
		addContentView(mCameraGridView, wrapContent);
		
		mCameraGridView.addCellsToActivity(this);
		mArrowManager.initializeArrows();

		// Defines a Handler object that's attached to the UI thread
		mHandler = new SolverActivityHandler(this, Looper.getMainLooper(), mArrowManager);
		mRubiksAlgorithm.setHandler(mHandler);
	}
	
	/**
	 * Makes the Color Palette visible, allowing users to modify the colors of the CameraGridView
	 * @param face TODO: remove this argument if unnecessary
	 */
	public void enableColorPalette(int face){
		mColorPalette.setPaletteVisibility(View.VISIBLE);
	}

	/**
	 * Makes the Color Palette visible, preventing users from modifying the colors of the CameraGridView
	 * @param face TODO: remove this argument if unnecessary
	 */
	public void disableColorPalette(int face){
		mColorPalette.setPaletteVisibility(View.INVISIBLE);
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
