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
			sb.append(singleJsonStr+",");	//注意此时的good_id对应的数字在json是字符串类型的
				}		
			String jsonStr=sb.toString();//现在的string最后有一个","，如何去掉？
			jsonStr=jsonStr.substring(0,jsonStr.length()-1);//取其子集。
			jsonStr="{'good':["+jsonStr+"]}";//对字符串进一步拼接为真正的json字符串
				//System.out.println(jsonStr);
			return jsonStr;//获取的json字符串后指定的数据库主键就不需要考虑了。前端指定的index都是这个字符串里面的，不是数据库主键的。
	  */

}
