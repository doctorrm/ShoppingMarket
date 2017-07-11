package com.yi.wechat.dao;

import java.util.List;
import com.yi.wechat.pojo.Good;

//dao layer ,or you can see this as the mybatis's mapper interface.
//去自习研读good_mapper.xml中的内容和这里有很大的关系！规范！
public interface IGoodOperation {
	public Good getGoodById(int goodId);
	public String getAllGood();//想返回字符串
	public void insertGood(Good good);
	public void updateGood(Good good);
	public void deleteGood(int goodId);
}

