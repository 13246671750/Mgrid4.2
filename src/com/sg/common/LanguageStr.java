package com.sg.common;

import java.util.Locale;

import android.content.Context;

public class LanguageStr {

	// ����ѡ��

	// MgridActivity
	public static String Load = "������";
	public static String PSS = "��Ƭ����ɹ�";
	public static String PSF = "��Ƭ����ʧ��";

	// DataGetter
	public static String meaning = "ͨ���ж�";

	// GridviewActivity+ImageviewActivity
	public static String back = "����";

	// MainWindow+LsyChangExpression
	public static String Success = "���óɹ�";
	public static String Fail = "����";

	// LoginUtil+SgImage
	public static String yes = "����";
	public static String no = "�˳�";
	public static String systemExit = "��½";
	public static String Text1 = "��������ȷ";
	public static String Text2 = "�û������������";

	// CoolButton+SgButton
	public static String Prompt = "��ʾ";
	public static String problem = "�Ƿ�ȷ��?";
	public static String OK = "ȷ��:";
	public static String ON = "ȡ��";
	public static String text = "ɾ���ɹ�,��������������";

	// HisEvent+SaveEquipt+sgEventList+SgSignalList
	public static String DeviceName = "�豸����";
	public static String AlarmName = "�澯����";
	public static String AlarmMeaning = "�澯����";
	public static String Numericalsignal = "�ź���ֵ";
	public static String AlarmSeverity = "�澯�ȼ�";
	public static String StartTime = "��ʼʱ��";
	public static String EndTime = "����ʱ��";
	public static String DeviceList = "  �豸��   ";
	public static String SetTime = "��������";
	public static String PreveDay = "��һ��";
	public static String NextDay = "ǰһ��";
	public static String Receive = "  ��ȡ   ";
	public static String AllDevice = "ȫ���豸";
	public static String one = "֪ͨ", two = "һ��澯", three = "���ظ澯", four = "�����澯", set = "����",level="�ȼ�";
	public static String SignalName = "�ź�����";
	public static String Value = "��ֵ";
	public static String Unit = "��λ";
	public static String ValueType = "��ֵ����";
	public static String AcquisitionTime = "�ɼ�ʱ��";

	// SgChangePassWord+SgChangXmlPW
	public static String oldPw = "������:";
	public static String newPw = "������:";
	public static String text1 = "�����޸ĳɹ�";
	public static String text2 = "�����������������������";
	public static String text3 = "�������벻����";
	public static String confirm = "ȷ    ��:";
	public static String text11 = "������ó��ִ���", text12 = "�����û��Ȩ��ҳ��", text13 = "�����������벻һ��",
			text14 = "���ӣ� ����̬���õı�ǩ����Ȩ��ҳ��ĸ���", text15 = "�����޸ĳɹ�", text16 = "�����������������������", text17 = "�������벻����";

	// SgChangNamePhoneTypeState
	public static String Name = "����";
	public static String Phone = "����";
	public static String Level = "�ȼ�";
	public static String Show = "��ʾ";
	public static String Add = "���";
	public static String Alter = "�޸�";
	public static String State = "״̬";
	public static String delete = "ɾ��";
	public static String NO = "ȡ��";
	public static String text4 = "��ȡ�ļ���������û������ļ�", text5 = "������δ��Ӻ���", text6 = "�޸ĳɹ�", text7 = "����λ������",
			text8 = "����������", text9 = "��ӳɹ�", text10 = "ɾ���ɹ�";

	// SgImage
	public static String denglu = "�û�Ȩ�޵�¼";
	public static String pwText = "����";
	public static String text18 = "��ȴ�������ɣ���";
	public static String text19 = "�û������������";
	public static String userName = "�û�����";
	public static String PWD = "��    �룺";

	// SgIsolationEventSetter
	public static String off = "�澯����", on = "�澯����";

	// SgSignalList
	public static String Names = "����";
	public static String RefreshTime = "ˢ��ʱ��";
	public static String Implication = "����";

	// SgYKParameter
	public static String Chooose = "��ѡ�� ��";
	
	
	//LanguageStr
	//public static String text_Su = "�л����,�ȴ�30s����";
	public static String title = "��ʾ";
	public static String content = "�л����,�ȴ�30s����";
	
