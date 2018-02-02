package POS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class POS_calculator extends JPanel{
	JPanel centerPanel;
	JPanel eastPanel;
	JPanel southPanel;
	JPanel northPanel;
	JLabel userLevel;
	JLabel userName;
	DefaultTableModel cModel;
	JTable cTable;
	
	
	GridLayout centerGrid;
	GridLayout eastGrid;
	GridLayout southGrid;
	GridLayout northGrid;
	public POS_calculator() {	
		layoutSetting();
		String colName[] = {"NO","상품명","단가","수량","할인","영수액","비고"};
		cModel = new DefaultTableModel(colName,0) { 
			public boolean isCellEditable(int row,int column) {
				return false;
			}
		};
		cTable = new JTable(cModel);
		cTable.setPreferredScrollableViewportSize(new Dimension(500, 300));
		
		
		
	}
	
	void layoutSetting() {
		setLayout(new BorderLayout());
		eastPanel = new JPanel();
		southPanel = new JPanel();
		northPanel = new JPanel();
		centerPanel = new JPanel();
		centerGrid = new GridLayout(1, 1);
		centerPanel.setLayout(centerGrid);
		centerPanel.add(new JScrollPane(cTable));
		add(eastPanel,BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);
		add(northPanel,BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		
	}
	void listenerSetting() {
	
	}
}
