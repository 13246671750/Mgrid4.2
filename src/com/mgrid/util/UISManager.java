package com.mgrid.util;

import java.util.ArrayList;
import java.util.List;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.user.User;
import com.sg.uis.LsyNewView.ChangeUserInfo;

/**
 * �������һ��ؼ��ļ���
 * 
 * @author Administrator
 *
 */
public class UISManager {

	public static List<ChangeUserInfo> ChangeUserInfoList = new ArrayList<ChangeUserInfo>();// ChangeUserInfo�ؼ�����

	/**
	 * ���س���ǰ�û�������view
	 */
	public void hideView() {
		User user = MGridActivity.userManager.getNowUser();
		if (user != null) {
			for (ChangeUserInfo info : ChangeUserInfoList) {
				if (user.getIndex().equals("0")) {
					info.showAllView();
				} else if (info.index != Integer.parseInt(user.getIndex())) {
					info.hideAllView();
				} else if (info.index ==  Integer.parseInt(user.getIndex())) {
					info.showAllView();
				}
			}
		}

	}
}
