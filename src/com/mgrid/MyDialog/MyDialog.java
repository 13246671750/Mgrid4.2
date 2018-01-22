package com.mgrid.MyDialog;

import com.mgrid.main.R;
import com.sg.uis.LsyNewView.FlikerProgressBar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * 
 * @author lsy
 * �Զ��������dialog
 */

public class MyDialog extends Dialog{

private FlikerProgressBar bar;
	
	
	public MyDialog(Context context) {
		super(context,R.style.MyDialog);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.mydialog);
	     //���հ״�����ȡ������
	     setCanceledOnTouchOutside(false);
	     //��ʼ������ؼ�
	     initView();
	
	}
	

    /**
     * ��ʼ������ؼ�
     */
    private void initView() {
    	bar=(FlikerProgressBar) findViewById(R.id.flikerbar);
    }
    
    public FlikerProgressBar getBar()
    {
    	return bar;
    }
}
