package com.mgrid.util;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.R;
import com.sg.common.LanguageStr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginUtil {

	private String yes = LanguageStr.yes;
	private String no =  LanguageStr.no;
	private String systemExit =  LanguageStr.systemExit;

	private String Text1 = LanguageStr.Text1;
	private String Text2 = LanguageStr.Text2;

	Context context;
	String filePath = Environment.getExternalStorageDirectory().getPath();

	public LoginUtil(Context context) {
		this.context = context;

	}	
	
	

	public void showListDialog()
	{
		final String[] item= {"密码登陆","人脸识别"};
		AlertDialog.Builder   dialog=new  AlertDialog.Builder(context);
		dialog.setTitle("登陆方式");
		dialog.setItems(item, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				switch (which) {
				case 0:
					
					Toast.makeText(context, "选择了密码登陆", Toast.LENGTH_LONG).show();
					
					break;
	            case 1:
					
	            	Toast.makeText(context, "选择了人脸识别", Toast.LENGTH_LONG).show();
	            	
					break;
				}
				
				
			}
		});
		dialog.show();
		
	}
	


	public void showWaiterAuthorizationDialog() {

		LayoutInflater factory = LayoutInflater.from(context);

		final View textEntryView = factory.inflate(R.layout.page_xml, null);

		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setTitle(systemExit)

				.setView(textEntryView)

				.setPositiveButton(yes, null)

				.setNegativeButton(no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						MGridActivity activity = (MGridActivity) context;
						activity.finish();
					}
				}).create();

		alertDialog.setCancelable(false);

		alertDialog.show();

		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						final EditText etPassword = (EditText) textEntryView
								.findViewById(R.id.pageet);

						String password = etPassword.getText().toString()
								.trim();

						if (password.equals(MGridActivity.loginPassWord)) {

							alertDialog.dismiss();

						} else {
							Toast.makeText(context, Text2, Toast.LENGTH_SHORT)
									.show();

						}
					}
				});
	}
}
