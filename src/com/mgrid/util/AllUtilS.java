package com.mgrid.util;

import android.content.Context;
import android.widget.Toast;

public class AllUtilS {
	
	
private static Toast toast;

/**

 * ���Toast�ظ����� ��ʱ�䲻��ʧ������

 * @param context

 * @param message

 */

public static void showToast(Context context,String message){

    if (toast==null){

        toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);

    }else {

        toast.setText(message);

    }

    toast.show();//�����µ���Ϣ��ʾ

 }




}
