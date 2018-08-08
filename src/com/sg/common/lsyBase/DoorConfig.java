package com.sg.common.lsyBase;

import java.util.List;

import com.mgrid.util.ByteUtil;

import android.util.Log;

public class DoorConfig {

	public static String get(int REMARK, int STATUS,String CargID,List<MyDoorUser> user) {
		switch (REMARK) {
		case 0:

			
			
			String s=ByteUtil.hexStr2decimal(CargID)+"";
			
			int i=10-s.length();
			while(i>0)
			{
				s="0"+s;
				i--;
			}
			
			
			Log.e("msg", s);
			
			for(MyDoorUser my:user)
			{
				if(s.equals(my.getCardID()))
				{
					return my.getName()+"���ųɹ�";
				}
			}
			
			
			return "���ųɹ�";
		case 1:

			return "�����û�ID���������뿪�ŵļ�¼��ȡ��)";
		case 2:

			return "Զ��(��SU)���ż�¼";
		case 3:

			return "�ֶ����ż�¼";
		case 4:

			return "�������ż�¼��ȡ����";
		case 5:

			return "���� (�򱨾�ȡ��) ��¼";
		case 6:

			return "SM�����¼";
		case 7:

			return "�ڲ����Ʋ������޸�";
		case 8:

			return "��Ч���û���ˢ����¼��";
		case 9:

			return "�û�������Ч���ѹ�";
		case 10:

			return "��ǰʱ����û����޽���Ȩ��";

		default:
			return "������Ϣ";
		}
	}

}
