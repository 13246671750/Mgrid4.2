package com.mgrid.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class AllUtilS {

	private static Toast toast;

	/**
	 * 
	 * ���Toast�ظ����� ��ʱ�䲻��ʧ������
	 * 
	 * @param context
	 * 
	 * @param message
	 * 
	 */

	public static void showToast(Context context, String message) {

		if (toast == null) {

			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);

		} else {

			toast.setText(message);

		}

		toast.show();// �����µ���Ϣ��ʾ

	}

	// �õ�������IP��ַ
	public static String getLocalIP() {
		String IP = null;
		StringBuilder IPStringBuilder = new StringBuilder();
		try {
			// NetworkInterface��ʾ����Ӳ���������ַ
			Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
				Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
				while (inetAddressEnumeration.hasMoreElements()) {
					InetAddress inetAddress = inetAddressEnumeration.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						IPStringBuilder.append(inetAddress.getHostAddress().toString());
					}
				}
			}
		} catch (SocketException ex) {

		}

		IP = IPStringBuilder.toString();
		return IP;
	}

}
