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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


class POS_employeePanel extends JPanel {
	String userLevel;
	String userid;
	String selectedLevel;
	String selectedId;
	DAO dao;
	employeeDialog eDialog;
	employeeDialog eDialog2;
	
	
	DefaultTableModel eModel;
	JTable eTable;

	JPanel center;// ���̺�
	JPanel east;// ��ư��
	

	JButton selectBtn;// ������ȸ
	JButton insertBtn;// �Է�
	JButton updateBtn;// ����
	JButton deleteBtn;// ����
	JButton searchBtn;// �˻�

	
	JComboBox<String> combobox1;
	JComboBox<String> combobox2;
	JComboBox<String> combobox3;
	
	
	public POS_employeePanel(String userid,String userLevel,DAO dao) {
		this.dao = dao;
		this.userLevel = userLevel;
		this.userid = userid;
		eDialog = new employeeDialog("�������",userLevel);
		eDialog2 = new employeeDialog("���� ���� ����",userLevel);
		
		
		String colName[] = { "ID","�̸�","����","�Ի�����" };
		eModel = new DefaultTableModel(colName, 0){
			public boolean isCellEditable(int row,int column) {
				return false;
			}
		};
		layoutSetting();
		listenerSetting();
	}
	
	void listenerSetting() {
		eTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				eTable = (JTable) e.getComponent();
				eModel = (DefaultTableModel) eTable.getModel();

				// ���̺��� ���ý� ID�� �����ͼ� �����Ҷ��� �����Ҷ� �����ֵ�����
				selectedId = (String) eModel.getValueAt(eTable.getSelectedRow(), 0);
				selectedLevel = (String) eModel.getValueAt(eTable.getSelectedRow(), 2);
				
				System.out.println(selectedId);
			
			}
		});
		
		
		
		selectBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dao.selectEmployee(eModel);
			}
		});
		
		if(userLevel.equals("����") == false) {
			insertBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
						eDialog.setVisible(true);
				}
			});
			deleteBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(userid.equals(selectedId)) {
						JOptionPane.showMessageDialog(null, "�ڱ� �ڽ��� �����Ҽ� �����ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else if (userLevel.equals("����")) {
						deleteProcess();
					}
					else if (userLevel.equals("����")) {
						if(selectedLevel.equals("����")) {
							deleteProcess();
						}else {
							JOptionPane.showMessageDialog(null, "������ �����ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}else {
						JOptionPane.showMessageDialog(null, "������ �����ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				
				}
			});
		}
		
		updateBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				eDialog2.idTf.setText(selectedId);
				if(selectedId.equals(userid)) {
					eDialog2.setVisible(true);
				}else if(userLevel.equals("����")) {
					JOptionPane.showMessageDialog(null, "������ �����ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}else if(userLevel.equals("����") && selectedLevel.equals("����")) {
					JOptionPane.showMessageDialog(null, "������ �����ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}else {
					eDialog2.setVisible(true);
				}
			}
		});
	
	}
	
	void deleteProcess() {
		if (JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "����",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			try {
				String result = dao.deleteEmployee(selectedId);
				if (result.equals("�����Ǿ����ϴ�.")) {
					JOptionPane.showMessageDialog(null, "���� �Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					dao.selectEmployee(eModel);
				} else {
					JOptionPane.showMessageDialog(null, "���� ����" + result, "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception a) {
				JOptionPane.showMessageDialog(null, "����"+a.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else {
			return;
		}
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
		if(userLevel.equals("����")==false) {
			insertBtn = new JButton("���� ���");
			deleteBtn = new JButton("���� ���� ����");
			east.add(insertBtn);
			east.add(deleteBtn);
		}
		updateBtn = new JButton("���� ���� ����");
		east.add(selectBtn);
		east.add(updateBtn);
		add(center, BorderLayout.CENTER);
		add(east, BorderLayout.SOUTH);
		setSize(280, 400);
	}
	
	
	class employeeDialog extends JDialog {
		JLabel nameLabel;
		JLabel idLabel;
		JLabel pwLabel;
		JLabel levelLabel;
		String userLevel;
		
		JTextField idTf;
		JPasswordField pwTf;
		JTextField nameTf;
		JTextField levelTf;
		
		JButton confirmBtn;
		JButton resetBtn;
		
		JPanel centerPanel;
		JPanel southPanel;

		public employeeDialog(String title,String userLevel) {
			this.userLevel = userLevel;
			
			setTitle(title);
			layoutSetting(title);
			listenerSetting();

		}
		
		void layoutSetting(String title) {
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
			
			if(title.equals("���� ���� ����")) {
				idTf.setText(selectedId);
				idTf.setEditable(false);
			}
			
			pwTf = new JPasswordField(10);
			pwTf.setBounds(50, 70, 160, 25);
			
			levelLabel = new JLabel("����");				
			levelLabel.setBounds(20, 100, 80, 25);
			
			levelTf = new JTextField(10);
			levelTf.setBounds(50, 100, 160, 25);
			levelTf.setVisible(false);
			levelLabel.setVisible(false);

			
			
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
			centerPanel.add(levelLabel);
			centerPanel.add(levelTf);
			
			
			southPanel.setLayout(new FlowLayout());
			southPanel.add(confirmBtn);
			southPanel.add(resetBtn);
			
			if(userLevel.equals("����")) {
				levelTf.setVisible(true);
				levelLabel.setVisible(true);
			}
			
			add(centerPanel, BorderLayout.CENTER);
			add(southPanel, BorderLayout.SOUTH);
			setSize(250, 350);
		}
		
		void listenerSetting() {
			confirmBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(eDialog.isVisible()) { //���� ���
						emptyTFCheck(userLevel);
						if(userLevel.equals("����")) {
							String name = eDialog.nameTf.getText();
							String id = eDialog.idTf.getText();
							String pw = new String(eDialog.pwTf.getPassword());
							String level = eDialog.levelTf.getText();
							String result = dao.m_insertEmployee(name, id, pw, level);
							if (result.equals("������� �Ǿ����ϴ�.")) {
								JOptionPane.showMessageDialog(null, result, "�˸�", JOptionPane.INFORMATION_MESSAGE);
								dao.selectEmployee(eModel);
							} else {
								JOptionPane.showMessageDialog(null, "�Է½���" + result, "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}else {
							String name = eDialog.nameTf.getText();
							String id = eDialog.idTf.getText();
							String pw = new String(eDialog.pwTf.getPassword());
							String result = dao.insertEmployee(name, id, pw);
							if (result.equals("������� �Ǿ����ϴ�.")) {
								JOptionPane.showMessageDialog(null, result, "�˸�", JOptionPane.INFORMATION_MESSAGE);
								dao.selectEmployee(eModel);
							} else {
								JOptionPane.showMessageDialog(null, "�Է½���" + result, "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}else {//���� ���� ����
						emptyTFCheck(userLevel);
						if(userLevel.equals("����")) {
							String name = eDialog2.nameTf.getText();
							String id = eDialog2.idTf.getText();
							String pw = new String(eDialog2.pwTf.getPassword());
							String level = eDialog2.levelTf.getText();
							String result = dao.m_updateEmployee(name,pw,level,id);
							if (result.equals("�����Ǿ����ϴ�.")) {
								JOptionPane.showMessageDialog(null, result, "�˸�", JOptionPane.INFORMATION_MESSAGE);
								dao.selectEmployee(eModel);
							} else {
								JOptionPane.showMessageDialog(null, "�Է½���" + result, "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}else {
							String name = eDialog2.nameTf.getText();
							String id = eDialog2.idTf.getText();
							String pw = new String(eDialog2.pwTf.getPassword());
							String result = dao.updateEmployee(name,pw,id);
							if (result.equals("�����Ǿ����ϴ�.")) {
								JOptionPane.showMessageDialog(null, result, "�˸�", JOptionPane.INFORMATION_MESSAGE);
								dao.selectEmployee(eModel);
							} else {
								JOptionPane.showMessageDialog(null, "�Է½���" + result, "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			});
			
			resetBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					nameTf.setText("");
					idTf.setText("");
					pwTf.setText("");
					if(userLevel.equals("����")) {
						levelTf.setText("");
					}
				}
			});
			
			
			addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					nameTf.setText("");
					idTf.setText("");
					pwTf.setText("");
					if(userLevel.equals("����")) {
						levelTf.setText("");
					}
					setVisible(false);
				}
			});
			
		
		}
		
		
		void emptyTFCheck(String userLevel) {
			if(userLevel.equals("����")) {
				if (nameTf.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "�̸��� �Է��ϼ���");
					nameTf.requestFocus(); // �ʵ�� Ŀ���� ����
				} else if (new String(pwTf.getPassword()).equals("")) {
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �Է��ϼ���");
					pwTf.requestFocus(); // �ʵ�� Ŀ���� ����
				} else if (levelTf.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "���޸� �Է��ϼ���");
					levelTf.requestFocus(); // �ʵ�� Ŀ���� ����
				} 
			}else {
				if (nameTf.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "�̸��� �Է��ϼ���");
					nameTf.requestFocus(); // �ʵ�� Ŀ���� ����
				} else if (new String(pwTf.getPassword()).equals("")) {
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �Է��ϼ���");
					pwTf.requestFocus(); // �ʵ�� Ŀ���� ����
				} 
			}
		}
		
		
	}

}




