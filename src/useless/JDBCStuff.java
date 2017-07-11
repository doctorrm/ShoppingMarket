package useless;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCStuff {
	private static  String url = "jdbc:mysql://localhost:3306/test" ;//change the database name.
	private static String user="root";
	private static String password="yudeqq814";
	private static String preJsonStr;
	public static String getStringa() throws SQLException{

		try{
			Class.forName("com.mysql.jdbc.Driver");
			
		}catch(ClassNotFoundException e){
			System.out.println("can't find the driver");
			e.printStackTrace();
		}		
		//conncet to the mysql.
		Connection con=DriverManager.getConnection(url,user,password);		
		//query,for example:String sql="select * from jokes";
		String sql="select * from good";
		PreparedStatement pstmt=con.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();

		/**
		 * change to the particular situation.
		 * @return [description]
		 */
		StringBuffer sb=new StringBuffer();
		while(rs.next()){
			//index begins from 1
			String content1="{'good_id':'"+rs.getString(1)+"','good_name':'"+rs.getString(2)+"','good_description':'"+rs.getString(4)+"','good_price':'"+rs.getString(3)+"','good_picture':'"+rs.getString(5)+"'}";
			//上面json的字符串格式中的变量名是要在前端中获取数据时要用到的，一一对应。
			if(rs.isLast()){//avoid the last content is followed by a “\n”;
				sb.append(content1);
			}else{
			sb.append(content1+",");
			}
		}
		preJsonStr=sb.toString();
		preJsonStr="{'good':["+preJsonStr+"]}";
		//System.out.println(preJsonStr);
		
		
		if(rs!=null) rs.close();
		if(pstmt!=null) pstmt.close();
		if(con!=null) con.close();		
		return preJsonStr;
	}
	public static void main(String[] args) throws SQLException{
		JDBCStuff js=new JDBCStuff();				
	}
}
