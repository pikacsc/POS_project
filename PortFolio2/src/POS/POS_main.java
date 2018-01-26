package POS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class POS_main extends JFrame {
	DAO dao;
	DTO dto;
	float version = 1.0f;

	DefaultTableModel model;
	JTable table;
	loginDialog login = new loginDialog(this,"�α���");
	
	
	public POS_main() {
		setTitle("����"+version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dao = new DAO();
		
		JMenuBar bar = new JMenuBar();
		
		JMenu m_goods = new JMenu("��ǰ����"); //�޴�
		bar.add(m_goods);
		
		
		JTabbedPane pane =  pos_goods();
		
		add(pane);
		
		
		addWindowListener(new WindowAdapter() {
		
			@Override
			public void windowClosing(WindowEvent e) {
				dao.closeAll();
			}
		});
		setSize(800, 500);
		setVisible(true);
		login.setVisible(true);
		
	}
	
	
	JTabbedPane pos_goods() {
		JTabbedPane pane = new JTabbedPane(JTabbedPane.NORTH);
		pane.addTab("��ǰ����", new pos_goodsPanel());
		return pane;
	}
	
	
	class pos_goodsPanel extends JPanel{
		JPanel north;//�ؽ�Ʈ�ʵ�
		JPanel center;//���̺�
		JPanel south;//��ư��
		
		
		JTextField gName;//��ǰ�̸�
		JTextField gPrice;//����
		JTextField gCount;//���
		JTextField gCode;//���ڵ�
		JButton selectBtn;//�����ȸ
		JButton insertBtn;//�Է�
		JButton updateBtn;//����
		JButton deleteBtn;//����
		JButton searchBtn;//�˻�
		
		public pos_goodsPanel() {
			setLayout(new BorderLayout());
			north = new JPanel();
			center = new JPanel();
			south = new JPanel();
			
			
			gName = new JTextField(20);
			gPrice = new JTextField(20);
			gCount = new JTextField(20);
			gCode = new JTextField(20);
			
			String colName[] = {"ǰ��","����","���","���ڵ�"};
			
			model = new DefaultTableModel(colName, 0);
			table = new JTable(model);
			table.setPreferredScrollableViewportSize(new Dimension(500, 300));
			
			table.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					table = (JTable)e.getComponent();
					model = (DefaultTableModel)table.getModel();
					
					//ǰ��
					String sgName = (String)model.getValueAt(table.getSelectedRow(),0);
					gName.setText(sgName);

					//����
					String sgPrice = (String)model.getValueAt(table.getSelectedRow(),1);
					gPrice.setText(sgPrice);
					
					//���
					String sgCount = (String)model.getValueAt(table.getSelectedRow(),2);
					gCount.setText(sgCount);
					
					//���ڵ�
					String sgCode = (String)model.getValueAt(table.getSelectedRow(),3);
					gCode.setText(sgCode);
				}
			});
			
			
			
			selectBtn = new JButton("�����ȸ");
			selectBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					 	 dao.selectGoods(model);
				}
			});
			
			
			insertBtn = new JButton("�Է�");
			insertBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if(gName.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "ǰ���� �Է��ϼ���");
							gName.requestFocus(); //�ʵ�� Ŀ���� ����
						}else if(gPrice.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "������ �Է��ϼ���");
							gPrice.requestFocus(); //�ʵ�� Ŀ���� ����
						}else if(gCount.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "��� �Է��ϼ���");
							gCount.requestFocus(); //�ʵ�� Ŀ���� ����
						}else if(gCode.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "���ڵ带 �Է��ϼ���");
							gCode.requestFocus(); //�ʵ�� Ŀ���� ����
						}else {
							String name = gName.getText();
							int price = Integer.parseInt(gPrice.getText());
							int count = Integer.parseInt(gCount.getText());
							String code = gCode.getText();
							String result = dao.insertGoods(name, price, count, code);
							if(result.equals("�ԷµǾ����ϴ�.")) {
								JOptionPane.showMessageDialog(null,"�ԷµǾ����ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
								dao.selectGoods(model);
							}else if(result.substring(0,9).equals("ORA-00001")){
								JOptionPane.showMessageDialog(null, "�Է½���, ���ڵ尡 �ߺ��˴ϴ�", "Error", JOptionPane.ERROR_MESSAGE);
							}else {
								JOptionPane.showMessageDialog(null, "�Է½���"+result, "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					}catch(Exception a) {
						JOptionPane.showMessageDialog(null, "���ڸ� �Է��ϼ���", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});
			
			
			updateBtn = new JButton("����");
			updateBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "����", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						try {
							String name = gName.getText();
							int price = Integer.parseInt(gPrice.getText());
							int count = Integer.parseInt(gCount.getText());
							String code = gCode.getText();
							String result = dao.updateGoods(name, price, count, code);
							if(result.equals("�����Ǿ����ϴ�.")) {
								JOptionPane.showMessageDialog(null,"���� �Ǿ����ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
								dao.selectGoods(model);
							}else {
								JOptionPane.showMessageDialog(null, "���� ����"+result, "Error", JOptionPane.ERROR_MESSAGE);
							}
						}catch(Exception a) {
							JOptionPane.showMessageDialog(null, "���ڸ� �Է��ϼ���", "Error", JOptionPane.ERROR_MESSAGE);	
							return;
						}
					} else {
						return;
					}
				}
			});
			
			
			deleteBtn = new JButton("����");
			deleteBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//Yes ��ư�� ��������
					if(JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "����", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						try {
							String code = gCode.getText();
							String result = dao.deleteGoods(code);
							if(result.equals("�����Ǿ����ϴ�.")) {
								JOptionPane.showMessageDialog(null, "���� �Ǿ����ϴ�.", "�˸�",JOptionPane.INFORMATION_MESSAGE);
								dao.selectGoods(model);
							}else {
								JOptionPane.showMessageDialog(null, "���� ����"+result, "Error", JOptionPane.ERROR_MESSAGE);
							}
						}catch(Exception a) {
							JOptionPane.showMessageDialog(null, "���ڸ� �Է��ϼ���", "Error", JOptionPane.ERROR_MESSAGE);	
							return;
						}
					} else {
						return;
					}
				}
			});
			
			layoutSetting();
			add(north,BorderLayout.NORTH);
			add(center,BorderLayout.CENTER);
			add(south,BorderLayout.SOUTH);
			setSize(280,400);
			
		}
		
		void layoutSetting() {
			north.setLayout(new FlowLayout());
			center.setLayout(new FlowLayout());
			south.setLayout(new FlowLayout());
			north.add(new JLabel("��ǰ�̸�:"));
			north.add(gName);			
			north.add(new JLabel("����:"));
			north.add(gPrice);
			north.add(new JLabel("���:"));
			north.add(gCount);
			north.add(new JLabel("���ڵ�:"));
			north.add(gCode);
			
			center.add(new JScrollPane(table));
			
			south.add(selectBtn);
			south.add(insertBtn);
			south.add(updateBtn);
			south.add(deleteBtn);
			
			
		}
		
		
	}
	
	
	class pos_employeePanel extends JPanel{
		
	}
	
	class loginDialog extends JDialog{
		JLabel idLabel = new JLabel("ID");
		JLabel pwLabel = new JLabel("pw");
		JTextField id = new JTextField(10);
		JTextField pw = new JTextField(10);
		JButton loginBtn = new JButton("�α���");
		
		public loginDialog(JFrame frame,String title) {
			super(frame,title,true);
			setLayout(new BorderLayout());
			JPanel centerPanel = new JPanel();
			JPanel southPanel = new JPanel();
			centerPanel.setLayout(new FlowLayout());
			centerPanel.add(idLabel);
			centerPanel.add(id);
			centerPanel.add(pwLabel);
			centerPanel.add(pw);
			southPanel.setLayout(new FlowLayout());
			southPanel.add(loginBtn);
			loginBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String result = dao.login(id.getText(), pw.getText());
					if(result.substring(0, 5).equals("�α��ν���")) {
						JOptionPane.showMessageDialog(null, result, "�α��ν���", JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
					}else {
						JOptionPane.showMessageDialog(null, result, "�α��� ����", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
			});
			add(centerPanel,BorderLayout.CENTER);
			add(southPanel,BorderLayout.SOUTH);
			setSize(350, 350);
			
			addWindowListener(new WindowAdapter() {
				
				@Override
				public void windowClosing(WindowEvent e) {
					dao.closeAll();
					System.exit(0);
				}
			});
			
			
		}
	}
	
	public static void main(String[] args) {
		new POS_main();

	}
}
