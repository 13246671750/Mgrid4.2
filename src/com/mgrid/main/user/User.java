package com.mgrid.main.user;


/*
 * �û���
 */

public class User {

	private String UserID;
	private String passWord;
	private int flag=-1;//0�������Ա������������ͨ�û�
	
	public User(String UserID,String passWord,int flag)
	{
		this.UserID=UserID;
		this.passWord=passWord;
		this.flag=flag;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
		
}
