import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private String username;
	private String password;
	private static JFrame UI2;
	static public Object[][] originalmails = new Object[][]{};
	
	public Object[][] getmails(){
		return originalmails;
	}
	
	public String getpassword(){
		return password;
	}
	
	public void close(){
		this.setVisible(false);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login frame = new login();
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
	public login() {
		setTitle("欢迎使用邮件知识推送系统");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("请输入您的邮箱信息。");
		label.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label.setBounds(28, 21, 200, 35);
		contentPane.add(label);
		
		JLabel label1 = new JLabel("无输入则运行预存收件箱。  交大邮箱亲测可用。");
		label1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label1.setBounds(28, 41, 360, 35);
		contentPane.add(label1);
		
		JLabel label_1 = new JLabel("账户：");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_1.setBounds(60, 80, 79, 25);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("密码：");
		label_2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_2.setBounds(60, 140, 54, 25);
		contentPane.add(label_2);
		
		textField = new JTextField();
		textField.setBounds(130, 80, 200, 25);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(130, 140, 200, 25);
		contentPane.add(passwordField);
		
		JButton button = new JButton("登陆");
		button.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent click) {
				username = textField.getText();//账户示例：lxy2003jacky@sjtu.edu.cn，已测试交大邮箱
				password = passwordField.getText();//密码
				JOptionPane.showMessageDialog(UI2,"登陆账户："+username+"\n登陆密码："+password);
				try {
					if(!username.isEmpty() && !password.isEmpty()){
						originalmails = ReceiveMails.receive(username,password);
						UI1.runUI1(originalmails);
					}
					else{
						String s = "Sia,           see below the FEA report by SANMAR for the enlarged link. They have raisen a warning over the strength of the enlarged link (currently at 110mm diameter).  In my opinion the report misses several major step:   - the analysis the have run is a non linear one, however they are focussing on resulting stresses instead of strains.   - Allowable strain has not been reported: for R4 it shall be the same as the one reported by GN Rope for the shackle (11.3%)   - the Proof load and the Break load shall be checked in non corroded condition.   - an additional load simulation shall be added for the corroded link (-10mm overall) under the accidental load.   Let me know your feedback by end of the day: given the potential impacts on the ROV hook and the chain mock up, this shall be treated with top priority.   Alf";
						UI1.runUI1(new Object[][]{
								{"Li Xinyu", "testsubject1", "2014.6.8", Boolean.TRUE,s},
								{null, "testsubject2", "2014.6.8", Boolean.TRUE,"testmailcontent2"},
								{"Li Xinyu", "testsubject3", "2014.6.8", Boolean.FALSE,"testmailcontent3"},
								{"Li Xinyu", null, "2014.6.8", Boolean.TRUE,"testmailcontent4"},
								{"Li Xinyu", "testsubject5", "2014.6.8", Boolean.FALSE,"testmailcontent5"},
								{"Li Xinyu", "testsubject6", null, Boolean.TRUE,"testmailcontent6"},
								{"Li Xinyu", "testsubject7", "2014.6.8", Boolean.TRUE,null},
								{"Li Xinyu", "testsubject8", "2014.6.8", null,"testmailcontent7"},
								{null, null, null, null,null},
								{null, null, null, null,null},
							});
					}

				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		});
		button.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		button.setBounds(166, 200, 93, 23);
		contentPane.add(button);
	}
}
