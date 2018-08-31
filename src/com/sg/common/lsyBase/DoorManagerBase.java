package com.sg.common.lsyBase;

public interface DoorManagerBase {
	
	
	/**
	 * ����û�
	 */
	public void addUsr(String str);
	
	/**
	 * ɾ���û�
	 */
	public void deleteUsr(String str);
	
	
	/**
	 * ����ʱ��
	 */
	public void setTime(String str);
	
	
	/**
	 * ����
	 */
	public void openDoor(String str);
	
	/**
	 * ��ȡ�����û�
	 */
	public void getUsers();
	
	/**
	 * ��ȡ���м�¼
	 */
	public void getAllEvent();
	
	
	/**
	 * ��ȡ���¼�¼
	 */
	public void getNewEvent();
	
	
	/**
	 * ������Ϣ
	 */
	
	public void getSendData(String recive);
	
	/**
	 * ע��ClientManager
	 */
	public void setManager(ClientManager manager);

}
