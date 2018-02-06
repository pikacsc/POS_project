package POS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class POS_goodsPanel extends JPanel {
	DAO dao;
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

	public POS_goodsPanel(DAO dao) {
		this.dao = dao;
		String colName[] = { "ǰ��", "����", "���", "���ڵ�" };
		model = new DefaultTableModel(colName, 0){
			public boolean isCellEditable(int row,int column) {
				return false;
			}
		};
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
				if(gCode.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "��Ͽ��� ������ ��ǰ�� Ŭ�����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					dao.selectGoods(model);
				}
				else if (JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "����",
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
		center.setLayout(new GridLayout(1,1));
		south.setLayout(new GridLayout(4,1));
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
		add(south, BorderLayout.EAST);
		setSize(280, 400);

	}

}