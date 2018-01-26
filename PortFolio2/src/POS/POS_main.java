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
	loginDialog login = new loginDialog(this,"로그인");
	
	
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
		setSize(800, 500);
		setVisible(true);
		login.setVisible(true);
		
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
			table.setPreferredScrollableViewportSize(new Dimension(500, 300));
			
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
								dao.selectGoods(model);
							}else if(result.substring(0,9).equals("ORA-00001")){
								JOptionPane.showMessageDialog(null, "입력실패, 바코드가 중복됩니다", "Error", JOptionPane.ERROR_MESSAGE);
							}else {
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
			
			
			deleteBtn = new JButton("삭제");
			deleteBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//Yes 버튼을 눌렀을때
					if(JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						try {
							String code = gCode.getText();
							String result = dao.deleteGoods(code);
							if(result.equals("삭제되었습니다.")) {
								JOptionPane.showMessageDialog(null, "삭제 되었습니다.", "알림",JOptionPane.INFORMATION_MESSAGE);
								dao.selectGoods(model);
							}else {
								JOptionPane.showMessageDialog(null, "삭제 실패"+result, "Error", JOptionPane.ERROR_MESSAGE);
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
		JButton loginBtn = new JButton("로그인");
		
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
					if(result.substring(0, 5).equals("로그인승인")) {
						JOptionPane.showMessageDialog(null, result, "로그인승인", JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
					}else {
						JOptionPane.showMessageDialog(null, result, "로그인 실패", JOptionPane.INFORMATION_MESSAGE);
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
