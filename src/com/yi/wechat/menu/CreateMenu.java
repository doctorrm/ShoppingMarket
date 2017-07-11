package com.yi.wechat.menu;

import com.yi.wechat.menu.Menu;
import com.yi.wechat.pojo.AccessToken;

public class CreateMenu {//not only create menu,but also create the url
	//which buttons links to.
	private static String appId="wx277afa1f1210a601";
	private static String appSecret="de27b2a651c6912d45b61adfe32f4fa9";
	
	public static void main(String[] args){
	MenuUtil menuUtil=new MenuUtil();
	AccessToken accessToken=menuUtil.getAccessToken(appId, appSecret);
	Menu menuInstance=MenuInstance.createMenuInstance();
	MenuUtil.createMenu(menuInstance, accessToken);	
  }
}
	


