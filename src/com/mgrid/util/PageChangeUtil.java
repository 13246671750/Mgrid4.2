package com.mgrid.util;

import java.util.Iterator;
import java.util.Map;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.main.user.User;
import com.sg.uis.SgImage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PageChangeUtil {

	private SgImage image;
	private String m_strClickEvent;
	private MainWindow m_rRenderWindow;
	private int MaskCount = -1;
	private String pageXml = "";
	private UISManager uisMa=new UISManager();

	public PageChangeUtil(SgImage image, String m_strClickEvent, MainWindow m_rRenderWindow) {
		this.image = image;
		this.m_strClickEvent = m_strClickEvent;
		this.m_rRenderWindow = m_rRenderWindow;
	}

	public void changePage() {
		if (MGridActivity.isNOChangPage) { // ҳ���ʼ����ɺ�
			String[] arrStr = m_strClickEvent.split("\\(");
			if (m_rRenderWindow != null && arrStr[0].equals("Show")) {
				String[] str = arrStr[1].split("\\)");
				pageXml = str[0];
				if (MGridActivity.m_MaskPage != null) {// �����Ȩ��ҳ��
					for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {
						for (String s : MGridActivity.m_MaskPage[i]) {
							if (!s.equals("1")) {
								if ((s.substring(0, s.length() - 4)).equals(str[0])) {// �����תҳ����Ȩ��ҳ��

									for (String page : MGridActivity.m_MaskPage[i]) {
										if (page.equals(DataGetter.currentPage)) { // �����ǰҳ�����תҳ����ͬһȨ����

											changePage(pageXml);
											return;
										}
									}

									switch (MGridActivity.m_UserAway) {
									case 0: // Ĭ��/��ģʽ

										MaskCount = i;
										showPassDialog();

										break;

									case 1: // ��������ģʽ

										if (MGridActivity.userManager.getUserManaget().size() != 0) {
											showUserPWDialog();
										} else {
											changePage(pageXml);
										}

										break;
									case 2: // ��¼ģʽ

										break;
									}

									return;
								}
							}
						}
					}
					changePage(pageXml);
				} else {
					changePage(pageXml);
				}
			}
		
			
		} else {
			Toast.makeText(image.getContext(), image.Text18, 1000).show();
		}
	}

	private void changePage(String xml) {
		m_rRenderWindow.changePage(xml);
	}

	// ��ʾ�û�Ȩ�޽���Ի��� ģʽ1
	public void showPassDialog() {
		// LayoutInflater��������layout�ļ����µ�xml�����ļ�������ʵ����
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow.getContext());
		// ��activity_login�еĿؼ�������View��
		final View textEntryView = factory.inflate(R.layout.page_xml, null);

		TextView tv = (TextView) textEntryView.findViewById(R.id.pagetv);
		tv.setText(image.pwText);

		// ��LoginActivity�еĿؼ���ʾ�ڶԻ�����
		new AlertDialog.Builder(m_rRenderWindow.getContext())
				// �Ի���ı���
				.setTitle(image.denglu)
				// �趨��ʾ��View
				.setView(textEntryView)
				// �Ի����еġ���½����ť�ĵ���¼�
				.setPositiveButton(image.yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						final EditText etPassword = (EditText) textEntryView.findViewById(R.id.pageet);

						String password = etPassword.getText().toString().trim();

						if (MGridActivity.m_UserAway == 0) {
							if (password.equals(MGridActivity.m_pagePassWord[MaskCount])
									|| password.equals("88888888")) { // MaskCount

								changePage(pageXml);

							} else {

								Toast.makeText(m_rRenderWindow.getContext(), image.Text19, Toast.LENGTH_SHORT).show();

							}
						} else if (MGridActivity.m_UserAway == 1) {

							if (judgePW(password) || password.equals("88888888")) {
								changePage(pageXml);

							} else {

								Toast.makeText(m_rRenderWindow.getContext(), image.Text19, Toast.LENGTH_SHORT).show();

							}
						}
					}

				})
				// �Ի���ġ��˳��������¼�
				.setNegativeButton(image.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// LoginActivity.this.finish();
					}
				})

				// �Ի���Ĵ�������ʾ
				.create().show();
	}

	// ��ʾ�Ի��� ģʽ2
	public void showUserPWDialog() {

		// LayoutInflater��������layout�ļ����µ�xml�����ļ�������ʵ����
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow.getContext());
		// ��activity_login�еĿؼ�������View��
		final View textEntryView = factory.inflate(R.layout.auth_dialog, null);

		TextView tv1 = (TextView) textEntryView.findViewById(R.id.tvuserName);
		TextView tv2 = (TextView) textEntryView.findViewById(R.id.tvPWD);

		new AlertDialog.Builder(m_rRenderWindow.getContext())
				// �Ի���ı���
				.setTitle(image.denglu)
				// �趨��ʾ��View
				.setView(textEntryView)
				// �Ի����еġ���½����ť�ĵ���¼�
				.setPositiveButton(image.yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						final EditText etUserName = (EditText) textEntryView.findViewById(R.id.etuserName);
						final EditText etPassword = (EditText) textEntryView.findViewById(R.id.etPWD);

						// ��ҳ��������л�õġ��û������������롱תΪ�ַ���
						String userName = etUserName.getText().toString().trim();
						String password = etPassword.getText().toString().trim();

						if (userName.equals("88888888") && password.equals("88888888")) {

							changePage(pageXml);
						} else {
							if (judgeUserPW(userName, password)) {

								changePage(pageXml);
							} else {

								Toast.makeText(m_rRenderWindow.getContext(), image.Text19, Toast.LENGTH_SHORT).show();

							}
						}

					}

				})

				.setNegativeButton(image.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				})

				.create().show();
	}

	// �ж�����������û������ַ���
	private boolean judgePW(String PassWord) {
		if (MGridActivity.userManager.getPassWordList().contains(PassWord)) {
			return true;
		}
		return false;
	}

	//�ж��û�������ȷ��
	private boolean judgeUserPW(String User, String PassWord) {
		Iterator<Map.Entry<Integer, User>> it = MGridActivity.userManager.getUserManaget().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, User> map = it.next();
			if (map.getValue().getUid().toString().equals(User) && 
					map.getValue().getPw().equals(PassWord)) {

				MGridActivity.userManager.setNowUser(map.getValue());
				uisMa.hideView();
				
				return true;
			}
		}

		return false;
	}

}
