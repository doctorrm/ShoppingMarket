package com.yi.wechat.menu;

import com.yi.wechat.menu.Button;

public class CommonButton extends Button{
	private String key;
	private String type;
	private String url;
	//�����ﵥ����ť������url�������������Ӳ˵��İ�ť������url!
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
