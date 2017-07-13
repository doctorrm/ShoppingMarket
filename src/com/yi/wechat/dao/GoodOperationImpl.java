package com.yi.wechat.dao;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.yi.wechat.pojo.Good;

public class GoodOperationImpl implements IGoodOperation {
	//一个原则，不能存在good_id是相同的。如果出现就需要自行修改一下id，很好修改的,setGood_id();就可以了。
	
	public static SqlSession getStaticSession(){
		String resource = "mybatis_conf.xml";
        InputStream is = GoodOperationImpl.class.getClassLoader().getResourceAsStream(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        return session;
	}
	
	public Good getGoodById(int goodId) {
		SqlSession session	=	GoodOperationImpl.getStaticSession();
		String statement="com.yi.wechat.dao.GoodOperationImpl.getGoodById";
		Good singleGood=session.selectOne(statement,goodId);
		return singleGood;
	}

	public String getAllGood() {
		SqlSession session=GoodOperationImpl.getStaticSession();
		String statement = "com.yi.wechat.dao.GoodOperationImpl.getAllGood";
		List<Good> all_good = session.selectList(statement);
		StringBuffer sb=new StringBuffer();
		ListIterator<Good> listIterator=all_good.listIterator(all_good.size());//iterator默认是在开头第一个的，要往前数就要加参数初始化到最后一个才有previous
			//倒排序只能用ListIterator，只用Iterator是没有previous方法的。
		while(listIterator.hasPrevious()){
	    Good singleGood=listIterator.previous();
	    	//下面和hasNext()，next()时候所用的一模一样，区别的只在上面。
		String singleJsonStr="{'good_id':'"+singleGood.getGood_id()+"','good_name':'"+singleGood.getGood_name()+
				"','good_description':'"+singleGood.getGood_description()+"','good_price':'"+singleGood.getGood_price()+
				"','good_main_pic_path':'"+singleGood.getGood_main_pic_path()+"','good_desc_pics_path':'"+singleGood.getGood_desc_pics_path()+"'}";
		sb.append(singleJsonStr+",");	//注意此时的good_id对应的数字在json是字符串类型的
		}		
		String jsonStr=sb.toString();//现在的string最前有一个","，如何去掉？
		jsonStr=jsonStr.substring(0,jsonStr.length()-1);//取其子集。
		jsonStr="{'good':["+jsonStr+"]}";//对字符串进一步拼接为真正的json字符串
			//System.out.println(jsonStr);
		return jsonStr;//获取的json字符串后指定的数据库主键就不需要考虑了。前端指定的index都是这个字符串里面的，不是数据库主键的。
  }

	public void insertGood(Good paramGood) {
		SqlSession session=GoodOperationImpl.getStaticSession();
		String statement="com.yi.wechat.dao.GoodOperationImpl.insertGood";
		int i=session.insert(statement,paramGood);
		session.commit();//insert后一定要commit();!
			//System.out.println(i);		
	}

	public void deleteGood(int goodId) {
		SqlSession session=GoodOperationImpl.getStaticSession();
		String statement="com.yi.wechat.dao.GoodOperationImpl.deleteGood";
		int result=session.delete(statement,goodId);//删除成功返回1。如果重复删除则除了第一次返回1外后面都会返回0.
			//System.out.println(result);
		session.commit();		
	}
	
	public void updateGood(Good paramGood) {
		//不能设置id的，记住！否则会不执行的。
		SqlSession session=GoodOperationImpl.getStaticSession();
		String statement="com.yi.wechat.dao.GoodOperationImpl.updateGood";
		int result=session.update(statement,paramGood);
			//System.out.println(result);
		session.commit();//记得要commit();
	}

	//for test...注意:这个类用不了junit单元测试，因为单元测试的方法好像是有条件的，一个是返回值必须是void，另外一个是不能有方法参数。
	//还有，注意单元测试的功能是和main一样的，一样会执行，不仅仅是测试，也会有运行结果的，如果是删除数据的就真的会把数据删除了。
	public  static void main(String[] args){
		GoodOperationImpl goi=new GoodOperationImpl();
		goi.deleteGood(22);
	}
}
