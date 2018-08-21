package com.sg.uis.LsyNewView.Handle;

import java.util.ArrayList;
import java.util.List;

public class NiuberDoorHandle {

	public  ArrayList<String> ports =new ArrayList<String>();
	public  static  NiuberDoorHandle NiuberDoorHandle;
	
	public  static  NiuberDoorHandle getIntance()
	{
	
		if(NiuberDoorHandle==null)
		{
			NiuberDoorHandle=new NiuberDoorHandle();
		}
		
		
		return NiuberDoorHandle;
	}
	
	
	private  NiuberDoorHandle() {
		
		for (int i = 1; i <=6; i++) {
						
			ports.add("����"+i);
			
		}
		
	}
	
	
	public  String getPath(String str) {

		String path="";
		
		switch (str) {

		case "����1":

			path="/dev/ttyS2";
			
			break;
		case "����2":

			path="/dev/ttyS3";
			
			break;
		case "����3":

			path="/dev/ttyS4";
			
			break;
		case "����4":

			path="/dev/ttyS5";
			
			break;
		case "����5":

			path="/dev/ttyS6";
			
			break;
		case "����6":

			path="/dev/ttyS7";
			
			break;

		}
		
		return path;

	}
	
	public  String getStr(String str) {

		String path="";
		
		switch (str) {

		case "/dev/ttyS2":

			path="����1";
			
			break;
		case "/dev/ttyS3":

			path="����2";
			
			break;
		case "/dev/ttyS4":

			path="����3";
			
			break;
		case "/dev/ttyS5":

			path="����4";
			
			break;
		case "/dev/ttyS6":

			path="����5";
			
			break;
		case "/dev/ttyS7":

			path="����6";
			
			break;

		}
		
		return path;

	}

}
