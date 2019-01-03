package com.mgrid.main;

import org.easydarwin.video.EasyPlayerClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.TextureView;

public class VideoMonitorActivity2 extends Activity{

	
	private  TextureView ttv;
	private  EasyPlayerClient  client;
	
	private static  String url="rtsp://admin:12345@192.168.1.12:554/h264/ch1/main/av_stream";
	public static final String KEY = "79393674363536526D3432416C5256636F2B70714A655A76636D63755A57467A65575268636E64706269356C59584E356347786865575679567778576F502B6C3430566863336C4559584A33615735555A57467453584E55614756435A584E30514449774D54686C59584E35";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		  setContentView(R.layout.videomonitor2);
		  
		  ttv = (TextureView) findViewById(R.id.ttv);
	        /**
	         * ����˵��
	         * ��һ������ΪContext,�ڶ�������ΪKEY
	         * ����������Ϊ��textureView,������ʾ��Ƶ����
	         * ���ĸ�����Ϊһ��ResultReceiver,��������SDK�㷢�������¼�֪ͨ;
	         * ���������ΪI420DataCallback,�����Ϊ��,�ǵײ���YUV���ݻص�����.
	         */
	        client = new EasyPlayerClient(this, KEY, ttv, null, null);
	        client.play(url);
	       
		  
		
	}
	
}
