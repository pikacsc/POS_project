package POS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	String userLevel;
	String userid;
	String userName;
	int safe;

	String selectedLevel;
	String selectedId;
	
	//0 : 사장
	//1 : 점장
	//2 : 직원
	
	float version = 1.0f;


	loginDialog login = new loginDialog(this, "로그인");
	logDialog log = new logDialog();
	POS_employeePanel ePanel;
	POS_calculator cPanel;
	
	public POS_main() {

		setTitle("포스" + version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dao = new DAO();
		
		JTabbedPane pane = pos_goods();

		add(pane);

		
		JMenuBar bar = new JMenuBar();
		JMenu systemMenu = new JMenu("시스템");
		bar.add(systemMenu);
		JMenuItem logMenu = new JMenuItem("기록보기");
		JMenuItem logoutMenu = new JMenuItem("로그아웃");
		systemMenu.add(logoutMenu);
		systemMenu.add(logMenu);
		
		setJMenuBar(bar);
		
		logMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				log.setVisible(true);
			}
		});
		
		
		logoutMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String state = "퇴근";
				int safe = cPanel.getSafe();
				dao.checkInOut(userName, userLevel, state, safe);
				pane.remove(ePanel);
				pane.remove(cPanel);
				login.setVisible(true);
				ePanel = new POS_employeePanel(userid,userLevel,dao);
				cPanel = new POS_calculator(dao,userName,userLevel);
				
				ePanel.selectedId = "";
				ePanel.selectedLevel = "";
				pane.addTab("직원관리", ePanel);
				pane.addTab("계산", cPanel);
				
			}
		});
		
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dao.closeAll();
			}
		});
		setSize(1200, 800);
		setVisible(true);
		login.setVisible(true);
		ePanel = new POS_employeePanel(userid,userLevel,dao);
		cPanel = new POS_calculator(dao,userName,userLevel);
		
		pane.addTab("직원관리",ePanel );
		pane.addTab("계산", cPanel);
		
	}

	JTabbedPane pos_goods() {
		JTabbedPane pane = new JTabbedPane(JTabbedPane.NORTH);
		pane.addTab("상품관리", new POS_goodsPanel(dao));
		
		return pane;
	}


	class logDialog extends JDialog{
		String userLevel;
		String userid;
		String userName;
		
		DefaultTableModel logModel;
		JTable eTable;
		
		public logDialog() {
			System.out.println(dao==null);
			JPanel center = new JPanel();
			JPanel south = new JPanel();
			center.setLayout(new GridLayout(1,1));
			south.setLayout(new FlowLayout());
			setTitle("시스템 기록");
			setLayout(new BorderLayout());
			String colName[] = {"시간","이름","직급","상태","품명","바코드","갯수","결제액","결제방식","금고"};
			logModel = new DefaultTableModel(colName, 0) {
				public boolean isCellEditable(int row,int column) {
					return false;
				}
			};
			eTable = new JTable(logModel);
			eTable.setPreferredScrollableViewportSize(new Dimension(500, 300));
			center.add(new JScrollPane(eTable));
			
			JButton listBtn = new JButton("보기");
			listBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dao.selectlog(logModel);
				}
			});
			JButton confirm = new JButton("확인");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			add(center,BorderLayout.CENTER);
			south.add(listBtn);
			south.add(confirm);
			add(south,BorderLayout.SOUTH);
			setSize(900,500);
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
			

			loginBtn.addKeyListener(new KeyAdapter() {
				
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
				userid = id.getText();
				JOptionPane.showMessageDialog(null, result, "로그인승인", JOptionPane.INFORMATION_MESSAGE);
				userLevel = result.substring(7, 9);
				userName = result.substring(10,13);
				String state = JOptionPane.showInputDialog(null, "'출근'\n 또는 로그인한 목적을 입력하시오 : ");
				String money = JOptionPane.showInputDialog(null, "현재 금고 액수를 입력하시오: ");
				int safe = Integer.parseInt(money);
				setVisible(false);
				cPanel.setSafe(safe);
				dao.checkInOut(userName, userLevel, state, safe);
			} else {
				JOptionPane.showMessageDialog(null, result, "로그인 실패", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			id.setText("");
			pw.setText("");
		}

		
	}

	public static void main(String[] args) {
		new POS_main();

	}
}
