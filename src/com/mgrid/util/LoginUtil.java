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
//		if (MGridActivity.whatLanguage) {
//
//			yes = "����";
//			no = "�˳�";
//			systemExit = "��½";
//
//			Text1 = "��������ȷ";
//			Text2 = "�û������������";
//
//		} else {
//
//			yes = "ok";
//			no = "cancel";
//			systemExit = "LOGIN";
//
//			Text1 = "Please enter sure����";
//			Text2 = "Password or user name error";
//
//		}
	}

//	private void saveLogin(String text) {
//		File f = new File(filePath + "/login" + ".login");
//		if (!f.exists()) {
//			try {
//				f.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		try {
//			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream(f), "gb2312"));
//			writer.write(text);
//			writer.flush();
//			writer.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

//	public void setPasswordDialog() {
//
//		LayoutInflater factory = LayoutInflater.from(context);
//
//		final View textEntryView = factory.inflate(R.layout.page_xml, null);
//
//		final AlertDialog alertDialog = new AlertDialog.Builder(context)
//				.setTitle(systemExit)
//
//				.setView(textEntryView)
//
//				.setPositiveButton(yes, null)
//
//				.setNegativeButton(no, new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//
//						MGridActivity activity = (MGridActivity) context;
//						activity.finish();
//					}
//				}).create();
//
//		alertDialog.setCancelable(false);
//
//		alertDialog.show();
//
//		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//
//						final EditText etPassword = (EditText) textEntryView
//								.findViewById(R.id.pageet);
//
//						final String password = etPassword.getText().toString()
//								.trim();
//
//						if (password != null && !password.equals("")) {
//
//							alertDialog.dismiss();
//							MGridActivity.xianChengChi.execute(new Runnable() {
//
//								@Override
//								public void run() {
//									saveLogin(password);
//								}
//							});
//
//						} else {
//							Toast.makeText(context, Text1, Toast.LENGTH_SHORT)
//									.show();
//
//						}
//					}
//				});
//	}

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
