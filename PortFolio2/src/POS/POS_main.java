package POS;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class POS_main extends JFrame {
	JMenuItem mi_list = new JMenuItem("")
	
	float version = 1.0f;
	public POS_main() {
		setTitle("����"+version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar bar = new JMenuBar();
		
		JMenu m_goods = new JMenu("��ǰ����"); //�޴�
		bar.add(m_goods);
		
		
		
		setSize(700, 500);
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new POS_main();
	}
}
