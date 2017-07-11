package com.yi.wechat.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.yi.wechat.dao.GoodOperationImpl;
import com.yi.wechat.dao.IGoodOperation;
import com.yi.wechat.pojo.Good;

/**
 * Servlet implementation class AddGoodServlet
 */
@WebServlet("/AddGoodServlet")
@MultipartConfig
public class AddGoodServlet extends HttpServlet {
	IGoodOperation igo;
	private static final long serialVersionUID = 1L;
    public AddGoodServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	/**
	 * 保存商品信息到数据库;
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		String inputName=request.getParameter("good_name");
		//下面根据商品名创建同名文件夹
		String picFolder="E://project_of_programming_software/images/"+inputName;
		File goodFolder=new File(picFolder);
		boolean isDriverFolderCreated=goodFolder.mkdir();
				//System.out.println("文件夹创建成功吗:"+isDriverFolderCreated);//如果inputName为空字符串则创建文件夹失败
		//保存到磁盘的路径，根据具体情况修改
	    String goodMainPicDriverPath=picFolder+"/main.jpg";
	    //保存到数据库的虚拟路径。
	    String goodMainPicPath="http://192.168.199.111:8080/images/"+inputName+"/main.jpg";
		String inputDescription=request.getParameter("good_description");
		String inputStringPrice=request.getParameter("good_price");		
		int inputIntPrice=Integer.valueOf(inputStringPrice);//把得到的价格字符串转化为int类型
		//String inputPic=request.getParameter("good_main_pic");//获取的是null...why?。
		Good good=new Good(inputName,inputDescription,inputIntPrice,goodMainPicPath);//保存的数据库的图片名和下面的保存到磁盘的图片的名字相同。
				//System.out.println(good);
		igo=new GoodOperationImpl();//面向接口编程
		igo.insertGood(good);//存入mysql。	
		insertMainPic(goodMainPicDriverPath,request,response);//调用方法插入封面图片到磁盘	
		//insertMulPics(goodMainPicDriverPath,request,response);//调用方法插入封面图片到磁盘	
		
	}
	public void insertMulPics(String goodMainPicDriverPath,HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		Part filePart=request.getPart("good_desc_pics");
		InputStream fileContent=filePart.getInputStream();
		BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(goodMainPicDriverPath));
			int i;  
		    while((i=fileContent.read())!=-1){  
		    	out.write(i);  
		    }  
		    out.flush();  		    
		    out.close();  
		    fileContent.close(); 	  		  
		    response.sendRedirect("back-end-index.html");//care for the typo.
		
		
	}
	/**
	 * 获取前端上传的封面图片并保存到磁盘指定位置
	 */
	public void insertMainPic(String goodMainPicDriverPath,HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{		 
		//write pic to the driver reference website: https://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet
		//String fileName=request.getParameter("good_main_pic");理应得到商品在客户端的名字，不知道为何这个得到了null
		Part filePart = request.getPart("good_main_pic"); // Retrieves <input type="file" name="good_main_pic">
		//String fileName0 = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();//特么得到照片在客户端的名字有个毛用！
	    InputStream fileContent = filePart.getInputStream();
	    	//System.out.println(fileContent);
	    	//System.out.println("fileName0"+fileName0);
	    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(goodMainPicDriverPath));  
	    int i;  
	    while((i=fileContent.read())!=-1){  
	    	out.write(i);  
	    }  
	    out.flush();  		    
	    out.close();  
	    fileContent.close(); 	  		  
	    //jump to another location's ref site: https://www.tutorialspoint.com/servlets/servlets-page-redirect.htm
	    response.sendRedirect("back-end-index.html");//care for the typo.
	    /*  
	     *  注释:another method to redirect to a new page location:
	     	String site=new String("testPic.html");
	     	response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
	  	 	response.setHeader("Location", site);
	  	 	注释:improper jump below:
	  	 	//request.getRequestDispatcher("/TestAjax.html").forward(request, response);//不是纯粹意义上的调整，会出现中文乱码和地址栏的地址还是servlet的地址。下面有解决solution
	     */		  
	}
}
