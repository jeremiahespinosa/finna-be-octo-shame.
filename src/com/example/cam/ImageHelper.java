package com.example.cam;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * 
 * ImageHelper will maintain the database and 
 * save the image data to the sqlite database
 * 
 */
public class ImageHelper {
	
	private SQLiteDatabase database;
	private MySQL_LiteHelper dbHelper;
	private String[] allColumns = {MySQL_LiteHelper.COLUMN_ID,
								   MySQL_LiteHelper.COLUMN_NAME,
								   MySQL_LiteHelper.COLUMN_DESCRIPTION};
	public ImageHelper(Context context){
		dbHelper = new MySQL_LiteHelper(context);
		
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void open(){
		database = dbHelper.getWritableDatabase();
	}
	
	public Image createImageInTable(String name,String desc){
		System.out.println("In CreateImageInTable");
		ContentValues values = new ContentValues();
		values.put(MySQL_LiteHelper.TABLE_IMAGES, name);
		values.put(MySQL_LiteHelper.TABLE_IMAGES, desc);
		
	    long insertId = database.insert(MySQL_LiteHelper.TABLE_IMAGES, null,
	            values);
	    Cursor cursor = database.query(MySQL_LiteHelper.TABLE_IMAGES, allColumns ,MySQL_LiteHelper.COLUMN_ID + " = "+insertId, null, null, null, null, null);
	    
	    System.out.println(cursor);
	    
	    cursor.moveToFirst();
	    Image newImage = cursorToImage(cursor);
	    cursor.close();
	   
		return newImage;	
	}

	private Image cursorToImage(Cursor cursor) {
		System.out.println("INSIDE cursorToImage");
		
		Image image = new Image();
		image.setId(cursor.getLong(0));
		image.setName(cursor.getString(1));
		image.setDescription(cursor.getString(1));
		
		System.out.println(image);
		return image;
	}
	
	public List<Image>getAllImages(){
		System.out.println("INSIDE getAllImages");
		
		List<Image> images = new ArrayList<Image>();
		
		Cursor cursor = database.query(MySQL_LiteHelper.TABLE_IMAGES, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Image image = cursorToImage(cursor);
			images.add(image);
			cursor.moveToNext();
		}
		cursor.close();
		System.out.println(images);
		
		return images;
	}
}
