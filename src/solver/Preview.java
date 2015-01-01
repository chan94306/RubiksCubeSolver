package solver;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;

public class Preview extends SurfaceView implements SurfaceHolder.Callback{
	SurfaceHolder mHolder;
	Camera mCamera;
	DrawOnTop mDrawOnTop;
	boolean mFinished;

	Preview(Context context, DrawOnTop drawOnTop) {
		super(context);

		mDrawOnTop = drawOnTop;
		mFinished = false;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		//	mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
//	        	mDrawOnTop.notifyClick();
//	        	Log.e("log", "hi");
	        }
	    });
	}

	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		Camera.Parameters mParams = mCamera.getParameters();
		
		Log.e("tag1", "preview1");
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
				public void onPreviewFrame(byte[] data, Camera camera)
				{
					if ( (mDrawOnTop == null) || mFinished )
						return;

					if (mDrawOnTop.mBitmap == null)
					{
						// Initialize the draw-on-top companion
						Camera.Parameters params = camera.getParameters();
						mDrawOnTop.mImageWidth = params.getPreviewSize().width;
						mDrawOnTop.mImageHeight = params.getPreviewSize().height;
						mDrawOnTop.mBitmap = Bitmap.createBitmap(mDrawOnTop.mImageWidth, 
								mDrawOnTop.mImageHeight, Bitmap.Config.RGB_565);
						mDrawOnTop.mRGBData = new int[mDrawOnTop.mImageWidth * mDrawOnTop.mImageHeight]; 
						mDrawOnTop.mYUVData = new byte[data.length];        			  
					}

					// Pass YUV data to draw-on-top companion
					System.arraycopy(data, 0, mDrawOnTop.mYUVData, 0, data.length);
					mDrawOnTop.invalidate();
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

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(320, 240);
		//	parameters.setPreviewFrameRate(15);
//		parameters.setSceneMode(Camera.Parameters.SCENE_MODE_NIGHT);
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}
}
