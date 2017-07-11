package com.yi.wechat.dao;

import java.util.List;
import com.yi.wechat.pojo.Good;

//dao layer ,or you can see this as the mybatis's mapper interface.
//ȥ��ϰ�ж�good_mapper.xml�е����ݺ������кܴ�Ĺ�ϵ���淶��
public interface IGoodOperation {
	public Good getGoodById(int goodId);
	public String getAllGood();//�뷵���ַ���
	public void insertGood(Good good);
	public void updateGood(Good good);
	public void deleteGood(int goodId);
}

