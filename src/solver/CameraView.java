package solver;

import grid.CameraGridView;

import java.io.IOException;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * A view that pulls data from the phone camera and displays it on screen
 * This is the main view of SolverActivity
 * Feeds data to a mDrawOnTop (TODO: Rename DrawOnTop to a more descriptive name)
 * 
 * @author Andy Zhang and that Stanford thing
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback{
	private Camera mCamera;
	//	private DrawOnTop mDrawOnTop;
	private CameraGridView mCameraGridView;
	private boolean mFinished;
	private boolean passData;

	public CameraView(Context context, CameraGridView cgv) {
		super(context);
		mCameraGridView = cgv;
		mFinished = false;
		passData = true;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		SurfaceHolder mHolder = getHolder();
		mHolder.addCallback(this);
		//	mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//	        	mDrawOnTop.notifyClick();
				//	        	Log.e("log", "hi");
			}
		});
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		Camera.Parameters mParams = mCamera.getParameters();

		//		if(mParams.isAutoExposureLockSupported()){
		//			mParams.setAutoExposureLock(true);
		//		}
		//		if(mParams.isAutoWhiteBalanceLockSupported()){
		//			mParams.setAutoWhiteBalanceLock(true);
		//		}
		//		
		//		mParams.setExposureCompensation(0);
		//		mParams.setWhiteBalance(value);
		//		
		//		Log.e("White balance", "" + mParams.getAutoWhiteBalanceLock());
		//		Log.e("exposure ", "" + mParams.getAutoExposureLock());
		//		Log.e("exposure compensation range", "" + mParams.getMinExposureCompensation() + " " + mParams.getMaxExposureCompensation());
		//		Log.e(tag, "" + mParams.get)

		mCamera.setParameters(mParams);

		try {
			mCamera.setPreviewDisplay(holder);

			// Preview callback used whenever new viewfinder frame is available
			mCamera.setPreviewCallback(new PreviewCallback() {
				public void onPreviewFrame(byte[] data, Camera camera) {
					if ((mCameraGridView == null) || mFinished )
						return;
					
					// If this is the first callback, we need to initialize everything 
					if (mCameraGridView.imageYUV == null) {
						Camera.Parameters params = camera.getParameters();
						mCameraGridView.initImageDimensions(params.getPreviewSize().width, params.getPreviewSize().height);
						mCameraGridView.imageYUV = new byte[data.length];
					}
					
					// If we need to pass new data (this is false after Capture button tapped and user selecting colors)
					if (passData) {
						System.arraycopy(data, 0, mCameraGridView.imageYUV, 0, data.length);
						// forces grid to redraw
						mCameraGridView.invalidate();
					}

					// Pass YUV data to draw-on-top companion

					//					mDrawOnTop.invalidate();

					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} 
		catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		mFinished = true;
		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		Camera.Parameters parameters = mCamera.getParameters();

		setResolution(parameters);

		//		parameters.setPreviewSize(320, 240);
		//	parameters.setPreviewFrameRate(15);
		//		parameters.setSceneMode(Camera.Parameters.SCENE_MODE_NIGHT);
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}
	
	/**
	 * Allow image data to be sent to CameraGridView
	 */
	public void enableData() {
		passData = true;
	}
	
	/**
	 * Stop image data from being sent to CameraGridView
	 */
	public void disableData() {
		passData = false;
	}

	/**
	 * Sets the resolution of the the Camera.Parameters to a resolution not too much greater than 320x240 
	 * (in terms of pixel count)
	 * @param parameters parameters of the Camera object to set resolution
	 */
	private void setResolution(Parameters parameters) {
		// original shitty resolution fixed at 320x240
		int width = UIValues.displayWidth;
		int height = UIValues.displayHeight;
		// Maximum number of pixels allowed on screen, keeping in mind that 320x240 = 76800
		int max = 85000;
		while(width*height > max){
			width = (int) (0.9*width);
			height = (int) (0.9*height);
		}

		List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
		for(int i = 0; i < previewSizes.size(); i++){
			Camera.Size s = previewSizes.get(i);
			Log.e("previewSizes" + i, ""+s.width + " "+s.height);
		}

		Log.e("resolution", "" + width + " " + height);
		parameters.setPreviewSize(320, 240);	
	}
}
