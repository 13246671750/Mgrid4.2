package com.sg.uis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.common.MyAdapter;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtTable;
import com.sg.web.SaveEquiptObject;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;
import com.sg.web.utils.ViewObjectColorUtil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import data_model.local_his_signal;

/** ��ʷ�ź� */
// �ź���ʷ���� SaveEquipt
// author :fjw0312
// time:2015 11 02
public class SaveEquipt extends UtTable implements IObject,ViewObjectSetCallBack {

	// ������Ӣ���л�
	private String DeviceName = LanguageStr.DeviceName;
	private String SignalName = LanguageStr.SignalName;
	private String Value = LanguageStr.Value;
	private String Unit = LanguageStr.Unit;
	private String ValueType = LanguageStr.ValueType;
	private String AlarmSeverity = LanguageStr.AlarmSeverity;
	private String AcquisitionTime = LanguageStr.AcquisitionTime;

	private String DeviceList = LanguageStr.DeviceList;
	private String SetTime = LanguageStr.SetTime;
	private String PreveDay = LanguageStr.PreveDay;
	private String NextDay = LanguageStr.NextDay;
	private String Receive = LanguageStr.Receive;

	private String set = LanguageStr.set;

	private String textColor = "#FF000000";
	private String btnColor = "#FFC0C0C0";
	private String titleColor = "#FF242424";

	private MyAdapter myAdapter = null;

	private PopupWindow popupWindow;

