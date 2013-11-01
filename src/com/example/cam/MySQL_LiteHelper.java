package com.example.cam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQL_LiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_IMAGES = "images";
  	public static final String COLUMN_ID = "_id";
  	public static final String COLUMN_DESCRIPTION = "description";
  	public static final String COLUMN_NAME ="name";
	
	private static final String DATABASE_NAME = "images.db";
	private static final int DATABASE_VERSION = 1;
	  
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
      + TABLE_IMAGES+ "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_NAME + " text, "
      + COLUMN_DESCRIPTION + " text);";
  
	public MySQL_LiteHelper(Context context){
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		//System.out.println("UP IN HERE:"+sTitle+" "+sDesc);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("CREATING DB");
		db.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	    Log.w(MySQL_LiteHelper.class.getName(),
	            "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
	        onCreate(db);
	}

}
