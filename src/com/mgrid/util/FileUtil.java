package com.mgrid.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data_model.locat_his_DoorEvent;

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
	
	// �ж�Ŀ¼�Ƿ���� �����ھʹ���
	public boolean isMuExit(File file) {
		if (!file.exists()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	/**
	 * �����ļ�
	 * 
	 * @param path
	 */
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
	public void doDeleteEmptyDir(String dir) {
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
	public boolean deleteDir(File dir) {

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

	/**
	 * ���ɿ����¼���¼�ļ�
	 * DoorInvented�ؼ�
	 */
	public void saveDoorEvent(String path,String name,String text) {

		try {
			File mu = new File(path);
			isMuExit(mu);
			File file=new File(path+"/"+name);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true),"gb2312"));
			bw.write(text);
			bw.newLine();
			bw.flush();
			bw.close();		
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�����¼�����
	 * @param file
	 */
	public List getDoorEvent(File file)
	{
		List<locat_his_DoorEvent> list=new ArrayList<>();
		
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"gb2312"));
		    String line=null;
		    while((line=br.readLine())!=null)
		    {
		    	locat_his_DoorEvent locat=new locat_his_DoorEvent();
		    	if(locat.read_string(line));
		    	list.add(locat);
		    }
		    br.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
		
	}
	
	//ɾ��USer
	public void deleteUser(File file,List<String> textList)
	{
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"gb2312"));
		    String line="";
		    StringBuffer sb=new StringBuffer();
		    while((line=br.readLine())!=null)
		    {
		    	sb.append(line).append("&&&");
		    }		    
		    String result=sb.toString();
		    for(String replaceText: textList)
		    {
		    	result=result.replace(replaceText, "");
		    }
		    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"gb2312"));
		    String[] str=result.split("&&&");
		    for(int i=0;i<str.length;i++)
		    {
		    	bw.write(str[i]);
		    	bw.newLine();
		    }
		    bw.flush();
		    bw.close();
		    br.close();
		    
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	//�޸�User
	public void replaceUser(File file,Map<String,String> textMap)
	{
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"gb2312"));
		    String line="";
		    StringBuffer sb=new StringBuffer();
		    while((line=br.readLine())!=null)
		    {
		    	sb.append(line).append("&&&");
		    }		    
		    String result=sb.toString();
		    
		    Iterator<Map.Entry<String, String>> it=textMap.entrySet().iterator();
		    while(it.hasNext())
		    {
		    	Entry<String, String> entry=it.next();
		    	System.out.println(entry.getKey()+"::::"+entry.getValue());
		    	result=result.replace(entry.getKey(), entry.getValue());
		    }		   
		    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"gb2312"));
		    String[] str=result.split("&&&");
		    for(int i=0;i<str.length;i++)
		    {
		    	bw.write(str[i]);
		    	bw.newLine();
		    }
		    bw.flush();
		    bw.close();
		    br.close();
		    
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	//���User
		public void AddUser(File file,Map<String,String> textMap)
		{
			try {
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"gb2312"));
			    String line="";
			    StringBuffer sb=new StringBuffer();
			    while((line=br.readLine())!=null)
			    {
			    	sb.append(line).append("&&&");
			    }		    
			    String result=sb.toString();
			    
			    
			    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"gb2312"));
			    String[] str=result.split("&&&");
			    for(int i=0;i<str.length;i++)
			    {
			    	if(i==9)
			    	{
			    		   Iterator<Map.Entry<String, String>> it=textMap.entrySet().iterator();
						    while(it.hasNext())
						    {
						    	Entry<String, String> entry=it.next();
						    	bw.write(entry.getKey());
						    	bw.newLine();
						    	bw.write(entry.getValue());
						    	bw.newLine();
						    }		
			    	}
			    	bw.write(str[i]);
			    	bw.newLine();
			    }
			    bw.flush();
			    bw.close();
			    br.close();
			    
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

}
