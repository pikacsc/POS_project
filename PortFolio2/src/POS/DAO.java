package POS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DAO {
	Connection conn;
	Statement stmt;
	String table;
	public DAO() {
		String url = null;
//		String uid = "h8";
//		String pw = "h8";
//		
//		url = "jdbc:oracle:thin:@192.168.0.27:1521:topcredu";
		String uid = "JASON";
		String pw = "oracle";
		
		url = "jdbc:oracle:thin:@localhost:1521:xe";
		
		
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
		try {//여기서 엄청해맸음 
			String result;
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				dbpw = rs.getString("pw");
				if(pw.equals(dbpw)) {
					lv = rs.getString("lv");
					name = rs.getString("name");
					return "로그인승인! "+lv+" "+name+"님 어서오세요!";
				}else {
					return "비번이 안맞습니다.";
				}
			}
			result = "없는 아이디 입니다.";
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
			return "없는 아이디 입니다.";
		}
	}
	
//출석 관리 및 금고관리
	String checkInOut(String userName,String userLevel,String state,int safe) {
		String sql = "insert into pos_manage values( default,'"
				+userName+"','"
				+userLevel+"','"
				+state+"','','','','','','"+safe+"')";
		try {
			stmt.executeUpdate(sql);
			return "입력되었습니다.";
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	String payCheck(String userName,String userLevel,String state,String name,String code,int count,int price,String paymentWay,int safe) {
		String sql = "insert into pos_manage values( default,'"
				+userName+"','"
				+userLevel+"','"
				+state+"','"+name+"','"+code+"','"+count+"','"+price+"','"+paymentWay+"','"+safe+"')";
		System.out.println(sql);
		try {
			stmt.executeUpdate(sql);
			return "로그 기록완료";
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	String trafficCardCheck(String userName,String userLevel,int price,int safe) {
		String sql = "insert into pos_manage values( default,'"
				+userName+"','"
				+userLevel+"','교통카드충전','교통카드','TMONEY333','1','"+price+"','현금','"+safe+"')";
		System.out.println(sql);
		try {
			stmt.executeUpdate(sql);
			return "로그 기록완료";
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	
	
	DefaultTableModel selectlog(DefaultTableModel model) {
		
		String sql = "select * from pos_manage";
		try {
			ResultSet rs = stmt.executeQuery(sql);	
			model.setNumRows(0);
			while(rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("checkdate");
				row[1] = rs.getString("e_name");
				row[2] = rs.getString("e_level");
				row[3] = rs.getString("state");
				row[4] = rs.getString("gname");
				row[5] = rs.getString("gcode");
				row[6] = rs.getString("gcount");
				row[7] = rs.getString("gprice");
				row[8] = rs.getString("paymentway");
				row[9] = rs.getString("safe");
				model.addRow(row);
			}
			rs.close();
			return model;	
		}
		catch(NullPointerException e1) {
			model.setNumRows(0);
			e1.getMessage();
			e1.printStackTrace();
			return model;
			
		}catch(SQLException e2) {
			e2.printStackTrace();
			e2.getMessage();
			return model;
			
		}
	}
	
	
	
//계산 항목
	String[] scanGoods(int index,String []row,String gcode,int gcount) {
		String sql = "select * from pos_goods where gcode ='"+gcode+"'";
		try {
			ResultSet rs = stmt.executeQuery(sql);	
			while(rs.next()) {
				row[0] = index+"";
				row[1] = rs.getString("gCode");
				row[2] = rs.getString("gName");
				row[3] = rs.getString("gPrice");
				row[4] = "";
				row[5] = "";
				row[6] = "";
			}
			rs.close();
			return row;	
		}
		catch(NullPointerException e1) {
			e1.getMessage();
			e1.printStackTrace();
			return row;
			
		}catch(SQLException e2) {
			e2.printStackTrace();
			e2.getMessage();
			return row;
			
		}
	}
	
	void afterPurchase(int x,String code) {
		String sql = "update pos_goods set gcount ='"
				+x+"' where gcode ='"
				+code+"'";
		try {
			stmt.executeUpdate(sql);
		}catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	int getGoodsCount(String code) {
		int count;
		String sql = "select gcount from pos_goods where gcode ='"+code+"'";
		try {
			ResultSet rs = stmt.executeQuery(sql);	
			while(rs.next()) {
				count = Integer.parseInt(rs.getString("gCount"));
				return count;	
			}
			rs.close();
		}catch(SQLException e2) {
			e2.printStackTrace();
			e2.getMessage();
		}
		return 0;
	}
	
	
//상품관리 항목	
	
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
	
	String deleteGoods(String code) {
		String sql = "delete from pos_goods where gcode = '"+code+"'";
		try {
			stmt.executeUpdate(sql);
			return "삭제되었습니다.";
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
		String sql = "update pos_goods set gname ='"
					+name+"' , gPrice ='"
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
	
	
	
	
//직원 항목	
	

	String insertEmployee(String id,String pw,String name) {
		String sql = "insert into pos_employees values('"
				+id+"','"
				+pw+"','"
				+name+"','직원',default)";
		try {
			stmt.executeUpdate(sql);
			return "직원등록 되었습니다.";
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	String m_insertEmployee(String id,String pw,String name,String level) {
		String sql = "insert into pos_employees values('"
				+id+"','"
				+pw+"','"
				+name+"','"
				+level+"',default)";
		try {
			stmt.executeUpdate(sql);
			return "직원등록 되었습니다.";
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	String deleteEmployee(String id) {
		String sql = "delete from pos_employees where e_id = '"+id+"'";
		try {
			stmt.executeUpdate(sql);
			return "삭제되었습니다.";
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
	
	
	
	
	String updateEmployee(String name,String pw,String id) {
		String sql = "update pos_employees set e_name = '"
					+name+"', e_pw ='"
					+pw+"' where e_id = '"+id+"'";
		try {
			stmt.executeUpdate(sql);
			return "수정되었습니다.";
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	String m_updateEmployee(String name,String pw,String level,String id) {
		String sql = "update pos_employees set e_name = '"
				+name+"', e_pw ='"
				+pw+"',e_level ='"
				+level+"' where e_id = '"
				+id+"'";
		System.out.println(sql);
		try {
			stmt.executeUpdate(sql);
			return "수정되었습니다.";
		}catch(Exception e){
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
