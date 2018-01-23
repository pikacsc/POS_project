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
	
	void insert() {
		
	}
	void update() {
			
	}
	void delete() {
		
	}
	void select() {
		
	}

}
