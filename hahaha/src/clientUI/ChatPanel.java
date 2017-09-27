package clientUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * 定义聊天及功能面板放在东侧，继承了JPanel
 */
public class ChatPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private TextArea messages;
	private JLabel statusLabel;
	private JPanel panel3;
	private JPanel panel2;
	private HashMap<String, JPanel> userList=new HashMap<String, JPanel>();
	private Boolean isManager;
	public ChatPanel(boolean isManager) {
		this.isManager=isManager;
		initUI();
	}



	private void initUI() {
		this.setPreferredSize(new Dimension(300, 0));
		//panel1 用于盛放系统状态
		JPanel panel1 = new JPanel();
		panel1.setBackground(new Color(220,220,220));
		panel1.setPreferredSize(new Dimension(296,20));
		panel1.setLayout(null);
		//statusLabel 放在panel1,用于显示系统状态
		statusLabel = new JLabel(" ● welcome to whiteboard");
		statusLabel.setBounds(0, 2, 296, 16);
		panel1.add(statusLabel);
		this.add(panel1);

		//panel2 用于盛放管理员和参与者的名片，及踢人功能
		panel2 = new JPanel(new GridLayout(4, 2, 2, 2));
		panel2.setBackground(new Color(220,220,220));
		panel2.setPreferredSize(new Dimension(296,100));
		this.add(panel2);

		//panel3 用于盛放聊天相关组件
		panel3 = new JPanel();
		panel3.setBackground(new Color(220,220,220));
		panel3.setPreferredSize(new Dimension(296,358));
		panel3.setLayout(null);	// 绝对布局
		//聊天内容显示框

		messages=new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		messages.setBounds(0,2,296, 298);
		messages.setEditable(false);
		panel3.add(messages);//加到panel3上

		// 实例化一个发送按钮
		ImageIcon icon = new ImageIcon("src" + File.separator+"ui"+ File.separator+"send.png");
		final JButton jbSend = new JButton(icon);
		jbSend.setBounds(240, 300, 56, 56);
		jbSend.setActionCommand("send");
		panel3.add(jbSend);//加到panel3上

		//实例化一个文字输入框
		JTextField txtWord = new JTextField();
		txtWord.setBounds(0, 300,242, 56);
		txtWord.setText("     Please enter your message here");
		txtWord.setToolTipText("");
		panel3.add(txtWord);//加到panel3上

		//监听鼠标点击，文字输入框鼠标点击时全选文字
		txtWord.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				txtWord.selectAll();
			}
			public void focusLost(FocusEvent e) {
			}
		});
		//监听回车键，回车等同于按下发送键
		txtWord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jbSend.doClick();
			}
		});

		//用匿名内部类来创建一个按钮的监听器对象，如果发送文字按钮按下，则调用其他method把文字送出去
		ActionListener l = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(txtWord.getText());//TODO 需要把sendMessage这个method改成往socket里面扔东西
				txtWord.setText("");
			}
		};
		jbSend.addActionListener(l);

		this.add(panel3);//panel3 放到右侧面板上

		
		addUser(true,"321312");


		addUser(false,"1111");
		addUser(false,"22222");
		addUser(false,"3333");
		addUser(false,"144444");
		addUser(false,"51555");
		addUser(false,"11111");
		addUser(false,"222122");
		addUser(false,"33313");
		/*测试用
		addUser(false,"444144");
		addUser(false,"55515");
		addUser(false,"11111");
		addUser(false,"222122");
		addUser(false,"33313");
		addUser(false,"444144");
		addUser(false,"55515");
		

		removeUser("321312");
		removeUser("1111");
		removeUser("1111");
		removeUser("1111");
		removeUser("1111");
		removeUser("1111");
		removeUser("22222");
		removeUser("3333");
		removeUser("144444");
		removeUser("51555");
		removeUser("11111");
		removeUser("222122");
		removeUser("33313");
		removeUser("444144");
		removeUser("55515");
		removeUser("11111");
		removeUser("222122");
		removeUser("33313");
		removeUser("444144");
		removeUser("55515");
		*/

	}


	public void sendMessage(String in) {
		//TODO 内外对接
		addMessage("aaa",in);//用于测试，实际调用时应为外传method，负责向外发消息
		setStatus("sent message ["+in+"]");
	}

	//传送sender和message，此method会将信息放出聊天框
	public void addMessage(String sender, String message) {
		String emp="";
		if(messages.getText().equals("")){
			emp="";
		}else{
			emp="\n";
		}
		messages.setText(messages.getText()+emp+" " +sender+": "+message);
		setStatus("got a new message from ["+sender+"]");
	}

	//用于更改状态标签显示的文字
	//TODO 任何一个需要显示状态的情况都可以调用这个method来显示在面板上，让用户看到
	public void setStatus(String show){
		synchronized(statusLabel){
			statusLabel.setText(" ● "+show);
		}
	}

	public void addUser(Boolean isM, String name){
		if(userList.containsKey(name) ){
			setStatus("user ["+name+"] already exists");
		}else{
			String imagePath="";
			if(isM==true){
				imagePath="manager";
			}else{
				if(isManager==true){
					imagePath="kick";
				}else{
					imagePath="user";
				}
			}

			JPanel panel = new JPanel();
			//panel.setPreferredSize(new Dimension(20,300));
			panel.setBackground(new Color(240,240,240));
			panel.setLayout(null);
			ImageIcon icon = new ImageIcon("src" + File.separator+"ui"+ File.separator+imagePath+".png");
			final JButton button = new JButton(icon);
			button.setBounds(0, 0, 25, 25);
			button.setActionCommand("send");
			//用匿名内部类来创建一个按钮的监听器对象，按下，则调用其他method把文字送出去
			// 用匿名内部类来创建一个按钮的监听工具选择面板的工具选取操作
			if(isM==false &&isManager==true){
				ActionListener l = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//查看被点击的是哪个人的卡片
						String kickUser = e.getActionCommand();
						//将目前的卡片按钮按下
						JButton sb=(JButton) e.getSource();
						sb.setEnabled(false);
						//TODO 交互需求，实际应向socket发送踢人指令
						System.out.println("send kicking request"+kickUser);
					}
				};
				// 设置按钮的动作命令
				button.setActionCommand(name);
				// 给按钮绑定监听器
				button.addActionListener(l);
			}
			panel.add(button);//加到panel上
			JLabel label = new JLabel(name);
			label.setBounds(25, 0, 300, 25);
			panel.add(label);
			panel2.add(panel);

			userList.put(name,panel);	
			setStatus("user ["+name+"] entered");
		}

	}

	//TODO sever发来的踢人指令，就可调用这个method把人在本地去除显示
	public void removeUser(String name){
		if(userList.containsKey(name) ){
			System.out.println(	userList.get(name));
			panel2.remove(userList.get(name));
			userList.remove(name);
			setStatus("["+name+"] has been kicked");
		}else{
			setStatus("["+name+"] does not exist");
		}
		System.out.println(	userList.size());
	}

}
