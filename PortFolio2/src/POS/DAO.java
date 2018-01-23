package POS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DAO {
	Connection conn;
	Statement stmt;
	String table;
	public DAO() {
		ResultSet rs = null;
		String url = null;
		String uid = "h8";
		String pw = "h8";
		
		url = "jdbc:oracle:thin:@192.168.0.27:1521:topcredu";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url,uid,pw);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	int insertGoods(String name,int price,int count,String code) {
		String sql = "insert into pos_goods values('"
					+name+"', '"
					+price+"', '"
					+count+"', '"
					+code+"');";
		try {
			stmt.executeUpdate(sql);
			System.out.println("입력되었습니다.");
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			e.getMessage();
			return 0;
		}
	}
	void updateGoods() {
			
	}
	void deleteGoods() {
		
	}
	void selectGoods() {
		
	}

}
