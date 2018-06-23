package com.sg.uis.LsyNewView;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.main.user.User;
import com.mgrid.util.FileUtil;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.text.InputFilter;
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
public class ChangeUserInfo extends TextView implements IObject {

	private String UserID=LanguageStr.UserID;
	private String PassWord=LanguageStr.PassWord;

	
	private String Show=LanguageStr.Show;
	private String Add=LanguageStr.Add;
	private String Alter=LanguageStr.Alter;
	

	private String delete=LanguageStr.delete;


	boolean isdelete = true;
	private ChangeUserInfo scnp;
	private FileUtil fileUtil=null;
	private String oldUserId="";
	private String oldPassWord="";
	private int index;


	public ChangeUserInfo(Context context) {
		super(context);
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		this.setFocusableInTouchMode(true);
		scnp = this;
		fileUtil=new FileUtil();
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
		

		tvName = new TextView(context);
		tvPhone = new TextView(context);
		
		tvTagorder = new TextView(context);

		
		btDelete = new Button(context);

	
		etName.setBackgroundResource(R.drawable.et_select);
		etPhone.setBackgroundResource(R.drawable.et_select);
		
		btDelete.setBackgroundResource(R.drawable.bg_shadow);
		setBackgroundResource(R.drawable.bg_shadow);

		tvName.setPadding(0, 0, 0, 0);
		tvPhone.setPadding(0, 0, 0, 0);
		
		tvTagorder.setPadding(0, 0, 0, 0);

		// btState.setPadding(0, 0, 0, 0);

		tvName.setTextSize(15);
		tvPhone.setTextSize(15);
	
		tvTagorder.setTextSize(15);

		
		btDelete.setTextSize(15);

		etName.setTextSize(15);
		etPhone.setTextSize(15);
		

		tvName.setText(UserID);
		tvPhone.setText(PassWord);
		
		this.setText(Show);

		
		btDelete.setText(delete);
		btDelete.setEnabled(false);

		
		btDelete.setTextColor(Color.BLACK);

		tvName.setTextColor(Color.BLACK);
		tvPhone.setTextColor(Color.BLACK);
	
		tvTagorder.setTextColor(Color.BLACK);

		etName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		etPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
		

		etName.setSingleLine();
		etPhone.setSingleLine();
		

		etName.setGravity(Gravity.CENTER);
		etPhone.setGravity(Gravity.CENTER);
	
		tvTagorder.setGravity(Gravity.CENTER);
		btDelete.setGravity(Gravity.CENTER);

		btDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ɾ��xml����

				Delete();

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

	

		

		etName.setTextColor(Color.BLACK);
		etPhone.setTextColor(Color.BLACK);
	

		etName.setCursorVisible(true);// ��edittext���ֹ��
		etPhone.setCursorVisible(true);
		

		
		etPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
	

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

	// ͨ������޸�ini�е�����
	protected void onClicked() {

		String str=this.getText().toString();
		
	        if(str.equals(Show))
	        {
	        	
	        	Show();
	        	
	        }else if(str.equals(Add))
	        {
	        	Add();
	        }else if(str.equals(Alter))
	        {
	        	Alter();
	        }
                		
		
	}
	
	

	//�޸�
	private void Alter() {
		 
	MGridActivity.xianChengChi.execute(new Runnable() {
			
			@Override
			public void run() {
				
				String id=etName.getText().toString();
				String pw=etPhone.getText().toString();
				
				if(id.equals("")||pw.equals(""))
				{
					handler.sendEmptyMessage(3);
				}else
				{
					Map<String,String> map=new HashMap<String,String>();
					map.put("User" +index+"="+oldUserId, "User" +index+"="+id);
					map.put("PassWord" +index+"="+oldPassWord, "PassWord" +index+"="+pw);
					fileUtil.replaceUser(new File(Environment.getExternalStorageDirectory().getPath() + "/MGrid.ini"), map);
				    
		 			MGridActivity.userManager.setUser(index, id, pw,oldUserId,oldPassWord);
					handler.sendEmptyMessage(1);
					
				}
				
				
						
				
			}
		});	
	}

	//���
	private void Add() {
		
       MGridActivity.xianChengChi.execute(new Runnable() {
			
			@Override
			public void run() {
				
				String id=etName.getText().toString();
				String pw=etPhone.getText().toString();
				
				if(id.equals("")||pw.equals(""))
				{
					handler.sendEmptyMessage(3);
				}else
				{
					Map<String,String> map=new HashMap<String,String>();
					map.put("User" +index+"="+id,"PassWord" +index+"="+pw);
					fileUtil.AddUser(new File(Environment.getExternalStorageDirectory().getPath() + "/MGrid.ini"), map);
					User user;
					if(index!=0)
					{
					    user=new User(id, pw, 1);
					}else
					{ 
					    user=new User(id, pw, 0);
					}
					
					MGridActivity.userManager.addUser(index, user);
					handler.sendEmptyMessage(2);
					 
				}
			}
		});		
	}
	
	//ɾ��
	private void Delete()
	{
		MGridActivity.xianChengChi.execute(new Runnable() {
			
			@Override
			public void run() {
				
				List<String> list=new ArrayList<String>();
				list.add("User" +index+"="+etName.getText().toString());
				list.add("PassWord" +index+"="+etPhone.getText().toString());		
				fileUtil.deleteUser(new File(Environment.getExternalStorageDirectory().getPath() + "/MGrid.ini"), list);
			    
				MGridActivity.userManager.deleteUser(index);
				handler.sendEmptyMessage(0);
			}
		});		
	}

	//��ʾ 
	private void Show() {
		
		Map<Integer,User> map=MGridActivity.userManager.getUserManaget();
		User user=map.get(Integer.parseInt(tvTagorder.getText().toString()));
		if(user!=null)
		{
			oldUserId=user.getUserID();
			etName.setText(oldUserId);
			oldPassWord=user.getPassWord();
			etPhone.setText(oldPassWord);
			this.setText(Alter);
			btDelete.setEnabled(true);
		}else
		{
			this.setText(Add);
		}
		
	}

	Handler handler =new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				
				setText(Add);
				etName.setText("");
				etPhone.setText("");
				btDelete.setEnabled(false);
				Toast.makeText(getContext(), "�h���ɹ�", Toast.LENGTH_SHORT).show();
				
				
				break;

			case 1:
				
				oldUserId=etName.getText().toString();
				oldPassWord=etPhone.getText().toString();
				Toast.makeText(getContext(), "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
				
				break;
            case 2:
				
				oldUserId=etName.getText().toString();
				oldPassWord=etPhone.getText().toString();
				setText(Alter);
				btDelete.setEnabled(true);
				Toast.makeText(getContext(), "��ӳɹ�", Toast.LENGTH_SHORT).show();
				
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

		Rect rect=new Rect();
		getPaint().getTextBounds("�û�����", 0, "�û�����".length(), rect);
		int height=rect.height();
		int width=rect.height();
		
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {

			tvTagorder.layout(nX, (int) (nY), nX + (int) (nWidth * 0.029f), nY + nHeight);
			tvName.layout(nX + (int) (nWidth * 0.058f), (int) (nY ), nX + (int) (nWidth * 0.158f),
					nY + nHeight);
			etName.layout(nX + (int) (nWidth * 0.187f), nY , nX + (int) (nWidth * 0.387f),
					nY + nHeight - nHeight / 10);
			tvPhone.layout(nX + (int) (nWidth * 0.416f), (int) (nY ), nX + (int) (nWidth * 0.516f),
					nY + nHeight);
			etPhone.layout(nX + (int) (nWidth * 0.545f), nY , nX + (int) (nWidth * 0.745f),
					nY + nHeight - nHeight / 10);
			
			this.layout(nX + (int) (nWidth * 0.774f), nY, nX + (int) (nWidth * 0.874f), nY + nHeight);
			btDelete.layout(nX + (int) (nWidth * 0.903f), nY, nX + (int) (nWidth * 1), nY + nHeight);
		}

		tvTagorder.setPadding(0, (int)((0.85 * nHeight-height)/2), 0, 0);
		tvName.setPadding((int)((0.1 * nWidth-width)/3), (int)((0.85 * nHeight-height)/2), 0, 0);
		tvPhone.setPadding((int)((0.1 * nWidth-width)/3), (int)((0.85 * nHeight-height)/2), 0, 0);
		btDelete.setPadding(0, (int)((0.85*nHeight-height)/2), 0, 0);
		etName.setPadding(0,(int)((0.8*nHeight-height)/2), 0, 0);
		etPhone.setPadding(0, (int)((0.8*nHeight-height)/2), 0, 0);
	
		//this.setPadding(0, (int)((nHeight-height)/2), 0, 0);

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
			index=Integer.parseInt(tvTagorder.getText().toString());
		} else if ("FontSize".equals(strName)) {
			fontSize = Integer.parseInt(strValue);
			tvName.setTextSize(fontSize);
			tvPhone.setTextSize(fontSize);
			
			tvTagorder.setTextSize(fontSize);

			btDelete.setTextSize(fontSize);

			etName.setTextSize(fontSize);
			etPhone.setTextSize(fontSize);
			
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

		rWin.addView(tvTagorder);
		
		rWin.addView(etName);
		rWin.addView(tvName);
		
		rWin.addView(etPhone);
		rWin.addView(tvPhone);
		
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

}

