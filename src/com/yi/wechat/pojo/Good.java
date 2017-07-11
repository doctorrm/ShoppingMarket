package com.yi.wechat.pojo;

public class Good {
	private int good_id;//必须must要和数据表中的列名相同
	private String good_name;
	private String good_description;
	private int good_price;
	private String good_main_pic_path;
	private String good_desc_pics_path;
	public Good(){};	
	public Good(String goodName,String goodDescription,int goodPrice,String goodMainPicPath,String goodDescPicsPath){
		good_name=goodName;
		good_description=goodDescription;
		good_price=goodPrice;
		good_main_pic_path=goodMainPicPath;
		good_desc_pics_path=goodDescPicsPath;
	}
	
	public String getGood_desc_pics_path() {
		return good_desc_pics_path;
	}
	public void setGood_desc_pics_path(String good_desc_pics_path) {
		this.good_desc_pics_path = good_desc_pics_path;
	}
	public int getGood_id() {
		return good_id;
	}
	public void setGood_id(int good_id) {
		this.good_id = good_id;
	}
	public String getGood_name() {
		return good_name;
	}
	public void setGood_name(String good_name) {
		this.good_name = good_name;
	}	
	public int getGood_price() {
		return good_price;
	}
	public void setGood_price(int good_price) {
		this.good_price = good_price;
	}	
	public String getGood_description() {
		return good_description;
	}
	public void setGood_description(String good_description) {
		this.good_description = good_description;
	}	
	public String getGood_main_pic_path() {
		return good_main_pic_path;
	}
	public void setGood_main_pic_path(String good_main_pic_path) {
		this.good_main_pic_path = good_main_pic_path;
	}
	@Override
	public String toString() {
		return "Good [good_id=" + good_id + ", good_name=" + good_name + ", good_description=" + good_description
				+ ", good_price=" + good_price + ", good_main_pic_path=" + good_main_pic_path + "]";
	}
	
	
	
}
