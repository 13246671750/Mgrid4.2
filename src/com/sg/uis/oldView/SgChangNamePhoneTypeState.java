package com.sg.uis.oldView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSON;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.web.ChangeNPTSObject;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;
import com.sg.web.utils.ViewObjectColorUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �ĺ���
 * 
 * @author Administrator
 * 
 */
public class SgChangNamePhoneTypeState extends TextView implements IObject, ViewObjectSetCallBack {

	private String Name = LanguageStr.Name;
	private String Phone = LanguageStr.Phone;

	private String Level = LanguageStr.Level;
	private String Show = LanguageStr.Show;
	private String Add = LanguageStr.Add;
	private String Alter = LanguageStr.Alter;
	private String State = LanguageStr.State;

	private String delete = LanguageStr.delete;

	private String text4 = LanguageStr.text4, text5 = LanguageStr.text5, text6 = LanguageStr.text6,
			text7 = LanguageStr.text7, text8 = LanguageStr.text8, text9 = LanguageStr.text9,
			text10 = LanguageStr.text10;

	private String NO = LanguageStr.NO;
	boolean isdelete = true;
	SgChangNamePhoneTypeState scnp;

	private Map<String, String> dataMap = new HashMap<>();

	public SgChangNamePhoneTypeState(Context context) {
		super(context);
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		this.setFocusableInTouchMode(true);
		scnp = this;
		m_fFontSize = this.getTextSize();
		this.setTextSize(20);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
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

					if (m_xscal == event.getX() && m_yscal == event.getY())
						onClicked();
					break;
				default:
					break;
				}
				return true;
			}
		});

		setPadding(0, 0, 0, 0);

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		etName = new EditText(context);
		etPhone = new EditText(context);
		etType = new EditText(context);
		etState = new EditText(context);

		tvName = new TextView(context);
		tvPhone = new TextView(context);
		tvType = new TextView(context);
		tvState = new TextView(context);
		tvTagorder = new TextView(context);

		btDelete = new Button(context);

		etName.setBackgroundResource(R.drawable.et_select);
		etPhone.setBackgroundResource(R.drawable.et_select);
		etState.setBackgroundResource(R.drawable.et_select);
		etType.setBackgroundResource(R.drawable.et_select);
		btDelete.setBackgroundResource(R.drawable.bg_shadow);
		setBackgroundResource(R.drawable.bg_shadow);

		tvName.setPadding(0, 0, 0, 0);
		tvPhone.setPadding(0, 0, 0, 0);
		tvState.setPadding(0, 0, 0, 0);
		tvType.setPadding(0, 0, 0, 0);
		tvTagorder.setPadding(0, 0, 0, 0);

		// btState.setPadding(0, 0, 0, 0);

		tvName.setTextSize(15);
		tvPhone.setTextSize(15);
		tvState.setTextSize(15);
		tvType.setTextSize(15);
		tvTagorder.setTextSize(15);

		btDelete.setTextSize(15);

		etName.setTextSize(15);
		etPhone.setTextSize(15);
		etState.setTextSize(15);
		etType.setTextSize(15);

		tvName.setText(Name);
		tvPhone.setText(Phone);
		tvState.setText(State);
		tvType.setText(Level);
		this.setText(Show);

		btDelete.setText(delete);
		btDelete.setEnabled(false);

		btDelete.setTextColor(Color.BLACK);

		tvName.setTextColor(Color.BLACK);
		tvPhone.setTextColor(Color.BLACK);
		tvState.setTextColor(Color.BLACK);
		tvType.setTextColor(Color.BLACK);
		tvTagorder.setTextColor(Color.BLACK);

		etName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		etPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
		etState.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
		etType.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });

		etName.setSingleLine();
		etPhone.setSingleLine();
		etState.setSingleLine();
		etType.setSingleLine();

		etName.setGravity(Gravity.CENTER);
		etPhone.setGravity(Gravity.CENTER);
		etState.setGravity(Gravity.CENTER);
		etType.setGravity(Gravity.CENTER);
		tvTagorder.setGravity(Gravity.CENTER);
		btDelete.setGravity(Gravity.CENTER);

		btDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ɾ��xml����

				String name = etName.getText().toString();
				String phone = etPhone.getText().toString();
				String type = "";

				delete(name, phone, type);

			}
		});

		etName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(etName, InputMethodManager.SHOW_FORCED);// ��ȡ������ࡣ
				etName.setFocusableInTouchMode(true);// ��ȡ����

			}
		});

		etPhone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(etPhone, InputMethodManager.SHOW_FORCED);// ��ȡ������ࡣ
				etPhone.setFocusableInTouchMode(true);// ��ȡ����

			}
		});

		etState.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(etState, InputMethodManager.SHOW_FORCED);// ��ȡ������ࡣ
				etState.setFocusableInTouchMode(true);// ��ȡ����

			}
		});

		etType.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(etType, InputMethodManager.SHOW_FORCED);// ��ȡ������ࡣ
				etType.setFocusableInTouchMode(true);// ��ȡ����

			}
		});

		// m_oEditTextNEW.setHint("������������"); ������ʾ����

		etName.setTextColor(Color.BLACK);
		etPhone.setTextColor(Color.BLACK);
		etState.setTextColor(Color.BLACK);
		etType.setTextColor(Color.BLACK);

		etName.setCursorVisible(true);// ��edittext���ֹ��
		etPhone.setCursorVisible(true);
		etState.setCursorVisible(true);
		etType.setCursorVisible(true);

		// etName.setInputType(EditorInfo.TYPE_CLASS_PHONE); //�����ı���ʽ
		etPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		// etState.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		etType.setInputType(EditorInfo.TYPE_CLASS_PHONE);

		imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);// ��ʾ���뷨����

	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
	}

	// ͨ������޸�xml�е�����
	protected void onClicked() {

		dbf = DocumentBuilderFactory.newInstance();
		String name = etName.getText().toString();
		String phone = etPhone.getText().toString();
		String level = etType.getText().toString();

		boolean isSave = false;
		xmlPhoneNumber = Integer.parseInt(tvTagorder.getText().toString());
		if (xmlPhoneNumber == -1)
			return;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(new File("/data/mgrid/sampler/XmlCfg/sms_notification.xml"));
			list1 = doc.getElementsByTagName("user");
			list2 = doc.getElementsByTagName("rule");

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (list1 == null || list2 == null) {
			Toast.makeText(getContext(), text4, 200).show();
			return;
		}
		if (this.getText().equals(Show)) {
			boolean isAdd = true;
			for (int i = 0; i < list1.getLength(); i++) {
				Element element1 = (Element) list1.item(i);
				String rule_type = element1.getAttribute("rule_type");
				if (rule_type.equals(xmlPhoneNumber + "")) {
					
					
					etName.setText(element1.getAttribute("name"));
					etPhone.setText(element1.getAttribute("tel_number"));
					
					
					for (int j = 0; j < list2.getLength(); j++) {
						Element element2 = (Element) list2.item(j);
						if (rule_type.equals(element2.getAttribute("type"))) {
							etType.setText(element2.getAttribute("alarm_level"));
						}
					}
					
					dataMap.clear();
					dataMap.put("name", etName.getText().toString());
					dataMap.put("tel_number", etPhone.getText().toString());
					dataMap.put("alarm_level", etType.getText().toString());
					
					this.setText(Alter);
					btDelete.setEnabled(true);
					isAdd = false;
					break;
				}
			}
			if (isAdd) {

				Toast.makeText(getContext(), text5, 200).show();
				this.setText(Add);
			}
		}

		else if (this.getText().equals(Alter)) {

			for (int i = 0; i < list1.getLength(); i++) {

				Element ele = (Element) list1.item(i);
				String rule_type = ele.getAttribute("rule_type");
				if (!(xmlPhoneNumber + "").equals(rule_type)) {
					continue;
				}

				if ((name.equals("") || level.equals("") || phone.equals("")) != true) {
					if (phone.length() == 11) {
						ele.setAttribute("name", name);
						ele.setAttribute("tel_number", phone);
						ele.setAttribute("enable", "ture");

						for (int j = 0; j < list2.getLength(); j++) {

							Element ele2 = (Element) list2.item(j);
							if (rule_type.equals(ele2.getAttribute("type"))) {
								ele2.setAttribute("alarm_level", level);

								break;
							}
						}

						isSave = true;

						dataMap.clear();
						dataMap.put("name", name);
						dataMap.put("tel_number", phone);
						dataMap.put("alarm_level", level);

						Toast.makeText(getContext(), text6, Toast.LENGTH_SHORT).show();

					} else {

						Toast.makeText(getContext(), text7, Toast.LENGTH_SHORT).show();
					}

				} else {

					Toast.makeText(getContext(), text8, Toast.LENGTH_SHORT).show();
				}
			}
		} else if (this.getText().equals(Add)) {

			if ((name.equals("") || level.equals("") || phone.equals("")) != true) {
				if (phone.length() == 11) {
					Element user = doc.createElement("user");
					user.setAttribute("name", name);
					user.setAttribute("tel_number", phone);
					user.setAttribute("enable", "true");
					Element users = (Element) doc.getElementsByTagName("users").item(0);

					user.setAttribute("rule_type", "" + xmlPhoneNumber);
					Element rule = doc.createElement("rule");
					rule.setAttribute("type", "" + xmlPhoneNumber);
					rule.setAttribute("alarm_level", level);
					Element rules = (Element) doc.getElementsByTagName("rules").item(0);
					rules.appendChild(rule);

					users.appendChild(user);
					isSave = true;

					dataMap.clear();
					dataMap.put("name", name);
					dataMap.put("tel_number", phone);
					dataMap.put("alarm_level", level);

					Toast.makeText(getContext(), text9, Toast.LENGTH_SHORT).show();
					this.setText(Alter);
					btDelete.setEnabled(true);
				} else {

					Toast.makeText(getContext(), text7, Toast.LENGTH_SHORT).show();
				}
			} else {

				Toast.makeText(getContext(), text8, Toast.LENGTH_SHORT).show();
			}
		} else {

			Toast.makeText(getContext(), text8, Toast.LENGTH_SHORT).show();
		}

		if (isSave) {
			saveXmlData();
		}
	}

	private void init() {
		dbf = DocumentBuilderFactory.newInstance();
		xmlPhoneNumber = Integer.parseInt(tvTagorder.getText().toString());
		if (xmlPhoneNumber == -1)
			return;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(new File("/data/mgrid/sampler/XmlCfg/sms_notification.xml"));
			list1 = doc.getElementsByTagName("user");
			list2 = doc.getElementsByTagName("rule");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:

				btDelete.setEnabled(false);
				etName.setText("");
				etPhone.setText("");
				etType.setText("");
				scnp.setText(Add);
				isdelete = false;
				dataMap.clear();
				Toast.makeText(getContext(), text10, 500).show();

				break;

			case 1:

				Map<String, String> map = (Map<String, String>) msg.obj;

				if (getText().equals(Alter)) {

					etName.setText(map.get("name"));
					etPhone.setText(map.get("tel_number"));
					etType.setText(map.get("alarm_level"));

				} else if (getText().equals(Add)) {

					etName.setText(map.get("name"));
					etPhone.setText(map.get("tel_number"));
					etType.setText(map.get("alarm_level"));
					scnp.setText(Alter);
					btDelete.setEnabled(true);
				}

				break;
			}

		};
	};

	private Map<String, String> show() {

		init();

		if (list1 == null || list2 == null) {
			Toast.makeText(getContext(), text4, 200).show();
			return dataMap;
		}

		for (int i = 0; i < list1.getLength(); i++) {
			Element element1 = (Element) list1.item(i);
			String rule_type = element1.getAttribute("rule_type");
			if (rule_type.equals(xmlPhoneNumber + "")) {

				dataMap.put("name", element1.getAttribute("name"));
				dataMap.put("tel_number", element1.getAttribute("tel_number"));

				for (int j = 0; j < list2.getLength(); j++) {
					Element element2 = (Element) list2.item(j);
					if (rule_type.equals(element2.getAttribute("type"))) {
						dataMap.put("alarm_level", element2.getAttribute("alarm_level"));
					}
				}
				break;
			}
		}

		return dataMap;
	}

	private void alter(String name, String phone, String level) {

		init();

		if (list1 == null || list2 == null) {
			Toast.makeText(getContext(), text4, 200).show();
			return;
		}

		boolean isSave = false;

		for (int i = 0; i < list1.getLength(); i++) {

			Element ele = (Element) list1.item(i);
			String rule_type = ele.getAttribute("rule_type");
			if (!(xmlPhoneNumber + "").equals(rule_type)) {
				continue;
			}

			if ((name.equals("") || level.equals("") || phone.equals("")) != true) {
				if (phone.length() == 11) {
					ele.setAttribute("name", name);
					ele.setAttribute("tel_number", phone);
					ele.setAttribute("enable", "ture");

					for (int j = 0; j < list2.getLength(); j++) {

						Element ele2 = (Element) list2.item(j);
						if (rule_type.equals(ele2.getAttribute("type"))) {
							ele2.setAttribute("alarm_level", level);
							break;
						}
					}

					isSave = true;

					this.post(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(getContext(), text6, Toast.LENGTH_SHORT).show();

						}
					});

				} else {

					this.post(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(getContext(), text7, Toast.LENGTH_SHORT).show();

						}
					});

				}

			} else {

				this.post(new Runnable() {

					@Override
					public void run() {

						Toast.makeText(getContext(), text8, Toast.LENGTH_SHORT).show();

					}
				});

			}
		}

		if (isSave) {

			saveXmlData();
			dataMap.clear();
			dataMap.put("name", name);
			dataMap.put("tel_number", phone);
			dataMap.put("alarm_level", level);

			Message msg = new Message();
			msg.obj = dataMap;
			msg.what = 1;
			handler.sendMessage(msg);
		}

	}

	private void delete(String name, String phone, String type) {

		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(new File("/data/mgrid/sampler/XmlCfg/sms_notification.xml"));
			list1 = doc.getElementsByTagName("user");
			list2 = doc.getElementsByTagName("rule");

		} catch (Exception e) {

			e.printStackTrace();
		}

		Element users = (Element) doc.getElementsByTagName("users").item(0);
		Element rules = (Element) doc.getElementsByTagName("rules").item(0);

		for (int i = 0; i < list1.getLength(); i++) {

			Element element1 = (Element) list1.item(i);
			String pn = element1.getAttribute("name");
			String np = element1.getAttribute("tel_number");

		

			if (pn.equals(name) && np.equals(phone)) {

				users.removeChild(element1);
				handler.sendEmptyMessage(0);

				saveXmlData();

			}

		}
		list1 = doc.getElementsByTagName("user");
		for (int i = 0; i < list1.getLength(); i++) {

			Element element1 = (Element) list1.item(i);
			type = type + "," + element1.getAttribute("rule_type");
		}

		for (int i = 0; i < list2.getLength(); i++) {
			Element element1 = (Element) list2.item(i);
			String s = element1.getAttribute("type");
			if (!type.contains(s)) {
				rules.removeChild(element1);
				saveXmlData();
			}
		}

	}

	private void Add(String name, String phone, String level) {

		init();

		if (list1 == null || list2 == null) {
			Toast.makeText(getContext(), text4, 200).show();
			return;
		}

		boolean isSave = false;

		if ((name.equals("") || level.equals("") || phone.equals("")) != true) {
			if (phone.length() == 11) {
				Element user = doc.createElement("user");
				user.setAttribute("name", name);
				user.setAttribute("tel_number", phone);
				user.setAttribute("enable", "true");
				Element users = (Element) doc.getElementsByTagName("users").item(0);

				user.setAttribute("rule_type", "" + xmlPhoneNumber);
				Element rule = doc.createElement("rule");
				rule.setAttribute("type", "" + xmlPhoneNumber);
				rule.setAttribute("alarm_level", level);
				Element rules = (Element) doc.getElementsByTagName("rules").item(0);
				rules.appendChild(rule);

				users.appendChild(user);
				isSave = true;

				this.post(new Runnable() {

					@Override
					public void run() {

						Toast.makeText(getContext(), text9, Toast.LENGTH_SHORT).show();

					}
				});

			} else {

				this.post(new Runnable() {

					@Override
					public void run() {

						Toast.makeText(getContext(), text7, Toast.LENGTH_SHORT).show();

					}
				});

			}
		} else {
			this.post(new Runnable() {

				@Override
				public void run() {

					Toast.makeText(getContext(), text8, Toast.LENGTH_SHORT).show();

				}
			});

		}

		if (isSave) {

			saveXmlData();
			dataMap.clear();
			dataMap.put("name", name);
			dataMap.put("tel_number", phone);
			dataMap.put("alarm_level", level);

			Message msg = new Message();
			msg.obj = dataMap;
			msg.what = 1;
			handler.sendMessage(msg);
		}

	}

	// ����xml����
	private void saveXmlData() {
		// ��������
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer tran;
		try {
			tran = tf.newTransformer();
			DOMSource dom = new DOMSource(doc);
			// ���ñ�����
			tran.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			StreamResult result = new StreamResult(
					new FileOutputStream(new File("/data/mgrid/sampler/XmlCfg/sms_notification.xml")));
			tran.transform(dom, result);
			// Toast.makeText(getContext(), "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {

		}
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t + (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;

		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {

			tvTagorder.layout(nX, (int) (nY + 0.15 * nHeight), nX + (int) (nWidth * 0.05f), nY + nHeight);
			tvName.layout(nX + (int) (nWidth * 0.051f), (int) (nY + 0.15 * nHeight), nX + (int) (nWidth * 0.111f),
					nY + nHeight);
			etName.layout(nX + (int) (nWidth * 0.112f), nY + nHeight / 10, nX + (int) (nWidth * 0.262f),
					nY + nHeight - nHeight / 10);
			tvPhone.layout(nX + (int) (nWidth * 0.263f), (int) (nY + 0.15 * nHeight), nX + (int) (nWidth * 0.323f),
					nY + nHeight);
			etPhone.layout(nX + (int) (nWidth * 0.324f), nY + nHeight / 10, nX + (int) (nWidth * 0.474f),
					nY + nHeight - nHeight / 10);
			tvType.layout(nX + (int) (nWidth * 0.475f), (int) (nY + 0.15 * nHeight), nX + (int) (nWidth * 0.535f),
					nY + nHeight);
			etType.layout(nX + (int) (nWidth * 0.536f), nY + nHeight / 10, nX + (int) (nWidth * 0.686f),
					nY + nHeight - nHeight / 10);
			this.layout(nX + (int) (nWidth * 0.687f), nY, nX + (int) (nWidth * 0.788f), nY + nHeight);
			btDelete.layout(nX + (int) (nWidth * 0.789f), nY, nX + (int) (nWidth * 0.890f), nY + nHeight);
		}

		btDelete.setPadding(0, nHeight / 5, 0, 0);
		etName.setPadding(0, nHeight / 5, 0, 0);
		etPhone.setPadding(0, nHeight / 5, 0, 0);
		etState.setPadding(0, nHeight / 5, 0, 0);
		etType.setPadding(0, nHeight / 5, 0, 0);
		this.setPadding(0, nHeight / 5, 0, 0);

	}

	@Override
	public void setUniqueID(String strID) {
		// TODO Auto-generated method stub
		m_strID = strID;
	}

	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return m_strID;
	}

	@Override
	public void setType(String strType) {
		// TODO Auto-generated method stub
		m_strType = strType;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return m_strType;
	}

	@Override
	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception {

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
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			// this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("BackgroundColor".equals(strName))
		// m_cBackgroundColor = Color.parseColor(strValue);
		{
			if (strValue != null && !strValue.equals("#FF000000")) {
				btDelete.setBackgroundResource(android.R.drawable.btn_default_small);
				setBackgroundResource(android.R.drawable.btn_default);
			}
		} else if ("FontColor".equals(strName)) {

			fontColor = strValue;
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
			btDelete.setTextColor(m_cFontColor);
			tvName.setTextColor(m_cFontColor);
			tvPhone.setTextColor(m_cFontColor);
			tvState.setTextColor(m_cFontColor);
			tvType.setTextColor(m_cFontColor);
			tvTagorder.setTextColor(m_cFontColor);

		} else if ("CmdExpression".equals(strName)) {
			m_strCmdExpression = strValue;
		} else if ("IsValueRelateSignal".equals(strName)) {
			if ("True".equals(strValue))
				m_bIsValueRelateSignal = true;
			else
				m_bIsValueRelateSignal = false;
		} else if ("ButtonWidthRate".equals(strName)) {
			m_fButtonWidthRate = Float.parseFloat(strValue);
		} else if ("Labelorder".equals(strName)) {

			label = strValue;
			tvTagorder.setText(strValue);
		} else if ("FontSize".equals(strName)) {
			fontSize = Integer.parseInt(strValue);
			tvName.setTextSize(fontSize);
			tvPhone.setTextSize(fontSize);
			tvState.setTextSize(fontSize);
			tvType.setTextSize(fontSize);
			tvTagorder.setTextSize(fontSize);

			// btState.setTextSize(20);
			btDelete.setTextSize(fontSize);

			etName.setTextSize(fontSize);
			etPhone.setTextSize(fontSize);
			etState.setTextSize(fontSize);
			etType.setTextSize(fontSize);
			this.setTextSize(fontSize);

		}
	}

	@Override
	public void initFinished() {
		setGravity(Gravity.CENTER);

		double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize()) / 2;
		setPadding(0, (int) padSize, 0, (int) padSize);

	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		m_rRenderWindow.viewList.add(base);

		rWin.addView(tvTagorder);
		rWin.addView(etName);
		rWin.addView(tvName);
		rWin.addView(etPhone);
		rWin.addView(tvPhone);
		rWin.addView(etType);
		rWin.addView(tvType);
		rWin.addView(btDelete);
		rWin.addView(this);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		// TODO Auto-generated method stub
		rWin.removeView(etName);
		rWin.removeView(tvName);
		rWin.removeView(this);
	}

	@Override
	public void updateWidget() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needupdate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBindingExpression() {
		// TODO Auto-generated method stub
		return m_strCmdExpression;
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public int getZIndex() {
		// TODO Auto-generated method stub
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// ����
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 10;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	boolean m_bIsBold = false;
	String m_strFontFamily = "΢���ź�";
	int m_cBackgroundColor = 0xFFFCFCFC;
	int m_cFontColor = 0xFF000000;
	String fontColor, label;
	String m_strContent = "Setting";
	String m_strCmdExpression = "";
	boolean m_bIsValueRelateSignal = false;
	float m_fButtonWidthRate = 0.3f;
	MainWindow m_rRenderWindow = null;
	float m_fFontSize = 6.0f;
	boolean m_bPressed = false;
	Paint m_oPaint = null;
	Rect m_rBBox = null;
	InputMethodManager imm = null;
	EditText etName = null;
	TextView tvName = null;
	EditText etPhone = null;
	TextView tvPhone = null;
	EditText etType = null;
	TextView tvType = null;
	EditText etState = null;
	TextView tvState = null;

	TextView tvTagorder = null;
	Button btDelete = null;

	// ����XML
	DocumentBuilderFactory dbf = null;
	DocumentBuilder db = null;
	NodeList list1 = null;
	NodeList list2 = null;
	Document doc = null;
	private int xmlPhoneNumber = -1;

	public void setNumber(int number) {
		xmlPhoneNumber = number;
	}

	int fontSize = 15;

	// ��¼�������꣬���˻���������������������������⡣
	public float m_xscal = 0;
	public float m_yscal = 0;

	private ViewObjectBase base = new ChangeNPTSObject();

	@Override
	public void onCall() {

		base.setZIndex(m_nZIndex);
		base.setFromHeight(MainWindow.FORM_HEIGHT);
		base.setFromWight(MainWindow.FORM_WIDTH);

		base.setWight(m_nWidth);
		base.setHeght(m_nHeight);

		base.setLeft(m_nPosX);
		base.setTop(m_nPosY);

		base.setTypeId(m_strID);
		base.setType(m_strType);

		show();
		((ChangeNPTSObject) base).setFontColor(ViewObjectColorUtil.getColor(fontColor));
		((ChangeNPTSObject) base).setLabelOrder(label);
		((ChangeNPTSObject) base).setFontSize(fontSize);
		((ChangeNPTSObject) base).setMap(dataMap);

	}

	@Override
	public void onSetData() {

		
		
	}

	@Override
	public void onControl(Object obj) {

		String str = (String) obj;

		Map<String, String> maps = (Map<String, String>) JSON.parse(str);

		String type = maps.get("type");
		String name = maps.get("name");
		String phone = maps.get("tel_number");
		String level = maps.get("alarm_level");

		Log.e("type", type);

		if (type.equals("delete")) {

			delete(name, phone, "");
		}

		if (type.equals("alter")) {

			
				if(dataMap.size()==0)
				{
					Add(name, phone, level);
				}else
				{
					alter(name, phone, level);
				}				
				
			

		}

	}

}
