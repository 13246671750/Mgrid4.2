package com.mgrid.main;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;
import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerDelegate;
import cn.nodemedia.NodePlayerView;

public class VideoMonitorActivity extends Activity implements OnClickListener,OnPreparedListener, NodePlayerDelegate{

	
	private static  String uriPath="rtsp://admin:12345@192.168.1.12:554/h264/ch1/main/av_stream";
	
	
    private NodePlayerView playSurface;
    private NodePlayer nodePlayer;
    private boolean isStarting;
    private Button btn;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		
		initindow();
		setContentView(R.layout.videomonitor4);
		initView();
		playRtsp();
		
		
	
	}
	
	
	
	/**
     * ��ʼ��window
     */
    private void  initindow()
    {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);         //ǿ�ƺ���
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);   //ȫ����ʾ

    }

    
    /**
     * ��ʼ��view
     */
    private void  initView()
    {
    	
    	playSurface=(NodePlayerView) findViewById(R.id.play_surface);
        btn=(Button) findViewById(R.id.play_btn);
        btn.setOnClickListener(this);
    }

    
    /**
     * ����
     */
    private void  playRtsp()
    {
    
    	  int videoScaleMode = 1;
         //���ò�����ͼ����Ⱦ��ģʽ,����ʹ��SurfaceView��TextureView. Ĭ��SurfaceView
         playSurface.setRenderType(NodePlayerView.RenderType.SURFACEVIEW);
         //������ͼ����������ģʽ

         NodePlayerView.UIViewContentMode mode = null;
         switch (videoScaleMode) {
             case 0:
                 mode = NodePlayerView.UIViewContentMode.ScaleToFill;
                 break;
             case 1:
                 mode = NodePlayerView.UIViewContentMode.ScaleAspectFit;
                 break;
             case 2:
                 mode = NodePlayerView.UIViewContentMode.ScaleAspectFill;
                 break;
         }
         playSurface.setUIViewContentMode(mode);

         nodePlayer = new NodePlayer(this,"c0KzkWKg5LoyRg+hR+2wtrnf/k61cQuoAibf2T8ghqFObNhHVuBiWqn28RhSSyAmLhcxuLVOXVLUf0Blk/axig==");
         nodePlayer.setNodePlayerDelegate(this);
         nodePlayer.setPlayerView(playSurface);

         /**
          * ���ò���ֱ����Ƶurl
          */
         nodePlayer.setInputUrl(uriPath);

         /**
          * ��������������ʱ��,��λ����.�˲�����ϵ��Ƶ�����ӳɹ���ʼ��ȡ���ݺ󻺳������ڶ��ٺ����ʼ����
          */
         nodePlayer.setBufferTime(500);

         /**
          * ������󻺳���ʱ��,��λ����.�˲�����ϵ��Ƶ��󻺳�ʱ��.
          * RTMP����TCPЭ�鲻����,���綶���һ���������,֮����Ȼ����ܵ������ڵĹ������ݰ�.
          * ���øĲ���,sdk�ڲ����Զ����������ֵ����ݰ��Ա�֤��������ۼ��ӳ�,ʼ����ֱ��ʱ���߱������maxBufferTime���ӳ�
          */
         nodePlayer.setMaxBufferTime(1000);

         /**
          *
          ����Ӳ������,֧��4.3����ϵͳ,��ʼ��ʧ���Զ���Ϊ�������,Ĭ�Ͽ���.
          */
         nodePlayer.setHWEnable(true);

         /**
          * �������ӳ�ʱʱ��,��λ����.Ĭ��Ϊ0 һֱ�ȴ�.
          * ���Ӳ���RTMP������,���ֲ����ӳɹ���,������һ�������ڵ�����ַʱ,��һֱ�ȴ���ȥ.
          * ���賬ʱ,���ø�ֵ.��ʱ�󷵻�1006״̬��.
          */
         // np.setConnectWaitTimeout(10*1000);

         /**
          * @brief rtmpdump ����connect����
          * Append arbitrary AMF data to the Connect message. The type must be B for Boolean, N for number, S for string, O for object, or Z for null.
          * For Booleans the data must be either 0 or 1 for FALSE or TRUE, respectively. Likewise for Objects the data must be 0 or 1 to end or begin an object, respectively.
          * Data items in subobjects may be named, by prefixing the type with 'N' and specifying the name before the value, e.g. NB:myFlag:1.
          * This option may be used multiple times to construct arbitrary AMF sequences. E.g.
          */
         // np.setConnArgs("S:info O:1 NS:uid:10012 NB:vip:1 NN:num:209.12 O:0");

         /**
          * ����RTSPʹ��TCP����ģʽ
          * ֧�ֵ�ģʽ��:
          * NodePlayer.RTSP_TRANSPORT_UDP
          * NodePlayer.RTSP_TRANSPORT_TCP
          * NodePlayer.RTSP_TRANSPORT_UDP_MULTICAST
          * NodePlayer.RTSP_TRANSPORT_HTTP
          */
         // np.setRtspTransport(NodePlayer.RTSP_TRANSPORT_TCP);

         /**
          * �ڱ��ؿ���һ��RTMP����,�����м�������,�������������ֻ����������������ֻ���ֱ�ӽ��в���,�������ķ�����֧��
          * ���ŵ�ip�����Ǳ���IP,Ҳ������0.0.0.0,��������127.0.0.1
          * app/stream �ɼӿɲ���,ֻҪ˫��ƥ�����
          */
         // np.setLocalRTMP(true);
         nodePlayer.start();
    	
    }
    
    
   
    @Override
    protected void onPause() {
    	super.onPause();
    	  	

    }
    
    
    @Override
    protected void onDestroy() {
    	
    	
    	
    	   /**
         * ֹͣ����
         */
        nodePlayer.stop();

        /**
         * �ͷ���Դ
         */
        nodePlayer.release();
        super.onDestroy();
    
    
    	
    }
    
    

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.play_btn:
			
			
		     finish();
			
			break;	
		}
		
	}



	@Override
	public void onPrepared(MediaPlayer mp) {
		

		
	}



	@Override
	public void onEventCallback(NodePlayer player, int event, String msg) {
		
		  handler.sendEmptyMessage(event);
		
	}
	
	
	   private Handler handler = new Handler() {
           // �ص�����
           @Override
           public void handleMessage(Message msg) {
               super.handleMessage(msg);
               switch (msg.what) {
                   case 1000:
                       // ����������Ƶ
//                       ToastUtils.show(mContext, mContext.getString(R.string.toast_1000));
                       break;
                   case 1001:
                       // ��Ƶ���ӳɹ�
//                       ToastUtils.show(mContext, mContext.getString(R.string.toast_1001));
                       isStarting = true;
                       break;
                   case 1002:
                       // ��Ƶ����ʧ�� ����ַ�����ڣ����߱��������޷��ͷ����ͨ�ţ��ص����5��������� ��ֹͣ
//                     ToastUtils.show(mContext, mContext.getString(R.string.toast_1002));
                       break;
                   case 1003:
                       // ��Ƶ��ʼ����,�Զ������ܿ���
//                      ToastUtils.show(mContext, mContext.getString(R.string.toast_1003));
                       break;
                   case 1004:
                       // ��Ƶ���Ž���
//                      ToastUtils.show(mContext, mContext.getString(R.string.toast_1004));
                       isStarting = false;
                       break;
                   case 1005:
                       // �����쳣,�����ж�,������;�����쳣���ص����1����������粻��Ҫ����ֹͣ
//                       ToastUtils.show(mContext, mContext.getString(R.string.toast_1005));
                       break;

               }
           }
       };
	
	
}
