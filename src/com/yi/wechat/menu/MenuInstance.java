package com.yi.wechat.menu;

import com.yi.wechat.menu.Button;
import com.yi.wechat.menu.CommonButton;
import com.yi.wechat.menu.ComplexButton;
import com.yi.wechat.menu.Menu;

public class MenuInstance {
	
	public static Menu createMenuInstance(){
		CommonButton b11=new CommonButton();
		b11.setName("��������");
		b11.setKey("11");
		b11.setType("view");
		b11.setUrl("http://www.baidu.com");

		CommonButton b21=new CommonButton();//now just has a sub-menu of shop
		b21.setName("�̳���ҳ");
		b21.setKey("21");
		b21.setType("view");
		b21.setUrl("http://northernlight.tunnel.echomod.cn/WechatDemo");

		CommonButton b31=new CommonButton();
		b31.setName("������ѯ");
		b31.setKey("31");
		b31.setType("view");
		b31.setUrl("http://www.baidu.com");
		
		CommonButton b32=new CommonButton();
		b32.setName("����");
		b32.setKey("32");
		b32.setType("view");
		b32.setUrl("http://northernlight.tunnel.echomod.cn/WechatDemo/index.html");
		
		CommonButton b33=new CommonButton();
		b33.setName("���߿ͷ�");
		b33.setKey("33");
		b33.setType("view");
		b33.setUrl("http://www.baidu.com");
		
		CommonButton b34=new CommonButton();
		b34.setName("��ϵ����");
		b34.setKey("34");
		b34.setType("view");
		b34.setUrl("http://www.baidu.com");
		
		CommonButton b35=new CommonButton();
		b35.setName("�û�����");
		b35.setKey("35");
		b35.setType("view");
		b35.setUrl("http://www.baidu.com");
		
		//father menu
		ComplexButton c2=new ComplexButton();
		c2.setName("�̳�");
		c2.setSub_button(new Button[]{b21});
		
		ComplexButton c3=new ComplexButton();
		c3.setName("�û�����");
		c3.setSub_button(new Button[]{b31,b32,b33,b34,b35});
		
		Menu menu=new Menu();
		menu.setButton(new Button[]{b11,c2,c3});
		
		return menu;
	}
}
