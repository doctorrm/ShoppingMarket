package useless;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.yi.wechat.pojo.Good;

public class TestDeleteFolder {
	public static void main(String[] args) throws IOException {
	String aString="http://192.168.199.111:8080/images/修改名称/1.jpg,http://192.168.199.111:8080/images/修改名称/2.jpg,http://192.168.199.111:8080/images/修改名称/3.jpg";
	String[] urls=aString.split(",");
	for(String w:urls){
		System.out.println(w);
	}
	
	}
}
