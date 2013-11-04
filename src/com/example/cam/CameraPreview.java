package com.example.cam;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = null;
	private SurfaceHolder mHolder;
	private Camera mCamera;
	
	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surfaceCreated");
		try{
			mCamera.setPreviewDisplay(mHolder);
		    
			Camera.Parameters params = mCamera.getParameters();
		    List<String> focusModes = params.getSupportedFocusModes();
		    
		    if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
		    	System.out.println("HAS Continuous FOCUS");
		    	params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		    }
		    else{
		    	System.out.println("does not support continuous focus");
		    	params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		    }
		    
		    mCamera.setParameters(params);
		    
			mCamera.startPreview();
		}catch(IOException e){
			Log.d(TAG,"ERROR setting camera preview"+e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(mCamera != null)
			mCamera.stopPreview();
		
	}

	public void setCamera(Camera camera) {
		if(mCamera != null )
			mCamera.release();
		mCamera = camera;
	}

}
