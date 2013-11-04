package com.example.cam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class CaptureWithCamera extends Activity{
	
	private Camera mCamera;
	private CameraPreview mPreview;
	private ImageButton shutterButton;
	private boolean mAutoFocus = true;
	FrameLayout frame;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	protected static final String TAG = "CaptureWithCamera";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_view);
		System.out.println("CREATE");
		
		if(safeCameraOpen() ){
			mPreview = new CameraPreview(this,mCamera);
			
			frame = (FrameLayout)findViewById(R.id.camera_preview);
			frame.addView(mPreview);
			
			initUI();	
			
		}

	}
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("PAUSE");
		
		releaseCameraAndPreview();
	}
	@Override
	protected void onResume(){
		super.onResume();
		System.out.println("RESUME");
		
		if (mCamera == null && safeCameraOpen()){
			System.out.println("Safe camera open inside the if");
			mPreview.setCamera(mCamera);
		}
	}
	private boolean safeCameraOpen() {
	    boolean cam_stat = false;
	  
	    try {
	        releaseCameraAndPreview();
	        mCamera = Camera.open();
	        mCamera.setDisplayOrientation(90);

	        cam_stat = (mCamera != null);
	    } catch (Exception e) {
	        Log.e(getString(R.string.app_name), "failed to open Camera");
	        e.printStackTrace();
	    }

	    return cam_stat;    
	}
	private void releaseCameraAndPreview() {
		if(mPreview != null)
			mPreview.setCamera(null);
	    if (mCamera != null) {
	        mCamera.release();
	        mCamera = null;
	    }
	}

	private void initUI(){
		System.out.println("init");
		
		shutterButton = (ImageButton)findViewById(R.id.shutter_button);
		shutterButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				System.out.println("CLICKED SHUTTER");
				mCamera.takePicture(null, null, mPicture);
			}
		});
	}
	
	private File getOutputMediaFile(int type){
		System.out.println("HEEEEEEERE");
		
		System.out.println( Environment.getExternalStorageState() );
		Environment.getExternalStorageState();
		
		File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"AirCapture");
		
		//if file can be found in underlying system
		if(!dir.exists()){
			if(!dir.mkdirs()){
				Log.e(TAG,"Failed to create storage directory");
				return null;
			}
		}
		
		//creating the name based on time stamp
		String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss",Locale.US).format(new Date());
		if(type == MEDIA_TYPE_IMAGE){
			
			return new File(dir.getPath() + File.separator+"IMG_"+timeStamp+".jpg");	
		}
		else
			return null;
	}
	
	private PictureCallback mPicture = new PictureCallback(){

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
			System.out.println("onPictureTaken");
			System.out.println(data.length);
			
			
			File picFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			
			if(picFile == null){
				Log.e(TAG,"Couldnt create media file; check storage permissions?");
				return;
			}
		    try {
		    	//need to write a temp file to device
		    	//storing the image on the device
		        FileOutputStream fos = new FileOutputStream(picFile);
		        fos.write(data);
		        fos.close();
		    	
		        //get image uri
		        Uri imageUri = Uri.fromFile(picFile);
		        System.out.println(imageUri);
		        
		        //start scanning for the file
		        startScannerToAddToGallery(imageUri);
		        
		        //start activity to edit the image in air capture
		        Intent intent = new Intent(CaptureWithCamera.this, PreviewImage.class);
				startActivity(intent);

		        //for this test project we will just go to another screen to save the image with data
		     } 
		    catch (FileNotFoundException e) {
		        Log.e(TAG, "File not found: " + e.getMessage());
		        e.getStackTrace();
		    } 
		    catch (IOException e) {
		          Log.e(TAG, "I/O error writing file: " + e.getMessage());
		          e.getStackTrace();
		    }
			
		}
		public void startScannerToAddToGallery(Uri capturedImagePath){
			System.out.println("adding to gallery");
			
			//start the scanner so that the image will show up when user opens gallery
		    Intent scanFileIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, capturedImagePath);
		    sendBroadcast(scanFileIntent);
		}
		
	};
	
}//end class
