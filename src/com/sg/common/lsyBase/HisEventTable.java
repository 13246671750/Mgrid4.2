package com.sg.common.lsyBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.sg.common.lsyBase.HisEventAdapter.TableCell;
import com.sg.common.lsyBase.HisEventAdapter.TableRow;

import android.content.Context;
import android.graphics.Color;
import android.widget.ListView;

public class HisEventTable extends ListView {

	public HisEventTable(Context context) {
		super(context);
		table= new ArrayList<TableRow>();
		m_tableAdapter = new HisEventAdapter(context, table);
		this.setAdapter(m_tableAdapter);
	}
	
	

	public void notifyTableLayoutChange(int l, int t, int r, int b) {
		m_nLeft = l;
		m_nTop = t;
		m_nRight = r;
		m_nBottom = b;

		m_tableAdapter.notifyDataSetChanged();
		this.layout(m_nLeft, m_nTop, m_nRight, m_nBottom);
	}

	public void update() {
		m_tableAdapter.notifyDataSetChanged();
		this.layout(m_nLeft, m_nTop, m_nRight, m_nBottom
				- m_nLayoutBottomOffset);
		m_nLayoutBottomOffset = -m_nLayoutBottomOffset;
	}

	public void updateContends(List<String> listTitles,
			List<List<String>> listContends) {
		if (listTitles == null || listContends == null) // ��������������Ϊ�� return��
			return;
		int column = listTitles.size(); // �õ�����ĸ���
		
      
        m_tableAdapter.titleIsChang=true;
        

		int width = (m_nRight - m_nLeft) / column; // �õ�ÿ������Ŀ��
	
		int lstcontendsize = listContends.size();
		m_tableAdapter.m_cTexColor = m_cFontColor;

		for (int i = 0; i < lstcontendsize; i++) {
			List<String> lst = listContends.get(i);

			TableCell[] cells = new TableCell[listTitles.size()];//һ�е�ĳһ��Ԫ��
			int cColor = i % 2 == 0 ? m_cEvenRowBackground
					: m_cOddRowBackground;
			for (int j = 0; j < lst.size(); ++j) {
				cells[j] = new TableCell(lst.get(j), width,
						LayoutParams.MATCH_PARENT, TableCell.STRING, cColor);   //Ϊÿ��Ԫ�ظ�ֵ
				
			}	
			table.add(new TableRow(cells));
		}		
	}

	public void setFontColor(int cColor) {
		m_cFontColor = cColor;
	}
	
	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}
	
	public static String getDate(long milliSeconds, String dateFormat) {
		if (0 == milliSeconds)
			return ""; // 0 ��ʾδ��ȡ����Чʱ�� -- CharlesChen

		DateFormat formatter = new SimpleDateFormat(dateFormat);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return new String(formatter.format(calendar.getTime()));
	}


	// params :
	protected HisEventAdapter m_tableAdapter = null;

	protected int m_nLeft = 0;
	protected int m_nTop = 0;
	protected int m_nRight = 0;
	protected int m_nBottom = 0;

	protected int m_nTableWidth = 0;
	protected int m_nTableHeight = 0;

	protected int m_cFontColor = Color.GREEN;
	public int m_cOddRowBackground = 0xFF000000; // ����
	public int m_cEvenRowBackground = 0xFF000000; // ż��
	public boolean m_bUseTitle = true;
	int m_nLayoutBottomOffset = 1; // ��̬����layout��С

	public ArrayList<TableRow> table =null;
	private int oldTitleSize=0;
	private boolean isChange=false;
    int i=0; 
    
    
}
