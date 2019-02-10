import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;


public class Alert {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Alert window = new Alert();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Alert() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Alert.class.getResource("/javax/swing/plaf/metal/icons/ocean/question.png")));
		frame.setBounds(100, 100, 400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Know-How related to your current task:");
		lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		lblNewLabel.setBounds(20, 22, 289, 25);
		frame.getContentPane().add(lblNewLabel);
		
		table = new JTable();
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setBounds(20, 58, 354, 250);
		table.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		table.setAutoCreateRowSorter(true);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setDoubleBuffered(true);
		table.setFillsViewportHeight(true);
		table.setRowMargin(2);
		table.setRowHeight(30);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"<html>&nbsp;<font size=\"4\"><u>1. Define Beam Section</u></font><html>", "<html>&nbsp;<font color=\"blue\"><u><i>View in KMap</i></u></font><html>"},
				{"<html>&nbsp;<font size=\"4\"><u>2. Plot Elemental Solution</u></font><html>", "<html>&nbsp;<font color=\"blue\"><u><i>View in KMap</i></u></font><html>"},
				{"<html>&nbsp;<font size=\"4\"><u>3. Use curve load</u></font><html>", "<html>&nbsp;<font color=\"blue\"><u><i>View in KMap</i></u></font><html>"},
				{"<html>&nbsp;<font size=\"4\"><u>4. Create file</u></font><html>", "<html>&nbsp;<font color=\"blue\"><u><i>View in KMap</i></u></font><html>"},
				{"<html>&nbsp;<font size=\"4\"><u>5. Perform fatigue analysis</u></font><html>", "<html>&nbsp;<font color=\"blue\"><u><i>View in KMap</i></u></font><html>"},
			},
			new String[] {
				"Knowledge item", "Useful?"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setRowHeight(50);
		table.getColumnModel().getColumn(0).setPreferredWidth(400);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		frame.getContentPane().add(table);
		
		JLabel lblNewLabel_1 = new JLabel("Next page >");
		lblNewLabel_1.setText("<html><u>Next page ></u></html>");
		lblNewLabel_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(266, 322, 83, 19);
		frame.getContentPane().add(lblNewLabel_1);
	}
}
