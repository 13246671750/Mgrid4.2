package com.mgrid.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import com.mgrid.main.MGridActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.text.format.DateFormat;

@SuppressWarnings("deprecation")
public class CameraUtils {

	private double C_sizeM = 100;
	public Camera camera;
	public boolean isPreviewing;
	public Context context;
	public static int count = 0;

	public CameraUtils(Context context) {
		this.context = context;

	}

	@SuppressLint("NewApi")
	public void openCamera() {

		Camera.CameraInfo cameraInfo = new CameraInfo();
		// ����豸�ϵ�Ӳ��camera����
		int count = Camera.getNumberOfCameras();

		for (int i = 0; i < count; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					camera = Camera.open(i); // ���Դ�ǰ������ͷ
				} catch (Exception e) {
					System.out.println("��qian����ͷ�쳣" + e.toString());
					e.printStackTrace();
					releaseCamera();
				}
			}
		}

		if (camera == null) {
			for (int i = 0; i < count; i++) {
				Camera.getCameraInfo(i, cameraInfo);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					try {
						camera = Camera.open(i); // ���Դ򿪺�������ͷ
					} catch (Exception e) {
						System.out.println("�򿪺�������ͷ�쳣" + e.toString());
						e.printStackTrace();
						releaseCamera();
					}
				}
			}
		}

		try {
			// camera.setPreviewDisplay(surfaceHolder);
			if (camera != null) {
				camera.startPreview(); // ��Ԥ������
				camera.autoFocus(autoFocusCallback);
				isPreviewing = true;
			} else {
				// MGridActivity.handler.sendEmptyMessage(3);
				System.out.println("û��ǰ������ͷ");
				releaseCamera();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �Զ��Խ�������
	 **/
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub

			System.out.println("�Զ��Խ����");

			if (success && camera != null) {

				// ���ûص������������ţ�Դ���ݣ�JPEG���ݣ�
				camera.takePicture(null, null, new PictureCallback() {

					@SuppressLint("SdCardPath")
					@Override
					public void onPictureTaken(final byte[] data, Camera camera) {
						// TODO Auto-generated method stub

						MGridActivity.xianChengChi.execute(new Runnable() {
							
							@Override
							public void run() {
								
								
								// ʹ�õ�ǰ��ʱ��ƴ��ͼƬ������
								String name = DateFormat.format("yyyy_MM_dd_hhmmss", Calendar.getInstance(Locale.CHINA))
										+ ".jpg";

								File file = new File("/mgrid/log/vtu_camera/");
								double sizeM = getDirSize(file);
								if (sizeM > C_sizeM) {
									deleteFile(file, count / 10);
								}

								file.mkdirs(); // �����ļ��б�����Ƭ
								String filename = file.getPath() + File.separator + name;

								Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
								try {

									FileOutputStream fileOutputStream = new FileOutputStream(filename);
									boolean b = bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
									fileOutputStream.flush();
									fileOutputStream.close();

									if (b) {
										MGridActivity.handler.sendEmptyMessage(4);
									} else {
										MGridActivity.handler.sendEmptyMessage(5);
									}

								} catch (FileNotFoundException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								} finally {
									releaseCamera();// �ͷ�Camera

								}
								
							}
						});						
					}
				});
			} else {
				releaseCamera();// �ͷ�Camera
			}
		}
	};

	public static double getDirSize(File file) {
		// �ж��ļ��Ƿ����
		if (file.exists()) {
			// �����Ŀ¼��ݹ���������ݵ��ܴ�С
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				count = children.length;
				for (File f : children)
					size += getDirSize(f);
				return size;
			} else {// ������ļ���ֱ�ӷ������С,�ԡ��ס�Ϊ��λ
				double size = (double) file.length() / 1024 / 1024;
				return size;
			}
		} else {
			System.out.println("�ļ������ļ��в����ڣ�����·���Ƿ���ȷ��");
			return 0.0;
		}
	}

	public static void deleteFile(File file, int count) {
		if (file.exists() && file.isDirectory()) {
			String year = TimeUtils.getYear();
			String month = TimeUtils.getMonth();
			String day = TimeUtils.getDay();
			String name = "";
			if (month.length() == 1) {
				name = year + "_" + "0" + month + "_" + day;
			} else {
				name = year + "_" + month + "_" + day;
			}
			System.out.println(name);
			File[] children = file.listFiles();
			for (File f : children) {
				if (count > 0) {
					if (!f.getName().contains(name)) {
						f.delete();
						count--;
					}
				} else {
					break;
				}
			}
		}
	}

	/**
	 * �ͷ�����ͷ��Դ
	 */
	private void releaseCamera() {
		if (camera != null) {
			try {
				camera.setPreviewDisplay(null);
				camera.stopPreview();
				camera.release();
				camera = null;
				isPreviewing = false;
				System.out.println("�ͷ���");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
