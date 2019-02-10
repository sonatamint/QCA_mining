import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Font;


public class UI2 {

	private JFrame frmKnowledgeEditor;
	private JTable table;
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI2 window = new UI2();
					window.frmKnowledgeEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmKnowledgeEditor = new JFrame();
		frmKnowledgeEditor.setTitle("Knowledge editor");
		frmKnowledgeEditor.setBounds(100, 100, 720, 480);
		frmKnowledgeEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmKnowledgeEditor.getContentPane().setLayout(null);
		
		table = new JTable();
		table.setBounds(24, 56, 148, 317);
		frmKnowledgeEditor.getContentPane().add(table);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Opposite");
		rdbtnNewRadioButton.setBounds(581, 52, 75, 23);
		frmKnowledgeEditor.getContentPane().add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Cause");
		rdbtnNewRadioButton_1.setBounds(581, 77, 65, 23);
		frmKnowledgeEditor.getContentPane().add(rdbtnNewRadioButton_1);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setBounds(581, 139, 75, 23);
		frmKnowledgeEditor.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Delete");
		btnNewButton_1.setBounds(99, 396, 75, 23);
		frmKnowledgeEditor.getContentPane().add(btnNewButton_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setBounds(291, 309, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setEditable(true);
		comboBox_1.setBounds(366, 309, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setEditable(true);
		comboBox_2.setBounds(441, 309, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox_2);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setEditable(true);
		comboBox_3.setBounds(516, 309, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox_3);
		
		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setEditable(true);
		comboBox_4.setBounds(591, 309, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox_4);
		
		JComboBox comboBox_5 = new JComboBox();
		comboBox_5.setEditable(true);
		comboBox_5.setBounds(291, 352, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox_5);
		
		JComboBox comboBox_6 = new JComboBox();
		comboBox_6.setEditable(true);
		comboBox_6.setBounds(366, 352, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox_6);
		
		JComboBox comboBox_7 = new JComboBox();
		comboBox_7.setEditable(true);
		comboBox_7.setBounds(441, 352, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox_7);
		
		JComboBox comboBox_8 = new JComboBox();
		comboBox_8.setEditable(true);
		comboBox_8.setBounds(516, 352, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox_8);
		
		JComboBox comboBox_9 = new JComboBox();
		comboBox_9.setEditable(true);
		comboBox_9.setBounds(591, 352, 65, 21);
		frmKnowledgeEditor.getContentPane().add(comboBox_9);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(196, 192, 481, 2);
		frmKnowledgeEditor.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(195, 56, 2, 363);
		frmKnowledgeEditor.getContentPane().add(separator_1);
		
		JLabel lblNewLabel = new JLabel("Recently added");
		lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		lblNewLabel.setBounds(24, 24, 121, 15);
		frmKnowledgeEditor.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Relations found");
		lblNewLabel_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(218, 24, 121, 15);
		frmKnowledgeEditor.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Add relation");
		lblNewLabel_2.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(218, 214, 121, 15);
		frmKnowledgeEditor.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Concept 1:");
		lblNewLabel_3.setBounds(218, 312, 65, 15);
		frmKnowledgeEditor.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Relation type:");
		lblNewLabel_4.setBounds(218, 250, 99, 15);
		frmKnowledgeEditor.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Concept 2:");
		lblNewLabel_5.setBounds(218, 355, 63, 15);
		frmKnowledgeEditor.getContentPane().add(lblNewLabel_5);
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Cause");
		rdbtnNewRadioButton_2.setBounds(323, 246, 65, 23);
		frmKnowledgeEditor.getContentPane().add(rdbtnNewRadioButton_2);
		
		JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("Opposite");
		rdbtnNewRadioButton_3.setBounds(400, 246, 82, 23);
		frmKnowledgeEditor.getContentPane().add(rdbtnNewRadioButton_3);
		
		JButton btnNewButton_2 = new JButton("OK");
		btnNewButton_2.setBounds(563, 396, 93, 23);
		frmKnowledgeEditor.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Cancel");
		btnNewButton_3.setBounds(453, 396, 93, 23);
		frmKnowledgeEditor.getContentPane().add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Find");
		btnNewButton_4.setBounds(581, 106, 75, 23);
		frmKnowledgeEditor.getContentPane().add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("Edit");
		btnNewButton_5.setBounds(24, 396, 65, 23);
		frmKnowledgeEditor.getContentPane().add(btnNewButton_5);
		
		table_1 = new JTable();
		table_1.setBounds(218, 56, 343, 114);
		frmKnowledgeEditor.getContentPane().add(table_1);
		
		JLabel lblVerb = new JLabel("Verb");
		lblVerb.setBounds(291, 284, 54, 15);
		frmKnowledgeEditor.getContentPane().add(lblVerb);
		
		JLabel lblAdjective = new JLabel("Adjective");
		lblAdjective.setBounds(366, 284, 54, 15);
		frmKnowledgeEditor.getContentPane().add(lblAdjective);
		
		JLabel lblNoun = new JLabel("Noun 1");
		lblNoun.setBounds(441, 284, 54, 15);
		frmKnowledgeEditor.getContentPane().add(lblNoun);
		
		JLabel lblNoun_1 = new JLabel("Noun 2");
		lblNoun_1.setBounds(516, 284, 54, 15);
		frmKnowledgeEditor.getContentPane().add(lblNoun_1);
		
		JLabel lblNoun_2 = new JLabel("Noun 3");
		lblNoun_2.setBounds(592, 284, 54, 15);
		frmKnowledgeEditor.getContentPane().add(lblNoun_2);
	}
}
