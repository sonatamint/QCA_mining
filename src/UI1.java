import java.awt.EventQueue;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Panel;

import javax.swing.JTextArea;
import javax.swing.ImageIcon;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Font;

import javax.swing.JToolBar;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;

import java.util.Iterator;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.JComboBox;
import javax.swing.ScrollPaneConstants;

import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class UI1 extends JFrame {

	private JPanel contentPane;
	private static JFrame UI2;
	private static JFrame UI3;
	private static JFrame UI4;
	private JTextField txtEnter;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTable table_1 = new JTable();
	private JTable table_2;
	private JTable table;
	JScrollPane scrollPane_3 = new JScrollPane();
	JScrollPane scrollPane = new JScrollPane();
	
	static public Object[][] originalmails = new Object[][]{};
	static public Object[][] shownmails = new Object[][]{};
	static public Object[][] selectedmails = new Object[500][5];
	private JTable table_3;

	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void runUI1(final Object[][] mails) throws Exception {
		
		
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//MainClass.posTagger = new MaxentTagger("left3words-wsj-0-18.tagger");
					//QcaRecord.resumeDataBase("D:/paseudoContext");
					originalmails = mails;
					int mailcount = originalmails.length;
					shownmails = new Object[mailcount][4];
					for(int i=0;i<mailcount;i++){
						shownmails[i][0]=originalmails[i][0];
						shownmails[i][1]=originalmails[i][1];
						shownmails[i][2]=originalmails[i][2];
						shownmails[i][3]=originalmails[i][3];
					}
					
					UI1 frame = new UI1();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public UI1() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(UI1.class.getResource("/com/sun/java/swing/plaf/windows/icons/Question.gif")));
		setTitle("Knowledge Need Predictor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1096, 640);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 10, 20));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1080, 30);
		menuBar.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		contentPane.add(menuBar);
		
		JMenu mnNewMenu = new JMenu("   File(F)   ");
		mnNewMenu.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		menuBar.add(mnNewMenu);
		
		JMenu menu = new JMenu("   ÐÂ½¨   ");
		menu.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		menu.setIcon(new ImageIcon(UI1.class.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
		mnNewMenu.add(menu);
		
		JMenu menu_1 = new JMenu("   ´ò¿ª   ");
		menu_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		menu_1.setIcon(new ImageIcon(UI1.class.getResource("/javax/swing/plaf/metal/icons/ocean/newFolder.gif")));
		mnNewMenu.add(menu_1);
		
		JMenu menu_2 = new JMenu("   ±£´æ   ");
		menu_2.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		menu_2.setIcon(new ImageIcon(UI1.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
		mnNewMenu.add(menu_2);
		
		JMenu menu_3 = new JMenu("   Áí´æÎª   ");
		menu_3.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		menu_3.setIcon(new ImageIcon(UI1.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
		mnNewMenu.add(menu_3);
		
		JMenu mnNewMenu_1 = new JMenu("   Tools(T)   ");
		mnNewMenu_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		menuBar.add(mnNewMenu_1);
		
		JMenu mnm = new JMenu("   Config(C)   ");
		mnm.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		menuBar.add(mnm);
		
		JMenu mnh = new JMenu("   Help(H)   ");
		mnh.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		menuBar.add(mnh);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(5, 48, 550, 492);
		tabbedPane.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		contentPane.add(tabbedPane);
		
			JPanel panel_5 = new JPanel();
			tabbedPane.addTab("Context  ", new ImageIcon(UI1.class.getResource("/com/sun/java/swing/plaf/windows/icons/TreeOpen.gif")), panel_5, null);
			panel_5.setLayout(null);
			
			JLabel lblSelectedItems = new JLabel("Items in context :");
			lblSelectedItems.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
			lblSelectedItems.setBounds(25, 15, 121, 25);
			panel_5.add(lblSelectedItems);
			

			scrollPane_3.setBounds(25, 50, 500, 360);
			panel_5.add(scrollPane_3);
			
			table = new JTable();
			table.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
			table.setAutoCreateRowSorter(true);
			table.setColumnSelectionAllowed(true);
			table.setCellSelectionEnabled(true);
			table.setDoubleBuffered(true);
			table.setFillsViewportHeight(true);
			table.setRowMargin(2);
			table.setRowHeight(25);
			table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					
				},
				new String[] {
					"Index", "Content"
				}
			) {
				Class[] columnTypes = new Class[] {
					Integer.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
					false, false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			table.getColumnModel().getColumn(0).setPreferredWidth(10);
			table.getColumnModel().getColumn(1).setPreferredWidth(420);
			scrollPane_3.setViewportView(table);
			
			JButton btnDetectKnowledgeNeeds = new JButton("Detect difficulties");
			btnDetectKnowledgeNeeds.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent click) {
					Object[][] toshow = new Object[50][2];
					try {
						String prepared = MainClass.readFile("D:/paseudoContext/SelectedRst.txt");
						String[] rsts = prepared.split("---\\r\\n");
						
						for(int i = 0; i<50&&i<rsts.length; i++){
							int stop = (rsts[i].indexOf("<br>")+188>rsts[i].length())? rsts[i].length(): rsts[i].indexOf("<br>")+188;
							if (stop==rsts[i].length()){
								toshow[i][0] = rsts[i].substring(0,stop)+"</html>";
							}else{
								toshow[i][0] = rsts[i].substring(0,stop)+"...</html>";
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*
					String usedContext = "";
					DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
					for (int i=0;i<tableModel.getRowCount();i++){
							usedContext += tableModel.getValueAt(i+1, 1);
							i++;
					}
					usedContext = usedContext.replaceAll("null","");
					Context sensedCtx = new Context(usedContext);
					Map<Context, Double> rsts;
					Object[][] toshow = new Object[50][2];
					try {
						QcaRecord.reselectNPs(sensedCtx);
						rsts = Cluster.simpleMatchWith(sensedCtx, 10);
						int j = 0;
						Iterator<Context> itt = rsts.keySet().iterator();
						while((j<50)&&(itt.hasNext())){
							Context kac = itt.next();
							System.out.println(kac.toString());//
							String shown = "<html>HOW TO: "+kac.SVO[1]+" "+kac.SVO[2]+"<br>";
							int i = 0;
							Iterator<String> itr = kac.nounPhrases.keySet().iterator();
							while((i<5)&&(itr.hasNext())){
								shown += itr.next()+"; ";
							}
							toshow[j][0] = shown+"</html>";
							j ++;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
					table_3.setModel(new DefaultTableModel(
						toshow,
						new String[] {
							"Knowledge item", "Useful?"
						}
					) {
						Class[] columnTypes = new Class[] {
							String.class, Boolean.class
						};
						public Class getColumnClass(int columnIndex) {
							return columnTypes[columnIndex];
						}
					});
					table_3.setRowHeight(80);
					//table_3.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());
					table_3.getColumnModel().getColumn(0).setPreferredWidth(400);
					table_3.getColumnModel().getColumn(1).setPreferredWidth(80);
					
					repaint();
				}
			});
			btnDetectKnowledgeNeeds.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
			btnDetectKnowledgeNeeds.setBounds(188, 422, 177, 23);
			panel_5.add(btnDetectKnowledgeNeeds);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Contact", new ImageIcon(UI1.class.getResource("/com/sun/java/swing/plaf/windows/icons/DetailsView.gif")), panel_3, null);
		panel_3.setLayout(null);
		
		JButton button_3 = new JButton("ÐÂ½¨ÁªÏµÈË");
		button_3.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		button_3.setBounds(25, 15, 103, 23);
		panel_3.add(button_3);
		
		JButton button_4 = new JButton("ÐÞ¸ÄÁªÏµÈË");
		button_4.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		button_4.setBounds(145, 15, 103, 23);
		panel_3.add(button_4);
		
		JButton btnNewButton_7 = new JButton("É¾³ýÁªÏµÈË");
		btnNewButton_7.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		btnNewButton_7.setBounds(265, 15, 103, 23);
		panel_3.add(btnNewButton_7);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(25, 50, 500, 390);
		panel_3.add(scrollPane_2);
		
		table_2 = new JTable();
		table_2.setDoubleBuffered(true);
		table_2.setAutoCreateRowSorter(true);
		table_2.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		table_2.setRowMargin(2);
		table_2.setRowHeight(25);
		table_2.setModel(new DefaultTableModel(
			new Object[][] {
				{"Li Xinyu", "<html><u>lxy2003jacky@sjtu.edu.cn</u></html>", "15000258061", Boolean.TRUE},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"\u59D3\u540D", "\u90AE\u7BB1\u5730\u5740", "\u8054\u7CFB\u65B9\u5F0F", "\u6807\u8BB0"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_2.getColumnModel().getColumn(0).setPreferredWidth(80);
		table_2.getColumnModel().getColumn(1).setPreferredWidth(160);
		table_2.getColumnModel().getColumn(2).setPreferredWidth(125);
		table_2.getColumnModel().getColumn(3).setPreferredWidth(35);
		table_2.setFillsViewportHeight(true);
		table_2.setColumnSelectionAllowed(true);
		table_2.setCellSelectionEnabled(true);
		scrollPane_2.setViewportView(table_2);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Inbox ", new ImageIcon(UI1.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize.gif")), panel_2, null);
		panel_2.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Outbox ", new ImageIcon(UI1.class.getResource("/javax/swing/plaf/metal/icons/ocean/maximize-pressed.gif")), panel_1, null);
		panel_1.setLayout(null);
		
		JLabel label = new JLabel("ÊÕ¼þÈË£º");
		label.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		label.setBounds(10, 10, 54, 20);
		panel_1.add(label);
		
		textField_1 = new JTextField();
		textField_1.setBounds(74, 7, 450, 25);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("  ³­ËÍ£º");
		lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 45, 54, 15);
		panel_1.add(lblNewLabel);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(74, 42, 450, 25);
		panel_1.add(textField_2);
		
		JLabel lblNewLabel_1 = new JLabel("  Ö÷Ìâ£º");
		lblNewLabel_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(10, 80, 54, 15);
		panel_1.add(lblNewLabel_1);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(74, 77, 450, 25);
		panel_1.add(textField_3);
		
		JLabel label_1 = new JLabel("  ÕýÎÄ£º");
		label_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		label_1.setBounds(10, 115, 54, 15);
		panel_1.add(label_1);
		
		JButton btnNewButton_1 = new JButton("·¢ËÍ");
		btnNewButton_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		btnNewButton_1.setBounds(431, 422, 93, 23);
		panel_1.add(btnNewButton_1);
		
		JButton button = new JButton("È¡Ïû");
		button.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		button.setBounds(328, 422, 93, 23);
		panel_1.add(button);
		
		JButton btnNewButton_2 = new JButton("Ìí¼Ó¸½¼þ");
		btnNewButton_2.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		btnNewButton_2.setBounds(74, 422, 93, 23);
		panel_1.add(btnNewButton_2);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(74, 112, 450, 300);
		panel_1.add(editorPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(74, 112, 450, 17);
		panel_1.add(toolBar);
		
		JButton btnNewButton_8 = new JButton("Add to context");
		btnNewButton_8.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent click) {
				JOptionPane.showMessageDialog(UI2,"ÓÊ¼þÒÑÑ¡Ôñ");

				DefaultTableModel tableModel = (DefaultTableModel) table_1.getModel();
				for (int i=0;i<tableModel.getRowCount()&&i<originalmails.length;i++){
						originalmails[i][3] = (Boolean) tableModel.getValueAt(i, 3);
				}
				selectedmails = ClickPushKnowledge(originalmails);
				table.setModel(new DefaultTableModel(selectedmails,new String[] {"Index", "Content"})
				{
						Class[] columnTypes = new Class[] {
							Integer.class, String.class
						};
						public Class getColumnClass(int columnIndex) {
							return columnTypes[columnIndex];
						}
				});
					table.getColumnModel().getColumn(0).setPreferredWidth(10);
					table.getColumnModel().getColumn(1).setPreferredWidth(420);
					repaint();
					scrollPane_3.setViewportView(table);
			}
		});
		btnNewButton_8.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		btnNewButton_8.setBounds(188, 422, 125, 23);
		panel_2.add(btnNewButton_8);
		
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(25, 10, 500, 400);
		panel_2.add(scrollPane_1);
		

		table_1.setFillsViewportHeight(true);
		table_1.setColumnSelectionAllowed(true);
		table_1.setCellSelectionEnabled(true);
		table_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		table_1.setRowMargin(2);
		table_1.setRowHeight(25);
		table_1.setModel(new DefaultTableModel(
			shownmails,
			new String[] {
				"Sender", "Title", "Time", "Selected"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_1.getColumnModel().getColumn(0).setPreferredWidth(80);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(200);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(85);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(35);
		scrollPane_1.setViewportView(table_1);
		
		table_1.addMouseListener(new java.awt.event.MouseAdapter() {
		     public void mouseClicked(java.awt.event.MouseEvent e) {
		    	 DefaultTableModel tableModel = (DefaultTableModel) table_1.getModel(); 
		    	 int row = table_1.getSelectedRow();
		    	 /*
		    	 int originalrow = 0;
		    	 for(int i=0;i<originalmails.length;i++){
		    		 if(originalmails[i][2].equals(tableModel.getValueAt(row, 2))&&originalmails[i][1].equals(tableModel.getValueAt(row, 1))&&originalmails[i][0].equals(tableModel.getValueAt(row, 0))){
		    			 originalrow = i;
		    			 break;
		    		 }
		    	 }
		    	 */
		    	 String mailcontent = (String) originalmails[row][4];
		    	 
		      repaint();
		               }
		   });
		
		
		Panel panel = new Panel();
		panel.setBounds(586, 36, 484, 30);
		contentPane.add(panel);
		panel.setLayout(null);
		
		txtEnter = new JTextField();
		txtEnter.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		txtEnter.setText("Enter keywords");
		txtEnter.setBounds(0, 4, 226, 26);
		panel.add(txtEnter);
		txtEnter.setColumns(10);
		
		JButton btnNewButton = new JButton("Search in listed");
		btnNewButton.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		btnNewButton.setBounds(236, 4, 127, 24);
		panel.add(btnNewButton);
		
		JButton btnNewButton_5 = new JButton("Search in KB");
		btnNewButton_5.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		btnNewButton_5.setBounds(367, 5, 117, 23);
		panel.add(btnNewButton_5);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(586, 72, 484, 468);
		contentPane.add(panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{0, 0};
		gbl_panel_6.rowHeights = new int[]{0, 0, 0};
		gbl_panel_6.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_6.add(scrollPane, gbc_scrollPane);
		
		table_3 = new JTable();
		//
		table_3.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		table_3.setAutoCreateRowSorter(true);
		table_3.setColumnSelectionAllowed(true);
		table_3.setCellSelectionEnabled(true);
		table_3.setDoubleBuffered(true);
		table_3.setFillsViewportHeight(true);
		table_3.setRowMargin(2);
		table_3.setRowHeight(30);
		table_3.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"Knowledge item", "Useful?"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_3.setRowHeight(30);
		table_3.getColumnModel().getColumn(0).setPreferredWidth(400);
		table_3.getColumnModel().getColumn(1).setPreferredWidth(80);
		scrollPane.setViewportView(table_3);
		
		JLabel lblNewLabel_2 = new JLabel("Loaded knowledg base: CAE experience");
		lblNewLabel_2.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(10, 563, 418, 15);
		contentPane.add(lblNewLabel_2);
		
	}
	
	public Object[][] ClickPushKnowledge(Object[][] mails){
		int selected=1;
		for(int i=0;i<mails.length;i++){
			if (mails[i][3]==(Object)true){
				selected++;
			}
		}
		Object[][] selectedmails = new Object[2*selected][2];
		int mailnumber=0;
		for(int i=0;i<mails.length;i++){
			if (mails[i][3]==(Object)true){
				selectedmails[2*mailnumber][0]=mailnumber+1;
				selectedmails[2*mailnumber][1]="Sender: " + (String)mails[i][0]+ "   Title: "+(String)mails[i][1]+ "   Time: " + (String)mails[i][2];
				selectedmails[2*mailnumber+1][1]=mails[i][4];
				mailnumber++;
			};

		}
		return selectedmails;
	}
	
	class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {
	    public TableCellTextAreaRenderer() {
	        setLineWrap(true);
	        setWrapStyleWord(true);
	    }

	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	        // ¼ÆËãµ±ÏÂÐÐµÄ×î¼Ñ¸ß¶È
	        int maxPreferredHeight = 30;
	        for (int i = 0; i < table.getColumnCount(); i++) {
	            setText("" + table.getValueAt(row, i));
	            setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
	            maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);
	        }

	        if (table.getRowHeight(row) != maxPreferredHeight)  // ÉÙÁËÕâÐÐÔò´¦ÀíÆ÷Ï¹Ã¦
	            table.setRowHeight(row, maxPreferredHeight);

	        setText(value == null ? "" : value.toString());
	        return this;
	    }
	}
}
