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
import org.apache.commons.io.FileUtils;

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
	String folderName;//存放图片的最低一级文件夹
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
		//保存到磁盘的实际路径（父目录，还没完整，在调用方法中完成）
		String uploadFolderPaths="E://project_of_programming_software/images";//still need a username as folder,all the pics are stored in it,do it in another method.
		//保存到数据库的虚拟路径（父目录，还没完整，在调用方法中完成）
		String dbPicFolderPaths="http://192.168.199.111:8080/images";
		//调用重要的表单处理方法
		Map<String, Object> dbEtyMap=handleFormSubmit(uploadFolderPaths,dbPicFolderPaths, request, response,"back-end-index.html");
			//System.out.println("FINAL MAP:"+dbEtyMap);
		String inputName=(String) dbEtyMap.get("good_name");
		String inputDescription=(String) dbEtyMap.get("good_description");
		String inputStrPrice=(String) dbEtyMap.get("good_price");
		int inputIntPrice=Integer.valueOf(inputStrPrice);
		String goodMainPicPath=dbPicFolderPaths+"/"+inputName+"/main.jpg";
		String goodDescPicsPath=(String) dbEtyMap.get("dbString");
			//System.out.println(goodDescPicsPath);

		if(dbEtyMap.get("hidden_id_text")!=null&&!((String)dbEtyMap.get("hidden_id_text")).equals("")){//Object判断是否为空用==null，字符串用.equals()
			//if通过，说明有id数字字符串过来，是前端点击了update。注意update包括update数据库&&删除原来的图片文件夹。
			//综合考虑前端，数据库和磁盘文件夹三大块，其实其它地方也要，只是这里比较典型。
			String aString=(String) dbEtyMap.get("hidden_id_text");
					//System.out.println(aString);
			int idInteger=Integer.valueOf((String) dbEtyMap.get("hidden_id_text"));
			igo=new GoodOperationImpl();//面向接口编程
			Good good2=igo.getGoodById(idInteger);//先把update前端的good2对象获取到以方便后面删除文件夹
			//System.out.println(idInteger);
			Good good=new Good(idInteger,inputName,inputDescription,inputIntPrice,goodMainPicPath,goodDescPicsPath);//保存的数据库的图片名和下面的保存到磁盘的图片的名字相同。
			//System.out.println(good);
			igo.updateGood(good);//存入mysql。
	//上面已经新创建了一个新的文件夹了，而update完成后原来的文件夹都还没删，要删掉：
			//注意：这里的父文件夹要根据业务情况修改。
			String fatherFolder="E://project_of_programming_software/images/";
			//从数据库得到对应于id的原先的name，然后拼入路径字符串得到文件夹字符串			
			String goodName=good2.getGood_name();
			fatherFolder=fatherFolder+goodName;
			//删除磁盘中对应的图片文件夹
			FileUtils.deleteDirectory(new File(fatherFolder));
			//至此更新完成！
		}else{
			//if不通过，说明没有id过来或者是id为空字符串，为添加商品
			Good good=new Good(inputName,inputDescription,inputIntPrice,goodMainPicPath,goodDescPicsPath);//保存的数据库的图片名和下面的保存到磁盘的图片的名字相同。
			//System.out.println(good);
			igo=new GoodOperationImpl();//面向接口编程
			igo.insertGood(good);//存入mysql。
		}
				
		
	}

	/**
	 * 整个函数中只需要根据实际情况修改一处就行了。就是那个username的地方
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");//注意：实际上为了中文设置编码应该在这里设置，因为获取和处理前端传来的数据是在这个方法中进行的，而不是在doPost(....)中进行！
		//使用apache的fileupload jar包来处理上传的多张图片的处理。
		//教程：http://www.codejava.net/java-ee/servlet/eclipse-file-upload-servlet-with-apache-common-file-upload
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
		if (!uploadDir.exists()) {//创建的是images的父目录，不是具体以用户名为文件夹名的子目录
		    uploadDir.mkdir();
		    //System.out.println("目录创建成功！");
		}
		
		//And this is the most important code: parsing the request to save upload data to a permanent file on disk:
		//如果保存的图片是重名的则会覆盖原先的图片。
		try {
			List formItems = upload.parseRequest(request);
				//System.out.println("请求的大小："+formItems.size());
			Iterator iter = formItems.iterator();
			aPicNameList=new ArrayList<String>();
			dbEntryMap=new HashMap<String,Object>();
			int flag=0;//立个flag判断是第几张图片，下标从0开始，为了能够将下面通过判断第一张图片将其名字改为main然后再存入磁盘
			// iterates over form's fields.重要！从表单获取数据都在这！
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();
			    // processes only fields that are not form fields
			    if (!item.isFormField()) {//处理上传的文件比如图片等。		    	
			        String fileName = new File(item.getName()).getName();
			        	//System.out.println("图片名字："+fileName);
		        	if(flag==0){//要求前端单个文件上传必须放在多个文件上传之前
			    		fileName="main.jpg";//如果是图片们中的第一张（也就是封面图片，因为是在描述图片们上传之前作为一张图片上传的，所以肯定是在第一个，即index=0）
			    	}else{
			    		//do nothing.
			    	}
			        aPicNameList.add(fileName);
			        String filePath = uploadFolderPath + File.separator + fileName;//要求填入的姓名必须放在文件上传之前 以让先创建好文件夹
			        File storeFile = new File(filePath);			 
			        // saves the file on disk
			        item.write(storeFile);
			        flag=flag+1;//只能放在 if下面，因为是针对图片的，else中是用户输入的，避免混淆。这样得到的flag就是代表第几张图片，下标从0开始
			    }else if (item.isFormField()) {//处理输入的用户名等。
			    	//注意：有一个很尴尬的地方，就是对于checkbox来说，因为一个名字name有几个对应的值（多选时），所以依次存放到map中会出现后面的checkbox覆盖了前面的checkbox
					String fieldName=item.getFieldName();									
					String fieldValue=item.getString("UTF-8");	
						System.out.println("name:"+fieldName+",value:"+fieldValue);//检验是否有中文乱码
						dbEntryMap.put(fieldName, fieldValue);
						if(fieldName.equals("good_name")){//如果已经把或者刚刚把名字保存到map中就执行下面语句：(根据具体情况修改字符串)
							folderName=(String) dbEntryMap.get("good_name");//把得到的value赋值到folderName中
									//System.out.println("具体姓名文件夹为："+folderName);
							uploadFolderPath=uploadFolderPath+"/"+folderName;//从这就得到了具体以姓名为文件夹的路径，下面进行创建：
							dbPicFolderPath=dbPicFolderPath+"/"+folderName;
							File uploadFolderDir = new File(uploadFolderPath);/*eg:String uploadPath = "E://project_of_programming_software/images/";*/
							if (!uploadFolderDir.exists()) {//创建的是具体以用户名为文件夹名的子目录
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
		aPicNameList.remove(0);//第一张图片已经存入磁盘和数据库，
		//剩下的描述图片们（也已经存入磁盘 ）再一块存入数据库。
		//第一张图片（封面图片）和其它描述图片们是存放在不同的mysql字段下的，要区分好！
		for(int i=0;i<aPicNameList.size();i++){
			String picName=aPicNameList.get(i);
			int ind=picName.indexOf(".");
			String subPicName=picName.substring(0, ind);//去掉后缀名
			try{
				Integer intName=Integer.valueOf(subPicName);//前端需要验证数字要在Integer限制范围内
				aSubPicNameList.add(intName);//保存数字形式的图片名到ArrayList
			}catch(NumberFormatException e){
				System.out.println("图片名字出现字符串，无法转化为数字。因此图片无法传入数据库");
			}
			//System.out.println("图片名:"+picName+",去除后缀之后的图片名"+subPicName);		
		}
		//对列表中的数字排序（存入的图片们准备在存入数据库之前先按数字排好序。）
		Collections.sort(aSubPicNameList);//其实应该是jar自动排好了的，再次排序应该没有必要，算了，保险起见吧。		
		String dbString="";
		//System.out.println("排序后的列表：");
		for(int i=0;i<aSubPicNameList.size();i++){
			Integer subPicName=aSubPicNameList.get(i);
			dbString=dbString+dbPicFolderPath+"/"+subPicName+".jpg,";
			//System.out.println(subPicName);
		}
		dbString=dbString.substring(0, dbString.length()-1);//得到的是存入数据库的多张图片路径的字符串（相互之间用逗号隔开）
			//System.out.println(dbString);	
		dbEntryMap.put("dbString", dbString);//dbString是要用到的。
		response.sendRedirect(jumpTo);
		return dbEntryMap;
	}
}


