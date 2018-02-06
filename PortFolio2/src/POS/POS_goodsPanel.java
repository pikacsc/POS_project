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

	
	JPanel north;// 텍스트필드
	JPanel center;// 테이블
	JPanel south;// 버튼들

	JTextField gName;// 상품이름
	JTextField gPrice;// 가격
	JTextField gCount;// 재고
	JTextField gCode;// 바코드
	JButton selectBtn;// 목록조회
	JButton insertBtn;// 입력
	JButton updateBtn;// 수정
	JButton deleteBtn;// 삭제
	JButton searchBtn;// 검색

	public POS_goodsPanel(DAO dao) {
		this.dao = dao;
		String colName[] = { "품명", "가격", "재고", "바코드" };
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

				// 품명
				String sgName = (String) model.getValueAt(table.getSelectedRow(), 0);
				gName.setText(sgName);

				// 가격
				String sgPrice = (String) model.getValueAt(table.getSelectedRow(), 1);
				gPrice.setText(sgPrice);

				// 재고
				String sgCount = (String) model.getValueAt(table.getSelectedRow(), 2);
				gCount.setText(sgCount);

				// 바코드
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
						JOptionPane.showMessageDialog(null, "품명을 입력하세요");
						gName.requestFocus(); // 필드로 커서가 가짐
					} else if (gPrice.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "가격을 입력하세요");
						gPrice.requestFocus(); // 필드로 커서가 가짐
					} else if (gCount.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "재고를 입력하세요");
						gCount.requestFocus(); // 필드로 커서가 가짐
					} else if (gCode.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "바코드를 입력하세요");
						gCode.requestFocus(); // 필드로 커서가 가짐
					} else {
						String name = gName.getText();
						int price = Integer.parseInt(gPrice.getText());
						int count = Integer.parseInt(gCount.getText());
						String code = gCode.getText();
						String result = dao.insertGoods(name, price, count, code);
						if (result.equals("입력되었습니다.")) {
							JOptionPane.showMessageDialog(null, "입력되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
							dao.selectGoods(model);
						} else if (result.substring(0, 9).equals("ORA-00001")) {
							JOptionPane.showMessageDialog(null, "입력실패, 바코드가 중복됩니다", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "입력실패" + result, "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} catch (Exception a) {
					JOptionPane.showMessageDialog(null, "숫자만 입력하세요", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});

		updateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "수정하시겠습니까?", "수정",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					try {
						String name = gName.getText();
						int price = Integer.parseInt(gPrice.getText());
						int count = Integer.parseInt(gCount.getText());
						String code = gCode.getText();
						String result = dao.updateGoods(name, price, count, code);
						if (result.equals("수정되었습니다.")) {
							JOptionPane.showMessageDialog(null, "수정 되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
							dao.selectGoods(model);
						} else {
							JOptionPane.showMessageDialog(null, "수정 실패" + result, "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception a) {
						JOptionPane.showMessageDialog(null, "숫자만 입력하세요", "Error", JOptionPane.ERROR_MESSAGE);
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
				// Yes 버튼을 눌렀을때
				if(gCode.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "목록에서 삭제할 상품을 클릭해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
					dao.selectGoods(model);
				}
				else if (JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "삭제",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					try {
						String code = gCode.getText();
						String result = dao.deleteGoods(code);
						if (result.equals("삭제되었습니다.")) {
							JOptionPane.showMessageDialog(null, "삭제 되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
							dao.selectGoods(model);
						} else {
							JOptionPane.showMessageDialog(null, "삭제 실패" + result, "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception a) {
						JOptionPane.showMessageDialog(null, "숫자만 입력하세요", "Error", JOptionPane.ERROR_MESSAGE);
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
		north.add(new JLabel("상품이름:"));
		north.add(gName);
		north.add(new JLabel("가격:"));
		north.add(gPrice);
		north.add(new JLabel("재고:"));
		north.add(gCount);
		north.add(new JLabel("바코드:"));
		north.add(gCode);
		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(500, 300));

		center.add(new JScrollPane(table));
		selectBtn = new JButton("목록조회");
		insertBtn = new JButton("입력");
		updateBtn = new JButton("수정");
		deleteBtn = new JButton("삭제");

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