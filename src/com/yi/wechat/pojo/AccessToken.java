package com.yi.wechat.pojo;
//access_token��ÿһ�ε��ýӿڵ�ƾ֤��2Сʱ�����ڵ��ַ�������token�����ýӿڣ��ʼ��ʱ������һ�Σ�û�ˣ�������ַ�������ƾ֤��

public class AccessToken {
	private String access_token;
	private int expiresIn;
	
	public String getToken() {
		return access_token;
	}
	public void setToken(String token) {
		this.access_token = token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
}
