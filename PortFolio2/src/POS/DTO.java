package POS;

import java.sql.Connection;
import java.sql.Statement;

public class DTO {
	
	Connection conn;
	Statement stmt;
	
	 
	 private String goodsName;
	 private int goodsPrice;
	 private int goodsCount;
	 private String goodsCode;
	 
	 private String employeeId;
	 private String employeeName;
	 private String employeeLevel;
	 private String joinDate;
	 
	 private int dailySales;
	 private int weeklySales;
	 private int monthlySales;
	 private int yearlySales;
	 
	 private int dailyPurchase;
	 private int weeklyPurchase;
	 private int monthlyPurchase;
	 private int yearlyPurchase;
	 
	 
	 private String oGoodsName;
	 private String oGoodsPrice;
	 private String oGoodsCount;
	 private String oGoodsFinalPrice;
	 
	 
	public DTO(Connection conn,Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
	} 
}
