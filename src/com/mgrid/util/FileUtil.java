package com.mgrid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

	/**
	 * ���������ļ�������
	 * 
	 * @param oldPath
	 *            String ԭ�ļ�·�� �磺c:/fqf
	 * @param newPath
	 *            String ���ƺ�·�� �磺f:/fqf/ff
	 * @return boolean
	 */
	public void copyFolder(String oldPath, String newPath) {

		System.out.println("�ϣ�"+oldPath);
		System.out.println("�£�"+newPath);
		
		try {
			(new File(newPath)).mkdirs(); // ����ļ��в����� �������ļ���
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					File f = new File(newPath + File.separator + file[i]);
					if (!f.exists())
						f.createNewFile();
					FileOutputStream output = new FileOutputStream(f);

					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// ��������ļ���
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("���������ļ������ݲ�������");
			e.printStackTrace();

		}

	}

	/**
	 * ���Ƶ����ļ�
	 * 
	 * @param oldPath
	 *            String ԭ�ļ�·�� �磺c:/fqf.txt
	 * @param newPath
	 *            String ���ƺ�·�� �磺f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			// int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			File newfile = new File(newPath);
			if (oldfile.exists()) { // �ļ�����ʱ
				if (!newfile.exists()) {
					InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
					FileOutputStream fs = new FileOutputStream(newPath);
					byte[] buffer = new byte[2097152];
					// int length;
					while ((byteread = inStream.read(buffer)) != -1) {
						// bytesum += byteread; //�ֽ��� �ļ���С
						// System.out.println(bytesum);
						fs.write(buffer, 0, byteread);
					}
					inStream.close();
					fs.close();
				}
				oldfile.delete();
			}
		} catch (Exception e) {
			System.out.println("���Ƶ����ļ���������");
			e.printStackTrace();
		}

	}

	
	public void copyFileno(String oldPath, String newPath) {
		try {
			// int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			File newfile = new File(newPath);
			if (oldfile.exists()) { // �ļ�����ʱ
				if (!newfile.exists()) {
					InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
					FileOutputStream fs = new FileOutputStream(newPath);
					byte[] buffer = new byte[2097152];
					// int length;
					while ((byteread = inStream.read(buffer)) != -1) {
						// bytesum += byteread; //�ֽ��� �ļ���С
						// System.out.println(bytesum);
						fs.write(buffer, 0, byteread);
					}
					inStream.close();
					fs.close();
				}
			}
		} catch (Exception e) {
			System.out.println("���Ƶ����ļ���������");
			e.printStackTrace();
		}

	}
	
	
	// �ж��ļ��Ƿ���� �����ھʹ���
	public boolean isExit(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	// �����ļ�
	public void readFile(String path) {

		File file = new File(path);
		isExit(file);

	}

	/**
	 * ɾ����Ŀ¼
	 * 
	 * @param dir
	 *            ��Ҫɾ����Ŀ¼·��
	 */
	public  void doDeleteEmptyDir(String dir) {
		boolean success = (new File(dir)).delete();
		if (success) {
			System.out.println("Successfully deleted empty directory: " + dir);
		} else {
			System.out.println("Failed to delete empty directory: " + dir);
		}
	}

	/**
	 * �ݹ�ɾ��Ŀ¼�µ������ļ�����Ŀ¼�������ļ�
	 * 
	 * @param dir
	 *            ��Ҫɾ�����ļ�Ŀ¼
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public  boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// �ݹ�ɾ��Ŀ¼�е���Ŀ¼��
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// Ŀ¼��ʱΪ�գ�����ɾ��
		return dir.delete();
	}

}
