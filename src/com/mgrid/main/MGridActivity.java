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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.arcsoft.facedetection.AFD_FSDKEngine;
import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facedetection.AFD_FSDKFace;
import com.arcsoft.facedetection.AFD_FSDKVersion;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.lsy.Service.TilmePlush.TimePlushService;
import com.mgrid.MyDialog.SelfDialog;
import com.mgrid.data.DataGetter;
import com.mgrid.main.broadcastreceiver.ScreenBroadcaseReceiver;
import com.mgrid.main.face.FaceBase;
import com.mgrid.main.user.User;
import com.mgrid.main.user.UserManager;
import com.mgrid.uncaughtexceptionhandler.MyApplication;
import com.mgrid.util.FileUtil;
import com.mgrid.util.ImageUtil;
import com.mgrid.util.LoginUtil;
import com.mgrid.util.MediaUtil;
import com.mgrid.util.XmlUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.common.RemoveRunableCallBack;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtIniReader;
import com.sg.uis.LsyNewView.AlarmAction.SgAlarmAction;
import com.sg.uis.LsyNewView.AlarmAction.SgAlarmChangTime;
import com.sg.uis.newView.AlarmShieldTime;
import com.sg.uis.newView.ChangeLabelBtn;
import com.sg.uis.newView.NBerDoorView;
import com.sg.uis.newView.SgSplineChart;
import com.sg.uis.newView.SgVideoView;
import com.sg.uis.oldView.SaveEquipt;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore.Audio.Media;
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
	
	private void StopTimeFlush() {
		
		Intent intent = new Intent(this, TimePlushService.class);
		stopService(intent);
		
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
		ViewJosnObject = new HashMap<>();
		ViewSetBackObject = new HashMap<>();

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 强制为横屏
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);// 输入法窗口
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);// 对该window进行硬件加速.
		// 设置屏幕宽高
		mContainer = new ContainerView(this);
		MainWindow.SCREEN_WIDTH = 1024;
		MainWindow.SCREEN_HEIGHT = 768;

		setBroadcastReceiver(); // 注册广播

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

	/**
	 * 广播注册 监听屏幕亮 熄
	 */
	private void setBroadcastReceiver() {

		sBroadcaseReceiver = new ScreenBroadcaseReceiver(MGridActivity.this);
		sBroadcaseReceiver.startReceiver();

	}
	
	

	private boolean parseMgridIni() {

		try {
			iniReader = new UtIniReader(Environment.getExternalStorageDirectory().getPath() + "/MGrid.ini");
		} catch (Exception e) {
			iniReader = null;
			e.printStackTrace();
			new AlertDialog.Builder(this).setTitle("错误").setMessage("读取配置文件 [ MGrid.ini ] 异常，停止加载！\n详情：" + e.toString())
					.show();
		}

		if (iniReader == null) {
			return false;
		}

		LanguageStr.iniLanguage = iniReader.getValue("SysConf", "Language");
		LanguageStr.whatLanguageSystem(context);
		LanguageStr.setLanguage();
		Load = LanguageStr.Load;
		PSS = LanguageStr.PSS;
		PSF = LanguageStr.PSF;

		m_sRootFolder = iniReader.getValue("SysConf", "FolderRoot");
		m_sMainPage = iniReader.getValue("SysConf", "MainPage");
		m_UserName = iniReader.getValue("SysConf", "UserName", "admin");
		m_PassWord = iniReader.getValue("SysConf", "PassWord", "12348765");
		alarmWay = iniReader.getValue("SysConf", "ControlAlarmWay");
		OPENWEB = Boolean.parseBoolean(iniReader.getValue("SysConf", "OpenWeb", "true"));

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
				Toast.makeText(this, "Mgrid.ini文件中SaveTime属性设置异常,已经恢复默认值", 1000).show();
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
		// 获取用户管理方式
		m_UserAway = Integer.parseInt(iniReader.getValue("SysConf", "UserAway", "0"));
		// 获取用户页面个数
		m_MaskCount = Integer.parseInt(iniReader.getValue("SysConf", "MaskCount", "0"));
		// 获取控制模式
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
				String te = iniReader.getValue("SysConf", "Time" + i, "20991230");
				if (id == null || pw == null) {
					continue;
				}

				User user = new User(id, pw, i + "", te);
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
				Toast.makeText(getApplicationContext(), "ini文件邮箱填写不规范", 1000).show();
				m_bTakeEMail = false;
			}

		} catch (Exception e) {
			mailProtocol = "smtp"; // 协议
			myEmailSMTPHost = "smtp.qq.com";
			myEmailAccount = "453938089@qq.com"; // 发送邮箱账号
			myEmailPassword = "sgipglsayogvcaih"; // 授权码
			receiveMailAccount = "leisiyang521@163.com"; // 接收邮箱账号
			Subject = "标题"; // 邮箱标题
			fromName = "发件人名称"; // 发件人名称
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

	private void parseView() {

		isBuiltHtml();

		if (parsePageList()) {
			loadOtherPage();
		} else {
			showTaskUI(true);
		}

	}

	private void runDataGetter() {
		mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

		DataGetter.currentPage = m_sMainPage;
		mDataGetter = new DataGetter(this);
		mDataGetter.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		mDataGetter.start();
	}

	private void initServiceManeger() {
		mServerManager = new ServerManager(this);
		mServerManager.register();
		mServerManager.startService();
	}

	private boolean parsePageList() // 解析Pagelist
	{
		String line = "";
		MainWindow page = null;
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(
							Environment.getExternalStorageDirectory().getPath() + m_sRootFolder + "pagelist"),
					"gb2312")); // 获取页面列表

			DataGetter.bIsLoading = true;
			for (int i = 0; i < 1024; i++) {

				if ((line = reader.readLine()) == null)
					break;

				line = line.trim(); // 移除两边的空白字符
				if (line.isEmpty())
					continue;

				m_oPageList.add(line);

				if (!line.equals(m_sMainPage)) {
					continue;
				}

				page = new MainWindow(this);
				page.m_strRootFolder = m_sRootFolder;// 路径
				page.m_bHasRandomData = m_bHasRandomData;// 是否使用随机数据
				page.loadPage(line);
				page.active(false);

				page.setVisibility(View.GONE);
				m_oViewGroups.put(line, page);
				mContainer.addView(page, 1024, 768);

			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			new AlertDialog.Builder(this).setTitle("错误").setMessage("读取配置文件 [ pagelist ] 异常，停止加载！\n详情：" + e.toString())
					.show();

			return false;
		}

		m_oSgSgRenderManager = m_oViewGroups.get(m_sMainPage);
		if (null == m_oSgSgRenderManager) {
			new AlertDialog.Builder(this).setTitle("错误").setMessage("找不到主页 [ " + m_sMainPage + " ] ！").show();
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

			requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// 全屏设置
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			// Window级控制硬件加速
			// setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);

			setContentView(mContainer);
			mContainer.requestFocus();

			showTaskUI(false);

			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);// 解决android软键盘挡住输入框问题！
		} else {

			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			requestWindowFeature(Window.FEATURE_PROGRESS);
			setContentView(R.layout.main);
			setProgressBarVisibility(true);
			setProgressBarIndeterminateVisibility(true);
			return false;
		}

		if (ISOPENFACE) {
			loginUtil = new LoginUtil(this);
			loginUtil.showListDialog();
		}

		return true;
	}

	// 判断程序是否进入后台

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

						initSuccese();
						return;
					}
					handler.postDelayed(this, tmp_load_int_time);
				} else {
					//System.out.println(tmp_load_pageseek);
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
						new AlertDialog.Builder(MGridActivity.this).setTitle("错误")
								.setMessage("加载页面 [ " + pagename + " ] 出现异常，停止加载！\n详情：" + e.toString()).show();
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
						
						
						initSuccese();
						
					}
				}
			} // end of run
		};

		handler.postDelayed(runnable, tmp_load_int_time);
		runDataGetter();
		if (OPENWEB) {
			initServiceManeger();
		}

	}
	
	
	private void initSuccese()
	{
		tmp_flag_loading = false;
		DataGetter.bIsLoading = false;
		isChangPage = true;

		Toast.makeText(MGridActivity.this, Load, Toast.LENGTH_LONG).show();
		isNOChangPage = true;
		isLoading = false;

		if (isBulitHtml) {
			String str = Environment.getExternalStorageDirectory().getPath()
					+ "/vtu_pagelist/refresh.ini";
			File file = new File(str);
			try {
				file.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		Log.d("Mgrid", "所用时间：" + (System.currentTimeMillis() - starttime));

	}
	

	// 修改密码
	public static void changPassWord(String type, String newPassWord) {
		textReplace(type, m_PassWord, newPassWord, -1);
		m_PassWord = newPassWord;
	}

	public static void changPassWord(String type, String newPassWord, int count) {
		textReplace(type, m_pagePassWord[count - 1], newPassWord, count);
		m_pagePassWord[count - 1] = newPassWord;

	}

	// 文本替换
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

		// 提供参数创建一个新的3D翻滚动画
		// 这个动画监听器用来触发下一个动画
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
				new AlertDialog.Builder(this).setTitle("提示！").setMessage("目标页面正在加载中 …").show();
			else
				new AlertDialog.Builder(this).setTitle("错误！").setMessage("无法找到组态页面： " + pagename).show();
			isChangGif = false;
			return;
		}

		m_oSgSgRenderManager.active(false);
		m_oSgSgRenderManager = m_oViewGroups.get(pagename);
		m_oSgSgRenderManager.active(true);

		// 不再使用设置显示 View 方案，以下面操作代替 -- CharlesChen May 8, 2014.
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

	/** 显示/隐藏任务菜单 */
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

	/** 调用一些刷新UI的函数 **/
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

	/** 消息提示显示 **/
	void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/** 消息提示显示 **/
	void showLoadProgToast(CharSequence msg, int duration) {
		if (m_bShowLoadProgress)
			Toast.makeText(this, msg, duration).show();
	}

	// 页面finish时执行
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (sBroadcaseReceiver != null) {
			sBroadcaseReceiver.stopReceiver();
		}

		if (mServerManager != null) {
			mServerManager.stopService();
		}
		
		MediaUtil.getMediaUtil().stopService(this);
		
		if(!SIP.equals(""))
		{
			StopTimeFlush();
		}
		
		for (RemoveRunableCallBack re : lstRemoveRunable) {
			
			re.removeAllRunable();
			
		}
		
		

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		if (!isAppOnForeground() && !ISFACEACTIVITY) {

			showTaskUI(true);
			
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		MGridActivity.ISFACEACTIVITY = false;
		LoginUtil.isPlay = false;

		if (requestCode == 111) {

			if (MyApplication.getUri() != null) {

				if (selfDialog == null) {
					selfDialog = new SelfDialog(context);

				}

				selfDialog.show();
				selfDialog.settext("注册中...");

				imageUtil = new ImageUtil(this);
				String file = imageUtil.getPath(MyApplication.getUri());
				bitMap = MyApplication.decodeImage(file);

				if (bitMap == null) {
					selfDialog.dismiss();
					return;
				}

				engine = new AFR_FSDKEngine();
				face1 = new AFR_FSDKFace();

				MGridActivity.xianChengChi.execute(new Runnable() {

					@Override
					public void run() {

						new Thread(new Runnable() {
							@Override
							public void run() {

								byte[] msgData = imageUtil.getNV21(bitMap.getWidth(), bitMap.getHeight(), bitMap);

								face1 = new AFR_FSDKFace();
								AFD_FSDKEngine engine_F = new AFD_FSDKEngine();
								AFD_FSDKVersion version = new AFD_FSDKVersion();
								List<AFD_FSDKFace> result = new ArrayList<AFD_FSDKFace>();
								AFD_FSDKError err = engine_F.AFD_FSDK_InitialFaceEngine(appid, fd_key,
										AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT, 16, 5);
								Log.d("TAG", "," + err.getCode());
								err = engine_F.AFD_FSDK_GetVersion(version);
								Log.d("TAG", ", " + err.getCode());
								err = engine_F.AFD_FSDK_StillImageFaceDetection(msgData, bitMap.getWidth(),
										bitMap.getHeight(), AFD_FSDKEngine.CP_PAF_NV21, result);
								Log.d("TAG", ", " + err.getCode() + "<" + result.size());

								if (result.size() <= 0) {

									Log.e("MGRID", "no faces");
									MGridActivity.handler.sendEmptyMessage(FACE_ERR);

									return;
								}

								AFR_FSDKError errs = engine.AFR_FSDK_InitialEngine(appid, fr_key);
								errs = engine.AFR_FSDK_ExtractFRFeature(msgData, bitMap.getWidth(), bitMap.getHeight(),
										AFR_FSDKEngine.CP_PAF_NV21, new Rect(result.get(0).getRect()),
										result.get(0).getDegree(), face1);
								Log.e("AFR_FSDK", err.getCode() + "");

								FaceBase facebase = new FaceBase(face1, bitMap);
								Message msg = new Message();
								msg.obj = facebase;
								msg.what = FACE_SUCC;
								MGridActivity.handler.sendMessage(msg);

							}
						}).start();

					}
				});

			}

		} else if (requestCode == 222) {
			if (data != null) {
				String str = data.getStringExtra("Type");
				if (str != "false") {
					loginUtil.showListDialog();
				}
			}

		}

	}

	// 重启应用
	private void restartApplication() {
		final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * 是否重新生存网页
	 */
	private void isBuiltHtml() {

		String str = Environment.getExternalStorageDirectory().getPath() + "/vtu_pagelist/refresh.ini";
		File file = new File(str);

		if (!file.exists()) {

			Log.e("Main", "创建html");

			isBulitHtml = true;

		} else {
			isBulitHtml = false;

		}
	}

	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			Object obj = msg.obj;

			// handler接收到消息后就会执行此方法
			switch (msg.what) {
			case 1:

				break;
			case 2:
				String s = (String) msg.obj;
				if (context != null)
					new AlertDialog.Builder(context).setTitle("错误")
							.setMessage("页面：" + MGridActivity.XmlFile + "\n" + "读取表达式 异常，停止加载！\n详情：" + s).show();

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

			case FACE_ERR:

				selfDialog.dismiss();
				Toast.makeText(context, "未找到人脸，注册失败，请重试", Toast.LENGTH_LONG).show();
				break;

			case FACE_SUCC:

				selfDialog.dismiss();

				Toast.makeText(context, "识别成功", Toast.LENGTH_LONG).show();
				FaceBase face = (FaceBase) obj;
				loginUtil.showAddFaceName(face);

				// MyApplication.mFaceDB.addFace("lsy", face1, bitMap);
				// faceList.add(face1);
				// MGridActivity.handler.sendEmptyMessage(FACE_SUCC);

				break;
			}
			super.handleMessage(msg);
		}
	};

	private ScreenBroadcaseReceiver sBroadcaseReceiver;

	// 解析Mgrid.ini
	public UtIniReader iniReader = null;

	// 网页服务管理
	public ServerManager mServerManager;
	public static boolean OPENWEB = true;
	public static boolean isBulitHtml = true;

	// SNMP功能
	public static boolean OPENSNMP = false;

	public static int sleepTime = 2 * 60 * 60;// 屏保视频休眠时间
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
	public WakeLock mWakeLock;// 锁屏类
	public static SgVideoView svv = null; // 播放视频
	public Handler mTimeHandler = new Handler();

	/**
	 * 喇叭告警声音的路径 因为原路径会导致文件删除不干净 所以生成一个新的路径
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
	// 加载用
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

	public static boolean m_bTakeEMail = false; // 是否实时告警邮件发送
	public static String mailProtocol = "smtp"; // 协议
	public static String myEmailSMTPHost = "smtp.qq.com";
	public static String myEmailAccount = "453938089@qq.com"; // 发送邮箱账号
	public static String myEmailPassword = "sgipglsayogvcaih"; // 授权码
	public static String receiveMailAccount = "leisiyang521@163.com"; // 接收邮箱账号
	public static String Subject = "标题"; // 邮箱标题
	public static String fromName = "发件人名称"; // 发件人名称d

	public static boolean whatLanguage = true;// 系统语言
	public static Map<String, Map<String, String>> EventClose = new HashMap<String, Map<String, String>>();
	public static HashMap<String, ArrayList<String>> AlarmShow = new HashMap<String, ArrayList<String>>();
	public static ExecutorService xianChengChi = Executors.newCachedThreadPool();
	public static ExecutorService ecOneService = Executors.newSingleThreadExecutor();
	public static boolean isNOChangPage = false;
	public static int saveTime; // 信号数据存储时间
	public static HashMap<String, List<ViewObjectBase>> ViewJosnObject = null;
	public static HashMap<String, Map<String, ViewObjectSetCallBack>> ViewSetBackObject = null;
	// public static HashMap<String,TreeMap<Integer, ViewObjectBase>> ViewJosnObject
	// = null;
	// 用户名和密码
	public static String m_UserName;
	public static String m_PassWord;

	// 页面权限用户名和密码
	// public static String m_pageUserName; //未使用过 移除
	public static String[] m_pagePassWord;

	// lsy 18/6/20新增 user管理功能
	public static int m_UserAway = 0;// 0代表老版本，1代表每个权限页面需要密码登录，可记录。 2代表用户名 密码登录后进行操作。

	public static String[][] m_MaskPage;// 权限页面内的子页面
	public static int m_MaskCount;// 总权限页面的个数
	public static int m_ControlAway;// 控制模式 0默认不输入密码 1输入密码。

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
	public static List<RemoveRunableCallBack> lstRemoveRunable=new ArrayList<>();

	public static String appid = "9RJnJmPrCEbK6ie6CC6CQnti1Hs2J5iBpe8vwVL9ESnf";
	public static String ft_key = "9jsBZdSCqsEo21Uo7a9rvUHonVCB8Cz8yweMF1WNrAgK";
	public static String fd_key = "9jsBZdSCqsEo21Uo7a9rvUHvwtTQmZJ7ioFFErJYjNRQ";
	public static String fr_key = "9jsBZdSCqsEo21Uo7a9rvUJRbVW2WJxAMswBrHm4iPxG";
	public static String age_key = "9jsBZdSCqsEo21Uo7a9rvUJfvJ2QJJTrQNxr9siQke4C";
	public static String gender_key = "9jsBZdSCqsEo21Uo7a9rvUJo5hHb2i566abL6cePLmsP";

	public static List<AFR_FSDKFace> faceList = new ArrayList<>();

	// 告警屏蔽时间保存
	public static HashMap<String, HashMap<Long, String>> AlarmShieldTimer = new HashMap<String, HashMap<Long, String>>();

	// 人脸识别
	public static LoginUtil loginUtil;
	private ImageUtil imageUtil;
	private Bitmap bitMap;
	private AFR_FSDKFace face1;
	private AFR_FSDKEngine engine;
	private static SelfDialog selfDialog = null;

	public static final int FACE_ERR = 6;
	public static final int FACE_SUCC = 7;
	public static boolean ISFACEACTIVITY = false;
	public static boolean ISOPENFACE = false;

	/**
	 * 以下代码为内部类 This class listens for the end of the first half of the animation.
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
	 * 该类负责切换View界面 并执行后半部分的动画效果
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
			 * j 两种切换角度的尝试 if (mPosition > -1) { mPhotosList.setVisibility(View.GONE);
			 * mImageView.setVisibility(View.VISIBLE); mImageView.requestFocus();
			 * 
			 * rotation = new Rotate3dAnimation(90, 180, centerX, centerY, 310.0f, false); }
			 * else { mImageView.setVisibility(View.GONE);
			 * mPhotosList.setVisibility(View.VISIBLE); mPhotosList.requestFocus();
			 * 
			 * rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false); }
			 */

			// TODO: 暂时尝试做圆周翻滚效果，以后有时间再调校最佳效果 -- CharlesChen
			rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false); // 再次优化， 从270度开始翻滚可避免反转。

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mContainer.startAnimation(rotation);
		}
	} /* end of class SwapViews */

}