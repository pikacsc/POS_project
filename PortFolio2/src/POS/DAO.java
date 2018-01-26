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
	

	String login(String id, String pw) {
		String sql ="select e_id as id , e_pw as pw , e_name as name , e_level as lv"
				+ " from pos_employees "
				+ " where e_id = '"+id+"'";
		String dbpw;
		String name;
		String lv;
		try {//���⼭ ��û�ظ��� 
			String result;
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				dbpw = rs.getString("pw");
				if(pw.equals(dbpw)) {
					lv = rs.getString("lv");
					name = rs.getString("name");
					return "�α��ν���! "+lv+" "+name+"�� �������!";
				}else {
					return "����� �ȸ½��ϴ�.";
				}
			}
			result = "���� ���̵� �Դϴ�.";
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
			return "���� ���̵� �Դϴ�.";
		}
	}
	
	
//��ǰ���� �׸�	
	
	String insertGoods(String name,int price,int count,String code) {
		String sql = "insert into pos_goods values('"
				+name+"','"
				+price+"','"
				+count+"','"
				+code+"')";
		try {
			stmt.executeUpdate(sql);
			return "�ԷµǾ����ϴ�.";
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	String deleteGoods(String code) {
		String sql = "delete from pos_goods where gcode = '"+code+"'";
		try {
			stmt.executeUpdate(sql);
			return "�����Ǿ����ϴ�.";
		}catch(Exception e) {
			System.out.println(sql);
			e.printStackTrace();
			return e.getMessage();
		}
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
			String[] row = {"����","����","����","����"};
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
			return "�����Ǿ����ϴ�.";
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	
	
	
//���� �׸�	
	

	String insertEmployee(String name,String id,String pw) {
		String sql = "insert into pos_employees values('"
				+name+"','"
				+id+"','"
				+pw+"','�Ƹ�����Ʈ',default)";
		try {
			stmt.executeUpdate(sql);
			return "���� "+name+"�� ��� �Ǿ����ϴ�.";
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	String m_insertEmployee(String name,String id,String pw,String level) {
		String sql = "insert into pos_employees values('"
				+name+"','"
				+id+"','"
				+pw+"','"
				+level+"',default)";
		try {
			stmt.executeUpdate(sql);
			return level+" "+name+"�� ��� �Ǿ����ϴ�.";
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	String deleteEmployee(String id) {
		String sql = "delete from pos_employees where e_id = '"+id+"'";
		try {
			stmt.executeUpdate(sql);
			return "�����Ǿ����ϴ�.";
		}catch(Exception e) {
			System.out.println(sql);
			e.printStackTrace();
			return e.getMessage();
		}
	}
	DefaultTableModel selectEmployee(DefaultTableModel model) {
		String sql = "select * from pos_employees";
		try {
			ResultSet rs = stmt.executeQuery(sql);	
			model.setNumRows(0);
			while(rs.next()) {
				String[] row = new String[4];
				row[0] = rs.getString("e_id");
				row[1] = rs.getString("e_name");
				row[2] = rs.getString("e_level");
				row[3] = rs.getString("joindate");
				model.addRow(row);
			}
			rs.close();
			return model;	
		}
		catch(NullPointerException e1) {
			model.setNumRows(0);
			String[] row = {"����","����","����","����"};
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
	
	
	
	
	String updateEmployee(String name,int price,int count,String code) {
		String sql = "update pos_goods set gname = '"
					+name+"', gPrice ='"
					+price+"', gcount ='"
					+count+"' where gcode ='"
					+code+"'";
		try {
			stmt.executeUpdate(sql);
			return "�����Ǿ����ϴ�.";
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
