package POS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

public class DAO {
	Connection conn;
	Statement stmt;
	String table;
	public DAO() {
		String url = null;
		String uid = "h8";
		String pw = "h8";
		
		url = "jdbc:oracle:thin:@192.168.0.27:1521:topcredu";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url,uid,pw);
			stmt = conn.createStatement();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	String insertGoods(String name,int price,int count,String code) {
		String sql = "insert into pos_goods values('"
				+name+"','"
				+price+"','"
				+count+"','"
				+code+"')";
		try {
			stmt.executeUpdate(sql);
			return "입력되었습니다.";
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	void updateGoods() {
			
	}
	void deleteGoods() {
		
	}
	DefaultTableModel selectGoods(DefaultTableModel model) {
		String sql = "select * from pos_goods";
		try {
			ResultSet rs = stmt.executeQuery(sql);	
			model.setNumRows(0);
			while(rs.next()) {
				String[] row = new String[4];
				row[0] = rs.getString("gName");
				row[1] = rs.getString("gPrice");
				row[2] = rs.getString("gCount");
				row[3] = rs.getString("gCode");
				model.addRow(row);
			}
			rs.close();
			return model;	
		}
		catch(NullPointerException e1) {
			model.setNumRows(0);
			String[] row = {"없음","없음","없음","없음"};
			model.addRow(row);
			e1.getMessage();
			e1.printStackTrace();
			return model;
			
		}catch(SQLException e2) {
			e2.printStackTrace();
			e2.getMessage();
			return model;
			
		}
	}
	
	
	
	
	String updateGoods(String name,int price,int count,String code) {
		String sql = "update pos_goods set gname = '"
					+name+"', gPrice ='"
					+price+"', gcount ='"
					+count+"' where gcode ='"
					+code+"'";
		try {
			stmt.executeUpdate(sql);
			return "수정되었습니다.";
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	
	
	
	
	void closeAll() {
		try {
			if(conn!=null) conn.close();
			if(stmt!=null) conn.close();
		}catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

}
