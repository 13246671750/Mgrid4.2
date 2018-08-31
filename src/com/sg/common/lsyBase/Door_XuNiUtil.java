package com.sg.common.lsyBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.user.User;
import com.mgrid.main.user.UserEvent;
import com.mgrid.mysqlbase.SqliteUtil;
import com.mgrid.util.TimeUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Door_XuNiUtil {

	private static MGridActivity mActivity = null;
	private Map<Integer, Door_XuNiCallBack> backMap = new HashMap<>();
	private static Door_XuNiUtil niUtil = new Door_XuNiUtil();
	private String nowUser = "";
	private SqliteUtil sql;

	private Door_XuNiUtil() {

	}

	public static Door_XuNiUtil getIntance() {
		return niUtil;
	}

	public void setCallBack(int index, Door_XuNiCallBack back) {

		backMap.put(index, back);

	}

	public void setSql(SqliteUtil sql) {
		this.sql = sql;
	}

	public void setContext(Context context) {
		if (mActivity == null) {

			mActivity = (MGridActivity) context;
		}
	}

	/**
	 * ����û�
	 */

	public void add(final String id, final String pw, final int index) {

		MGridActivity.ecOneService.execute(new Runnable() {

			@Override
			public void run() {

				Door_XuNiCallBack back = backMap.get(index);

				if (id.equals("") || pw.equals("")) {

					if (back != null) {
						
						back.onFail(3);
						backMap.get(-1).onFail(2);
						
					}

				} else {

					Map<String, String> map = new HashMap<String, String>();
					map.put("User" + index, id);
					map.put("PassWord" + index, pw);

					User user;
					user = new User(id, pw, index + "");
					MGridActivity.userManager.addUser(index, user);

					saveData("SysConf", map, false);

					if (back != null)
					{
						backMap.get(-1).onSuccess(2,id,pw);
						back.onSuccess(2,id,pw);	
					}

				}

			}

		});

	}

	/**
	 * ɾ���û�
	 */

	public void delete(final String id, final String pw, final int index) {

		MGridActivity.ecOneService.execute(new Runnable() {

			@Override
			public void run() {
				
				
				
				
				Door_XuNiCallBack back = backMap.get(index);
				
				if(id.equals("") || pw.equals(""))
				{
					
					
					if (back != null) {
						
						back.onFail(3);
						backMap.get(-1).onFail(3);
						
					}
					
					return ;
					
				}
				

				
				Map<String, String> map = new HashMap<String, String>();

				map.put("User" + index, id);
				map.put("PassWord" + index, pw);

				saveData("SysConf", map, true);

				MGridActivity.userManager.deleteUser(index);

				if (back != null)
				{
					back.onSuccess(0,id,pw);
					backMap.get(-1).onSuccess(3,id,pw);
					
				}
			}
		});

	}

	/**
	 * �޸��û�
	 */

	public void alter(final String id, final String pw, final int index) {
		MGridActivity.ecOneService.execute(new Runnable() {

			@Override
			public void run() {

				Door_XuNiCallBack back = backMap.get(index);
				if (id.equals("") || pw.equals("")) {

					if (back != null)
						back.onFail(3);

				} else {
					Map<String, String> map = new HashMap<String, String>();

					map.put("User" + index, id);
					map.put("PassWord" + index, pw);

					saveData("SysConf", map, false);

					MGridActivity.userManager.setUser(index, id, pw);

					if (back != null)
						back.onSuccess(1,id,pw);

				}

			}
		});
	}

	public void openDoor(String uid, String pw, int index) {
		if (uid != null) {

			User user = MGridActivity.userManager.getUserManaget().get(index);
			nowUser = uid;
			if (user.getUid().equals(uid) && user.getPw().equals(pw)) {
				saveResult(1);

			} else {
				saveResult(0);
			}

		} else {

			if (isSure(pw)) {

			} else {
				saveResult(0);
			}

		}

	}

	// �����Ƿ���ȷ
	private boolean isSure(String passWord) {

		getNowUser(passWord);

		if (MGridActivity.m_ControlAway == 1) {

			if (MGridActivity.userManager.getNowUser() != null) {

				if (MGridActivity.userManager.getNowUser().getPw().equals(passWord)) {

					saveResult(1);
					return true;

				}

			}

		} else if (MGridActivity.m_ControlAway == 0) {

			if (MGridActivity.userManager.getPassWordList().contains(passWord)) {

				saveResult(1);
				return true;

			}

		}

		return false;
	}

	// ������
	private void saveResult(final int i) {

		MGridActivity.ecOneService.execute(new Runnable() {

			@Override
			public void run() {

				String nowTime = TimeUtils.getNowTime();
				String event = "����";

				switch (i) {
				case 0:

					UserEvent ue1 = new UserEvent(nowUser, nowTime, event, "ʧ��");
					sql.addXuNiEventValue(ue1, 0);
					backMap.get(-1).onFail(0);
					

					break;

				case 1:

					UserEvent ue2 = new UserEvent(nowUser, nowTime, event, "�ɹ�");
					sql.addXuNiEventValue(ue2, 0);
					backMap.get(-1).onSuccess(1,"","");

					break;
				}

			}
		});

	}

	/**
	 * �õ���ǰ�����û�
	 * 
	 * @param string
	 */
	private void getNowUser(String passWord) {

		if (MGridActivity.m_ControlAway == 1) {
			if (MGridActivity.userManager.getNowUser() != null) {
				nowUser = MGridActivity.userManager.getNowUser().getUid();
			}
		} else if (MGridActivity.m_ControlAway == 0) {

			Map<Integer, User> userManaget = MGridActivity.userManager.getUserManaget();
			Iterator<Map.Entry<Integer, User>> it = userManaget.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, User> entry = it.next();
				if (entry.getValue().getPw().equals(passWord)) {
					nowUser = entry.getValue().getUid();
					return;
				}

				nowUser = passWord;
			}

		}
	}

	/**
	 * �����û���INI
	 * 
	 * @param type
	 * @param map
	 * @param isDelete
	 */
	private void saveData(String type, Map<String, String> map, boolean isDelete) {

		try {

			if (mActivity.iniReader != null) {

				synchronized (MGridActivity.mgridIniPath) {

					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(new File(MGridActivity.mgridIniPath)), "gb2312"));

					Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, String> entry = it.next();
						String key = entry.getKey();
						String value = entry.getValue();
						if (isDelete) {

							mActivity.iniReader.removeStr(type, key, value);

						} else {

							mActivity.iniReader.setStr(type, key, value);

						}

					}

					mActivity.iniReader.writeStr(bw);

					bw.flush();
					bw.close();

				}

			}

		} catch (Exception e) {

		}

	}

}
