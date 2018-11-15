package com.sg.uis.newView;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSON;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.main.user.User;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.common.lsyBase.DoorCallBack;
import com.sg.common.lsyBase.Door_XuNiCallBack;
import com.sg.common.lsyBase.Door_XuNiUtil;
import com.sg.web.ChangeUserInfoObject;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
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
public class ChangeUserInfo extends TextView implements IObject, Door_XuNiCallBack,ViewObjectSetCallBack {

	private String UserID = LanguageStr.UserID;
	private String PassWord = LanguageStr.PassWord;
	private String Show = LanguageStr.Show;
	private String Add = LanguageStr.Add;
	private String Alter = LanguageStr.Alter;
	private String delete = LanguageStr.delete;
	private String OkTime = LanguageStr.OkTime;
	private int x, y, w, h;
	private int offset=1;
	private static boolean isLayout = false;

	private String oid, opw,otime;

	private Door_XuNiUtil door_XuNiUtil = null;
	// private String oldUserId = "";
	// private String oldPassWord = "";
	public int index;
	private DoorCallBack callBack;
	private Map<String,String> mapData=new HashMap<>();
	
	

	public ChangeUserInfo(Context context) {
		super(context);

		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		this.setFocusableInTouchMode(true);

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
		etTime = new EditText(context);

		tvName = new TextView(context);
		tvPhone = new TextView(context);
		tvTime = new TextView(context);

		tvTagorder = new TextView(context);

		btDelete = new Button(context);

		etName.setBackgroundResource(R.drawable.et_select);
		etPhone.setBackgroundResource(R.drawable.et_select);
		etTime.setBackgroundResource(R.drawable.et_select);


		btDelete.setBackgroundResource(R.drawable.bg_shadow);
		setBackgroundResource(R.drawable.bg_shadow);

		tvName.setPadding(0, 0, 0, 0);
		tvPhone.setPadding(0, 0, 0, 0);
		tvTime.setPadding(0, 0, 0, 0);

		tvTagorder.setPadding(0, 0, 0, 0);

		// btState.setPadding(0, 0, 0, 0);

		tvName.setTextSize(15);
		tvPhone.setTextSize(15);
		tvTime.setTextSize(15);

		tvTagorder.setTextSize(15);

		btDelete.setTextSize(15);

		etName.setTextSize(15);
		etPhone.setTextSize(15);

		tvName.setText(UserID);
		tvPhone.setText(PassWord);
		tvTime.setText(OkTime);

		this.setText(Show);

		btDelete.setText(delete);
		btDelete.setEnabled(false);

		btDelete.setTextColor(Color.BLACK);

		tvName.setTextColor(Color.BLACK);
		tvPhone.setTextColor(Color.BLACK);
		tvTime.setTextColor(Color.BLACK);
		
		tvTagorder.setTextColor(Color.BLACK);

		etName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		etPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
		

		etName.setSingleLine();
		etPhone.setSingleLine();
		etTime.setSingleLine();

		etName.setGravity(Gravity.CENTER);
		etPhone.setGravity(Gravity.CENTER);
		etTime.setGravity(Gravity.CENTER);

		tvTagorder.setGravity(Gravity.CENTER);
		btDelete.setGravity(Gravity.CENTER);

		btDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ɾ��xml����

				String id = etName.getText().toString();
				String pw = etPhone.getText().toString();

				Delete(id, pw, index);

			}
		});

		// etName.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// imm.showSoftInput(etName, InputMethodManager.SHOW_FORCED);// ��ȡ������ࡣ
		// etName.setFocusableInTouchMode(true);// ��ȡ����
		//
		// }
		// });
		//
		// etPhone.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// imm.showSoftInput(etPhone, InputMethodManager.SHOW_FORCED);// ��ȡ������ࡣ
		// etPhone.setFocusableInTouchMode(true);// ��ȡ����
		//
		// }
		// });

		etName.setTextColor(Color.BLACK);
		etPhone.setTextColor(Color.BLACK);
		etTime.setTextColor(Color.BLACK);

		// etName.setCursorVisible(true);// ��edittext���ֹ��
		// etPhone.setCursorVisible(true);

		etPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		etTime.setInputType(EditorInfo.TYPE_CLASS_PHONE);

		// imm = (InputMethodManager)
		// context.getSystemService(Context.INPUT_METHOD_SERVICE);// ��ʾ���뷨����

		door_XuNiUtil = Door_XuNiUtil.getIntance();
		door_XuNiUtil.setContext(context);

	}

	public void callBackResult() {

		if (callBack != null) {

			callBack.onSetErr();
		}

	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
	}

	// ͨ������޸�ini�е�����
	protected void onClicked() {

		String str = this.getText().toString();

		if (str.equals(Show)) {

			Show();

		} else if (str.equals(Add)) {

			String id = etName.getText().toString();
			String pw = etPhone.getText().toString();
			String time=etTime.getText().toString();

			Add(id, pw, index,time);

		} else if (str.equals(Alter)) {

			String id = etName.getText().toString();
			String pw = etPhone.getText().toString();
			String time=etTime.getText().toString();

			Alter(id, pw, index,time);
		}

	}

	/**
	 * ��������View
	 */
	public void hideAllView() {

		tvTagorder.setVisibility(View.GONE);
		etName.setVisibility(View.GONE);
		tvName.setVisibility(View.GONE);
		etPhone.setVisibility(View.GONE);
		tvPhone.setVisibility(View.GONE);
		etTime.setVisibility(View.GONE);
		tvTime.setVisibility(View.GONE);
		btDelete.setVisibility(View.GONE);		
		this.setVisibility(View.GONE);

	}

	/**
	 * ��ʾ����View
	 */
	public void showAllView() {

		tvTagorder.setVisibility(View.VISIBLE);
		etName.setVisibility(View.VISIBLE);
		tvName.setVisibility(View.VISIBLE);
		etPhone.setVisibility(View.VISIBLE);
		tvPhone.setVisibility(View.VISIBLE);
		etTime.setVisibility(View.VISIBLE);
		tvTime.setVisibility(View.VISIBLE);
		btDelete.setVisibility(View.VISIBLE);
		this.setVisibility(View.VISIBLE);

	}

	// �޸�
	private void Alter(String id, String pw, int index,String time) {

		door_XuNiUtil.alter(id, pw, index,time);
	}

	// ���
	public void Add(String id, String pw, int index,String time) {
		door_XuNiUtil.add(id, pw, index,time);

	}

	// ɾ��
	private void Delete(String id, String pw, int index) {

		door_XuNiUtil.delete(id, pw, index);
	}

	// ��ʾ
	private void Show() {

		Map<Integer, User> map = MGridActivity.userManager.getUserManaget();
		User user = map.get(Integer.parseInt(tvTagorder.getText().toString()));
		if (user != null) {
			// oldUserId = user.getUserID();
			etName.setText(user.getUid());
			// oldPassWord = user.getPassWord();
			etPhone.setText(user.getPw());
			etTime.setText(user.getTime());
			this.setText(Alter);
			btDelete.setEnabled(true);
		} else {
			this.setText(Add);
			etName.setText("");
			etPhone.setText("");
			etTime.setText("");
		}

	}

	private void setTexts(final String id, final String pw,final String time) {

		etName.setText(id);
		etPhone.setText(pw);
		etTime.setText(time);

		 etName.layout(x + (int) (w * 0.12f)+offset, y, x + (int) (w * 0.28f), y + h - h
		 / 10);
		 etPhone.layout(x + (int) (w * 0.38f)+offset, y, x + (int) (w * 0.54f), y + h - h
		 / 10);
		 etTime.layout(x + (int) (w * 0.64f)+offset, y, x + (int) (w *0.8f), y + h - h
		 / 10);
		
		 offset=-offset;
	}

	public void setCallBack(DoorCallBack callBack) {
		this.callBack = callBack;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:

				setText(Add);
				etName.setText("");
				etPhone.setText("");
				etTime.setText("");
				btDelete.setEnabled(false);
				mapData.clear();
				Toast.makeText(getContext(), "�h���ɹ�", Toast.LENGTH_SHORT).show();

				break;

			case 1:

				
				setData();				
				setTexts(oid, opw,otime);
				Toast.makeText(getContext(), "�޸ĳɹ�", Toast.LENGTH_SHORT).show();

				break;
			case 2:
				

				setData();			
				if (isLayout) {
					setTexts(oid, opw,otime);
					setText(Alter);
					btDelete.setEnabled(true);
				} else {

				}
				Toast.makeText(getContext(), "��ӳɹ�", Toast.LENGTH_SHORT).show();
				break;

			case 3:

				Toast.makeText(getContext(), "ʧ��", Toast.LENGTH_SHORT).show();

				break;
			}

		};
	};

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

		x = nX;
		y = nY;
		w = nWidth;
		h = nHeight;

		Rect rect = new Rect();
		getPaint().getTextBounds("�û�����", 0, "�û�����".length(), rect);
		int height = rect.height();
		int width = rect.height();

		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {

//			tvTagorder.layout(nX, (int) (nY), nX + (int) (nWidth * 0.029f), nY + nHeight);
//			tvName.layout(nX + (int) (nWidth * 0.058f), (int) (nY), nX + (int) (nWidth * 0.158f), nY + nHeight);
//			etName.layout(nX + (int) (nWidth * 0.187f), nY, nX + (int) (nWidth * 0.387f), nY + nHeight - nHeight / 10);
//			tvPhone.layout(nX + (int) (nWidth * 0.416f), (int) (nY), nX + (int) (nWidth * 0.516f), nY + nHeight);
//			etPhone.layout(nX + (int) (nWidth * 0.545f), nY, nX + (int) (nWidth * 0.745f), nY + nHeight - nHeight / 10);
//
//			this.layout(nX + (int) (nWidth * 0.774f), nY, nX + (int) (nWidth * 0.874f), nY + nHeight);
//			btDelete.layout(nX + (int) (nWidth * 0.903f), nY, nX + (int) (nWidth * 1), nY + nHeight);
			
			tvTagorder.layout(nX, (int) (nY), nX + (int) (nWidth * 0.02f), nY + nHeight);
			tvName.layout(nX + (int) (nWidth * 0.02f), (int) (nY), nX + (int) (nWidth * 0.12f), nY + nHeight);
			etName.layout(nX + (int) (nWidth * 0.12f), nY, nX + (int) (nWidth * 0.28f), nY + nHeight - nHeight / 10);
			tvPhone.layout(nX + (int) (nWidth * 0.28f), (int) (nY), nX + (int) (nWidth * 0.38f), nY + nHeight);
			etPhone.layout(nX + (int) (nWidth * 0.38f), nY, nX + (int) (nWidth * 0.54f), nY + nHeight - nHeight / 10);
			
			tvTime.layout(nX + (int) (nWidth * 0.54f), (int) (nY), nX + (int) (nWidth * 0.64f), nY + nHeight);
			etTime.layout(nX + (int) (nWidth * 0.64f), nY, nX + (int) (nWidth * 0.8f), nY + nHeight - nHeight / 10);

			this.layout(nX + (int) (nWidth * 0.8f), nY, nX + (int) (nWidth * 0.897f), nY + nHeight);
			btDelete.layout(nX + (int) (nWidth * 0.903f), nY, nX + (int) (nWidth * 1), nY + nHeight);
			
		}

		tvTagorder.setPadding(0, (int) ((0.85 * nHeight - height) / 2), 0, 0);
		tvName.setPadding((int) ((0.1 * nWidth - width) / 3), (int) ((0.85 * nHeight - height) / 2), 0, 0);
		tvPhone.setPadding((int) ((0.1 * nWidth - width) / 3), (int) ((0.85 * nHeight - height) / 2), 0, 0);
		tvTime.setPadding((int) ((0.1 * nWidth - width) / 3), (int) ((0.85 * nHeight - height) / 2), 0, 0);
		btDelete.setPadding(0, (int) ((0.85 * nHeight - height) / 2), 0, 0);
		etName.setPadding(0, (int) ((0.8 * nHeight - height) / 2), 0, 0);
		etPhone.setPadding(0, (int) ((0.8 * nHeight - height) / 2), 0, 0);
		etTime.setPadding(0, (int) ((0.8 * nHeight - height) / 2), 0, 0);

		isLayout = true;

		// this.setPadding(0, (int)((nHeight-height)/2), 0, 0);

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
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
			btDelete.setTextColor(m_cFontColor);
			tvName.setTextColor(m_cFontColor);
			tvPhone.setTextColor(m_cFontColor);
			tvTime.setTextColor(m_cFontColor);
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
			tvTagorder.setText(strValue);
			index = Integer.parseInt(tvTagorder.getText().toString());
			door_XuNiUtil.setCallBack(index, this);

		} else if ("FontSize".equals(strName)) {
			fontSize = Integer.parseInt(strValue);
			tvName.setTextSize(fontSize);
			tvPhone.setTextSize(fontSize);
			tvPhone.setTextSize(fontSize);
			tvTime.setTextSize(fontSize);
			tvTagorder.setTextSize(fontSize);

			btDelete.setTextSize(fontSize);

			etName.setTextSize(fontSize);
			etPhone.setTextSize(fontSize);
			etTime.setTextSize(fontSize);
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
		
		rWin.addView(etTime);
		rWin.addView(tvTime);

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
	EditText etTime = null;
	TextView tvTime = null;

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
	
	
	ViewObjectBase base=new ChangeUserInfoObject();
	
	
	
	private void  setData()
	{
		
		Map<Integer, User> map = MGridActivity.userManager.getUserManaget();
		User user = map.get(Integer.parseInt(tvTagorder.getText().toString()));
		if (user != null) {
			
			mapData.put("uid", user.getUid());
			mapData.put("pw", user.getPw());
			mapData.put("time", user.getTime());
			

		}
		
	}
	

	@Override
	public void onSuccess(int state, String id, String pw,String time) {

		switch (state) {
		case 0:

			handler.sendEmptyMessage(0);

			break;

		case 1:

			handler.sendEmptyMessage(1);

			break;
		case 2:

			oid = id;
			opw = pw;
			otime=time;

			handler.sendEmptyMessage(2);

			break;
		case 3:

			break;
		}

	}

	@Override
	public void onFail(int state) {

		switch (state) {
		case 0:

			break;

		case 1:

			break;
		case 2:

			break;
		case 3:

			handler.sendEmptyMessage(3);

			break;
		}

	}

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
		
		setData();
		
		((ChangeUserInfoObject)base).setIndex(index);
		
		((ChangeUserInfoObject)base).setMapData(mapData);
		
		
	}

	@Override
	public void onSetData() {
		
		
		((ChangeUserInfoObject)base).setMapData(mapData);
	}

	@Override
	public void onControl(Object obj) {
		
		String str = (String) obj;

		Map<String, String> maps = (Map<String, String>) JSON.parse(str);

		String type = maps.get("type");
		String id = maps.get("uid");
		String pw = maps.get("pw");
		String time = maps.get("time");

		Log.e("type", type);

		if (type.equals("delete")) {

			Delete(id, pw, index);
		}

		if (type.equals("alter")) {

			
			     oid = id;
			     opw = pw;
			     otime=time;
			
				if(mapData.size()==0)
				{
					Add(id, pw, index,time);
					
				}else
				{
					Alter(id, pw, index,time);
				}				
				
			

		}
		
		
		
	}

}
