package com.example.cam;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button capture;
	private Button view;
	private static final int TAKE_PIC= 25;
	private Bitmap bmp;
	private String photoFileName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		
		System.out.println("android:"+android.os.Build.MANUFACTURER);
	}
	
	private void initUI(){
		
		capture = (Button)findViewById(R.id.capture);
		view = (Button)findViewById(R.id.view);
		
		view.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				clicked("view");
			}
		});
		
		capture.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				clicked("capture");
			}
		});
	}
	private void clicked(String id){
		if(id == "capture"){
			Toast.makeText(this, "CLICKED CAPTURE", Toast.LENGTH_SHORT).show();
			System.out.println("CLICKED 1");
			
			Intent intent = new Intent(this,CaptureWithCamera.class);
			startActivity(intent);
			
		}
		else{
			Toast.makeText(this, "CLICKED VIEW", Toast.LENGTH_SHORT).show();
			System.out.println("CLICKED 2");
			
			Intent intent = new Intent(this,ViewImagesActivity.class);
			startActivity(intent);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