	//SgSplineChart
	//public static String text_Su = "�л����,�ȴ�30s����";
	public static String h = "ʱ";
	public static String d = "��";
	public static String m = "��";
	public static String y = "��";
	
	
	public static String text20 = "�ļ�������";
	
	

	// ϵͳ���Ժ��������� trueΪ���� falseΪӢ
	public static boolean systemLanguage = true;
	public static String iniLanguage = "";

	// ��ȡϵͳ����
	public static void whatLanguageSystem(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			systemLanguage = true;
		else
			systemLanguage = false;
	}

	// ��������
	public static void setLanguage() {
		if (iniLanguage == null || iniLanguage.equals("")) {
			if (systemLanguage == true) {

			} else {
				setEnglish();
			}
		} else {
			if (iniLanguage.equals("Chinese")) {

			} else if (iniLanguage.equals("English")) {

				setEnglish();
			}
		}
	}

	// ����Ӣ������
	public static void setEnglish() {

		// MgridActivity
		Load = "Loaded";
		PSS="Save success";
		PSF="Save failure";

		// DataGetter
		meaning = "Lost";

		// GridviewActivity+ImageviewActivity
		back = "Back";

		// MainWindow+LsyChangExpression
		Success = "Success";
		Fail = "Fail";

		// LoginUtil+SgImage
		yes = "ok";
		no = "cancel";
		systemExit = "LOGIN";
		Text1 = "Please enter sure����";
		Text2 = "Password or user name error";

		// CoolButton+SgButton
		Prompt = "Hint";
		problem = "Are you sure? ";
		OK = "yes";
		ON = "cancel";
		text = "Delete Success, Please Restart";

		// HisEvent+SaveEquipt+sgEventList+SgSignalList
		DeviceName = "Device Name";
		AlarmName = "Alarm Name";
		AlarmMeaning = "Alarm Meaning";
		Numericalsignal = "Numerical Signal";
		AlarmSeverity = "Alarm Severity";
		StartTime = "Start Time";
		EndTime = "End Time";
		DeviceList = "  Device��   ";
		SetTime = "Set Time";
		PreveDay = "Previous Day";
		NextDay = "Next Day";
		Receive = "  Receipt   ";
		AllDevice = "All Device";
		one = "Notice";
		two = "GeneralAlarm";
		three = "CriticalAlarm";
		four = "FatalAlarm";
		set = "Set";
		level="Level";

		SignalName = "Signal Name";
		Value = "Value";
		Unit = "Unit";
		ValueType = "Value Type";
		AcquisitionTime = "Acquisition Time";

		// SgChangePassWord+SgChangXmlPW
		oldPw = "Old Password";
		newPw = "New Password";
		text1 = "Password changes succeeded";
		text2 = "Old password entered error, please re-enter";
		text3 = "The password input is incomplete";
		confirm = "Confirm";
		text11 = "There is an error in your configuration";
		text12 = "You have no permissions page at all";
		text13 = "The two password input is not the same";
		text14 = "The label of your configuration configuration is greater than the number of permissions pages ";
		text15 = "Password changes succeeded";
		text16 = "Old password entered error, please re-enter";
		text17 = "The password input is incomplete";

		// SgChangNamePhoneTypeState
		Name = "Name";
		Phone = "Phone";
		Level = "Level";
		Show = "Show";
		Add = "Add";
		Alter = "Alter";
		State = "State";
		delete = "Delete";
		NO = "NO";
		text4 = "NO File";
		text5 = "NO Phone";
		text6 = "Success";
		text7 = "phone num error";
		text8 = "Please input full";
		text9 = "Success";
		text10 = "Success";

		// SgImage
		denglu = "Login";
		pwText = "PassWord";
		text18 = "Please wait for loading to complete����";
		text19 = "Password or user name error";
		userName = "User  ID:";
		PWD = "PassWord:";

		// SgIsolationEventSetter
		off = "OFF";
		on = "ON";

		// SgSignalList

		Names = "Name";
		RefreshTime = "RefreshTime";
		Implication = "Meaning";

		// SgYKParameter
		Chooose = "Choose��";
		
		//LanguageStr
		title="Title";
		content = "Please wait for the reboot of the 30s";
		
		//SgSplineChart
		h="hr";
		d="day";
		m="mon";
		y="year";
		
		
		text20="fail";
		
		
	}

}
