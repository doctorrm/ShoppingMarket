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
		//不知道为什么加了下面2个之后中文乱码问题就解决了，之前好像不可以呀，或许是界面逻辑延迟的问题，在新的页面输入地址再运行后发现就可以了
		response.setContentType("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		igo=new GoodOperationImpl();
		String jsonStr=igo.getAllGood();//采用mybatis来获取数据表数据。
				//System.out.println(jsonStr);
		//String jsonStr=JDBCStuff.getStringa();//,采用了jdbc来获取数据表数据。这里必须是json格式的字符串,并且字符串都是有引号的。																
		JSONObject jo=JSONObject.fromObject(jsonStr);
		PrintWriter pw=response.getWriter();
		pw.println(jo);//就算是ln和加了\n，最后发送到前端的都是json类型的，所以都是紧凑的字符串					
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
		String goodIdStr=buffStr.toString();//包含goodId和signal
			//System.out.println(goodIdStr);
		JSONObject jsonObject=JSONObject.fromObject(goodIdStr);
		Set<?> set=jsonObject.keySet();
		Object[] keys=set.toArray();
		String key0=(String) keys[0];
		String value0=(String)jsonObject.get(key0);//前端是jsonObject.key0
			//System.out.println("key0:"+key0+",value0:"+value0);
		int goodIdInt=Integer.valueOf(value0);//把id转换为数字		
		igo=new GoodOperationImpl();
		Good good=igo.getGoodById(goodIdInt);
		
		if(key0.equals("deleteSignal")){//如果是删除信号
			//注意：这里的父文件夹要根据业务情况修改。
			String fatherFolder="E://project_of_programming_software/images/";
			//从数据库得到对应于id的name，然后拼入路径字符串得到文件夹字符串
			String goodName=good.getGood_name();
			fatherFolder=fatherFolder+goodName;
			//删除磁盘中对应的图片文件夹
			FileUtils.deleteDirectory(new File(fatherFolder));
			//删除数据库中商品纪录。
			igo.deleteGood(goodIdInt);//删除对应id的商品！数据库中的纪录真的删了！	
		
		}else if (key0.equals("getDetailGoodSignal")) {//如果是要获取具体某件商品的详情图片路径信号
			String jsonGood=good.toString();
				//System.out.println(jsonGood);
			JSONObject jsonObject2=JSONObject.fromObject(jsonGood);
			PrintWriter pw=response.getWriter();
			pw.println(jsonObject2);
	
			
		}
	}
}
