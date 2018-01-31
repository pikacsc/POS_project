package POS;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class POS_main extends JFrame {
	DAO dao;
	DTO dto;
	String userLevel;
	String userid;
	
	String selectedLevel;
	String selectedId;
	
	//0 : 사장
	//1 : 점장
	//2 : 직원
	
	float version = 1.0f;


	loginDialog login = new loginDialog(this, "로그인");
	POS_employeePanel ePanel;
	POS_calculator calculator;
	
	public POS_main() {

		setTitle("포스" + version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dao = new DAO();
		
		JTabbedPane pane = pos_goods();

		add(pane);

		
		JMenuBar bar = new JMenuBar();
		JMenu systemMenu = new JMenu("시스템");
		bar.add(systemMenu);
		JMenuItem logoutMenu = new JMenuItem("로그아웃");
		systemMenu.add(logoutMenu);
		setJMenuBar(bar);
		
		logoutMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pane.remove(ePanel);
				login.setVisible(true);
				ePanel = new POS_employeePanel(userid,userLevel,dao);
				pane.addTab("직원관리", ePanel);
			}
		});
		
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dao.closeAll();
			}
		});
		setSize(800, 500);
		setVisible(true);
		login.setVisible(true);
		ePanel = new POS_employeePanel(userid,userLevel,dao);
		pane.addTab("직원관리",ePanel );
		
	}

	JTabbedPane pos_goods() {
		JTabbedPane pane = new JTabbedPane(JTabbedPane.NORTH);
		pane.addTab("계산", calculator);
		pane.addTab("상품관리", new POS_goodsPanel(dao));
		
		return pane;
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
				setVisible(false);
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
