package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class YTParameterObject extends ViewObjectBase{
	
	String content;
	String fontColor;
	boolean click;
	
	public boolean isClick() {
		return click;
	}
	public void setClick(boolean click) {
		this.click = click;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	float  fontSize;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	

}