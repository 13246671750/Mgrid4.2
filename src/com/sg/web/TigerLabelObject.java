package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class TigerLabelObject extends ViewObjectBase{

	
	public String TextColor;//������ɫ
	public String text;//�ı����� 
	public float  textSize;
	public String getTextColor() {
		return TextColor;
	}
	public void setTextColor(String textColor) {
		TextColor = textColor;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public float getTextSize() {
		return textSize;
	}
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	
}
