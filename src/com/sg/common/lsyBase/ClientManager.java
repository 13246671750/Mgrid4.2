package com.sg.common.lsyBase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.sg.common.IObject;
import com.sg.uis.newView.NBerDoorView;

import android.util.Log;

public class ClientManager implements Runnable {

	private String message = "";

	private Socket socket;

	private DoorManagerBase DBManager;

	private PrintWriter out = null;
	private BufferedReader in = null;

	public ClientManager(Socket socket, DoorManagerBase DBManager) {
		
		this.socket = socket;	
		this.DBManager=DBManager;
		
		DBManager.setManager(this);
		
		
		try {

			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));// �������������
						
			
			// �ͻ���ֻҪһ�������������㷢�����ӳɹ�����Ϣ
			message = "��������ַ��" + this.socket.getInetAddress();
			this.sendMessage(message);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {

				String str = "";

				while ((str = in.readLine()) != null && str.length() != 0) {

					// ���ͻ��˷��͵���ϢΪ��exitʱ���ر�����
					if (message.equals("exit")) {
						closeSocket();
						break;
					} else {
						
						DBManager.getSendData(str);
					
					}
				}
				
		

			}
		} catch (Exception e) {

			Log.e("�߳�", "�쳣");
			e.printStackTrace();

		}
	}

	/**
	 * �رտͻ���
	 * 
	 * @throws IOException
	 */
	public void closeSocket() throws IOException {

		this.sendMessage(message);
		if (!Thread.currentThread().isInterrupted()) {
			Thread.currentThread().interrupt();
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
		if (socket != null) {
			socket.close();
		}
	}

	/**
	 * �����յ���Ϣת����ÿһ���ͻ���
	 * 
	 * @param msg
	 */

	public void sendMessage(String msg) {

		try {
			if (out == null) {
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK")),
						true);// �������������
			}

			out.println(msg);// ת��
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
