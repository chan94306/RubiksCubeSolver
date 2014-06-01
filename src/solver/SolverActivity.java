package solver;

import cube.Cube;
import cube.Main;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

// THIS IS THE MAIN ACTIVITY

public class SolverActivity extends Activity{    
	private Preview mPreview;
	private DrawOnTop mDrawOnTop;
	private RubiksAlgorithm mRubiksAlgorithm;

	private final ColorToggleButton[][] colorToggles = new ColorToggleButton[3][3];
	private TextView dialog;
	private Button captureButton, skipButton;
	private Handler mHandler;

	private Cube current = new Cube(), future = new Cube();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//		displayInstructions(false);
		//		displayInstructions(true);
		//		displayInstructions(5, false);

		// Hide the window title.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Get some information about the display
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int displayWidth = size.x;
		int displayHeight = size.y;

		// Creates some handler shit
		// Defines a Handler object that's attached to the UI thread
		mHandler = new Handler(Looper.getMainLooper()) {
			/*
			 * handleMessage() defines the operations to perform when
			 * the Handler receives a new Message to process.
			 */
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				Toast.makeText(getApplicationContext(), bundle.getString("Instruction message"), Toast.LENGTH_SHORT).show();
				//				dialog.setText(bundle.getString("Instruction message"));

			}
		};
		

		// Initializes the general-purposes dialog box
		dialog = new TextView(this);
		dialog.setTextColor(Color.RED);
		dialog.setText("Follow rotation instructions, then tap to read face");
		dialog.setX(50);
		dialog.setY(50);
		dialog.setTextSize(displayHeight/30);

		// Create our Preview view and set it as the content of our activity.
		// Create our DrawOnTop view.
		mDrawOnTop = new DrawOnTop(this, current, future);
		mPreview = new Preview(this, mDrawOnTop);
		mRubiksAlgorithm = new RubiksAlgorithm(dialog, current, future, mDrawOnTop, mHandler, getApplicationContext());



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
		captureButton.setY(size.y - 100);



		captureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(captureButton.getText().equals("Capture")){
					//					new RubiksAlgorithm(dialog, current, future, mDrawOnTop, getApplicationContext()).execute();


					//					Log.e("phase" ,"" + mDrawOnTop.getPhase());

					//					mDrawOnTop.notifyCapture();
					mDrawOnTop.readFace(mDrawOnTop.getPhase());
					enableColorSelection(mDrawOnTop.getPhase());
					captureButton.setText("Continue");
				}else{
					//					mDrawOnTop.notifyContinue();
					disableColorSelection(mDrawOnTop.getPhase());

					if(mDrawOnTop.getPhase() == 5){
						//						mDrawOnTop.debugCubeColors();
						mDrawOnTop.recompileCubeColors();
						if(!current.isValidCube()){
							mDrawOnTop.setPhase(0);
							captureButton.setText("Capture");
							current = new Cube();
							future = new Cube();
							Toast.makeText(getApplicationContext(), "You screwed up. Reread cube faces.", Toast.LENGTH_SHORT).show();
							return;
						}
						//						mDrawOnTop.debugCubeInts();
						captureButton.setVisibility(Button.INVISIBLE);
						skipButton.setVisibility(Button.VISIBLE);
						dialog.setText("");

						mRubiksAlgorithm.execute();
					}
					mDrawOnTop.displayInstructionsToast(mDrawOnTop.getPhase());		// can't access canvas so screw it
					mDrawOnTop.incrementPhase();
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

		// The rest is all for creating 9 damn buttons -----------------

		int squareLength;
		int leftBound, topBound;

		//		Log.e("dims", "" + mImageWidth + " " + mImageHeight);		// 320, 240

		if(displayWidth < displayHeight){
			squareLength = (int)(displayWidth/3.0);
			leftBound = 0;
			topBound = (int)(displayHeight/2.0 - 1.5*squareLength);
		}
		// else flush by height and offset width
		else{
			squareLength = (int)(displayHeight/3.0);
			leftBound = (int)(displayWidth/2.0 - 1.5*squareLength);
			topBound = 0;
		}

		// CHECK TO MAKE SURE INVISIBLE BUTTONS CAN'T BE PRESSED
		for(int i = 0; i < colorToggles.length; i++){
			for(int j = 0; j < colorToggles[i].length; j++){
				colorToggles[i][j] = new ColorToggleButton(this);
				colorToggles[i][j].setVisibility(Button.INVISIBLE);
				colorToggles[i][j].setX(leftBound + squareLength*j);
				colorToggles[i][j].setY(topBound + squareLength*i);
				addContentView(colorToggles[i][j], new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				// ONCLICKLISTENER IS ALREADY SET IN CLASS CONSTRUCTOR
			}
		}

	}

	///----------------///




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

}
