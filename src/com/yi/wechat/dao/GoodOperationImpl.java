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
	//һ��ԭ�򣬲��ܴ���good_id����ͬ�ġ�������־���Ҫ�����޸�һ��id���ܺ��޸ĵ�,setGood_id();�Ϳ����ˡ�
	
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
		ListIterator<Good> listIterator=all_good.listIterator(all_good.size());//iteratorĬ�����ڿ�ͷ��һ���ģ�Ҫ��ǰ����Ҫ�Ӳ�����ʼ�������һ������previous
			//������ֻ����ListIterator��ֻ��Iterator��û��previous�����ġ�
		while(listIterator.hasPrevious()){
	    Good singleGood=listIterator.previous();
	    	//�����hasNext()��next()ʱ�����õ�һģһ���������ֻ�����档
		String singleJsonStr="{'good_id':'"+singleGood.getGood_id()+"','good_name':'"+singleGood.getGood_name()+
				"','good_description':'"+singleGood.getGood_description()+"','good_price':'"+singleGood.getGood_price()+
				"','good_main_pic_path':'"+singleGood.getGood_main_pic_path()+"','good_desc_pics_path':'"+singleGood.getGood_desc_pics_path()+"'}";
		sb.append(singleJsonStr+",");	//ע���ʱ��good_id��Ӧ��������json���ַ������͵�
		}		
		String jsonStr=sb.toString();//���ڵ�string��ǰ��һ��","�����ȥ����
		jsonStr=jsonStr.substring(0,jsonStr.length()-1);//ȡ���Ӽ���
		jsonStr="{'good':["+jsonStr+"]}";//���ַ�����һ��ƴ��Ϊ������json�ַ���
			//System.out.println(jsonStr);
		return jsonStr;//��ȡ��json�ַ�����ָ�������ݿ������Ͳ���Ҫ�����ˡ�ǰ��ָ����index��������ַ�������ģ��������ݿ������ġ�
  }

	public void insertGood(Good paramGood) {
		SqlSession session=GoodOperationImpl.getStaticSession();
		String statement="com.yi.wechat.dao.GoodOperationImpl.insertGood";
		int i=session.insert(statement,paramGood);
		session.commit();//insert��һ��Ҫcommit();!
			//System.out.println(i);		
	}

	public void deleteGood(int goodId) {
		SqlSession session=GoodOperationImpl.getStaticSession();
		String statement="com.yi.wechat.dao.GoodOperationImpl.deleteGood";
		int result=session.delete(statement,goodId);//ɾ���ɹ�����1������ظ�ɾ������˵�һ�η���1����涼�᷵��0.
			//System.out.println(result);
		session.commit();		
	}
	
	public void updateGood(Good paramGood) {
		//��������id�ģ���ס������᲻ִ�еġ�
		SqlSession session=GoodOperationImpl.getStaticSession();
		String statement="com.yi.wechat.dao.GoodOperationImpl.updateGood";
		int result=session.update(statement,paramGood);
			//System.out.println(result);
		session.commit();//�ǵ�Ҫcommit();
	}

	//for test...ע��:������ò���junit��Ԫ���ԣ���Ϊ��Ԫ���Եķ����������������ģ�һ���Ƿ���ֵ������void������һ���ǲ����з���������
	//���У�ע�ⵥԪ���ԵĹ����Ǻ�mainһ���ģ�һ����ִ�У��������ǲ��ԣ�Ҳ�������н���ģ������ɾ�����ݵľ���Ļ������ɾ���ˡ�
	public  static void main(String[] args){
		GoodOperationImpl goi=new GoodOperationImpl();
		goi.deleteGood(22);
	}
}
