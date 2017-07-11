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
	 * ������Ʒ��Ϣ�����ݿ�;
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		String inputName=request.getParameter("good_name");
		//���������Ʒ������ͬ���ļ���
		String picFolder="E://project_of_programming_software/images/"+inputName;
		File goodFolder=new File(picFolder);
		boolean isDriverFolderCreated=goodFolder.mkdir();
				//System.out.println("�ļ��д����ɹ���:"+isDriverFolderCreated);//���inputNameΪ���ַ����򴴽��ļ���ʧ��
		//���浽���̵�·�������ݾ�������޸�
	    String goodMainPicDriverPath=picFolder+"/main.jpg";
	    //���浽���ݿ������·����
	    String goodMainPicPath="http://192.168.199.111:8080/images/"+inputName+"/main.jpg";
		String inputDescription=request.getParameter("good_description");
		String inputStringPrice=request.getParameter("good_price");		
		int inputIntPrice=Integer.valueOf(inputStringPrice);//�ѵõ��ļ۸��ַ���ת��Ϊint����
		//String inputPic=request.getParameter("good_main_pic");//��ȡ����null...why?��
		Good good=new Good(inputName,inputDescription,inputIntPrice,goodMainPicPath);//��������ݿ��ͼƬ��������ı��浽���̵�ͼƬ��������ͬ��
				//System.out.println(good);
		igo=new GoodOperationImpl();//����ӿڱ��
		igo.insertGood(good);//����mysql��	
		insertMainPic(goodMainPicDriverPath,request,response);//���÷����������ͼƬ������	
		//insertMulPics(goodMainPicDriverPath,request,response);//���÷����������ͼƬ������	
		
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
	 * ��ȡǰ���ϴ��ķ���ͼƬ�����浽����ָ��λ��
	 */
	public void insertMainPic(String goodMainPicDriverPath,HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{		 
		//write pic to the driver reference website: https://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet
		//String fileName=request.getParameter("good_main_pic");��Ӧ�õ���Ʒ�ڿͻ��˵����֣���֪��Ϊ������õ���null
		Part filePart = request.getPart("good_main_pic"); // Retrieves <input type="file" name="good_main_pic">
		//String fileName0 = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();//��ô�õ���Ƭ�ڿͻ��˵������и�ë�ã�
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
	     *  ע��:another method to redirect to a new page location:
	     	String site=new String("testPic.html");
	     	response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
	  	 	response.setHeader("Location", site);
	  	 	ע��:improper jump below:
	  	 	//request.getRequestDispatcher("/TestAjax.html").forward(request, response);//���Ǵ��������ϵĵ������������������͵�ַ���ĵ�ַ����servlet�ĵ�ַ�������н��solution
	     */		  
	}
}
