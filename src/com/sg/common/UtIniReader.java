package com.sg.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.mgrid.main.MGridActivity;

public class UtIniReader {
	protected HashMap<String, Properties> sections = new HashMap<String, Properties>();
	private transient String currentSecion;
	private transient Properties current;

	public UtIniReader(String filename) throws IOException {

		synchronized (MGridActivity.mgridIniPath) {

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(MGridActivity.mgridIniPath), "gb2312"));
			read(reader);
			reader.close();

		}

	}

	protected void read(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			parseLine(line);
		}
	}

	protected void parseLine(String line) {
		line = line.trim(); // 去掉字符串收尾空格

		if (line.isEmpty() || '#' == line.charAt(0))
			return; // 使配置文件支持 # 注释 -- CharlesChen TODO: 尚未验证

		if (line.matches("\\[.*\\]")) {
			currentSecion = line.replaceFirst("\\[(.*)\\]", "$1");
			current = new Properties();
			sections.put(currentSecion, current);
		} else if (line.matches(".*=.*")) {
			if (current != null) {
				int i = line.indexOf('=');
				String name = line.substring(0, i);
				String value = line.substring(i + 1);
				current.setProperty(name, value);
			}
		}
	}

	public boolean writeStr(BufferedWriter bw) {

		try {

			if (bw == null) {
				return false;
			}

			Iterator<Map.Entry<String, Properties>> it = sections.entrySet().iterator();
			while (it.hasNext()) {

				Map.Entry<String, Properties> entry = it.next();
				String type = entry.getKey();
				Properties pro = entry.getValue();
				bw.write("[" + type + "]");
				bw.newLine();
				for (String Key : pro.stringPropertyNames()) {
					bw.write(Key + "=" + pro.getProperty(Key));
					bw.newLine();
				}

			}
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void setStr(String type, String key, String Value) {

		current = sections.get(type);
		if (current != null) {
			current.setProperty(key, Value);
		}

	}

	public void removeStr(String type, String key, String Value) {

		current = sections.get(type);
		if (current != null) {
			if (current.containsKey(key)) {
				current.remove(key);
			}

		}

	}

	public String getValue(String section, String name) {
		Properties p = (Properties) sections.get(section);
		if (p == null) {
			return null;
		}
		String value = p.getProperty(name);
		return value;
	}

	public String[] getPageValue(String section, String name) {
		String[] defualt = { "1" };
		Properties p = (Properties) sections.get(section);
		if (p == null) {
			return defualt;
		}
		String s = p.getProperty(name);
		if (s != null) {
			String value[] = s.split("\\/");
			return value;
		}

		return defualt;
	}

	public String getDefPageValue(String section, String name) {
		String defualt = "1";
		Properties p = (Properties) sections.get(section);
		if (p == null) {
			return defualt;
		}
		String s = p.getProperty(name);
		if (s != null) {
			String value = s;
			return value;
		}

		return defualt;
	}

	public String getValue(String section, String name, String defualt) {
		Properties p = (Properties) sections.get(section);
		if (p == null) {
			return defualt;
		}
		String value = p.getProperty(name);
		return null == value ? defualt : value;
	}

}

/*
 * 使用方法： IniReader iniReader = new IniReader(strFileName); String strTmp =
 * iniReader.getValue("Section1", "Key1");
 */