package POS;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class POS_calculator extends JPanel{
	JPanel centerPanel;
	JPanel eastPanel;
	JPanel southPanel;
	JPanel northPanel;
	JLabel userLevel;
	JLabel userName;
	GridLayout centerGrid;
	GridLayout eastGrid;
	GridLayout southGrid;
	GridLayout northGrid;
	JButton test;
	public POS_calculator() {	
		layoutSetting();
	}
	
	void layoutSetting() {
		test = new JButton("Å×½ºÆ®");
		setLayout(new BorderLayout());
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		southPanel = new JPanel();
		northPanel = new JPanel();
		centerGrid = new GridLayout(1, 1);
		centerPanel.setLayout(centerGrid);
		centerPanel.add(test);
		add(centerPanel, BorderLayout.CENTER);
	}
	void listenerSetting() {
	
	}
}
