package com.mgrid.main;

/**
 * �������յ�Activity
 * @author Chenww
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class CameraActivity extends Activity {

	private SurfaceView mySurfaceView;
	private SurfaceHolder myHolder;
	private double C_sizeM = 100;
	public Camera camera;
	public Context context;
	public static double count = 0;
	public static List<String> nameData = new ArrayList<String>();
	private static final String TAG = "CameraActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		// ȫ��
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// ���ò���
		setContentView(R.layout.activity_camera);
		Log.i(TAG, "oncreate");

		// ��ʼ��surface
		initSurface();

		// ����ÿ��߳̽������գ���ΪActivity��δ��ȫ��ʾ��ʱ�����޷��������յģ�SurfaceView��������ʾ
		new Thread(new Runnable() {
			@Override
			public void run() {
				// ��ʼ��camera���Խ�����
				initCamera();
			}
		}).start();

	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		MGridActivity.isNOChangPage=true;
		System.out.println("�ұ�������");
	}
	
	// ��ʼ��surface
	@SuppressWarnings("deprecation")
	private void initSurface() {
		// ��ʼ��surfaceview
		mySurfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);

		// ��ʼ��surfaceholder
		myHolder = mySurfaceView.getHolder();

	}

	// ��ʼ������ͷ
	private void initCamera() {

		// �����������ͷ
		if (checkCameraHardware(getApplicationContext())) {
			// ��ȡ����ͷ����ѡǰ�ã���ǰ��ѡ���ã�
			if (openFacingFrontCamera()) {
				Log.i(TAG, "openCameraSuccess");
				// ���жԽ�
				autoFocus();
			} else {
				Log.i(TAG, "openCameraFailed");
			}

		}
	}

	// �Խ�������
	private void autoFocus() {

		try {
			// ��Ϊ��������ͷ��Ҫʱ�䣬�������߳�˯����
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// �Զ��Խ�
		camera.autoFocus(myAutoFocus);

		// �Խ�������
		camera.takePicture(null, null, myPicCallback);
	}

	// �ж��Ƿ��������ͷ
	private boolean checkCameraHardware(Context context) {

		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// �豸��������ͷ
			return true;
		} else {
			// �豸����������ͷ
			return false;
		}

	}

	// �õ���������ͷ
	private boolean openFacingFrontCamera() {

		// ���Կ���ǰ������ͷ
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					Log.i(TAG, "tryToOpenCamera");
					camera = Camera.open(camIdx);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return false;
				}
			}
		}

		// �������ǰ��ʧ�ܣ���ǰ�ã���������
		if (camera == null) {
			for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
				Camera.getCameraInfo(camIdx, cameraInfo);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					try {
						camera = Camera.open(camIdx);
					} catch (RuntimeException e) {
						return false;
					}
				}
			}
		}

		try {
			// �����myCameraΪ�Ѿ���ʼ����Camera����
			camera.setPreviewDisplay(myHolder);
		} catch (IOException e) {
			e.printStackTrace();
			releaseCamera();
		}

		camera.startPreview();

		return true;
	}

	// �Զ��Խ��ص�����(��ʵ��)
	private AutoFocusCallback myAutoFocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
		}
	};

	// ���ճɹ��ص�����
	private PictureCallback myPicCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(final byte[] data, Camera camera) {
			
			
			// ������պ�ر�Activity
			MGridActivity.xianChengChi.execute(new Runnable() {

				@Override
				public void run() {

					
					long time = System.currentTimeMillis();

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					Date date = new Date(time);

					String nowTime = formatter.format(date);

					String f_time = nowTime.substring(0,10);

					String h = nowTime.substring(11,13);

					String m = nowTime.substring(14,16);

					String s = nowTime.substring(17,19);

					nowTime = f_time+"D"+h+"_"+m+"_"+s;

					
					// ʹ�õ�ǰ��ʱ��ƴ��ͼƬ������
					//String name = DateFormat.format("yyyy_MM_dd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
					String name=nowTime;
					File file = new File("/mgrid/log/vtu_camera/");
//					double sizeM = getDirSize(file);
//					while (sizeM > C_sizeM) {
//
//						double d = Math.ceil(count / 10);						
//						deleteFile(file, d);
//						sizeM = getDirSize(file);
//					}

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

	public static void deleteFile(File file, double count) {
		if (file.exists() && file.isDirectory()) {
			nameData.clear();
			File[] children = file.listFiles();
			for (File f : children) {

				String name = f.getName();
				nameData.add(name);

			}
			System.out.println("�ļ��ܸ�����" + nameData.size());
			Collections.sort(nameData);

			for (String name : nameData) {
				for (File f2 : children) {
					if (count > 0) {
						if (name.equals(f2.getName())) {
							System.out.println(name + "��ɾ");
							f2.delete();
							count--;
							System.out.println("��Ҫɾ��" + count + "��");
							break;
						}
					}
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
				Intent intent = new Intent(CameraActivity.this, MGridActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				MGridActivity.isNOChangPage=true;
				CameraActivity.this.finish();
				System.out.println("�ͷ���");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ��ȡ�ļ���
	private File getDir() {
		// �õ�SD����Ŀ¼
		File dir = Environment.getExternalStorageDirectory();

		if (dir.exists()) {
			return dir;
		} else {
			dir.mkdirs();
			return dir;
		}
	}
}
