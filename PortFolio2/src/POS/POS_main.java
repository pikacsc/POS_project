package POS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class POS_main extends JFrame {
	DAO dao;
	DTO dto;
	String employeeLevel;
	//0 : ����
	//1 : ����
	//2 : ����
	
	float version = 1.0f;


	loginDialog login = new loginDialog(this, "�α���");
	
	public POS_main() {

		setTitle("����" + version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dao = new DAO();
		
		JTabbedPane pane = pos_goods();

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
		pane.addTab("��������", new pos_employeePanel());
		
	}

	JTabbedPane pos_goods() {
		JTabbedPane pane = new JTabbedPane(JTabbedPane.NORTH);
		pane.addTab("��ǰ����", new pos_goodsPanel());
		
		return pane;
	}

	class pos_goodsPanel extends JPanel {
		DefaultTableModel model;
		JTable table;

		
		JPanel north;// �ؽ�Ʈ�ʵ�
		JPanel center;// ���̺�
		JPanel south;// ��ư��

		JTextField gName;// ��ǰ�̸�
		JTextField gPrice;// ����
		JTextField gCount;// ���
		JTextField gCode;// ���ڵ�
		JButton selectBtn;// �����ȸ
		JButton insertBtn;// �Է�
		JButton updateBtn;// ����
		JButton deleteBtn;// ����
		JButton searchBtn;// �˻�

		public pos_goodsPanel() {
			String colName[] = { "ǰ��", "����", "���", "���ڵ�" };
			model = new DefaultTableModel(colName, 0);
			layoutSetting();
			listenerSetting();
			
		}

		void listenerSetting() {
			table.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					table = (JTable) e.getComponent();
					model = (DefaultTableModel) table.getModel();

					// ǰ��
					String sgName = (String) model.getValueAt(table.getSelectedRow(), 0);
					gName.setText(sgName);

					// ����
					String sgPrice = (String) model.getValueAt(table.getSelectedRow(), 1);
					gPrice.setText(sgPrice);

					// ���
					String sgCount = (String) model.getValueAt(table.getSelectedRow(), 2);
					gCount.setText(sgCount);

					// ���ڵ�
					String sgCode = (String) model.getValueAt(table.getSelectedRow(), 3);
					gCode.setText(sgCode);
				}
			});
			
			selectBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					dao.selectGoods(model);
				}
			});
			
			insertBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (gName.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "ǰ���� �Է��ϼ���");
							gName.requestFocus(); // �ʵ�� Ŀ���� ����
						} else if (gPrice.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "������ �Է��ϼ���");
							gPrice.requestFocus(); // �ʵ�� Ŀ���� ����
						} else if (gCount.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "��� �Է��ϼ���");
							gCount.requestFocus(); // �ʵ�� Ŀ���� ����
						} else if (gCode.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "���ڵ带 �Է��ϼ���");
							gCode.requestFocus(); // �ʵ�� Ŀ���� ����
						} else {
							String name = gName.getText();
							int price = Integer.parseInt(gPrice.getText());
							int count = Integer.parseInt(gCount.getText());
							String code = gCode.getText();
							String result = dao.insertGoods(name, price, count, code);
							if (result.equals("�ԷµǾ����ϴ�.")) {
								JOptionPane.showMessageDialog(null, "�ԷµǾ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
								dao.selectGoods(model);
							} else if (result.substring(0, 9).equals("ORA-00001")) {
								JOptionPane.showMessageDialog(null, "�Է½���, ���ڵ尡 �ߺ��˴ϴ�", "Error",
										JOptionPane.ERROR_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "�Է½���" + result, "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					} catch (Exception a) {
						JOptionPane.showMessageDialog(null, "���ڸ� �Է��ϼ���", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});

			updateBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "����",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						try {
							String name = gName.getText();
							int price = Integer.parseInt(gPrice.getText());
							int count = Integer.parseInt(gCount.getText());
							String code = gCode.getText();
							String result = dao.updateGoods(name, price, count, code);
							if (result.equals("�����Ǿ����ϴ�.")) {
								JOptionPane.showMessageDialog(null, "���� �Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
								dao.selectGoods(model);
							} else {
								JOptionPane.showMessageDialog(null, "���� ����" + result, "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						} catch (Exception a) {
							JOptionPane.showMessageDialog(null, "���ڸ� �Է��ϼ���", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					} else {
						return;
					}
				}
			});

			deleteBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Yes ��ư�� ��������
					if (JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "����",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						try {
							String code = gCode.getText();
							String result = dao.deleteGoods(code);
							if (result.equals("�����Ǿ����ϴ�.")) {
								JOptionPane.showMessageDialog(null, "���� �Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
								dao.selectGoods(model);
							} else {
								JOptionPane.showMessageDialog(null, "���� ����" + result, "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						} catch (Exception a) {
							JOptionPane.showMessageDialog(null, "���ڸ� �Է��ϼ���", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					} else {
						return;
					}
				}
			});
		}

		void layoutSetting() {
			setLayout(new BorderLayout());
			north = new JPanel();
			center = new JPanel();
			south = new JPanel();

			gName = new JTextField(20);
			gPrice = new JTextField(20);
			gCount = new JTextField(20);
			gCode = new JTextField(20);

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
			table = new JTable(model);
			table.setPreferredScrollableViewportSize(new Dimension(500, 300));

			center.add(new JScrollPane(table));
			selectBtn = new JButton("�����ȸ");
			insertBtn = new JButton("�Է�");
			updateBtn = new JButton("����");
			deleteBtn = new JButton("����");

			south.add(selectBtn);
			south.add(insertBtn);
			south.add(updateBtn);
			south.add(deleteBtn);
			add(north, BorderLayout.NORTH);
			add(center, BorderLayout.CENTER);
			add(south, BorderLayout.SOUTH);
			setSize(280, 400);

		}

	}

	class pos_employeePanel extends JPanel {
		DefaultTableModel eModel;
		JTable eTable;

		JPanel center;// ���̺�
		JPanel east;// ��ư��

		JButton selectBtn;// ������ȸ
		JButton insertBtn;// �Է�
		JButton updateBtn;// ����
		JButton deleteBtn;// ����
		JButton searchBtn;// �˻�
		employeeDialog eDialog = new employeeDialog("�������");
		
		public pos_employeePanel() {
			String colName[] = { "ID","�̸�","����","�Ի�����" };
			eModel = new DefaultTableModel(colName, 0);
			layoutSetting();
			listenerSetting();
		}
		
		void listenerSetting() {
			selectBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dao.selectEmployee(eModel);
				}
			});
			
			insertBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("��������"+employeeLevel);
					eDialog.setVisible(true);
				}
			});
			
			updateBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			});
			
			deleteBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			});
		}
		
		void layoutSetting(){
			setLayout(new BorderLayout());
			center = new JPanel();
			east = new JPanel();
			

			center.setLayout(new FlowLayout());
			east.setLayout(new FlowLayout());
			eTable = new JTable(eModel);
			eTable.setPreferredScrollableViewportSize(new Dimension(500, 300));

			center.add(new JScrollPane(eTable));
			selectBtn = new JButton("���� ��� ��ȸ");
			insertBtn = new JButton("���� ���");
			updateBtn = new JButton("���� ���� ����");
			deleteBtn = new JButton("���� ���� ����");
			east.add(selectBtn);
			east.add(insertBtn);
			east.add(updateBtn);
			east.add(deleteBtn);
			add(center, BorderLayout.CENTER);
			add(east, BorderLayout.SOUTH);
			setSize(280, 400);
		}

	}
	
	
	class employeeDialog extends JDialog {
		JLabel nameLabel;
		JLabel idLabel;
		JLabel pwLabel;
		JLabel levelLabel;
		
		
		JTextField idTf;
		JPasswordField pwTf;
		JTextField nameTf;
		JTextField levelTf;
		
		JButton confirmBtn;
		JButton resetBtn;
		
		JPanel centerPanel;
		JPanel southPanel;

		public employeeDialog(String title) {
			setTitle(title);
			layoutSetting();
			listenerSetting();

			
			
		
		}
		
		void layoutSetting() {
			setLayout(new BorderLayout());
			
			nameLabel = new JLabel("�̸�");
			nameLabel.setBounds(20,10,80,25);
			
			
			idLabel = new JLabel("ID");
			idLabel.setBounds(20,40,80,25);
			
			pwLabel = new JLabel("pw");
			pwLabel.setBounds(20, 70, 80, 25);
			
			nameTf = new JTextField(10);
			nameTf.setBounds(50, 10, 160, 25);
			
			
			idTf = new JTextField(10);
			idTf.setBounds(50, 40, 160, 25);
			
			pwTf = new JPasswordField(10);
			pwTf.setBounds(50, 70, 160, 25);
			
			
			
			confirmBtn = new JButton("Ȯ��");
			resetBtn = new JButton("�ʱ�ȭ");
				
			
			centerPanel = new JPanel();
			southPanel = new JPanel();
			
			centerPanel.setLayout(null);
			centerPanel.add(nameLabel);
			centerPanel.add(nameTf);
			centerPanel.add(idTf);
			centerPanel.add(idLabel);
			centerPanel.add(idTf);
			centerPanel.add(pwLabel);
			centerPanel.add(pwTf);
			
			southPanel.setLayout(new FlowLayout());
			southPanel.add(confirmBtn);
			southPanel.add(resetBtn);
			//�������� �α��� �Ҷ� ������� â���� �󺧰� �ؽ�Ʈ�ʵ尡 ���߰��ǰ� ������...
			if(employeeLevel.equals("����")) {
				System.out.println();
				levelLabel = new JLabel("����");				
				levelLabel.setBounds(20, 100, 80, 25);
				
				levelTf = new JTextField(10);
				levelTf.setBounds(50, 100, 160, 25);
				
				centerPanel.add(levelLabel);
				centerPanel.add(levelTf);
			}
			
			add(centerPanel, BorderLayout.CENTER);
			add(southPanel, BorderLayout.SOUTH);
			setSize(250, 450);
		}
		
		void listenerSetting() {
			confirmBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(employeeLevel.equals("����")) {
						String name = nameTf.getText();
						String id = idTf.getText();
						String pw = new String(pwTf.getPassword());
						String level = levelTf.getText();
						dao.m_insertEmployee(name, id, pw, level);
					}else {
						String name = nameTf.getText();
						String id = idTf.getText();
						String pw = new String(pwTf.getPassword());
						dao.insertEmployee(name, id, pw);
					}
				}
			});
			
			resetBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					nameTf.setText("");
					idTf.setText("");
					pwTf.setText("");
				}
			});
			
			
			addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					nameTf.setText("");
					idTf.setText("");
					pwTf.setText("");
					setVisible(false);
				}
			});
			
		
		}
		
		void joinProcess() {
		
		}

		
	}
	
	
	

	class loginDialog extends JDialog {
		JLabel idLabel;
		JLabel pwLabel;
		JTextField id;
		JPasswordField pw;
		JButton loginBtn;
		JPanel centerPanel;
		JPanel southPanel;

		public loginDialog(JFrame frame, String title) {
			super(frame, title, true);
			layoutSetting();
			listenerSetting();
		}
		
		void layoutSetting() {
			setLayout(new BorderLayout());
			idLabel = new JLabel("ID");
			idLabel.setBounds(20,10,80,25);
			
			pwLabel = new JLabel("pw");
			pwLabel.setBounds(20, 40, 80, 25);
			
			id = new JTextField(10);
			id.setBounds(50, 10, 160, 25);
			
			pw = new JPasswordField(10);
			pw.setBounds(50, 40, 160, 25);
			
			loginBtn = new JButton("�α���");
			
			centerPanel = new JPanel();
			southPanel = new JPanel();
			
			centerPanel.setLayout(null);
			centerPanel.add(idLabel);
			centerPanel.add(id);
			centerPanel.add(pwLabel);
			centerPanel.add(pw);
			
			southPanel.setLayout(new FlowLayout());
			southPanel.add(loginBtn);
			
			add(centerPanel, BorderLayout.CENTER);
			add(southPanel, BorderLayout.SOUTH);
			setSize(250, 150);
		}
		
		void listenerSetting() {
			loginBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					loginProcess();
				}
			});
			
			//������......
			addKeyListener(new KeyAdapter() {
				
				@Override
				public void keyPressed(KeyEvent e) {
					 int keyCode = e.getKeyCode();
					 switch(keyCode){
					 	case KeyEvent.VK_ENTER: 
					 		loginProcess(); 
					 	break;
					 };
				}
			});
			
			addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					dao.closeAll();
					System.exit(0);
				}
			});
			
		
		}
		
		void loginProcess() {
			String result = dao.login(id.getText(),new String(pw.getPassword()));
			if (result.substring(0, 5).equals("�α��ν���")) {
				JOptionPane.showMessageDialog(null, result, "�α��ν���", JOptionPane.INFORMATION_MESSAGE);
				employeeLevel = result.substring(7, 10);
				setVisible(false);
			} else {
				JOptionPane.showMessageDialog(null, result, "�α��� ����", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}

		
	}

	public static void main(String[] args) {
		new POS_main();

	}
}
