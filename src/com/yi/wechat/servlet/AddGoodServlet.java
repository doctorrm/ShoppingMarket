package com.yi.wechat.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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
	private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
	private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
	List<String>  aPicNameList;
	List<Integer> aSubPicNameList;
	Map<String,Object> dbEntryMap;
	String folderName;//���ͼƬ�����һ���ļ���
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
		//���浽���̵�ʵ��·������Ŀ¼����û�������ڵ��÷�������ɣ�
		String uploadFolderPaths="E://project_of_programming_software/images";//still need a username as folder,all the pics are stored in it,do it in another method.
		//���浽���ݿ������·������Ŀ¼����û�������ڵ��÷�������ɣ�
		String dbPicFolderPaths="http://192.168.199.111:8080/images";
		//������Ҫ�ı�������
		Map<String, Object> dbEtyMap=handleFormSubmit(uploadFolderPaths,dbPicFolderPaths, request, response,"back-end-index.html");
			//System.out.println(dbEtyMap);
		String inputName=(String) dbEtyMap.get("good_name");
		String inputDescription=(String) dbEtyMap.get("good_description");
		String inputStrPrice=(String) dbEtyMap.get("good_price");
		Integer inputIntPrice=Integer.valueOf(inputStrPrice);
		String goodMainPicPath=dbPicFolderPaths+"/"+inputName+"/main.jpg";
		String goodDescPicsPath=(String) dbEtyMap.get("dbString");
				//System.out.println(goodDescPicsPath);
		
		Good good=new Good(inputName,inputDescription,inputIntPrice,goodMainPicPath,goodDescPicsPath);//��������ݿ��ͼƬ��������ı��浽���̵�ͼƬ��������ͬ��
				//System.out.println(good);
		igo=new GoodOperationImpl();//����ӿڱ��
		igo.insertGood(good);//����mysql��
		
	}

	/**
	 * ����������ֻ��Ҫ����ʵ������޸�һ�������ˡ������Ǹ�username�ĵط�
	 * @param uploadFolderPath
	 * @param dbPicFolderPath
	 * @param request
	 * @param response
	 * @param jumpTo
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public Map<String, Object> handleFormSubmit(String uploadFolderPath,String dbPicFolderPath,HttpServletRequest request,HttpServletResponse response,String jumpTo)throws ServletException, IOException{
		//ʹ��apache��fileupload jar���������ϴ��Ķ���ͼƬ�Ĵ���
		//�̳̣�http://www.codejava.net/java-ee/servlet/eclipse-file-upload-servlet-with-apache-common-file-upload
		// checks if the request actually contains upload file
		if (!ServletFileUpload.isMultipartContent(request)) {
		    PrintWriter writer = response.getWriter();
		    writer.println("Request does not contain upload data");
		    writer.flush();
		    return null;
		}
		
		// configures upload settings(including the temp location)
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(THRESHOLD_SIZE);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		 
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(MAX_REQUEST_SIZE);
	
		// constructs the directory path to store upload file
		// creates the directory if it does not exist.If it already exists,forget it!
		File uploadDir = new File(uploadFolderPath);/*eg:String uploadPath = "E://project_of_programming_software/images";*/
		if (!uploadDir.exists()) {//��������images�ĸ�Ŀ¼�����Ǿ������û���Ϊ�ļ���������Ŀ¼
		    uploadDir.mkdir();
		    //System.out.println("Ŀ¼�����ɹ���");
		}
		
		//And this is the most important code: parsing the request to save upload data to a permanent file on disk:
		//��������ͼƬ����������Ḳ��ԭ�ȵ�ͼƬ��
		try {
			List formItems = upload.parseRequest(request);
				//System.out.println("����Ĵ�С��"+formItems.size());
			Iterator iter = formItems.iterator();
			aPicNameList=new ArrayList<String>();
			dbEntryMap=new HashMap<String,Object>();
			int flag=0;//����flag�ж��ǵڼ���ͼƬ���±��0��ʼ��Ϊ���ܹ�������ͨ���жϵ�һ��ͼƬ�������ָ�ΪmainȻ���ٴ������
			// iterates over form's fields.��Ҫ���ӱ���ȡ���ݶ����⣡
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();
			    // processes only fields that are not form fields
			    if (!item.isFormField()) {//�����ϴ����ļ�����ͼƬ�ȡ�		    	
			        String fileName = new File(item.getName()).getName();
			        	//System.out.println("ͼƬ���֣�"+fileName);
		        	if(flag==0){//Ҫ��ǰ�˵����ļ��ϴ�������ڶ���ļ��ϴ�֮ǰ
			    		fileName="main.jpg";//�����ͼƬ���еĵ�һ�ţ�Ҳ���Ƿ���ͼƬ����Ϊ��������ͼƬ���ϴ�֮ǰ��Ϊһ��ͼƬ�ϴ��ģ����Կ϶����ڵ�һ������index=0��
			    	}else{
			    		//do nothing.
			    	}
			        aPicNameList.add(fileName);
			        String filePath = uploadFolderPath + File.separator + fileName;//Ҫ�������������������ļ��ϴ�֮ǰ �����ȴ������ļ���
			        File storeFile = new File(filePath);			 
			        // saves the file on disk
			        item.write(storeFile);
			        flag=flag+1;//ֻ�ܷ��� if���棬��Ϊ�����ͼƬ�ģ�else�����û�����ģ���������������õ���flag���Ǵ���ڼ���ͼƬ���±��0��ʼ
			    }else if (item.isFormField()) {//����������û����ȡ�
			    	//ע�⣺��һ�������εĵط������Ƕ���checkbox��˵����Ϊһ������name�м�����Ӧ��ֵ����ѡʱ�����������δ�ŵ�map�л���ֺ����checkbox������ǰ���checkbox
					String fieldName=item.getFieldName();									
					String fieldValue=item.getString();				
						//System.out.println("name:"+fiedName+",value:"+fieldValue);
						dbEntryMap.put(fieldName, fieldValue);
						if(fieldName.equals("good_name")){//����Ѿ��ѻ��߸ոհ����ֱ��浽map�о�ִ��������䣺(���ݾ�������޸��ַ���)
							folderName=(String) dbEntryMap.get("good_name");//�ѵõ���value��ֵ��folderName��
									//System.out.println("���������ļ���Ϊ��"+folderName);
							uploadFolderPath=uploadFolderPath+"/"+folderName;//����͵õ��˾���������Ϊ�ļ��е�·����������д�����
							dbPicFolderPath=dbPicFolderPath+"/"+folderName;
							File uploadFolderDir = new File(uploadFolderPath);/*eg:String uploadPath = "E://project_of_programming_software/images/";*/
							if (!uploadFolderDir.exists()) {//�������Ǿ������û���Ϊ�ļ���������Ŀ¼
								uploadFolderDir.mkdir();					    
							}
						}
				}
			    
			}						
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		aSubPicNameList=new ArrayList<Integer>();
		aPicNameList.remove(0);//��һ��ͼƬ�Ѿ�������̺����ݿ⣬
		//ʣ�µ�����ͼƬ�ǣ�Ҳ�Ѿ�������� ����һ��������ݿ⡣
		//��һ��ͼƬ������ͼƬ������������ͼƬ���Ǵ���ڲ�ͬ��mysql�ֶ��µģ�Ҫ���ֺã�
		for(int i=0;i<aPicNameList.size();i++){
			String picName=aPicNameList.get(i);
			int ind=picName.indexOf(".");
			String subPicName=picName.substring(0, ind);//ȥ����׺��
			try{
				Integer intName=Integer.valueOf(subPicName);
				aSubPicNameList.add(intName);//����������ʽ��ͼƬ����ArrayList
			}catch(NumberFormatException e){
				System.out.println("ͼƬ���ֳ����ַ������޷�ת��Ϊ����");
			}
			//System.out.println("ͼƬ��:"+picName+",ȥ����׺֮���ͼƬ��"+subPicName);		
		}
		//���б��е��������򣨴����ͼƬ��׼���ڴ������ݿ�֮ǰ�Ȱ������ź��򡣣�
		Collections.sort(aSubPicNameList);//��ʵӦ����jar�Զ��ź��˵ģ��ٴ�����Ӧ��û�б�Ҫ�����ˣ���������ɡ�		
		String dbString="";
		//System.out.println("�������б�");
		for(int i=0;i<aSubPicNameList.size();i++){
			Integer subPicName=aSubPicNameList.get(i);
			dbString=dbString+dbPicFolderPath+"/"+subPicName+".jpg,";
			//System.out.println(subPicName);
		}
		dbString=dbString.substring(0, dbString.length()-1);//�õ����Ǵ������ݿ�Ķ���ͼƬ·�����ַ������໥֮���ö��Ÿ�����
			//System.out.println(dbString);	
		dbEntryMap.put("dbString", dbString);//dbString��Ҫ�õ��ġ�
		response.sendRedirect(jumpTo);
		return dbEntryMap;
	}
}


