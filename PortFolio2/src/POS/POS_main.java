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
	//0 : 사장
	//1 : 점장
	//2 : 직원
	
	float version = 1.0f;


	loginDialog login = new loginDialog(this, "로그인");
	
	public POS_main() {

		setTitle("포스" + version);
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
		pane.addTab("직원관리", new pos_employeePanel());
		
	}

	JTabbedPane pos_goods() {
		JTabbedPane pane = new JTabbedPane(JTabbedPane.NORTH);
		pane.addTab("상품관리", new pos_goodsPanel());
		
		return pane;
	}

	class pos_goodsPanel extends JPanel {
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

		public pos_goodsPanel() {
			String colName[] = { "품명", "가격", "재고", "바코드" };
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
					if (JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "삭제",
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
			add(south, BorderLayout.SOUTH);
			setSize(280, 400);

		}

	}

	class pos_employeePanel extends JPanel {
		DefaultTableModel eModel;
		JTable eTable;

		JPanel center;// 테이블
		JPanel east;// 버튼들

		JButton selectBtn;// 직원조회
		JButton insertBtn;// 입력
		JButton updateBtn;// 수정
		JButton deleteBtn;// 삭제
		JButton searchBtn;// 검색
		employeeDialog eDialog = new employeeDialog("직원등록");
		
		public pos_employeePanel() {
			String colName[] = { "ID","이름","직급","입사일자" };
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
					System.out.println("현재직급"+employeeLevel);
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
			selectBtn = new JButton("직원 목록 조회");
			insertBtn = new JButton("직원 등록");
			updateBtn = new JButton("직원 정보 수정");
			deleteBtn = new JButton("직원 정보 삭제");
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
			
			nameLabel = new JLabel("이름");
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
			
			
			
			confirmBtn = new JButton("확인");
			resetBtn = new JButton("초기화");
				
			
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
			//사장으로 로그인 할때 직원등록 창에서 라벨과 텍스트필드가 더추가되게 구현중...
			if(employeeLevel.equals("사장")) {
				System.out.println();
				levelLabel = new JLabel("직급");				
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
					if(employeeLevel.equals("사장")) {
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
			
			loginBtn = new JButton("로그인");
			
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
			
			//구현중......
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
			if (result.substring(0, 5).equals("로그인승인")) {
				JOptionPane.showMessageDialog(null, result, "로그인승인", JOptionPane.INFORMATION_MESSAGE);
				employeeLevel = result.substring(7, 10);
				setVisible(false);
			} else {
				JOptionPane.showMessageDialog(null, result, "로그인 실패", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}

		
	}

	public static void main(String[] args) {
		new POS_main();

	}
}
