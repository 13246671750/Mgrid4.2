package com.mgrid.main.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * �û�������
 */
public class UserManager {

	private Map<Integer,User> userManaget=new HashMap<Integer, User>();//���е��û��ϼ�
	private List<String> userList=new ArrayList<String>();//���е��û����ϼ�    
	private List<String> passWordList=new ArrayList<String>();//���е�����ϼ�
	private User nowUser=null;//��ǰ��¼���û���  Ϊnull��ʾΪδ��¼

	public Map<Integer,User> getUserManaget() {
		return userManaget;
	}

	public User getNowUser() {
		return nowUser;
	}

	public void setNowUser(User nowUser) {
		this.nowUser = nowUser;
	}

	public void setUserManaget(Map<Integer,User> userManaget) {
		this.userManaget = userManaget;
	}
		
	//���User
	public void addUser(int index,User user)
	{
			
		userManaget.put(index, user);
		ResetList();
	}
	
	//ɾ��User
	public void deleteUser(int index)
	{
		
		userManaget.remove(index);
		ResetList();
	}
	
	private void ResetList()
	{
		userList.clear();
		passWordList.clear();
		Iterator<Entry<Integer,User>> it=userManaget.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Integer,User> entry=it.next();
			User u=entry.getValue();
	
			userList.add(u.getUid());
			passWordList.add(u.getPw());
		}
		
	}
	
	
	
	public void setUser(int index,String UserID,String PassWord)
	{
		

		userManaget.get(index).setUid(UserID);
		userManaget.get(index).setPw(PassWord);
		ResetList();
		
		
	}
	
	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public List<String> getPassWordList() {
		return passWordList;
	}

	public void setPassWordList(List<String> passWordList) {
		this.passWordList = passWordList;
	}

	//���User	
	public User getUser(int index)
	{
		
		return userManaget.get(index);
	}
	
	
	
}
