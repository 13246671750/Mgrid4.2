package com.sg.web.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;

import com.alibaba.fastjson.JSON;
import com.mgrid.main.MGridActivity;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import android.util.Log;

public class IndexHandler implements RequestHandler{

	@RequestMapping(method = {RequestMethod.POST})
	@Override
	public void handle(HttpRequest arg0, HttpResponse arg1, HttpContext arg2) throws HttpException, IOException {
		
		
		  Log.e("HEHE","�ҽ�����");

		  Map<String, String> params = HttpRequestParser.parseParams(arg0);	       

		  if(params.containsKey("titleName"))
		  {
			  
			  String json=JSON.toJSON(MGridActivity.ViewJosnObject.get(params.get("titleName"))).toString();	
//			  String json=JSON.toJSONString(MGridActivity.ViewJosnObject.get(params.get("titleName")));
//			  Log.e("json", MGridActivity.ViewJosnObject.get(params.get("titleName")).size()+"");
			 // Log.e("Tag", json.length()+"");
			  //showLogCompletion(json,2000);
			  StringEntity stringEntity = new StringEntity(json, "utf-8");
			  arg1.setStatusCode(200);
			  arg1.setEntity(stringEntity);
			  
		  }else
		  {
			  
			  Log.e("", "ʧ����");
			  StringEntity stringEntity = new StringEntity("fail", "utf-8");
			  arg1.setStatusCode(200);
			  arg1.setEntity(stringEntity); 
			  
		  }
		  
		
	}
	
	
	
	/**
	 * �ֶδ�ӡ���ϳ�log�ı�
	 * @param log        ԭlog�ı�
	 * @param showCount  �涨ÿ����ʾ�ĳ��ȣ���ò�Ҫ����eclipse���Ƴ��ȣ�
	 */
	public static void showLogCompletion(String log,int showCount){
		if(log.length() >showCount){
			String show = log.substring(0, showCount);
//			System.out.println(show);
			Log.i("TAG", show+"");
			if((log.length() - showCount)>showCount){//ʣ�µ��ı����Ǵ��ڹ涨����
				String partLog = log.substring(showCount,log.length());
				showLogCompletion(partLog, showCount);
			}else{
				String surplusLog = log.substring(showCount, log.length());
//				System.out.println(surplusLog);
				Log.i("TAG", surplusLog+"");
			}
			
		}else{
//			System.out.println(log);
			Log.i("TAG", log+"");
		}
	}

	
	
	
}



