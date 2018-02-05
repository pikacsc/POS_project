package POS;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class POS_calculator extends JPanel{

	JLabel userLevel;
	JLabel userName;
	DefaultTableModel cModel;
	JTable cTable;
	
	JLabel finalLabel;
	JTextField finalSum;
	JTextField finalSum2;
	JTextField finalCount;
	JTextField finalDC;
	
	
	
	JTextField paymentWay;
	JTextField receivePay;
	JTextField change;
	
	
	JPanel ButtonPanel;
	JButton memberShip;
	JButton pointUseBtn;
	JButton creditCardBtn;
	JButton trafficCardBtn;
	JButton prePaidBtn;
	JButton receiptBtn;
	
	JButton oneViewBtn;
	JButton selectPayBtn;
	JButton serviceBtn;
	JButton searchBtn;
	JButton standbyBtn;
	JButton safeBtn;
	JButton refundBtn;
	
	
	
	
	GridLayout centerGrid;
	GridLayout eastGrid;
	GridLayout southGrid;
	GridLayout northGrid;
	
	public POS_calculator() {	
		setLayout(new BorderLayout());
		String colName[] = {"NO","상품명","단가","수량","할인","영수액","비고"};
		cModel = new DefaultTableModel(colName,0) { 
			public boolean isCellEditable(int row,int column) {
				return false;
			}
		};
		cTable = new JTable(cModel);
		cTable.setPreferredScrollableViewportSize(new Dimension(500, 300));
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(8,1));
		JPanel finalPanel = new JPanel();
		
		finalLabel = new JLabel("합계 수량 / 금액 / 할인");
		finalSum = new JTextField(10);
		finalSum2 = new JTextField(10);
		finalSum2 = finalSum;
		finalCount = new JTextField(10);
		finalDC = new JTextField(10);
		
		finalPanel.setLayout(new GridLayout(1,4));
		finalPanel.add(finalLabel);
		finalPanel.add(finalSum);
		finalPanel.add(finalCount);
		finalPanel.add(finalDC);
		
		ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new GridLayout(1, 6));
		memberShip = new JButton("멤버쉽할인");
		pointUseBtn = new JButton("Point사용/적립");
		creditCardBtn = new JButton("신용카드");
		trafficCardBtn = new JButton("교통카드결제");
		prePaidBtn = new JButton("프리페이드");
		receiptBtn = new JButton("영수증");
		
		ButtonPanel.add(memberShip);
		ButtonPanel.add(pointUseBtn);
		ButtonPanel.add(creditCardBtn);
		ButtonPanel.add(trafficCardBtn);
		ButtonPanel.add(prePaidBtn);
		ButtonPanel.add(receiptBtn);
		
		paymentWay = new JTextField(20);
		receivePay = new JTextField(20);
		change = new JTextField(20);
		
		
		
		southPanel.add(finalPanel);
		southPanel.add(ButtonPanel);
		
		southPanel.add(new JLabel("결제 내역"));
		southPanel.add(paymentWay);
		
		southPanel.add(new JLabel("받을 금액"));
		southPanel.add(finalSum2);
		
		southPanel.add(new JLabel("받은 금액"));
		southPanel.add(receivePay);
		
		southPanel.add(new JLabel("거스름돈"));
		southPanel.add(change);
		
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(8, 1));
		oneViewBtn = new JButton("한눈에보기");
		selectPayBtn = new JButton("결제선택");
		serviceBtn = new JButton("서비스");
		searchBtn = new JButton("통합조회");
		standbyBtn = new JButton("대기");
		safeBtn = new JButton("금고보관");
		refundBtn = new JButton("환불");
		
		eastPanel.add(oneViewBtn);
		eastPanel.add(selectPayBtn);
		eastPanel.add(serviceBtn);
		eastPanel.add(searchBtn);
		eastPanel.add(standbyBtn);
		eastPanel.add(safeBtn);
		eastPanel.add(refundBtn);
		
		
		add(new JScrollPane(cTable),BorderLayout.CENTER);
		add(southPanel,BorderLayout.SOUTH);
		add(eastPanel,BorderLayout.EAST);

	}
	
}
