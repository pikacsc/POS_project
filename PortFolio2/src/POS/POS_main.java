package POS;

import javax.swing.JFrame;

public class POS_main extends JFrame {
	
	float version = 1.0f;
	public POS_main() {
		setTitle("Æ÷½º"+version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(500, 500);
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new POS_main();
	}
}
