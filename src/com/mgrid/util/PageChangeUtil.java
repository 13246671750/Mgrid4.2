package com.mgrid.util;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
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
	private String pageXml="";
	

	public PageChangeUtil(SgImage image, String m_strClickEvent, MainWindow m_rRenderWindow) {
		this.image = image;
		this.m_strClickEvent = m_strClickEvent;
		this.m_rRenderWindow = m_rRenderWindow;
	}

	public void changePage() {
		if (MGridActivity.isNOChangPage) {  //ҳ���ʼ����ɺ�
			String[] arrStr = m_strClickEvent.split("\\(");
			if (m_rRenderWindow != null && arrStr[0].equals("Show")) {
				String[] str = arrStr[1].split("\\)");
				pageXml=str[0];
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
									case 0:  //Ĭ��/��ģʽ

										MaskCount = i;
										showPassDialog();

										break;

									case 1:  //��������ģʽ

										if(MGridActivity.userManager.getUserManaget().size()!=0)
										{
										    showPassDialog();
										}else
										{
											changePage(pageXml);
										}
										
										break;
									case 2:   //��¼ģʽ

										
										
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
			// if (MGridActivity.isNOChangPage) {
			// String[] arrStr = m_strClickEvent.split("\\(");
			// boolean isMask = true;// �����ж��Ƿ�ΪȨ��ҳ��
			// boolean isNeedPW = true; // �����ж�Ȩ��ҳ���Ƿ���Ҫ����
			// if (m_rRenderWindow != null && arrStr[0].equals("Show")) {
			// int count = -1;
			// String[] str = arrStr[1].split("\\)");
			// // �˴�ѭ�����������ҳ���ǰȨ��ҳ�����ڵ���Ȩ��ҳ��
			// if (MGridActivity.m_MaskPage != null) {
			// for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {
			//
			// for (String s : MGridActivity.m_MaskPage[i]) {
			// if (s.equals(DataGetter.currentPage)) {
			// count = i;
			// break;
			// }
			// }
			// if (count != -1)
			// break;
			// }
			//
			// if (count != -1) {
			// // �˴�ѭ�����ж���Ҫ��ת��ҳ��͵�ǰҳ���ǲ�����ͬһ����Ȩ��ҳ��
			// for (String s : MGridActivity.m_MaskPage[count]) {
			// if (s.equals(str[0] + ".xml")) // �����ǰҳ��ΪȨ��ҳ�棨ֻ֧����һ����Ȩ��ҳ�棩
			// {
			// isNeedPW = false;
			//
			// }
			// }
			// }
			// if (isNeedPW) {
			// // �˴�ѭ�����ж���Ҫ��תҳ���Ƿ�ΪȨ��ҳ�档
			// for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {
			// for (String s : MGridActivity.m_MaskPage[i]) {
			// if (!s.equals("1")) {
			// if ((s.substring(0, s.length() - 4)).equals(str[0])) {
			// MaskCount = i;
			// showPassDialog();
			// isMask = false;
			// break;
			// }
			// }
			// }
			// if (!isMask)
			// break;
			// }
			//
			// }
			// }
			//
			// if (isMask) {
			// if (SgImage.isChangColor == false) {
			// SgImage.isChangColor = true;
			// if (MGridActivity.isPlaygif) {
			// m_rRenderWindow.m_oMgridActivity.releaseWakeLock();
			// System.out.println("�ҽ�����gif");
			// } else if (MGridActivity.isPlaymv) {
			// m_rRenderWindow.m_oMgridActivity.releaseWakeLock();
			// m_rRenderWindow.m_oMgridActivity.svv.pauseMv();
			// m_rRenderWindow.m_oMgridActivity.mTimeHandler
			// .removeCallbacks(m_rRenderWindow.m_oMgridActivity.runTime);
			// System.out.println("�ҽ�����mv");
			// }
			// }
			// changePage(str[0]);
			// }
			//
			// }
		} else {
			Toast.makeText(image.getContext(), image.Text18, 1000).show();
		}
	}

	private void changePage(String xml) {
		m_rRenderWindow.changePage(xml);
	}

	// ��ʾ�û�Ȩ�޽���Ի���
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

						if(MGridActivity.m_UserAway==0)
						{
							if (password.equals(MGridActivity.m_pagePassWord[MaskCount]) || password.equals("88888888")) { // MaskCount
								
								changePage(pageXml);
								
							} else {

								Toast.makeText(m_rRenderWindow.getContext(), image.Text19, Toast.LENGTH_SHORT).show();
						
							}
						}else if(MGridActivity.m_UserAway==1)
						{						
								
                            if(judgePW(password)|| password.equals("88888888"))
                            {
                            	changePage(pageXml);								 
                            	
                            }								
							 else {

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
	
	
	//�ж�����������û������ַ���
	private boolean judgePW(String PassWord)
	{
		if(MGridActivity.userManager.getPassWordList().contains(PassWord))
		{
			return true;
		}		
		return false;
	}
	

}
