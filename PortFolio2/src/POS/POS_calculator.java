package POS;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class POS_calculator extends JPanel{
	DAO dao;
	int row;
	int index;
	int finalPrice;
	JLabel userLevel;
	JLabel userName;
	DefaultTableModel cModel;
	JTable cTable;
	
	JTextField searchText;
	JButton serachBtn;
	JButton resetBtn;
	
	JLabel paymentWay;

	

	JButton trafficCardBtn;
	JButton prePaidBtn;
	JButton payBtn;
	JButton insertBtn;
	JButton safeBtn;
	JButton refundBtn;
	
	
	
	
	GridLayout centerGrid;
	GridLayout eastGrid;
	GridLayout southGrid;
	GridLayout northGrid;
	
	public POS_calculator(DAO dao) {
		
		this.dao = dao;
		setLayout(new BorderLayout());
		String colName[] = {"NO","���ڵ�","��ǰ��","�ܰ�","����","������","���"};
	
		cModel = new DefaultTableModel(colName,0);
		
		
		cTable = new JTable(cModel);
		cTable.setPreferredScrollableViewportSize(new Dimension(500, 300));
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout());
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(8,1));
		JPanel finalPanel = new JPanel();
		
		searchText = new JTextField(20);
		insertBtn = new JButton("���ڵ��Է�");
		resetBtn = new JButton("�ʱ�ȭ");		
		
		trafficCardBtn = new JButton("����ī�����");
		prePaidBtn = new JButton("�������̵�");

		
		
		northPanel.add(searchText);
		northPanel.add(insertBtn);
		northPanel.add(resetBtn);
		
		southPanel.add(finalPanel);
	
		
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(4,1));
		
		
		payBtn = new JButton("����");
		safeBtn = new JButton("�ݰ���");
		
		eastPanel.add(payBtn);
		eastPanel.add(safeBtn);
		eastPanel.add(trafficCardBtn);
		eastPanel.add(prePaidBtn);
	
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(new JScrollPane(cTable),BorderLayout.CENTER);
		
		
		insertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(searchText.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "���ڵ带 �Է����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					return;
				}else {
					int gcount = 1;
					index += 1;
					String row[] = new String[7];
					String gcode = searchText.getText();
					row = dao.scanGoods(index,row,gcode,gcount);
					if(row.equals(null)) {
						JOptionPane.showMessageDialog(null, "���� ��ǰ�Դϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					row[4] = JOptionPane.showInputDialog("�����Է�");
					int count = Integer.parseInt(row[3]);
					int price = Integer.parseInt(row[4]);
					row[5] = count*price+"";
					row[6] = "";
					cModel.addRow(row);
				}
			}
		});
		
		resetBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				index = 0;
				cModel.setNumRows(0);
			}
		});
		
		payBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				finalPrice = 0;
				row = cModel.getRowCount(); //���� ���뿡 �÷��� ��ǰ��

				if(row>1) {
					for(int i=0;i<row;i++) {
						finalPrice += Integer.parseInt(cModel.getValueAt(i, 5)+"");
					}
					MyDialog m = new MyDialog("����", finalPrice);
					m.setVisible(true);
				}else if(row==1){
					finalPrice += Integer.parseInt(cModel.getValueAt(0, 5)+"");				
					MyDialog m = new MyDialog("����", finalPrice);
					m.setVisible(true);
				}else {
					JOptionPane.showMessageDialog(null, "������ ��ǰ�� �����ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		
		
		add(centerPanel,BorderLayout.CENTER);
		add(northPanel,BorderLayout.NORTH);
		add(southPanel,BorderLayout.SOUTH);
		add(eastPanel,BorderLayout.EAST);

	}
	
	
	class MyDialog extends JDialog{
		JButton insertCountBtn;
		JButton DCBtn;
		JButton selectPayBtn;
		JButton refundBtn;
		JButton safeBtn;
		JButton trafficCardBtn;
		
		public MyDialog(String title,int finalPrice) {
		
			if(title.equals("����")) {
				setLayout(new GridLayout(6,1));
				add(new JLabel("�޾ƾߵɵ� : "+finalPrice+"��"));
				paymentWay = new JLabel();
				if(JOptionPane.showConfirmDialog(null, "�����ݾ� :"+finalPrice+"��\n ī��� ���� �Ͻðڽ��ϱ�?", "������� ����",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					paymentWay.setText("ī��"); 	
				}else {
					paymentWay.setText("����");
					String a = JOptionPane.showInputDialog(null, "�޾ƾߵɵ� : "+finalPrice+"\n �����ݾ�");
					int receiveMoney = Integer.parseInt(a);
					add(new JLabel("�����ݾ� : "+a+"��"));
					int change = receiveMoney-finalPrice; 		
					add(new JLabel("�Ž����� : "+change+"��"));
				}
				add(paymentWay);
				add(new JLabel("���� �Ǿ����ϴ�."));
				JButton confirmPay = new JButton("����Ȯ��");
				JButton cancelPay = new JButton("�������");
				JPanel buttonPanel = new JPanel();
				
				confirmPay.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						index = 0;
						row = cModel.getRowCount(); //���� ���뿡 �÷��� ��ǰ��

						for(int i = 0;i<row;i++) {
							String code = cModel.getValueAt(i,1)+"";
							int originalCount = dao.getGoodsCount(code);
							int minusCount = Integer.parseInt(cModel.getValueAt(i,4)+"");
							int finalCount = originalCount - minusCount;
							dao.afterPurchase(finalCount, code);
						}
						System.out.println("DB������Ʈ��");
						cModel.setNumRows(0);
						
					}
				});
				cancelPay.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				buttonPanel.setLayout(new FlowLayout());
				buttonPanel.add(confirmPay);
				buttonPanel.add(cancelPay);
				add(buttonPanel);
				setSize(350,350);
			}else if(title.equals("�ݰ���")) {
				setSize(350,350);
			}else if(title.equals("����ī�����")) {
				setSize(350,350);
			}else {
				setSize(350,350);
			}
		}
		
	}
	
}
