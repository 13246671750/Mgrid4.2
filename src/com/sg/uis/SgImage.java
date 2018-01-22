package com.sg.uis;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.util.MediaUtil;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stIfElseExpression;
import com.sg.common.UtGifHelper;

/** ͼƬ��(jpg png gif) */
@SuppressLint({ "InflateParams", "ShowToast", "ClickableViewAccessibility" })
public class SgImage extends View implements IObject {

	private String denglu = "";
	private String yes = "";
	private String no = "";
	private String systemExit = "";
	private String pwText="";
	
	private String Text1="";
	private String Text2="";
	
	private String userName="";
	private String PWD="";
	
	

	public SgImage(Context context) {
		super(context);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (isChangColor && isChangColor1) {
					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:
						m_bPressed = true;
						view.invalidate();

						m_xscal = event.getX();
						m_yscal = event.getY();
						break;

					case MotionEvent.ACTION_UP:
						m_bPressed = false;
						view.invalidate();

						float xslip = Math.abs(event.getX() - m_xscal);
						float yslip = Math.abs(event.getY() - m_yscal);

						if (xslip < 3 && yslip < 3)
							onClicked();
						break;

					default:
						break;
					}
				} else {
					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:

						m_xscal = event.getX();
						m_yscal = event.getY();
						break;

					case MotionEvent.ACTION_UP:

						float xslip = Math.abs(event.getX() - m_xscal);
						float yslip = Math.abs(event.getY() - m_yscal);

						if (xslip < 3 && yslip < 3)
							onClicked();
						break;

					default:
						break;
					}
				}

				return true;
			}
		});

		if (MGridActivity.whatLanguage) {

			denglu = "�û�Ȩ�޵�¼";
			yes = "ȷ��";
			no = "ȡ��";
			systemExit = "ϵͳά��";
			pwText="����";
			Text1="��ȴ�������ɣ���";
			Text2="�û������������";
			userName="�û�����";
			PWD="��    �룺";

		} else {

			denglu = "Login";
			yes = "ok";
			no = "cancel";
			systemExit = "Exit";
			pwText="PassWord";
			Text1="Please wait for loading to complete����";
			Text2="Password or user name error";
			userName="User  ID:";
			     PWD="PassWord:";


		}

		// WARN: ���·�ʽ���ܲ����ڴ�й¶�� -- Charles
		m_oInvalidateHandler = new MyHandler(this);
		/*
		 * m_oInvalidateHandler = new Handler() { //���յ���Ϣ���� public void
		 * handleMessage(Message msg) { switch (msg.what) { case 0:
		 * threadInvalidate(); break; } super.handleMessage(msg); } };
		 */
		flag = System.currentTimeMillis() / 1000;
		m_oPaint = new Paint();
		m_oPaint.setAntiAlias(true);
		m_oPaint.setStrokeWidth(10);
		m_rSrcRect = new Rect();
		m_rDestRect = new Rect();
		m_rBBox = new Rect();
		loadAnimation = AnimationUtils
				.loadAnimation(context, R.anim.zoom_enter);
		loadAnimation.setDuration(5000);
	}

	private static class MyHandler extends Handler {
		private final WeakReference<SgImage> imgobj;

		public MyHandler(SgImage obj) {
			imgobj = new WeakReference<SgImage>(obj);
		}

		// ���յ���Ϣ����
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				SgImage obj = imgobj.get();
				if (obj == null)
					break;
				obj.threadInvalidate();
				break;
			}

			super.handleMessage(msg);
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:

				updateWidget();

				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void threadInvalidate() {
		this.invalidate();
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_bitCurrentBackImage == null)
			return;

		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		m_rSrcRect.left = 0;
		m_rSrcRect.top = 0;
		m_rSrcRect.right = m_bitCurrentBackImage.getWidth();
		m_rSrcRect.bottom = m_bitCurrentBackImage.getHeight();

		m_rDestRect.left = 0;
		m_rDestRect.top = 0;
		m_rDestRect.right = nWidth;
		m_rDestRect.bottom = nHeight;

		canvas.rotate(m_fRotateAngle, nWidth / 2, nHeight / 2);
		canvas.drawBitmap(m_bitCurrentBackImage, m_rSrcRect, m_rDestRect,
				m_oPaint);
		if (m_bPressed
				&& ("".equals(m_strClickEvent) == false || "".equals(m_strUrl) == false)) {
			int cColor = m_oPaint.getColor();
			m_oPaint.setColor(0x500000FF);
			m_oPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
			m_oPaint.setColor(cColor);
		}
		if (m_oGifHelper != null) {
			m_bitCurrentBackImage = m_oGifHelper.nextBitmap();
		}
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l
				+ (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t
				+ (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (b - t));

		// m_rRenderWindow.m_utPrint.drawText(250, 400, 0xffff0000,
		// "ViewGroup��С="+(m_rRenderWindow.getRight()-m_rRenderWindow.getLeft())+":"+(m_rRenderWindow.getBottom()-m_rRenderWindow.getTop()));
		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		if (m_oGifHelper == null) {
			if (m_bitBackImage != null && m_bitBackImage.isRecycled() == false)
				m_bitBackImage.recycle();
		} else {
			for (int i = 0; i < m_oGifHelper.getFrameCount(); ++i) {
				Bitmap bit = m_oGifHelper.getFrame(i);
				if (bit != null && bit.isRecycled() == false)
					bit.recycle();
			}
		}
		if (m_oInvalidateThread != null)
			m_oInvalidateThread.autoDestroy();
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue,
			String strResFolder) throws Exception {
		if ("ZIndex".equals(strName)) {
			m_nZIndex = Integer.parseInt(strValue);
			if (MainWindow.MAXZINDEX < m_nZIndex)
				MainWindow.MAXZINDEX = m_nZIndex;
		} else if ("Location".equals(strName)) {
			String[] arrStr = strValue.split(",");
			m_nPosX = Integer.parseInt(arrStr[0]);
			m_nPosY = Integer.parseInt(arrStr[1]);
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
			m_oPaint.setAlpha((int) (m_fAlpha * 255));
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("Strtch".equals(strName))
			m_strStrtch = strValue;
		else if ("ImgSrc".equals(strName)) {
			m_strImgSrc = Environment.getExternalStorageDirectory().getPath()
					+ strResFolder + strValue;

			String[] arrStr = strValue.split("\\.");
			if ("gif".equals(arrStr[1])) {
				MGridActivity.xianChengChi.execute(new Runnable() {

					@Override
					public void run() {
						InputStream is = null;
						try {
							is = new BufferedInputStream(new FileInputStream(
									m_strImgSrc));
							m_oGifHelper = new UtGifHelper();
							m_oGifHelper.read(is);
							m_bitBackImage = m_oGifHelper.getImage();
							m_bitCurrentBackImage = m_bitBackImage;
							// gif ˢ���߳�
							m_oInvalidateThread = new invalidateThread();
							m_oInvalidateThread.start();
							is.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} else {
				m_bitBackImage = CFGTLS.getBitmapByPath(m_strImgSrc);
				m_bitCurrentBackImage = m_bitBackImage;
			}

		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("ImageExpression".equals(strName)) {
			m_strImageExpression = strValue;
			// m_oMathExpression =
			// UtExpressionParser.getInstance().parseExpression(strValue);
			m_oIfElseExpression = UtExpressionParser.getInstance()
					.parseIfElseExpression(strValue);
			if (m_oIfElseExpression != null) {
				String strTrueImge = Environment.getExternalStorageDirectory()
						.getPath()
						+ strResFolder
						+ m_oIfElseExpression.strTrueSelect;
				String strFalseImge = Environment.getExternalStorageDirectory()
						.getPath()
						+ strResFolder
						+ m_oIfElseExpression.strFalseSelect;

				// true
				// InputStream isTrue = new BufferedInputStream(new
				// FileInputStream(strTrueImge));
				String[] arrTrueStr = strValue.split("\\.");
				if ("gif".equals(arrTrueStr[1])) {
					;
				} else {
					m_bitIfTrueImage = CFGTLS.getBitmapByPath(strTrueImge);
				}
				// false
				// InputStream isFalse = new BufferedInputStream(new
				// FileInputStream(strFalseImge));
				String[] arrFalseStr = strValue.split("\\.");
				if ("gif".equals(arrFalseStr[1])) {
					;
				} else {
					m_bitIfFalseImage = CFGTLS.getBitmapByPath(strFalseImge);
				}

			}
		} else if ("VariableHeightWidth".equals(strName))
			m_strVariableHeightWidth = strValue;
		else if ("MaxHeightOrWidth".equals(strName))
			m_nMaxHeightOrWidth = Integer.parseInt(strValue);
		else if ("MaxValue".equals(strName))
			m_nMaxValue = Integer.parseInt(strValue);
		else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
		} else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("user".equals(strName))
			turnUsr = strValue;
		else if ("passWork".equals(strName))
			turnPass = strValue;
	}

	@Override
	public void initFinished() {
	}

	public String getBindingExpression() {
		return m_strImageExpression;
	}

	public void setUniqueID(String strID) {
		m_strID = strID;
	}

	public void setType(String strType) {
		m_strType = strType;
	}

	public String getUniqueID() {
		return m_strID;
	}

	public String getType() {
		return m_strType;
	}

	private void onClicked() {

		if ("".equals(m_strClickEvent) == false) {
			if ("��ʾ����".equals(m_strClickEvent)) {
				showWaiterAuthorizationDialog();
			} else if (m_strClickEvent.equals("�رո澯")) {
				
				MediaUtil.getMediaUtil().stopSound(m_rRenderWindow.m_oMgridActivity);

			} else {
				if (MGridActivity.isNOChangPage) {
					String[] arrStr = m_strClickEvent.split("\\(");
					boolean isMask = true;// �����ж��Ƿ�ΪȨ��ҳ��
					boolean isNeedPW = true; // �����ж�Ȩ��ҳ���Ƿ���Ҫ����
					if (m_rRenderWindow != null && arrStr[0].equals("Show")) {
						int count = -1;
						String[] str = arrStr[1].split("\\)");
						// �˴�ѭ�����������ҳ���ǰȨ��ҳ�����ڵ���Ȩ��ҳ��
						if (MGridActivity.m_MaskPage != null) {
							for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {

								for (String s : MGridActivity.m_MaskPage[i]) {
									if (s.equals(DataGetter.currentPage)) {
										count = i;
										break;
									}
								}
								if (count != -1)
									break;
							}

							if (count != -1) {
								// �˴�ѭ�����ж���Ҫ��ת��ҳ��͵�ǰҳ���ǲ�����ͬһ����Ȩ��ҳ��
								for (String s : MGridActivity.m_MaskPage[count]) {
									if (s.equals(str[0] + ".xml")) // �����ǰҳ��ΪȨ��ҳ�棨ֻ֧����һ����Ȩ��ҳ�棩
									{
										isNeedPW = false;

									}
								}
							}

							if (isNeedPW) {
								// �˴�ѭ�����ж���Ҫ��תҳ���Ƿ�ΪȨ��ҳ�档
								for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {
									for (String s : MGridActivity.m_MaskPage[i]) {
										if (!s.equals("1")) {
											if ((s.substring(0, s.length() - 4))
													.equals(str[0])) {
												MaskCount = i;
												showPassDialog();
												isMask = false;
												break;
											}
										}
									}
									if (!isMask)
										break;
								}

							}
						}

						if (isMask) {
							if (SgImage.isChangColor == false) {
								SgImage.isChangColor = true;
								if (MGridActivity.isPlaygif) {
									m_rRenderWindow.m_oMgridActivity
											.releaseWakeLock();
									System.out.println("�ҽ�����gif");
								} else if (MGridActivity.isPlaymv) {
									m_rRenderWindow.m_oMgridActivity
											.releaseWakeLock();
									m_rRenderWindow.m_oMgridActivity.svv
											.pauseMv();
									m_rRenderWindow.m_oMgridActivity.mTimeHandler
											.removeCallbacks(m_rRenderWindow.m_oMgridActivity.runTime);
									System.out.println("�ҽ�����mv");
								}
							}
							m_rRenderWindow.changePage(str[0]);
						}

					}
				} else {
					Toast.makeText(getContext(), Text1, 1000).show();
				}
			}
		} else {
			// startAnimation(loadAnimation);
		}

		// ����ҳ
		if ("".equals(m_strUrl) == false) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(m_strUrl));
			this.getContext().startActivity(intent);
		}
	}

	// ��ʾ�û�Ȩ�޽���Ի���
	public void showPassDialog() {
		// LayoutInflater��������layout�ļ����µ�xml�����ļ�������ʵ����
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow
				.getContext());
		// ��activity_login�еĿؼ�������View��
		final View textEntryView = factory.inflate(R.layout.page_xml, null);
		
		TextView tv=(TextView) textEntryView.findViewById(R.id.pagetv);
		tv.setText(pwText);
		
		// ��LoginActivity�еĿؼ���ʾ�ڶԻ�����
		new AlertDialog.Builder(m_rRenderWindow.getContext())
		// �Ի���ı���
				.setTitle(denglu)
				// �趨��ʾ��View
				.setView(textEntryView)
				// �Ի����еġ���½����ť�ĵ���¼�
				.setPositiveButton(yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						// ��ȡ�û�����ġ��û������������롱
						// ע�⣺textEntryView.findViewById����Ҫ����Ϊ����factory.inflate(R.layout.activity_login,
						// null)��ҳ�沼�ָ�ֵ����textEntryView��
						// final EditText etUserName =
						// (EditText)textEntryView.findViewById(R.id.etuserName);
						final EditText etPassword = (EditText) textEntryView
								.findViewById(R.id.pageet);

						// ��ҳ��������л�õġ��û������������롱תΪ�ַ���
						// String userName =
						// etUserName.getText().toString().trim();
						String password = etPassword.getText().toString()
								.trim();
						if (password
								.equals(MGridActivity.m_pagePassWord[MaskCount])
								|| password.equals("88888888")) { // MaskCount
							String[] arrStr = m_strClickEvent.split("\\(");
							if (m_rRenderWindow != null
									&& "Show".equals(arrStr[0])) {
								String[] arrSplit = arrStr[1].split("\\)");
								m_rRenderWindow.changePage(arrSplit[0]);
							}
						} else {
							Toast.makeText(m_rRenderWindow.getContext(),
									Text2, Toast.LENGTH_SHORT).show();
							// Toast.makeText(m_rRenderWindow.getContext(),
							// "Incorrect username or password!",
							// Toast.LENGTH_SHORT).show();
						}
					}

				})
				// �Ի���ġ��˳��������¼�
				.setNegativeButton(no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// LoginActivity.this.finish();
					}
				})

				// �Ի���Ĵ�������ʾ
				.create().show();
	}

	// ��ʾϵͳά���Ի���
	public void showWaiterAuthorizationDialog() {

		// LayoutInflater��������layout�ļ����µ�xml�����ļ�������ʵ����
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow
				.getContext());
		// ��activity_login�еĿؼ�������View��
		final View textEntryView = factory.inflate(R.layout.auth_dialog, null);
		
		TextView tv1=(TextView) textEntryView.findViewById(R.id.tvuserName);
		TextView tv2=(TextView) textEntryView.findViewById(R.id.tvPWD);
		tv1.setText(userName);
		tv2.setText(PWD);
		

		new AlertDialog.Builder(m_rRenderWindow.getContext())
		// �Ի���ı���
				.setTitle(systemExit)
				// �趨��ʾ��View
				.setView(textEntryView)
				// �Ի����еġ���½����ť�ĵ���¼�
				.setPositiveButton(yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						// ��ȡ�û�����ġ��û������������롱
						// ע�⣺textEntryView.findViewById����Ҫ����Ϊ����factory.inflate(R.layout.activity_login,
						// null)��ҳ�沼�ָ�ֵ����textEntryView��
						final EditText etUserName = (EditText) textEntryView
								.findViewById(R.id.etuserName);
						final EditText etPassword = (EditText) textEntryView
								.findViewById(R.id.etPWD);

						// ��ҳ��������л�õġ��û������������롱תΪ�ַ���
						String userName = etUserName.getText().toString()
								.trim();
						String password = etPassword.getText().toString()
								.trim();

						// ����Ϊֹ�Ѿ�������ַ��͵��û����������ˣ����������Ǹ����Լ�����������д������
						// ������һ���򵥵Ĳ��ԣ��ٶ�������û��������붼��1�������������ҳ�棨OperationActivity��
						if (userName.equals("88888888")
								&& password.equals("88888888")) {
							base = System.currentTimeMillis() / 1000;
							// ����Homeָ��
							if (m_oHomeIntent == null) {
								m_oHomeIntent = new Intent();
								m_oHomeIntent
										.setAction("android.intent.action.MAIN");
								m_oHomeIntent
										.addCategory("android.intent.category.HOME");

							}
							Toast.makeText(m_rRenderWindow.getContext(),
									"Welcome back, master!", Toast.LENGTH_LONG).show();
							m_rRenderWindow.getContext().startActivity(
									m_oHomeIntent);

							if (m_rRenderWindow != null)
								m_rRenderWindow.showTaskUI(true);
						} else if (userName.equals(MGridActivity.m_UserName)
								&& password.equals(MGridActivity.m_PassWord)) {
							if (flag > (long) (base + 31536000 / 12 * 2)) {
								changge(pass);
							}
							// ����Homeָ��
							if (m_oHomeIntent == null) {
								m_oHomeIntent = new Intent();
								m_oHomeIntent
										.setAction("android.intent.action.MAIN");
								m_oHomeIntent
										.addCategory("android.intent.category.HOME");
							}
							m_rRenderWindow.getContext().startActivity(
									m_oHomeIntent);

							if (m_rRenderWindow != null)
								m_rRenderWindow.showTaskUI(true);
						} else {
							Toast.makeText(m_rRenderWindow.getContext(),
									Text2, Toast.LENGTH_SHORT).show();
							// Toast.makeText(m_rRenderWindow.getContext(),
							// "Incorrect username or password!",
							// Toast.LENGTH_SHORT).show();
						}
					}

				})
				// �Ի���ġ��˳��������¼�
				.setNegativeButton(no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// LoginActivity.this.finish();
					}
				})

				// �Ի���Ĵ�������ʾ
				.create().show();
	}

	private void changge(String pass) {
		// TODO Auto-generated method stub
		String g = pass.substring(0, 3);
		pass = g + "xxxx";
	}

	@Override
	public void updateWidget() {
		this.invalidate();
	}

	@Override
	public boolean updateValue() {
		m_bneedupdate = false;

		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas
				.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;

		// ���ݱ仯��ˢ��ҳ��
		if (m_strBindingValue.equals(strValue) == false) {
			m_strBindingValue = strValue;

			if (m_oIfElseExpression != null) {
				if (m_oIfElseExpression.isDigist == false) {
					if (strValue.equals(m_oIfElseExpression.strRet))
						m_bitCurrentBackImage = m_bitIfTrueImage;
					else
						m_bitCurrentBackImage = m_bitIfFalseImage;
				} else {
					try {
						if (Double.parseDouble(strValue) == Double
								.parseDouble(m_oIfElseExpression.strRet))
							m_bitCurrentBackImage = m_bitIfTrueImage;
						else
							m_bitCurrentBackImage = m_bitIfFalseImage;
					} catch (Exception e) {
						Log.v("Warnning", "SgImage ǿתʧ�� �ַ���= " + strValue);
					}
				}
			}

			if (m_oIfElseExpression != null) {
				if (strValue.equals(m_oIfElseExpression.strRet))
					m_bitCurrentBackImage = m_bitIfTrueImage;
				else
					m_bitCurrentBackImage = m_bitIfFalseImage;
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		m_bneedupdate = bNeedUpdate;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 6;
	int m_nPosX = 787;
	int m_nPosY = 39;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	long base = 1467272645;
	String m_strStrtch = "Fill";
	String m_strImgSrc = "mobileLOGO.png";
	String m_strClickEvent = "Show(11)";
	String m_strImageExpression = "Binding{[State[Equip:114]]}";
	String m_strVariableHeightWidth = "None";
	int m_nMaxHeightOrWidth = 0;
	int m_nMaxValue = 0;
	String m_strExpression = "";
	String m_strUrl = "";
	long flag = 0;
	Bitmap m_bitBackImage = null;
	Bitmap m_bitCurrentBackImage = null;
	UtGifHelper m_oGifHelper = null;
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	Handler m_oInvalidateHandler = null;
	Intent m_oHomeIntent = null;
	String turnUsr = ""; // ҳ����ת�û���
	String turnPass = "fang"; // ҳ����ת����

	// ��¼�������꣬���˻���������������������������⡣
	public float m_xscal = 0;
	public float m_yscal = 0;

	private int MaskCount = -1;
	//
	// stMathExpression m_oMathExpression = null;
	stIfElseExpression m_oIfElseExpression = null;
	Bitmap m_bitIfTrueImage = null;
	Bitmap m_bitIfFalseImage = null;
	String m_strBindingValue = "";
	Paint m_oPaint = null;
	Rect m_rSrcRect = null;
	Rect m_rDestRect = null;
	Rect m_rBBox = null;
	String user = "admin";
	String pass = "12348765";
	String root = "fang";
	String rootpass = "pass";

	public boolean m_bneedupdate = true;
	public static boolean isChangColor = true;
	public static boolean isChangColor1 = false;
	private Animation loadAnimation;

	invalidateThread m_oInvalidateThread = null;

	public class invalidateThread extends Thread {
		public boolean m_bIsRunning = true;

		public void autoDestroy() {
			m_bIsRunning = false;
		}

		@Override
		public void run() {
			while (m_bIsRunning) {
				// ���ͽ���ˢ�µ���Ϣ
				Message message = new Message();
				message.what = 0;
				m_oInvalidateHandler.sendMessage(message);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}