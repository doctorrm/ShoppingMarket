package com.yi.wechat.pojo;
//access_token是每一次调用接口的凭证（2小时保质期的字符串），token是配置接口（最开始的时候用了一次，没了，不变的字符串）的凭证。

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
