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
		String colName[] = {"NO","��ǰ��","�ܰ�","����","����","������","���"};
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
		
		finalLabel = new JLabel("�հ� ���� / �ݾ� / ����");
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
		memberShip = new JButton("���������");
		pointUseBtn = new JButton("Point���/����");
		creditCardBtn = new JButton("�ſ�ī��");
		trafficCardBtn = new JButton("����ī�����");
		prePaidBtn = new JButton("�������̵�");
		receiptBtn = new JButton("������");
		
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
		
		southPanel.add(new JLabel("���� ����"));
		southPanel.add(paymentWay);
		
		southPanel.add(new JLabel("���� �ݾ�"));
		southPanel.add(finalSum2);
		
		southPanel.add(new JLabel("���� �ݾ�"));
		southPanel.add(receivePay);
		
		southPanel.add(new JLabel("�Ž�����"));
		southPanel.add(change);
		
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(8, 1));
		oneViewBtn = new JButton("�Ѵ�������");
		selectPayBtn = new JButton("��������");
		serviceBtn = new JButton("����");
		searchBtn = new JButton("������ȸ");
		standbyBtn = new JButton("���");
		safeBtn = new JButton("�ݰ���");
		refundBtn = new JButton("ȯ��");
		
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
