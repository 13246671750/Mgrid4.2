package com.sg.common.lsyBase;

import java.util.List;

import com.mgrid.util.ByteUtil;

import android.util.Log;

public class DoorConfig {

	public static String get(int REMARK, int STATUS, String CargID, List<MyDoorUser> user) {

		String s = ByteUtil.hexStr2decimal(CargID) + "";

		int i = 10 - s.length();

		while (i > 0) {
			s = "0" + s;
			i--;
		}

		Log.e("msg", s);

		switch (REMARK) {
		case 0:

			for (MyDoorUser my : user) {
				if (s.equals(my.getCardid())) {
					return my.getName() + "���ųɹ�";
				}
			}

			return "���ųɹ�";
		case 1:

			return "�����û�ID���������뿪�ŵļ�¼��ȡ��)";
		case 2:

			return "Զ��(��SU)���ż�¼";
		case 3:

			return "�ڲ���ť���ż�¼";
		case 4:

			return "�������ż�¼��ȡ����";
		case 5:

			switch (CargID) {

			case "0000000000":

				return "���ⱨ����ʼ";

			case "0000000001":

				return "����ֹͣ����";

			case "0000000002":

				return "����������";

			case "0000000003":

				return "�ű��رգ�������״̬�Ĺ��ţ�";

			case "0000000004":

				return "����(I2)��Ч";
			case "0000000005":

				return "����(I2)��Ч";
			case "0000000006":

				return "SM�ڲ��洢����������, SM�Զ������˳�ʼ������";
			case "0000000007":

				return "�����ⱻ�ر�";

			case "0000000008":

				return "�����⿪��";

			case "0000000009":

				return "�������ؼ�ⱻ�ر�";

			case "0000000010":

				return "�������ؼ�⿪��";

			case "0000000081":

				return "��Ҫ�����ʱ�ڣ���������";
			case "0000000082":

				return "��ʱδ����(��Ҫ�����ʱ�ڣ�δ����,�����ǿ���)";
			case "0000000083":

				return "����(��Ҫ�����ʱ�ڹ���)";
			case "0000000084":

				return "��ʱδ����";

			default:
				return "��Ч�ĸ澯��¼";
			}

		case 6:

			return "SM�����¼";
		case 7:

			switch (s) {

			case "0000000000":

				return "�޸���SM������";

			case "0000000002":

				return "�޸����ŵ����Կ��Ʋ���";

			case "0000000004":

				return "���������û�";

			case "0000000008":

				return "ɾ�����û�����";

			case "0000000010":

				return "�޸���ʵʱ��";

			case "0000000020":

				return "�޸��˿���׼����ʱ������";
			case "0000000040":

				return "�޸��˽ڼ����б�";
			case "0000000080":

				return "�޸��˺��⿪�����رգ������ÿ�����";

			default:
				return "�ڲ����Ʋ������޸�";
			}

		case 8:

			return "��Ч���û���ˢ����¼��";
		case 9:

			for (MyDoorUser my : user) {
				if (s.equals(my.getCardid())) {
					return my.getName() + "�û�������Ч���ѹ� ";
				}
			}

			return "�û�������Ч���ѹ�";
		case 10:

			return "��ǰʱ����û����޽���Ȩ��";
				
			
		case 48:

			return "����Կ�׿��ż�¼�����ţ�";

		default:
			return "������Ϣ";
		}
	}

}
