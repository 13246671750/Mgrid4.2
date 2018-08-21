package com.mgrid.mysqlbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sg.common.lsyBase.MyDoorEvent;
import com.sg.common.lsyBase.MyDoorUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqliteUtil {

	private Context context;
	private List<MyDoorUser> listUs=new ArrayList<>();
	
	private List<Map<String, Object>> lists=new ArrayList<>();
	private static SQLiteDatabase sql;
	

	public SqliteUtil(Context context) {

		this.context = context;
	}

	public SQLiteDatabase openorgetSql() {

		if (sql == null) {

			MySqlBase base = new MySqlBase(context);
			sql = base.getWritableDatabase();
		}

		return sql;
	}

	/**
	 * 方法增 增加用户
	 */
	public void addUserValue(MyDoorUser my) {
		ContentValues values = new ContentValues();

		values.put("UserName", my.getName());
		values.put("CardId", my.getCardid());
		values.put("UserId", my.getUid());
		values.put("PassWord", my.getPw());
		values.put("Time", my.getTime());

		if (sql != null)
			sql.insert(MySqlBase.doorTable, null, values);
	}

	/**
	 * 语法增  增加用户
	 */

	public void addUsersValues(MyDoorUser my) {

		// String sql1="insert into " + SQLITE.TABLE_NAME + " (oid, digit, mean) values
		// ('10', '13', '0.0')";
		String exe = "insert OR IGNORE into " + MySqlBase.doorTable
				+ " (UserName,CardId,UserId,PassWord,Time) values ('" + my.getName() + "','" + my.getCardid() + "','"
				+ my.getCardid() + "','" + my.getPw() + "','" + my.getTime() + "')";

		if (sql != null)
			sql.execSQL(exe);

	}
	
	
	/**
	 * 添加记录
	 * @param my
	 */

	public void addEventValue(MyDoorEvent my) {
		ContentValues values = new ContentValues();

		values.put("CardId", my.getCardid());
		values.put("Time", my.getTime());
		values.put("Event", my.getEvent());

		if (sql != null)
			sql.insert(MySqlBase.doorEventTable, null, values);
	}

	/**
	 * 查询用户
	 */

	public List<Map<String, Object>> setListValues(List<Map<String, Object>> list,List<MyDoorUser> listU) {

		
		 setvalue(list, listU);
		 return list;
	}
	
	

	
	
	
	private void setvalue(List<Map<String, Object>> list,List<MyDoorUser> listU)
	{
		list.clear();
		listU.clear();
		 if (sql != null) {
				
			 
		        Cursor cursor = sql.rawQuery("select * from "+MySqlBase.doorTable,null);
		        while(cursor.moveToNext()){
		        	
		        	
		            String name = cursor.getString(cursor.getColumnIndex("UserName"));
		            String CID = cursor.getString(cursor.getColumnIndex("CardId"));	  	            
		            String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
		            String PassWord = cursor.getString(cursor.getColumnIndex("PassWord"));
		            String Time = cursor.getString(cursor.getColumnIndex("Time"));
		            
		            MyDoorUser my=new MyDoorUser(name,CID,UserId,PassWord,Time);
		            listU.add(my);
		            
		            Map<String, Object> hh = new HashMap<String, Object>();
					hh.put("text", "用户:" +name + " 卡号:" + CID);
					list.add(hh);
		             
		        }
			 }
		
	}
	
	
	
	/**
	 * 得到用户list
	 */
	public List<MyDoorUser> getUser()
	{
		setvalue(lists,listUs);
		return listUs;
	}
	
	
	

	
	/**
	 * 得到所有记录
	 * @return
	 */
	
	public List<MyDoorEvent> getListValues() {
		List<MyDoorEvent> list = new ArrayList<MyDoorEvent>();
		Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorEventTable, null);
		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			MyDoorEvent my = new MyDoorEvent(CID, time, event);
			list.add(my);
		}
		


		return list;

	}

	
	/*
	 * 根据ID删除用户
	 */
	public void deleteValue(String str) {
		// String exe="delete from "+MySqlBase.doorTable+"where CardId="+str;
		// sql.execSQL(exe);
		sql.delete(MySqlBase.doorTable, "CardId=?", new String[] { str });

	}
	
	/*
	 * 根据得到用户
	 */
	public boolean getUserValue(String CID) {
		
		
		Cursor CS=sql.query(MySqlBase.doorTable, null, "CardId=?", new String[] { CID }, null, null, null);
		if(CS.getCount()>0)
		{
			return false;
		}else
		{
			return true;
		}

	}
	

	/*
	 * 删除所有用户
	 */
	public void cleanUserTable() {
		String exe = "delete from " + MySqlBase.doorTable;
		sql.execSQL(exe);
	}

}
