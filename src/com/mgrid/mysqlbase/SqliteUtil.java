package com.mgrid.mysqlbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mgrid.main.user.UserEvent;
import com.sg.common.lsyBase.MyDoorEvent;
import com.sg.common.lsyBase.MyDoorUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
	 * ������ �����û�
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
	 * �﷨��  �����û�
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
	 * ��Ӽ�¼
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
	 * ��Ӽ�¼
	 * @param my
	 */

	public void addEventValue(MyDoorEvent my,int check) {
		ContentValues values = new ContentValues();

		values.put("CardId", my.getCardid());
		values.put("Time", my.getTime());
		values.put("Event", my.getEvent());
		values.put("CheckData", check);

		if (sql != null)
			sql.insert(MySqlBase.doorEventTable, null, values);
	}
	
	
	/**
	 * ��ӵ������¼
	 * @param my
	 */

	public void addXuNiEventValue(UserEvent my,int check) {
		ContentValues values = new ContentValues();

		values.put("CardId", my.getUid());
		values.put("Time", my.getTime());
		values.put("Event", my.getEvent());
		values.put("EventResult",my.getEventresult());	
		values.put("CheckData", check);

		if (sql != null)
			sql.insert(MySqlBase.doorXuNiEventTable, null, values);
	}
	

	/**
	 * ��ѯ�û�
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
					hh.put("text", "�û�:" +name );
					hh.put("text1"," ����:" + CID);
					hh.put("text2"," ��Чʱ��:" + Time);
					list.add(hh);
		             
		        }
			 }
		
	}
	
	
	
	/**
	 * �õ��û�list
	 */
	public List<MyDoorUser> getUser()
	{
		setvalue(lists,listUs);
		return listUs;
	}
	
	
	

	
	/**
	 * �õ����м�¼
	 * @return
	 */
	
	public List<MyDoorEvent> getListValues() {
		List<MyDoorEvent> list = new ArrayList<MyDoorEvent>();
		Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorEventTable, null);
		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String checkData= cursor.getString(cursor.getColumnIndex("CheckData"));			
			MyDoorEvent my = new MyDoorEvent(CID, time, event);
			list.add(my);
			
			if(checkData.equals("0"));
			{
				String exe = "update "+MySqlBase.doorEventTable+" set CheckData = 1 where id = "+id;  
				//ִ��SQL  
				sql.execSQL(exe);
			}
			
		}

		return list;

	}
	
	/**
	 * �õ�δ�鿴�ļ�¼
	 * @return
	 */
	public List<MyDoorEvent> getNowListValues() {
		List<MyDoorEvent> list = new ArrayList<MyDoorEvent>();
		Cursor cursor=sql.query(MySqlBase.doorEventTable, null, "CheckData=?", new String[] { "0" }, null, null, null);
		while (cursor.moveToNext()) {

			String id = cursor.getString(cursor.getColumnIndex("id"));
			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			MyDoorEvent my = new MyDoorEvent(CID, time, event);
			list.add(my);
			
			
			String exe = "update "+MySqlBase.doorEventTable+" set CheckData = 1 where id = "+id;  
			//ִ��SQL  
			sql.execSQL(exe);
			
			
		}
		
		return list;

	}
	
	
	/**
	 * ʱ��β�ѯ
	 * @return
	 */
	
	public List<MyDoorEvent> getListValues(String start,String end) {
		List<MyDoorEvent> list = new ArrayList<MyDoorEvent>();
		Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorEventTable+" where Time>=? and Time<=?",new String[] {start,end});
		
		
		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			MyDoorEvent my = new MyDoorEvent(CID, time, event);
	
			list.add(my);
		}
		return list;

	}
	
	/**
	 * ʱ��β�ѯ������Ž���¼
	 * @return
	 */
	
	public List<UserEvent> getXuNiListValues(String start,String end) {
		List<UserEvent> list = new ArrayList<UserEvent>();
		Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorXuNiEventTable+" where Time>=? and Time<=?",new String[] {start,end});
		
		
		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			String eventresult = cursor.getString(cursor.getColumnIndex("EventResult"));
			UserEvent my = new UserEvent(CID, time, event,eventresult);
		
			list.add(my);
		}
		return list;

	}
	
	/**
	 * �õ����м�¼
	 * @return
	 */
	
	public List<UserEvent> getXuNiListValues() {
		List<UserEvent> list = new ArrayList<UserEvent>();
		Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorXuNiEventTable, null);
		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String checkData= cursor.getString(cursor.getColumnIndex("CheckData"));	
			String eventR= cursor.getString(cursor.getColumnIndex("EventResult"));	
			UserEvent my = new UserEvent(CID, time, event,eventR);
			
			list.add(my);			
			if(checkData.equals("0"));
			{
				String exe = "update "+MySqlBase.doorXuNiEventTable+" set CheckData = 1 where id = "+id;  
				//ִ��SQL  
				sql.execSQL(exe);
			}
			
		}
		
		return list;

	}
	
	
	
	
	
	/**
	 * �õ�δ�鿴�ļ�¼
	 * @return
	 */
	public List<UserEvent> getXuNiNowListValues() {
		List<UserEvent> list = new ArrayList<UserEvent>();
		Cursor cursor=sql.query(MySqlBase.doorXuNiEventTable, null, "CheckData=?", new String[] { "0" }, null, null, null);
		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			String id = cursor.getString(cursor.getColumnIndex("id"));	
			String eventR= cursor.getString(cursor.getColumnIndex("EventResult"));	
			UserEvent my = new UserEvent(CID, time, event,eventR);
			list.add(my);
			
			
			String exe = "update "+MySqlBase.doorXuNiEventTable+" set CheckData = 1 where id = "+id;  
			//ִ��SQL  
			sql.execSQL(exe);
			
			
		}
		


		return list;

	}
	

	
	/*
	 * ����IDɾ���û�
	 */
	public void deleteValue(String str) {
		// String exe="delete from "+MySqlBase.doorTable+"where CardId="+str;
		// sql.execSQL(exe);
		sql.delete(MySqlBase.doorTable, "CardId=?", new String[] { str });

	}
	
	/*
	 * �ж�CID�Ƿ����
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
	
	
	public MyDoorUser getUser(String cid) {
		
		
		Cursor cursor=sql.query(MySqlBase.doorTable, null, "CardId=?", new String[] { cid }, null, null, null);
		
		
		
		  while(cursor.moveToNext()){
	        	
	        	
	            String name = cursor.getString(cursor.getColumnIndex("UserName"));
	            String CID = cursor.getString(cursor.getColumnIndex("CardId"));	  	            
	            String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
	            String PassWord = cursor.getString(cursor.getColumnIndex("PassWord"));
	            String Time = cursor.getString(cursor.getColumnIndex("Time"));	            
	            MyDoorUser my=new MyDoorUser(name,CID,UserId,PassWord,Time);
	            
	            return my;

	       }
		  
		  return null;

	}
	

	/*
	 * ɾ�������û�
	 */
	public void cleanUserTable() {
		String exe = "delete from " + MySqlBase.doorTable;
		sql.execSQL(exe);
	}
	
	/*
	 * ɾ�����м�¼
	 */
	public void cleanEventTable() {
		String exe = "delete from " + MySqlBase.doorEventTable;
		sql.execSQL(exe);
	}
	
	/*
	 * ɾ�����м�¼
	 */
	public void cleanXuniEventTable() {
		String exe = "delete from " + MySqlBase.doorXuNiEventTable;
		sql.execSQL(exe);
	}

}
