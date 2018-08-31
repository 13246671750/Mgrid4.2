package com.mgrid.mysqlbase;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class MySqlBase extends SQLiteOpenHelper{
	
	
	public static  String dbName="Mgrid.db";
	public static  final String doorTable="Door_T";
	public static  final String doorEventTable="Event_T";
	public static  final String doorXuNiEventTable="XuNiEvent_T";
	private static final String  dbPath="SQL";
	private static final String DB_PATH=Environment.getExternalStorageDirectory() + File.separator + dbPath + File.separator;

	private static  final int    dbVersion=2;
	

	public MySqlBase(Context context) {
		
		super(context, dbName, null, dbVersion);
		
	}
	
	
	 static{
	        //�ж�sd���Ƿ���������
	        if(! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	        	dbName = "Mgrid.db";//sd��δ��������,�����ݿ⻹������Ĭ��λ��
	        }else{
	        	dbName = DB_PATH + "Mgrid.db";
	            File parentFile = new File(DB_PATH);

	            // Ŀ¼���������Զ�����Ŀ¼
	            if (!parentFile.exists()){
	            	parentFile.mkdirs();
	            }
	        }
	    }

	

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String doorManagerTable="create table if not exists "+doorTable+" (UserName text ,CardId text primary key,UserId text,PassWord text,Time text) ";
		String doorEvent="create table if not exists "+doorEventTable+" (id integer primary key autoincrement,CardId text,Time text,Event text,CheckData integer) ";
		String doorXuNiEvent="create table if not exists "+doorXuNiEventTable+" (id integer primary key autoincrement,CardId text,Time text,Event text,EventResult text,CheckData integer) "; 
		db.execSQL(doorManagerTable);
		db.execSQL(doorEvent);
		db.execSQL(doorXuNiEvent);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
		switch (newVersion) {
		case 1:
			
		
			
			break;
			
        case 2:
			
        	String doorXuNiEvent="create table if not exists "+doorXuNiEventTable+" (id integer primary key autoincrement,CardId text,Time text,Event text,EventResult text,CheckData integer) "; 
        	db.execSQL(doorXuNiEvent);
			
			break;
			

		default:
			break;
		}
		
	}

	
	
}
