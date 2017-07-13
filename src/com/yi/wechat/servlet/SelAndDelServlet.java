package com.yi.wechat.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.yi.wechat.dao.GoodOperationImpl;
import com.yi.wechat.dao.IGoodOperation;
import com.yi.wechat.pojo.Good;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletAjax
 */
@WebServlet("/SelAndDelServlet")
public class SelAndDelServlet extends HttpServlet {
	IGoodOperation igo;
	private static final long serialVersionUID = 1L;       
    public SelAndDelServlet() {
        super();
    }
    
    /**
     *select good and show in the html. 
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
				//System.out.println("from doget()!");
		request.setCharacterEncoding("UTF-8");			
		//��֪��Ϊʲô��������2��֮��������������ͽ���ˣ�֮ǰ���񲻿���ѽ�������ǽ����߼��ӳٵ����⣬���µ�ҳ�������ַ�����к��־Ϳ�����
		response.setContentType("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		igo=new GoodOperationImpl();
		String jsonStr=igo.getAllGood();//����mybatis����ȡ���ݱ����ݡ�
				//System.out.println(jsonStr);
		//String jsonStr=JDBCStuff.getStringa();//,������jdbc����ȡ���ݱ����ݡ����������json��ʽ���ַ���,�����ַ������������ŵġ�																
		JSONObject jo=JSONObject.fromObject(jsonStr);
		PrintWriter pw=response.getWriter();
		pw.println(jo);//������ln�ͼ���\n������͵�ǰ�˵Ķ���json���͵ģ����Զ��ǽ��յ��ַ���					
	}

	/**delete good by id in real!
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				//System.out.println("I come from the doPost() method!");
		request.setCharacterEncoding("UTF-8");			
		response.setContentType("UTF-8");
		response.setCharacterEncoding("UTF-8");
		StringBuffer buffStr=new StringBuffer();
		BufferedReader reader=request.getReader();
		String str=null;
		while((str=reader.readLine())!=null){
			buffStr.append(str);
		}
		String goodIdStr=buffStr.toString();//����goodId��signal
			//System.out.println(goodIdStr);
		JSONObject jsonObject=JSONObject.fromObject(goodIdStr);
		Set<?> set=jsonObject.keySet();
		Object[] keys=set.toArray();
		String key0=(String) keys[0];
		String value0=(String)jsonObject.get(key0);//ǰ����jsonObject.key0
			//System.out.println("key0:"+key0+",value0:"+value0);
		int goodIdInt=Integer.valueOf(value0);//��idת��Ϊ����		
		igo=new GoodOperationImpl();
		Good good=igo.getGoodById(goodIdInt);
		
		if(key0.equals("deleteSignal")){//�����ɾ���ź�
			//ע�⣺����ĸ��ļ���Ҫ����ҵ������޸ġ�
			String fatherFolder="E://project_of_programming_software/images/";
			//�����ݿ�õ���Ӧ��id��name��Ȼ��ƴ��·���ַ����õ��ļ����ַ���
			String goodName=good.getGood_name();
			fatherFolder=fatherFolder+goodName;
			//ɾ�������ж�Ӧ��ͼƬ�ļ���
			FileUtils.deleteDirectory(new File(fatherFolder));
			//ɾ�����ݿ�����Ʒ��¼��
			igo.deleteGood(goodIdInt);//ɾ����Ӧid����Ʒ�����ݿ��еļ�¼���ɾ�ˣ�	
		
		}else if (key0.equals("getDetailGoodSignal")) {//�����Ҫ��ȡ����ĳ����Ʒ������ͼƬ·���ź�
			String jsonGood=good.toString();
				//System.out.println(jsonGood);
			JSONObject jsonObject2=JSONObject.fromObject(jsonGood);
			PrintWriter pw=response.getWriter();
			pw.println(jsonObject2);
	
			
		}
	}
}
