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
	
	public POS_main() {
		setTitle("포스"+version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dao = new DAO();
		
		JMenuBar bar = new JMenuBar();
		
		JMenu m_goods = new JMenu("상품관리"); //메뉴
		bar.add(m_goods);
		
		
		JTabbedPane pane =  pos_goods();
		
		add(pane);
		
		
		addWindowListener(new WindowAdapter() {
		
			@Override
			public void windowClosing(WindowEvent e) {
				dao.closeAll();
			}
		});
		setSize(700, 500);
		setVisible(true);
	}
	
	
	JTabbedPane pos_goods() {
		JTabbedPane pane = new JTabbedPane(JTabbedPane.NORTH);
		pane.addTab("상품관리", new pos_goodsPanel());
		return pane;
	}
	
	
	class pos_goodsPanel extends JPanel{
		JPanel north;//텍스트필드
		JPanel center;//테이블
		JPanel south;//버튼들
		
		
		JTextField gName;//상품이름
		JTextField gPrice;//가격
		JTextField gCount;//재고
		JTextField gCode;//바코드
		JButton selectBtn;//목록조회
		JButton insertBtn;//입력
		JButton updateBtn;//수정
		JButton deleteBtn;//삭제
		JButton searchBtn;//검색
		
		public pos_goodsPanel() {
			setLayout(new BorderLayout());
			north = new JPanel();
			center = new JPanel();
			south = new JPanel();
			
			
			gName = new JTextField(20);
			gPrice = new JTextField(20);
			gCount = new JTextField(20);
			gCode = new JTextField(20);
			
			String colName[] = {"품명","가격","재고","바코드"};
			
			model = new DefaultTableModel(colName, 0);
			table = new JTable(model);
			table.setPreferredScrollableViewportSize(new Dimension(400, 300));
			
			table.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					table = (JTable)e.getComponent();
					model = (DefaultTableModel)table.getModel();
					
					//품명
					String sgName = (String)model.getValueAt(table.getSelectedRow(),0);
					gName.setText(sgName);

					//가격
					String sgPrice = (String)model.getValueAt(table.getSelectedRow(),1);
					gPrice.setText(sgPrice);
					
					//재고
					String sgCount = (String)model.getValueAt(table.getSelectedRow(),2);
					gCount.setText(sgCount);
					
					//바코드
					String sgCode = (String)model.getValueAt(table.getSelectedRow(),3);
					gCode.setText(sgCode);
				}
			});
			
			
			
			selectBtn = new JButton("목록조회");
			selectBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					 	 dao.selectGoods(model);
				}
			});
			
			
			insertBtn = new JButton("입력");
			insertBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if(gName.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "품명을 입력하세요");
							gName.requestFocus(); //필드로 커서가 가짐
						}else if(gPrice.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "가격을 입력하세요");
							gPrice.requestFocus(); //필드로 커서가 가짐
						}else if(gCount.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "재고를 입력하세요");
							gCount.requestFocus(); //필드로 커서가 가짐
						}else if(gCode.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "바코드를 입력하세요");
							gCode.requestFocus(); //필드로 커서가 가짐
						}else {
							String name = gName.getText();
							int price = Integer.parseInt(gPrice.getText());
							int count = Integer.parseInt(gCount.getText());
							String code = gCode.getText();
							String result = dao.insertGoods(name, price, count, code);
							if(result.equals("입력되었습니다.")) {
								JOptionPane.showMessageDialog(null,"입력되었습니다.","알림",JOptionPane.INFORMATION_MESSAGE);
							}else if(result.substring(0,8).equals("ORA-00001")){
								JOptionPane.showMessageDialog(null, "입력실패, 바코드가 중복됩니다", "Error", JOptionPane.ERROR_MESSAGE);
							}else {
								System.out.println(result);
								JOptionPane.showMessageDialog(null, "입력실패"+result, "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					}catch(Exception a) {
						JOptionPane.showMessageDialog(null, "숫자만 입력하세요", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});
			
			
			updateBtn = new JButton("수정");
			updateBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(JOptionPane.showConfirmDialog(null, "수정하시겠습니까?", "수정", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						try {
							String name = gName.getText();
							int price = Integer.parseInt(gPrice.getText());
							int count = Integer.parseInt(gCount.getText());
							String code = gCode.getText();
							String result = dao.updateGoods(name, price, count, code);
							if(result.equals("수정되었습니다.")) {
								JOptionPane.showMessageDialog(null,"수정 되었습니다.","알림",JOptionPane.INFORMATION_MESSAGE);
								dao.selectGoods(model);
							}else {
								JOptionPane.showMessageDialog(null, "수정 실패"+result, "Error", JOptionPane.ERROR_MESSAGE);
							}
						}catch(Exception a) {
							JOptionPane.showMessageDialog(null, "숫자만 입력하세요", "Error", JOptionPane.ERROR_MESSAGE);	
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
			north.add(new JLabel("상품이름:"));
			north.add(gName);			
			north.add(new JLabel("가격:"));
			north.add(gPrice);
			north.add(new JLabel("재고:"));
			north.add(gCount);
			north.add(new JLabel("바코드:"));
			north.add(gCode);
			
			center.add(new JScrollPane(table));
			
			south.add(selectBtn);
			south.add(insertBtn);
			south.add(updateBtn);
			
			
		}
		
		
	}
	
	
	
	public static void main(String[] args) {
		new POS_main();
	}
}
