package com.sg.web.base;

public class ViewObjectBase {
	
	String typeId; //����ID
	String type;//����
	float  left;  //���Ͻ�x����
	float  top;    //���Ͻ�y����
	float  wight;  //���
    float  heght;  //����
    int    ZIndex;  //��ʾ˳��  Խ��Խ��ʾ�ں�㣻
    float  fromWight;//��ʼ���
    float  fromHeight;//��ʼ�߶�
    String value;
    String cmd;

   
    
 

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public float getFromWight() {
		return fromWight;
	}

	public void setFromWight(float fromWight) {
		this.fromWight = fromWight;
	}

	public float getFromHeight() {
		return fromHeight;
	}

	public void setFromHeight(float fromHeight) {
		this.fromHeight = fromHeight;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ViewObjectBase(String typeId,float  left,float  top,float  wight,float  heght,int    ZIndex)
    {
    	this.typeId=typeId;
    	this.left=left;
    	this.top=top;
    	this.wight=wight;
    	this.heght=heght;
    	this.ZIndex=ZIndex;
    }
    
    public ViewObjectBase()
    {
    	
    }
    
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public float getLeft() {
		return left;
	}
	public void setLeft(float left) {
		this.left = left;
	}
	public float getTop() {
		return top;
	}
	public void setTop(float top) {
		this.top = top;
	}
	public float getWight() {
		return wight;
	}
	public void setWight(float wight) {
		this.wight = wight;
	}
	public float getHeght() {
		return heght;
	}
	public void setHeght(float heght) {
		this.heght = heght;
	}
	public int getZIndex() {
		return ZIndex;
	}
	public void setZIndex(int zIndex) {
		ZIndex = zIndex;
	}
    
    
}
