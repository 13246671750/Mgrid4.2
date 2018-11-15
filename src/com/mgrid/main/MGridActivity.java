package com.mgrid.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lsy.Service.TilmePlush.TimePlushService;
import com.mgrid.VariableConfig.VariableConfig;
import com.mgrid.data.DataGetter;
import com.mgrid.main.user.User;
import com.mgrid.main.user.UserManager;
import com.mgrid.util.FileUtil;
import com.mgrid.util.XmlUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtIniReader;
import com.sg.uis.LsyNewView.AlarmAction.SgAlarmAction;
import com.sg.uis.LsyNewView.AlarmAction.SgAlarmChangTime;
import com.sg.uis.LsyNewView.TimingAndDelayed.TimingAndDelayedView;
import com.sg.uis.newView.AlarmShieldTime;
import com.sg.uis.newView.ChangeLabelBtn;
import com.sg.uis.newView.NBerDoorView;
import com.sg.uis.newView.SgSplineChart;
import com.sg.uis.newView.SgVideoView;
import com.sg.uis.oldView.SaveEquipt;
import com.sg.uis.oldView.SgImage;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import comm_service.service;
import data_model.ipc_control;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("InlinedApi")
@SuppressWarnings("deprecation")
public class MGridActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();
	}

	private void startTimeFlush() {
		Intent intent = new Intent(this, TimePlushService.class);
		startService(intent);
	}

	public void Mainfinish() {
		this.finish();
	}

	private void init() {

		starttime = System.currentTimeMillis();
		context = this;
		m_oViewGroups = new HashMap<String, MainWindow>();
		m_oPageList = new ArrayList<String>();
		userManager = new UserManager();
		ViewJosnObject=new HashMap<>();
		ViewSetBackObject=new HashMap<>();

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// ǿ��Ϊ����
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);// ���뷨����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);// �Ը�window����Ӳ������.
		// ������Ļ���
		mContainer = new ContainerView(this);
		MainWindow.SCREEN_WIDTH = 1024;
		MainWindow.SCREEN_HEIGHT = 768;

		setBroadcastReceiver(); // ע��㲥
		if (parseMgridIni()) {
			// setProgressDialog();
			if (!SIP.equals("")) {
				startTimeFlush();
			}
			parseView();
		} else {
			showTaskUI(true);
		}
		
		
		

	}

	// �㲥ע��
	private void setBroadcastReceiver() {
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);// ������Ļ ��
		filter.addAction(Intent.ACTION_SCREEN_OFF);// ������Ļ ��
		// ͨ���㲥��������Ϣ
		BroadcastReceiver BroastcastScreenOn = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {

				if (arg1.getAction().equals(Intent.ACTION_SCREEN_ON)) { // ������Ļ��

					// ���չ����Ƿ���
					if (m_bTakePhoto) {
						// �����չ���
						// final CameraUtils cameraUtils = new CameraUtils(getApplicationContext());
						// cameraUtils.openCamera();
						// ��������ҳ��
						isNOChangPage = false;
						Intent intent = new Intent(MGridActivity.this, CameraActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);

					}
				}

				if (arg1.getAction().equals(Intent.ACTION_SCREEN_OFF)) {// ������ĻϨ��

					if (!isLoading && isPlaygif) {// �ж��Ƿ������ɲ��ҿ�������gif����
						onPageChange("gif.xml");
						if (isChangGif) {
							SgImage.isChangColor = false;
							acquireWakeLock();
						}
					} else if (!isLoading && isPlaymv) {// �ж��Ƿ������ɲ��ҿ�������mv����
						if (isChangGif) {
							if (isSleep) {
								SgImage.isChangColor = true;
								onPageChange(m_sMainPage);
							} else {
								onPageChange("mv.xml");
								SgImage.isChangColor = false;
								acquireWakeLock();
								mTimeHandler.postDelayed(runTime, sleepTime * 1000);
							}
						}
					} else if (!isLoading) {
						onPageChange(m_sMainPage);
					}
				}
			}
		};
		getApplicationContext().registerReceiver(BroastcastScreenOn, filter);
	}

	private boolean parseMgridIni() {

		try {
			iniReader = new UtIniReader(Environment.getExternalStorageDirectory().getPath() + "/MGrid.ini");
		} catch (Exception e) {
			iniReader = null;
			e.printStackTrace();
			new AlertDialog.Builder(this).setTitle("����").setMessage("��ȡ�����ļ� [ MGrid.ini ] �쳣��ֹͣ���أ�\n���飺" + e.toString())
					.show();
		}

		if (iniReader == null) {
			return false;
		}

		LanguageStr.iniLanguage = iniReader.getValue("SysConf", "Language");
		LanguageStr.whatLanguageSystem(context);
		;
		LanguageStr.setLanguage();
		Load = LanguageStr.Load;
		PSS = LanguageStr.PSS;
		PSF = LanguageStr.PSF;

		m_sRootFolder = iniReader.getValue("SysConf", "FolderRoot");
		m_sMainPage = iniReader.getValue("SysConf", "MainPage");
		m_UserName = iniReader.getValue("SysConf", "UserName", "admin");
		m_PassWord = iniReader.getValue("SysConf", "PassWord", "12348765");
		alarmWay = iniReader.getValue("SysConf", "ControlAlarmWay");
		if (alarmWay != null && alarmWay.equals("wav")) {
			xianChengChi.execute(new Runnable() {

				@Override
				public void run() {

					synchronized (MGridActivity.NewWavPath) {

						FileUtil file = new FileUtil();
						file.copyFile(MGridActivity.oldWavPath, MGridActivity.NewWavPath);

					}

				}
			});
		}
		SIP = iniReader.getValue("SysConf", "SIP", "");

		xianChengChi.execute(new Runnable() {

			@Override
			public void run() {

				if (alarmWay != null) {
					if (alarmWay.equals("DO1")) {
						ipc_control ipc = new ipc_control();
						ipc.equipid = 1;
						XmlUtils xml = XmlUtils.getXml();
						NodeList list = xml.getCommandlist();
						if (list != null) {
							for (int i = 0; i < list.getLength(); i++) {
								Element e = (Element) list.item(i);
								String name = e.getAttribute("CommandName");
								if (name.equals("DO1")) {
									String ctrlid = e.getAttribute("CommandId");
									ipc.ctrlid = Integer.parseInt(ctrlid);
									break;
								}
							}
						}
						ipc.valuetype = 1;
						ipc.value = "1";
						lstCtrlDo1 = new ArrayList<ipc_control>();
						lstCtrlDo1.add(ipc);
					} else if (alarmWay.equals("DO2")) {
						ipc_control ipc = new ipc_control();
						ipc.equipid = 1;
						XmlUtils xml = XmlUtils.getXml();
						NodeList list = xml.getCommandlist();
						if (list != null) {
							for (int i = 0; i < list.getLength(); i++) {
								Element e = (Element) list.item(i);
								String name = e.getAttribute("CommandName");
								if (name.equals("DO2")) {
									String ctrlid = e.getAttribute("CommandId");
									ipc.ctrlid = Integer.parseInt(ctrlid);
									break;
								}
							}
						}
						ipc.valuetype = 1;
						ipc.value = "1";
						lstCtrlDo2 = new ArrayList<ipc_control>();
						lstCtrlDo2.add(ipc);
					}
				}
			}
		});

		try {
			SaveEquipt.save_time = Integer.parseInt(iniReader.getValue("SysConf", "SaveTime", "24")) * 60 * 60;
			if (SaveEquipt.save_time < 3600) {
				SaveEquipt.save_time = 1 * 60 * 60;
			}

		} catch (Exception e) {

			try {
				SaveEquipt.save_time = (int) Float.parseFloat(iniReader.getValue("SysConf", "SaveTime", "24")) * 60
						* 60;
				if (SaveEquipt.save_time < 3600) {
					SaveEquipt.save_time = 1 * 60 * 60;
				}

			} catch (Exception e2) {
				Toast.makeText(this, "Mgrid.ini�ļ���SaveTime���������쳣,�Ѿ��ָ�Ĭ��ֵ", 1000).show();
				SaveEquipt.save_time = 24 * 60 * 60;
			}

		}

		String playWay = iniReader.getValue("SysConf", "playWay");
		if (playWay != null) {
			if (playWay.equals("gif")) {
				isPlaygif = true;
			} else if (playWay.equals("mv")) {
				isPlaymv = true;
			}
		}

		String time = iniReader.getValue("SysConf", "playTime");
		if (time != null) {
			try {
				sleepTime = Integer.parseInt(time) * 60;
			} catch (Exception e) {

				sleepTime = 2 * 60 * 60;
			}
		}

		// m_pageUserName = iniReader.getValue("SysConf", "MaskPageUser", "admin");
		// ��ȡ�û�����ʽ
		m_UserAway = Integer.parseInt(iniReader.getValue("SysConf", "UserAway", "0"));
		// ��ȡ�û�ҳ�����
		m_MaskCount = Integer.parseInt(iniReader.getValue("SysConf", "MaskCount", "0"));
		// ��ȡ����ģʽ
		m_ControlAway = Integer.parseInt(iniReader.getValue("SysConf", "ControlAway", "0"));

		if (m_MaskCount == 0) {
			m_MaskPage = new String[1][1];
			m_pagePassWord = new String[1];
			m_MaskPage[0][0] = iniReader.getDefPageValue("SysConf", "MaskPage");
			m_pagePassWord[0] = iniReader.getValue("SysConf", "MaskPagePassword", "admin");
			m_UserAway = 0;
		} else {
			m_MaskPage = new String[m_MaskCount][];
			if (m_UserAway == 0) {
				m_pagePassWord = new String[m_MaskCount];
			}
		}

		for (int i = 0; i < m_MaskCount; i++) {
			m_MaskPage[i] = iniReader.getPageValue("SysConf", "MaskPage" + (i + 1));
			if (m_UserAway == 0) {
				m_pagePassWord[i] = iniReader.getValue("SysConf", "MaskPagePassword" + (i + 1), "admin");
			}
		}

		if (m_UserAway > 0) {

			for (int i = 0; i < 10; i++) {

				String id = iniReader.getValue("SysConf", "User" + i);
				String pw = iniReader.getValue("SysConf", "PassWord" + i);
				String te = iniReader.getValue("SysConf", "Time" + i,"20991230");
				if (id == null || pw == null) {
					continue;
				}

				User user = new User(id, pw, i + "",te);
				userManager.addUser(i, user);

			}
		}

		m_bHasRandomData = Boolean.parseBoolean(iniReader.getValue("SysConf", "HasRandomData"));
		m_bBitmapHIghQuality = Boolean.parseBoolean(iniReader.getValue("SysConf", "BitmapHIghQuality"));
		m_bErrMsgParser = !Boolean.parseBoolean(iniReader.getValue("SysConf", "NoErrMsgParser"));
		m_bShowLoadProgress = Boolean.parseBoolean(iniReader.getValue("SysConf", "ShowLoadProgress", "true"));

		try {
			tmp_load_int_time = Integer.parseInt(iniReader.getValue("SysConf", "LoadingInterval"));
		} catch (java.lang.NumberFormatException e) {
			tmp_load_int_time = 200;
		}

		try {
			MainWindow.SWITCH_STYLE = Integer.parseInt(iniReader.getValue("SysConf", "UseAnimation"));
		} catch (java.lang.NumberFormatException e) {
			MainWindow.SWITCH_STYLE = 0;
		}

		try {
			m_bCanZoom = Boolean.parseBoolean(iniReader.getValue("SysConf", "WindowCanZoom"));
		} catch (Exception e) {
			m_bCanZoom = true;
		}
		try {
			m_bTakePhoto = Boolean.parseBoolean(iniReader.getValue("SysConf", "TakePhoto"));
		} catch (Exception e) {
			m_bTakePhoto = false;
		}

		try {
			m_bTakeEMail = Boolean.parseBoolean(iniReader.getValue("SysConf", "TakeEMail"));
		} catch (Exception e) {
			m_bTakeEMail = false;
		}

		try {
			String login = iniReader.getValue("SysConf", "IsLogin");
			if (login != null)
				isLogin = Boolean.parseBoolean(login);
			if (isLogin) {
				final File loginFile = new File(logeFilePath);
				if (!loginFile.exists())
					loginPassWord = iniReader.getValue("SysConf", "LoginPassWord", "12345678");
				else {
					MGridActivity.xianChengChi.execute(new Runnable() {

						@Override
						public void run() {

							loginPassWord = readText(loginFile);
						}
					});
				}
			} else {
				final File loginFile = new File(logeFilePath);
				if (loginFile.exists())
					loginFile.delete();
			}
		} catch (Exception e) {
			isLogin = false;
		}

		try {

			mailProtocol = iniReader.getValue("SysConf", "MailProtocol");
			myEmailSMTPHost = iniReader.getValue("SysConf", "MyEmailSMTPHost");
			myEmailAccount = iniReader.getValue("SysConf", "MyEmailAccount");
			myEmailPassword = iniReader.getValue("SysConf", "MyEmailPassword");
			receiveMailAccount = iniReader.getValue("SysConf", "ReceiveMailAccount");
			Subject = iniReader.getValue("SysConf", "Subject");
			fromName = iniReader.getValue("SysConf", "FromName");

			if (m_bTakeEMail == true && (mailProtocol == null || myEmailSMTPHost == null || myEmailAccount == null
					|| myEmailPassword == null || receiveMailAccount == null || Subject == null || fromName == null)) {
				Toast.makeText(getApplicationContext(), "ini�ļ�������д���淶", 1000).show();
				m_bTakeEMail = false;
			}

		} catch (Exception e) {
			mailProtocol = "smtp"; // Э��
			myEmailSMTPHost = "smtp.qq.com";
			myEmailAccount = "453938089@qq.com"; // ���������˺�
			myEmailPassword = "sgipglsayogvcaih"; // ��Ȩ��
			receiveMailAccount = "leisiyang521@163.com"; // ���������˺�
			Subject = "����"; // �������
			fromName = "����������"; // ����������
		}

		CFGTLS.BITMAP_HIGHQUALITY = m_bBitmapHIghQuality;

		String strIPC_IP = iniReader.getValue("NetConf", "IPC_IP");
		if (null != strIPC_IP && !strIPC_IP.trim().isEmpty()) {
			service.IP = strIPC_IP.trim();
		}

		try {
			int port = Integer.parseInt(iniReader.getValue("NetConf", "IPC_PORT"));
			service.PORT = port;
		} catch (java.lang.NumberFormatException e) {
		}
		return true;
	}

	private String readText(File f) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "gb2312"));
			String str = reader.readLine();
			reader.close();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public Runnable runTime = new Runnable() {
		public void run() {
			if (svv != null) {
				svv.pauseMv();
				releaseWakeLock();
				isSleep = true;
			}
		}
	};

	public void acquireWakeLock() {
		if (mWakeLock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
			mWakeLock.acquire();
		}
	}

	public void releaseWakeLock() {
		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	private void parseView() {

		if (parsePageList()) {
			loadOtherPage();
		} else {
			showTaskUI(true);
		}

	}

	private void runDataGetter() {
		mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

		DataGetter.currentPage = m_sMainPage;
		mDataGetter = new DataGetter();
		mDataGetter.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		mDataGetter.start();
	}
	
	private void initServiceManeger()
	{
		mServerManager=new ServerManager(this);
		mServerManager.register();
		mServerManager.startService();
	}
	

	private boolean parsePageList() // ����Pagelist
	{
		String line = "";
		MainWindow page = null;
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(
							Environment.getExternalStorageDirectory().getPath() + m_sRootFolder + "pagelist"),
					"gb2312")); // ��ȡҳ���б�

			DataGetter.bIsLoading = true;
			for (int i = 0; i < 1024; i++) {

				if ((line = reader.readLine()) == null)
					break;

				line = line.trim(); // �Ƴ����ߵĿհ��ַ�
				if (line.isEmpty())
					continue;

				m_oPageList.add(line);

				if (!line.equals(m_sMainPage)) {
					continue;
				}

				page = new MainWindow(this);
				page.m_strRootFolder = m_sRootFolder;// ·��
				page.m_bHasRandomData = m_bHasRandomData;// �Ƿ�ʹ���������
				page.loadPage(line);
				page.active(false);

				page.setVisibility(View.GONE);
				m_oViewGroups.put(line, page);
				mContainer.addView(page, 1024, 768);

			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			new AlertDialog.Builder(this).setTitle("����").setMessage("��ȡ�����ļ� [ pagelist ] �쳣��ֹͣ���أ�\n���飺" + e.toString())
					.show();

			return false;
		}

		m_oSgSgRenderManager = m_oViewGroups.get(m_sMainPage);
		if (null == m_oSgSgRenderManager) {
			new AlertDialog.Builder(this).setTitle("����").setMessage("�Ҳ�����ҳ [ " + m_sMainPage + " ] ��").show();
			return false;
		}
		if (0 != mContainer.getChildCount() && null != m_oSgSgRenderManager) {
			// m_oPageList.trimToSize();

			m_oSgSgRenderManager.active(true);
			// setContentView(m_oSgSgRenderManager);

			mContainer.setClipChildren(false);
			mContainer.mCurrentView = m_oSgSgRenderManager;
			m_oSgSgRenderManager.setVisibility(View.VISIBLE);
			// m_oSgSgRenderManager.requestFocus();

			requestWindowFeature(Window.FEATURE_NO_TITLE); // ȡ������
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// ȫ������
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			// Window������Ӳ������
			// setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);

			setContentView(mContainer);
			mContainer.requestFocus();

			showTaskUI(false);

			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);// ���android����̵�ס��������⣡
		} else {

			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			requestWindowFeature(Window.FEATURE_PROGRESS);
			setContentView(R.layout.main);
			setProgressBarVisibility(true);
			setProgressBarIndeterminateVisibility(true);
			return false;
		}

		return true;
	}

	// �жϳ����Ƿ�����̨

	private boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	private void loadOtherPage() {
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			public void run() {
				// m_oSgSgRenderManager.notifylistflush();

				String pagename = m_oPageList.get(tmp_load_pageseek);

				if (pagename.equals(m_sMainPage)) {

					MainWindow mainPage = m_oViewGroups.get(pagename);
					mainPage.m_oPrevPage = tmp_load_prevpage;
					if (null != tmp_load_prevpage)
						tmp_load_prevpage.m_oNextPage = mainPage;
					tmp_load_prevpage = mainPage;

					tmp_load_pageseek++;
					if (m_oPageList.size() > tmp_load_pageseek) {
						// bar.setProgress(tmp_load_pageseek*100/m_oPageList.size());
						pagename = m_oPageList.get(tmp_load_pageseek);
					} else {

						tmp_flag_loading = false;
						DataGetter.bIsLoading = false;
						isChangPage = true;

						Toast.makeText(MGridActivity.this, Load, Toast.LENGTH_LONG).show();
						isNOChangPage = true;
						isLoading = false;

						
						System.out.println("����ʱ�䣺" + (System.currentTimeMillis() - starttime));

						return;
					}
					handler.postDelayed(this, tmp_load_int_time);
				} else {
					System.out.println(tmp_load_pageseek);
					MainWindow page = new MainWindow(MGridActivity.this);
					page.m_strRootFolder = m_sRootFolder;
					page.m_bHasRandomData = m_bHasRandomData;

					try {
						page.loadPage(pagename);
						page.active(false);
						page.setVisibility(View.GONE);
						m_oViewGroups.put(pagename, page);
						mContainer.addView(page, 1024, 768);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						tmp_flag_loading = false;
						DataGetter.bIsLoading = false;
						new AlertDialog.Builder(MGridActivity.this).setTitle("����")
								.setMessage("����ҳ�� [ " + pagename + " ] �����쳣��ֹͣ���أ�\n���飺" + e.toString()).show();
						return;
					}

					page.m_oPrevPage = tmp_load_prevpage;
					if (null != tmp_load_prevpage)
						tmp_load_prevpage.m_oNextPage = page;
					tmp_load_prevpage = page;

					tmp_load_pageseek++;

					if (m_oPageList.size() > tmp_load_pageseek) {

						// bar.setProgress(tmp_load_pageseek*100/m_oPageList.size());
						handler.postDelayed(this, tmp_load_int_time);

					} else {
						tmp_flag_loading = false;
						DataGetter.bIsLoading = false;
						isChangPage = true;
						Toast.makeText(MGridActivity.this, Load, Toast.LENGTH_LONG).show();
						isLoading = false;
						isNOChangPage = true;
						
						System.out.println("����ʱ�䣺" + (System.currentTimeMillis() - starttime));
					}
				}
			} // end of run
		};

		handler.postDelayed(runnable, tmp_load_int_time);
		runDataGetter();
		if(OPENWEB)
		{
		  initServiceManeger();
		}
		
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
						IPStringBuilder.append(inetAddress.getHostAddress().toString() + "\n");
					}
				}
			}
		} catch (SocketException ex) {

		}

		IP = IPStringBuilder.toString();
		return IP;
	}

	// �޸�����
	public static void changPassWord(String type, String newPassWord) {
		textReplace(type, m_PassWord, newPassWord, -1);
		m_PassWord = newPassWord;
	}

	public static void changPassWord(String type, String newPassWord, int count) {
		textReplace(type, m_pagePassWord[count - 1], newPassWord, count);
		m_pagePassWord[count - 1] = newPassWord;

	}

	// �ı��滻
	public static void textReplace(String type, String oldText, String newText, int count) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(Environment.getExternalStorageDirectory().getPath() + "/MGrid.ini"), "gb2312"));
			String MGridTxt = "";
			String line;
			while ((line = reader.readLine()) != null) {
				MGridTxt = MGridTxt + line + "&&&";
			}
			if (count != -1) {
				MGridTxt = MGridTxt.replaceAll(type + count + "=" + oldText, type + count + "=" + newText);
			} else {
				MGridTxt = MGridTxt.replaceAll(type + "=" + oldText, type + "=" + newText);
			}

			String[] MGridArgs = MGridTxt.split("&&&");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/MGrid.ini"),
					"gb2312"));
			for (int i = 0; i < MGridArgs.length; i++) {
				// System.out.println(MGridArgs[i]);
				writer.write(MGridArgs[i]);
				writer.newLine();
			}
			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void applyRotation(final String pagename, float start, float end) {
		// Find the center of the container
		final float centerX = mContainer.getWidth() / 2.0f;
		final float centerY = mContainer.getHeight() / 2.0f;

		// �ṩ��������һ���µ�3D��������
		// �����������������������һ������
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(pagename));

		mContainer.startAnimation(rotation);
	}

	public void onPageChange(String pagename) {

		if (null == m_oViewGroups.get(pagename)) {
			if (tmp_flag_loading)
				new AlertDialog.Builder(this).setTitle("��ʾ��").setMessage("Ŀ��ҳ�����ڼ����� ��").show();
			else
				new AlertDialog.Builder(this).setTitle("����").setMessage("�޷��ҵ���̬ҳ�棺 " + pagename).show();
			isChangGif = false;
			return;
		}

		m_oSgSgRenderManager.active(false);
		m_oSgSgRenderManager = m_oViewGroups.get(pagename);
		m_oSgSgRenderManager.active(true);

		// ����ʹ��������ʾ View ������������������� -- CharlesChen May 8, 2014.
		// setContentView(m_oSgSgRenderManager);

		mContainer.mCurrentView.setVisibility(View.GONE);
		mContainer.mCurrentView = m_oSgSgRenderManager;
		mContainer.mCurrentView.setVisibility(View.VISIBLE);
		// mContainer.bringChildToFront(mContainer.mCurrentView);

		DataGetter.currentPage = pagename;

		isChangGif = true;
		isSleep = false;

		xianChengChi.execute(new Runnable() {

			@Override
			public void run() {

				Iterator<String> iter = m_oSgSgRenderManager.m_mapUIs.keySet().iterator();
				while (iter.hasNext()) {
					String strKey = iter.next();
					IObject obj = m_oSgSgRenderManager.m_mapUIs.get(strKey);
					if (obj.getType().equals("AlarmAction")) {
						SgAlarmAction sg = (SgAlarmAction) obj;
						sg.updateText();
					} else if (obj.getType().equals("SgAlarmChangTime")) {
						SgAlarmChangTime sa = (SgAlarmChangTime) obj;
						SgAlarmAction sg = (SgAlarmAction) MGridActivity.AlarmAll.get(sa.label);
						if (sg != null) {
							sa.updateText(sg.TimeLapse);
						}
					} else if (obj.getType().equals("SgVideoView")) {

						svv = (SgVideoView) obj;
						svv.startMv();
					} else if (obj.getType().equals("ChangeLabelBtn")) {

						ChangeLabelBtn CLB = (ChangeLabelBtn) obj;
						CLB.setText();
					} else if (obj.getType().equals("AlarmShieldTime")) {
						AlarmShieldTime ast = (AlarmShieldTime) obj;
						if (MGridActivity.AlarmShieldTimer.get(ast.equitId + "_" + ast.eventId) != null) {
							ast.updateText();
						}
					} else if (obj.getType().equals("NBerDoorView")) {
						NBerDoorView ast = (NBerDoorView) obj;
						ast.setHindText();
					} else if ("SgSplineChart".equals(obj.getType())) {

						SgSplineChart sc = (SgSplineChart) obj;

						if (sc.m_rRenderWindow != null && sc.m_rRenderWindow.m_bIsActive) {

							
							sc.setUpdata(true);
						}

					}

					Message msg = new Message();
					msg.obj = obj;
					handler.sendMessage(msg);

				}

			}
		});
	}

	/** ��ʾ/��������˵� */
	public void showTaskUI(boolean bShow) {
		if (m_oTaskIntent == null)
			m_oTaskIntent = new Intent();
		if (bShow) {
			m_oTaskIntent.setAction("android.intent.action.STATUSBAR_VISIBILITY");
			// m_oSgSgRenderManager.getContext().sendBroadcast(m_oTaskIntent);
			getApplicationContext().sendBroadcast(m_oTaskIntent);
		} else {
			m_oTaskIntent.setAction("android.intent.action.STATUSBAR_INVISIBILITY");
			// m_oSgSgRenderManager.getContext().sendBroadcast(m_oTaskIntent);
			getApplicationContext().sendBroadcast(m_oTaskIntent);
		}
	}

	/** ����һЩˢ��UI�ĺ��� **/
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onresume");

		if (m_oSgSgRenderManager == null)
			return;
		showTaskUI(false);

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	
	}

	/** ��Ϣ��ʾ��ʾ **/
	void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/** ��Ϣ��ʾ��ʾ **/
	void showLoadProgToast(CharSequence msg, int duration) {
		if (m_bShowLoadProgress)
			Toast.makeText(this, msg, duration).show();
	}

	// ҳ��finishʱִ��
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// restartApplication();
		releaseWakeLock();
		
		if(mServerManager!=null)
		{
			mServerManager.stopService();
		}
		

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("onStop");
		if (!isAppOnForeground()) {

			showTaskUI(true);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		System.out.println("onpause");
		// mWakeLock.acquire();

	}

	// ����Ӧ��
	private void restartApplication() {
		final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			Object obj = msg.obj;

			// handler���յ���Ϣ��ͻ�ִ�д˷���
			switch (msg.what) {
			case 1:

				break;
			case 2:
				String s = (String) msg.obj;
				if (context != null)
					new AlertDialog.Builder(context).setTitle("����")
							.setMessage("ҳ�棺" + MGridActivity.XmlFile + "\n" + "��ȡ���ʽ �쳣��ֹͣ���أ�\n���飺" + s).show();

				break;
			case 3:

				IObject Iobj = (IObject) obj;
				Iobj.initFinished();

				break;
			case 4:

				Toast.makeText(context, PSS, Toast.LENGTH_LONG).show();
				break;
			case 5:

				Toast.makeText(context, PSF, Toast.LENGTH_LONG).show();
				break;
			}
			super.handleMessage(msg);
		}
	};

	// ����Mgrid.ini
	public UtIniReader iniReader = null;
	
	//��ҳ�������
	public ServerManager mServerManager;
	public static boolean OPENWEB=false;

	private int sleepTime = 2 * 60 * 60;// ������Ƶ����ʱ��
	private Intent m_oTaskIntent = null;
	private MainWindow m_oSgSgRenderManager = null;
	private HashMap<String, MainWindow> m_oViewGroups = null;
	private String Load = "";
	private static String PSS = "";
	private static String PSF = "";

	private long starttime = 0;
	private DataGetter mDataGetter;
	private ContainerView mContainer;
	// private FlikerProgressBar bar;
	// private SelfDialog dialog = null;
	public static String mgridIniPath = Environment.getExternalStorageDirectory().getPath() + "/MGrid.ini";
	public static String logeFilePath = Environment.getExternalStorageDirectory().getPath() + "/login" + ".login";
	public WakeLock mWakeLock;// ������
	public SgVideoView svv = null; // ������Ƶ
	public Handler mTimeHandler = new Handler();

	/**
	 * ���ȸ澯������·�� ��Ϊԭ·���ᵼ���ļ�ɾ�����ɾ� ��������һ���µ�·��
	 */
	public static String oldWavPath = Environment.getExternalStorageDirectory().getPath() + "/vtu_pagelist/Alarm.wav";
	public static String NewWavPath = Environment.getExternalStorageDirectory().getPath() + "/Alarm.wav";

	public static boolean isPlaymv = false;
	public static boolean isPlaygif = false;
	public static boolean isSleep = false;
	public static boolean isLogin = false;
	public static String loginPassWord = "12345678";
	public static Context context = null;
	public static String XmlFile = "";
	public static String SIP = "192.168.1.238";
	public static String Language = "";

	public InputMethodManager mImm = null;
	// ������
	public int tmp_load_int_time = 20;
	public int tmp_load_pageseek = 0;
	public boolean tmp_flag_loading = true;
	public MainWindow tmp_load_prevpage = null;

	// Params:
	public static String m_sMainPage = null;
	public String m_sRootFolder = null;
	public ArrayList<String> m_oPageList = null;

	public static boolean m_bHasRandomData = false;
	public static boolean m_bBitmapHIghQuality = false;
	public static boolean m_bShowLoadProgress = true;
	public static boolean m_bErrMsgParser = true;
	public static boolean m_bCanZoom = true;
	public static boolean m_bTakePhoto = false;

	public static boolean m_bTakeEMail = false; // �Ƿ�ʵʱ�澯�ʼ�����
	public static String mailProtocol = "smtp"; // Э��
	public static String myEmailSMTPHost = "smtp.qq.com";
	public static String myEmailAccount = "453938089@qq.com"; // ���������˺�
	public static String myEmailPassword = "sgipglsayogvcaih"; // ��Ȩ��
	public static String receiveMailAccount = "leisiyang521@163.com"; // ���������˺�
	public static String Subject = "����"; // �������
	public static String fromName = "����������"; // ����������d

	public static boolean whatLanguage = true;// ϵͳ����
	public static Map<String, Map<String, String>> EventClose = new HashMap<String, Map<String, String>>();
	public static HashMap<String, ArrayList<String>> AlarmShow = new HashMap<String, ArrayList<String>>();
	public static ExecutorService xianChengChi = Executors.newCachedThreadPool();
	public static ExecutorService ecOneService = Executors.newSingleThreadExecutor();
	public static boolean isNOChangPage = false;
	public static int saveTime; // �ź����ݴ洢ʱ��
	public static HashMap<String,List<ViewObjectBase>> ViewJosnObject = null;
	public static HashMap<String,Map<String,ViewObjectSetCallBack>> ViewSetBackObject = null;
	//public static HashMap<String,TreeMap<Integer, ViewObjectBase>> ViewJosnObject = null;
	// �û���������
	public static String m_UserName;
	public static String m_PassWord;

	// ҳ��Ȩ���û���������
	// public static String m_pageUserName; //δʹ�ù� �Ƴ�
	public static String[] m_pagePassWord;

	// lsy 18/6/20���� user������
	public static int m_UserAway = 0;// 0�����ϰ汾��1����ÿ��Ȩ��ҳ����Ҫ�����¼���ɼ�¼�� 2�����û��� �����¼����в�����

	public static String[][] m_MaskPage;// Ȩ��ҳ���ڵ���ҳ��
	public static int m_MaskCount;// ��Ȩ��ҳ��ĸ���
	public static int m_ControlAway;// ����ģʽ 0Ĭ�ϲ��������� 1�������롣

	public static UserManager userManager;

	public static HashMap<String, IObject> AlarmAll = new HashMap<String, IObject>();
	public static File all_Event_file = new File("/mgrid/data/Command/0.log");

	public static boolean isChangPage = false;
	public static boolean isLoading = true;
	public static boolean isChangGif = true;
	public static ArrayList<String> LabelList = new ArrayList<String>();
	public static HashMap<String, stBindingExpression> m_DoubleButton = null;
	public static String usbName = "";
	public static String alarmWay = "";
	public static List<ipc_control> lstCtrlDo1 = null;
	public static List<ipc_control> lstCtrlDo2 = null;

	// �澯����ʱ�䱣��
	public static HashMap<String, HashMap<Long, String>> AlarmShieldTimer = new HashMap<String, HashMap<Long, String>>();

	/**
	 * ���´���Ϊ�ڲ��� This class listens for the end of the first half of the animation.
	 * It then posts a new action that effectively swaps the views when the
	 * container is rotated 90 degrees and thus invisible.
	 */
	private final class DisplayNextView implements Animation.AnimationListener {
		private final String mPageName;

		private DisplayNextView(String pagename) {
			mPageName = pagename;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			mContainer.post(new SwapViews(mPageName));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * ���ฺ���л�View���� ��ִ�к�벿�ֵĶ���Ч��
	 */
	private final class SwapViews implements Runnable {
		private final String mPageName;

		public SwapViews(String pagename) {
			mPageName = pagename;
		}

		public void run() {
			final float centerX = mContainer.getWidth() / 2.0f;
			final float centerY = mContainer.getHeight() / 2.0f;
			Rotate3dAnimation rotation;

			onPageChange(mPageName);

			/*
			 * j �����л��Ƕȵĳ��� if (mPosition > -1) { mPhotosList.setVisibility(View.GONE);
			 * mImageView.setVisibility(View.VISIBLE); mImageView.requestFocus();
			 * 
			 * rotation = new Rotate3dAnimation(90, 180, centerX, centerY, 310.0f, false); }
			 * else { mImageView.setVisibility(View.GONE);
			 * mPhotosList.setVisibility(View.VISIBLE); mPhotosList.requestFocus();
			 * 
			 * rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false); }
			 */

			// TODO: ��ʱ������Բ�ܷ���Ч�����Ժ���ʱ���ٵ�У���Ч�� -- CharlesChen
			rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false); // �ٴ��Ż��� ��270�ȿ�ʼ�����ɱ��ⷴת��

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mContainer.startAnimation(rotation);
		}
	} /* end of class SwapViews */

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == 1) {

		}
	}

}