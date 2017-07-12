package com.yi.wechat.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

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
		PrintWriter out=response.getWriter();
		out.println(jo);//就算是ln和加了\n，最后发送到前端的都是json类型的，所以都是紧凑的字符串					
	}

	/**delete good by id in real!
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				//System.out.println("I come from the doPost() method!");
		StringBuffer buffStr=new StringBuffer();
		BufferedReader reader=request.getReader();
		String str=null;
		while((str=reader.readLine())!=null){
			buffStr.append(str);
		}
		String goodIdStr=buffStr.toString();
		int goodIdInt=Integer.valueOf(goodIdStr);//把id转换为数字		
			//System.out.println(goodIdInt);

		//注意：这里的父文件夹要根据业务情况修改。
		String fatherFolder="E://project_of_programming_software/images/";
		igo=new GoodOperationImpl();
		//从数据库得到对应于id的name，然后拼入路径字符串得到文件夹字符串
		Good good=igo.getGoodById(goodIdInt);
		String goodName=good.getGood_name();
		fatherFolder=fatherFolder+goodName;
		//删除磁盘中对应的图片文件夹
		FileUtils.deleteDirectory(new File(fatherFolder));
		//删除数据库中商品纪录。
		igo.deleteGood(goodIdInt);//删除对应id的商品！数据库中的纪录真的删了！	
	}

}
