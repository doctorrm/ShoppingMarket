package useless;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.yi.wechat.pojo.Good;

public class MybatisTest {
	/*public static void main(String[] args) {
		while(iterator.hasNext()){
		    Good singleGood=iterator.next();
			String singleJsonStr="{'good_id':'"+singleGood.getGood_id()+"','good_name':'"+singleGood.getGood_name()+"','good_descriptor':'"+singleGood.getGood_descriptor()+"','good_price':'"+singleGood.getGood_price()+"','good_main_picture':'"+singleGood.getGood_pic()+"'}";
			sb.append(singleJsonStr+",");	//ע���ʱ��good_id��Ӧ��������json���ַ������͵�
				}		
			String jsonStr=sb.toString();//���ڵ�string�����һ��","�����ȥ����
			jsonStr=jsonStr.substring(0,jsonStr.length()-1);//ȡ���Ӽ���
			jsonStr="{'good':["+jsonStr+"]}";//���ַ�����һ��ƴ��Ϊ������json�ַ���
				//System.out.println(jsonStr);
			return jsonStr;//��ȡ��json�ַ�����ָ�������ݿ������Ͳ���Ҫ�����ˡ�ǰ��ָ����index��������ַ�������ģ��������ݿ������ġ�
	  */

}