	public SaveEquipt(Context context) {
		super(context);
		m_rBBox = new Rect();

		// if(MGridActivity.whatLanguage)
		// {
		// DeviceName="�豸����";
		// SignalName="�ź�����";
		// Value="��ֵ";
		// Unit="��λ";
		// ValueType="��ֵ����";
		// AlarmSeverity="�澯�ȼ�";
		// AcquisitionTime="�ɼ�ʱ��";
		//
		// DeviceList=" �豸�� ";
		// SetTime="��������";
		// PreveDay="ǰһ��";
		// NextDay="��һ��";
		// Receive=" ��ȡ ";
		// }
		// else
		// {
		// DeviceName="Device Name";
		// SignalName="Signal Name";
		// Value="Value";
		// Unit="Numerical Signal";
		// ValueType="Value Type";
		// AlarmSeverity="Alarm Severity";
		// AcquisitionTime="Acquisition Time";
		//
		// DeviceList=" Device�� ";
		// SetTime="Set Time";
		// PreveDay="Previous Day";
		// NextDay="Next Day";
		// Receive=" Receipt ";
		// }

		
		
		// ��ͷ����
		lstTitles = new ArrayList<String>();
		lstTitles.add(DeviceName);
		lstTitles.add(SignalName);
		lstTitles.add(Value);
		lstTitles.add(Unit);
		lstTitles.add(ValueType);
		lstTitles.add(AlarmSeverity);
		lstTitles.add(AcquisitionTime);
		// �ź�����ʾtext
		view_text = new TextView(context);
		view_text.setTextColor(Color.BLACK);
		view_text.setText(DeviceList); // Equiptment��
		view_text.setTextSize(15);
		view_text.setGravity(Gravity.CENTER);
		view_text.setBackgroundColor(Color.argb(100, 100, 100, 100));
		// view_text.setBackgroundColor(Color.parseColor(btnColor));
		// view_text.setTextColor(Color.parseColor(textColor));

		// ����ѡ��button
		view_timeButton = new Button(context);
		view_timeButton.setText(SetTime); // Set Time
		view_timeButton.setTextColor(Color.BLACK);
		view_timeButton.setTextSize(15);
		view_timeButton.setPadding(2, 2, 2, 2);
		view_timeButton.setOnClickListener(l);// ���øÿؼ��ļ���
		// view_timeButton.setBackgroundResource(android.R.drawable.btn_default);
		// view_timeButton.setBackgroundColor(Color.parseColor(btnColor));
		// view_timeButton.setTextColor(Color.parseColor(textColor));
		// ǰһ��button
		view_PerveDay = new Button(context);
		view_PerveDay.setText(PreveDay); // PreveDay
		view_PerveDay.setTextColor(Color.BLACK);
		view_PerveDay.setTextSize(15);
		view_PerveDay.setPadding(2, 2, 2, 2);
		view_PerveDay.setOnClickListener(l);// ���øÿؼ��ļ���
		// view_PerveDay.setBackgroundResource(android.R.drawable.btn_default);
		// view_PerveDay.setBackgroundColor(Color.parseColor(btnColor));
		// view_PerveDay.setTextColor(Color.parseColor(textColor));
		// ��һ��button
		view_NextDay = new Button(context);
		view_NextDay.setText(NextDay); // NextDay
		view_NextDay.setTextColor(Color.BLACK);
		view_NextDay.setTextSize(15);
		view_NextDay.setPadding(2, 2, 2, 2);
		view_NextDay.setOnClickListener(l);// ���øÿؼ��ļ���
		// view_NextDay.setBackgroundResource(android.R.drawable.btn_default);
		// view_NextDay.setBackgroundColor(Color.parseColor(btnColor));
		// view_NextDay.setTextColor(Color.parseColor(textColor));
		// ����receive
		view_Receive = new Button(context);
		view_Receive.setText(Receive); // Receive
		view_Receive.setTextColor(Color.BLACK);
		view_Receive.setTextSize(15);
		view_Receive.setPadding(2, 2, 2, 2);
		view_Receive.setOnClickListener(l); // ���øÿؼ��ļ���
		// view_Receive.setBackgroundResource(android.R.drawable.btn_default);
		// view_Receive.setBackgroundColor(Color.parseColor(btnColor));
		// view_Receive.setTextColor(Color.parseColor(textColor));

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		// dialog = new DatePickerDialog(context, new OnDateSetListener() {
		// @Override
		// public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		// // TODO Auto-generated method stub
		// num = 0;
		//
		// } 
		// }, year, month, day);

		dialog = new DatePickerDialog(context, null, year, month, day);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, set, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				num = 0;
			}
		});
		dialog.setTitle("");
		dialog.getDatePicker().setCalendarViewShown(false);

		myAdapter = new MyAdapter(getContext(), nameList);
		view_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isFirst) {
					parse_expression();
					isFirst = false;
				}

				View view = m_rRenderWindow.m_oMgridActivity.getLayoutInflater().inflate(R.layout.pop, null);
				popupWindow = new PopupWindow(view, view_text.getWidth(), 200, true);
				// ����һ��͸���ı�������Ȼ�޷�ʵ�ֵ�������⣬������ʧ
				popupWindow.setBackgroundDrawable(new BitmapDrawable());

				// ���õ�������ⲿ��������ʧ
				popupWindow.setOutsideTouchable(true);
				popupWindow.setFocusable(true);
				popupWindow.showAsDropDown(view_text);

				ListView lv = (ListView) view.findViewById(R.id.lv_list);

				myAdapter.setTextColor(textColor);
				myAdapter.setBtnColor(btnColor);
				lv.setAdapter(myAdapter);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						view_text.setText(nameList.get(position));
						popupWindow.dismiss();
					}
				});

			}
		});

		lstContends = new ArrayList<List<String>>();

		map_EquiptNameList = new HashMap<String, String>(); // <�ź�����,�豸id-�ź�id>

	}
	
	
	private DatePicker findDatePicker(ViewGroup group) {
		   if (group != null) {
		      for (int i = 0, j = group.getChildCount(); i < j; i++) {
		         View child = group.getChildAt(i);
		         if (child instanceof DatePicker) {
		            return (DatePicker) child;
		         } else if (child instanceof ViewGroup) {
		            DatePicker result = findDatePicker((ViewGroup) child);
		            if (result != null)
		               return result;
		         }
		      }
		   }
		   return null;
	}

	// ������ view_Receive
	private OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			//
			// TODO Auto-generated method stub

			// ��ȡ���õ�����
			int set_year = dialog.getDatePicker().getYear();
			int set_month = dialog.getDatePicker().getMonth() + 1;
			int set_day = dialog.getDatePicker().getDayOfMonth();

			// �ж���һ����������
			if (arg0 == view_timeButton) {
				dialog.show(); // �������ڶԻ���
		
				flag = 1;
				num = 0;
				return;
			} else if (arg0 == view_Receive) {
				num = 0;
				//
			} else if (arg0 == view_NextDay) {
				//
				num++;
				//
				set_day = set_day + num; // ������һ�죻 num������֮��
				// �жϲ�������������
				long now_time = java.lang.System.currentTimeMillis();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ʱ���ʽת��
				Date date = new Date(now_time);
				String sampletime = formatter.format(date);
				String now_year = sampletime.substring(0, 4);
				String now_month = sampletime.substring(5, 7);
				String now_day = sampletime.substring(8, 10);
				int int_now_year = Integer.valueOf(now_year);
				int int_now_month = Integer.valueOf(now_month);
				int int_now_day = Integer.valueOf(now_day);
				// ��ĩ�ж�
				if (set_day > 31) {
					set_day = set_day - 31;
					set_month++;
					if (set_month > 12) {
						set_month = 1;
						set_year++;
					}
				}
				if (set_day < 1) {
					set_day = set_day + 31;
					set_month--;
					if (set_month < 1) {
						set_month = 12;
						set_year--;
					}
				}
				if ((set_year == int_now_year) && (set_month == int_now_month) && (set_day > int_now_day)) {
					set_day = int_now_day;
					num--;
				}
				//

			} else if (arg0 == view_PerveDay) {
				//

				num--; // num������֮��
				set_day = set_day + num; // ������1��
				if (set_day < 1) {
					set_day = 31 + set_day;
					set_month--;
					if (set_month < 1) {
						set_month = 12;
						set_year--;
					}
				}
				if (set_day > 31) {
					set_day = set_day - 31;
					set_month++;
					if (set_month > 12) {
						set_month = 1;
						set_year++;
					}
				}

			}
			// �����·������ַ���ʽ
			String str_day;
			String str_nomth;
			if (set_day < 10) {
				str_day = "0" + String.valueOf(set_day);
			} else {
				str_day = String.valueOf(set_day);
			}
			if (set_month < 10) {
				str_nomth = "0" + String.valueOf(set_month);
			} else {
				str_nomth = String.valueOf(set_month);
			}
			get_day = String.valueOf(set_year) + "-" + str_nomth + "-" + str_day;

			// get_day =
			// String.valueOf(set_year)+String.valueOf(set_month)+String.valueOf(set_day);
			//
			if ("".equals(get_day))
				return;

			String equipt_name = view_text.getText().toString();

			if (DeviceList.equals(equipt_name))
				return;
			str_EquiptId = map_EquiptNameList.get(equipt_name);
			//

			// ���ÿؼ���Ҫ���±�ʶ����
			m_bneedupdate = true;
			view_Receive.setEnabled(false);
			handler.postDelayed(runable, 2000);
		}
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
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			notifyTableLayoutChange(nX, nY, nX + nWidth, nY + nHeight);

			for (int i = 0; i < m_title.length; ++i)
				m_title[i].layout(nX + i * nWidth / m_title.length, nY - 18,
						nX + i * nWidth / m_title.length + nWidth / m_title.length, nY);

			// ����view_button�ĵװ�ռ�
			int pv = nWidth / 5;
			view_text.layout(nX, nY - 40, nX + pv, nY - 15);
			view_timeButton.layout(nX + pv + 20, nY - 40, nX + 2 * pv, nY - 16);
			view_NextDay.layout(nX + 2 * pv + 20, nY - 40, nX + 3 * pv, nY - 16);
			view_PerveDay.layout(nX + 3 * pv + 20, nY - 40, nX + 4 * pv, nY - 16);
			view_Receive.layout(nX + 4 * pv + 20, nY - 40, nX + 5 * pv, nY - 16);
		}
	}

	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		// Log.e("SaveEquipt-onDraw", "into onDraw!");
		super.onDraw(canvas);
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		this.setClickable(true);
		this.setBackgroundColor(m_cBackgroundColor);

		m_bUseTitle = false;
		m_title = new TextView[lstTitles.size()];
		for (int i = 0; i < m_title.length; i++) {
			m_title[i] = new TextView(getContext());
			// m_title[i].setTextColor(Color.BLACK);
			// m_title[i].setTextSize(25);
			// m_title[i].setBackgroundColor(Color.GRAY);
			m_title[i].setGravity(Gravity.CENTER);
			m_title[i].setText(lstTitles.get(i));
			m_title[i].setTextColor(Color.parseColor(titleColor));
			rWin.addView(m_title[i]);
		}
		m_rRenderWindow = rWin;
		m_rRenderWindow.viewList.add(base);
		rWin.addView(this);
		// view_button������ӵ�����
		rWin.addView(view_Receive);
		rWin.addView(view_NextDay);
		rWin.addView(view_PerveDay);
		rWin.addView(view_text);

		rWin.addView(view_timeButton);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {

		rWin.removeView(this);
		// view_button�����ӵ�����ȥ��
		rWin.removeView(view_Receive);
		rWin.removeView(view_NextDay);
		rWin.removeView(view_PerveDay);
		rWin.removeView(view_text);

		rWin.removeView(view_timeButton);
	}

	public void parseProperties(String strName, String strValue, String strResFolder) {
		if ("ZIndex".equals(strName)) {
			m_nZIndex = Integer.parseInt(strValue);
			if (MainWindow.MAXZINDEX < m_nZIndex)
				MainWindow.MAXZINDEX = m_nZIndex;
		} else if ("Location".equals(strName)) {
			String[] arrStr = strValue.split(",");
			m_nPosX = Integer.parseInt(arrStr[0]);
			m_nPosY = Integer.parseInt(arrStr[1]);

			// �趨�б��������
			m_nLeft = m_nPosX;
			m_nTop = m_nPosY;
			m_nRight = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);

			// �趨�б��������
			m_nTableWidth = m_nWidth;
			m_nTableHeight = m_nHeight;
			m_nRight = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
			// parse_expression();
		} else if ("RadioButtonColor".equals(strName)) {
			m_cRadioButtonColor = Color.parseColor(strValue);
		} else if ("ForeColor".equals(strName)) {
			foreColor=strValue;
			m_cForeColor = Color.parseColor(strValue);
			this.setFontColor(m_cForeColor);
		} else if ("BackgroundColor".equals(strName)) {
			backgroundColor=strValue;
			m_cBackgroundColor = Color.parseColor(strValue);
			this.setBackgroundColor(m_cBackgroundColor);
		} else if ("BorderColor".equals(strName)) {
			
			m_cBorderColor = Color.parseColor(strValue);
		} else if ("OddRowBackground".equals(strName)) {
			oddRowBackground=strValue;
			m_cOddRowBackground = Color.parseColor(strValue);
		} else if ("EvenRowBackground".equals(strName)) {
			evenRowBackground=strValue;
			m_cEvenRowBackground = Color.parseColor(strValue);
		} else if ("SaveTime".equals(strName)) {
			// save_time = Integer.parseInt(strValue);
			// save_time = save_time*60*60; //����������ĵ�λΪh

		} else if ("BtnColor".equals(strName)) {
			if (!strValue.isEmpty()) {

			
				
				if ("#FF000000".equals(strValue)) {
					view_text.setBackgroundResource(R.drawable.bg_shadow);
					view_timeButton.setBackgroundResource(R.drawable.bg_shadow);
					view_PerveDay.setBackgroundResource(R.drawable.bg_shadow);
					view_NextDay.setBackgroundResource(R.drawable.bg_shadow);
					view_Receive.setBackgroundResource(R.drawable.bg_shadow);
					// myAdapter.setBtnColor("#FFFFFFFF");
					btnColor = "#FF4D4D4D";
				} else {
					btnColor = strValue;
					view_text.setBackgroundColor(Color.parseColor(btnColor));
					view_timeButton.setBackgroundColor(Color.parseColor(btnColor));
					view_PerveDay.setBackgroundColor(Color.parseColor(btnColor));
					view_NextDay.setBackgroundColor(Color.parseColor(btnColor));
					view_Receive.setBackgroundColor(Color.parseColor(btnColor));
					myAdapter.setBtnColor(btnColor);
				}
			}
		} else if ("TextColor".equals(strName)) {
			
			
			if (!strValue.isEmpty()) {
			
				textColor = strValue;
				myAdapter.setTextColor(textColor);
				view_text.setTextColor(Color.parseColor(textColor));
				view_timeButton.setTextColor(Color.parseColor(textColor));
				view_PerveDay.setTextColor(Color.parseColor(textColor));
				view_NextDay.setTextColor(Color.parseColor(textColor));
				view_Receive.setTextColor(Color.parseColor(textColor));
			}
		} else if ("TitleColor".equals(strName)) {
			if (!strValue.isEmpty()) {
			
				titleColor = strValue;
				myAdapter.notifyDataSetChanged();
			}
		}
	}

	Runnable runable = new Runnable() {
		public void run() {

			handler.sendEmptyMessage(2);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 2:

				view_Receive.setEnabled(true);

				break;
			}

		};
	};

	@Override
	public void initFinished() {
	}

	public String getBindingExpression() {
		return m_strExpression;
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

	@Override
	public void updateWidget() {
		update();
	}

	@Override
	public boolean updateValue() // ���ڸ��²�����������Ҫ�����´��� fjw notice
	{
		m_bneedupdate = false; // ���Ϊ�棬��ʾ���ݲ��������ݸ���ʱʱˢ����
		//
		// ��ȡ��ʷ�����豸�б� ����豸����ʷ�����б� �б�<�豸��<��ʷ�źŽṹ��>>
		List<local_his_signal> his_sig_list = new ArrayList<local_his_signal>();
		if (m_rRenderWindow.m_oShareObject.m_mapSaveEquipt == null)
			return false;
		his_sig_list = m_rRenderWindow.m_oShareObject.m_mapSaveEquipt.get(this.getUniqueID());
		// ������һ���б� ��õ�һ���豸��ʷ�ź�
		if (his_sig_list == null) {

			return false;
		}

		// ������һ���豸�б� ��ÿ����ʷ�źŵĽṹ��
		lstContends.clear(); // ���ҳ�����ǰ���� ���ź�
		Iterator<local_his_signal> iterator_his = his_sig_list.iterator();
		while (iterator_his.hasNext()) {
			local_his_signal his_sig = iterator_his.next();
			List<String> lstRow_his = new ArrayList<String>();

			lstRow_his.add(his_sig.equip_name);
			lstRow_his.add(his_sig.name);
			float f = Float.parseFloat(his_sig.value);
			int ii = (int) (f * 100);
			f = (float) (ii / 100.0);
			lstRow_his.add(f + ""); // ��һ���������Ϊ�˱���С�������λ

			lstRow_his.add(his_sig.unit);
			lstRow_his.add(his_sig.value_type);
			lstRow_his.add(his_sig.severity);
			lstRow_his.add(his_sig.freshtime);
			// fjw_signal.addAll(lstRow_his);
			lstContends.add(lstRow_his);

		}
		updateContends(lstTitles, lstContends);
		updateContends(lstTitles, lstContends);
		// fjw_signal.clear();
		his_sig_list.clear();

		return true;
	}

	// �������ؼ����ʽ�����ؿؼ����ʽ��
	@SuppressWarnings({ "unchecked", "static-access" })
	public boolean parse_expression() {
		nameList.add(DeviceList);
		if ("".equals(m_strExpression))
			return false;
		String Mathstr = UtExpressionParser.getInstance().getMathExpression(m_strExpression);
		ArrayList<Integer> list = new ArrayList<Integer>();
		String[] strCExp = Mathstr.split("\\|");
		for (String str : strCExp) {
			String[] strResult = str.split("\\]");
			String[] strResult1 = strResult[0].split("\\:");
			list.add(Integer.parseInt(strResult1[1]));
		}
		for (int id : list) {
			String str_equiptName = DataGetter.getEquipmentName(id);
			map_EquiptNameList.put(str_equiptName, String.valueOf(id));
			nameList.add(str_equiptName);
		}

		return true;
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
	int m_nZIndex = 15;
	int m_nPosX = 40;
	int m_nPosY = 604;
	int m_nWidth = 277;
	int m_nHeight = 152;
	float m_fAlpha = 0.8f;
	String m_strExpression = "Binding{[Equip[Equip:2]]}";
	int m_cRadioButtonColor = 0xFFFF8000;
	int m_cForeColor = 0xFF00FF00;
	int m_cBackgroundColor = 0xFF000000;
	int m_cBorderColor = 0xFFFFFFFF;

	// radio buttons
	// RadioButton[] m_oRadioButtons;

	// �̶�������
	TextView[] m_title;
	TextView view_text; // �ź�����ʾtext
	// �ź���ѡ��spinner
	Button view_timeButton; // ����ѡ��button
	Button view_PerveDay; // ǰһ��button
	Button view_NextDay; // ��һ��button
	Button view_Receive; // ����receive

	private HashMap<String, String> map_EquiptNameList = null; // <�豸�����豸id>

	private DatePickerDialog dialog; // ���ڶԻ���ѡ��Ӧ��
	private int year, month, day; // �Ի�����ʾ�������ձ���
	private Calendar calendar;
	private int flag = 0;
	private int num = 0; // �Ӽ���Ŧ�Ӽ���
	public static String str_EquiptId = ""; // ����Ҫ���豸id�ַ���
	public static String get_day = ""; // ��Ҫ��ȡ���ݵ�����
	public static int save_time = 60 * 60 * 2; // saveEquipt�ɼ�ʱ�� 2h

	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;

	public boolean m_bNeedINIT = true;
	public boolean m_bneedupdate = false;

	// TODO: ��ʱ��������
	boolean m_needsort = true;
	// ArrayList<String> m_sortedarray = null;
	List<String> lstTitles = null;
	List<List<String>> lstContends = null;

	private ArrayList<String> nameList = new ArrayList<String>();
	
	private boolean isFirst = true;
	private ViewObjectBase base=new SaveEquiptObject();
	
	String foreColor,backgroundColor,oddRowBackground,evenRowBackground;

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
		
		
	
		
		((SaveEquiptObject)base).setBtnColor(ViewObjectColorUtil.getColor(btnColor));
		((SaveEquiptObject)base).setTextColor(ViewObjectColorUtil.getColor(textColor));
		((SaveEquiptObject)base).setTitleColr(ViewObjectColorUtil.getColor(titleColor));
		((SaveEquiptObject)base).setNameList(lstTitles);
		
		
		((SaveEquiptObject)base).setBackgroundColor(ViewObjectColorUtil.getColor(backgroundColor)); 
		((SaveEquiptObject)base).setForeColor(ViewObjectColorUtil.getColor(foreColor));
		((SaveEquiptObject)base).setEvenRowBackground(ViewObjectColorUtil.getColor(evenRowBackground));
		((SaveEquiptObject)base).setOddRowBackground(ViewObjectColorUtil.getColor(oddRowBackground));
		
	}


	@Override
	public void onSetData() {
		// TODO Auto-generated method stub
		
	}
}
