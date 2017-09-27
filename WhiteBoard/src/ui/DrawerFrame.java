package ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



/**
 * XP画板的窗体类，继承JFrame
 * 
 */
public class DrawerFrame extends JFrame {
	//true为管理员，false为普通用户，改这个参数就能设置成两个不同的权限
	//管理员可以踢人，普通用户点不了
	private Boolean isManager=true;
	
	JFileChooser fc = new JFileChooser();//创建文件对话框对象  
	private static final long serialVersionUID = 1L;
	private String ip = "localhost";
	private static int port = 3000;
	private String userName="goudan";

	
	private List<JMenuItem> list;// list用于存放菜单选项方便给每个菜单选项添加监听器
	private File file;//当前打开或保存到的文件路径
	/**
	 * 程序的入口主函数
	 */
	public static void main(String[] args) {
		try{//检查传入参数
			if(	args.length<3){
				System.out.println("invalid argument,please specify address, port and username");
				System.exit(0);
			}else{
				if(args[0]==""||args[1]==""){
					System.out.println("invalid argument,please specify address and port");
					System.exit(0);
				}else if(Integer.parseInt(args[1])<1024||Integer.parseInt(args[1])>65535){
					System.out.println("invalid port,please use port from 1024 to 65535");
					System.exit(0);
				}else{
					System.out.println(args);

					
					//传入的参数没问题才能开始窗口构造
					DrawerFrame frame = new DrawerFrame(args[0],args[1],args[2]);
					frame.initUI();
				}
			}
		}catch(NumberFormatException e){
			System.out.println("invalid argument,please specify address and port");
			System.exit(0);
		}
		

	}

	/**
	 * 窗体类的构造方法
	 */
	public DrawerFrame(String a,String b,String c) {
		this.list = new ArrayList<JMenuItem>();
		ip = a;
		port = Integer.parseInt(b);
		userName=c;
	}

	/**
	 * 初始化画板主界面的方法
	 */
	public void initUI() {
		// 设置窗体的属性
		this.setTitle("Online Whiteboard");
		this.setSize(new Dimension(1024, 550));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(new ImageIcon("src" + File.separator+"ui"+ File.separator+"draw.png").getImage());

		// 调用创建菜单选项的方法在窗体上创建菜单选项
		createMenuBar();		
		// 实例化一个JPanel对象用于放置画板绘图区域
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
		centerPanel.setBackground(Color.GRAY);// 设置背景色为灰色

		// 实例化一个画图区域对象，实际上就是JPanel的实例
		JPanel drawPanel = new JPanel();
		// 设置绘图区域的背景色为白色
		drawPanel.setBackground(Color.WHITE);
		// 设置绘图区域的大小
		drawPanel.setPreferredSize(new Dimension(657, 492));
		// 将绘图区域添加到centerPanel中
		centerPanel.add(drawPanel);
		// 将centerPanel添加到窗体中
		this.add(centerPanel, BorderLayout.CENTER);

		// 实例化一个图形选择工具条
		ToolsPanel toolsPanel = new ToolsPanel();
		// 将图形选择工具条添加到窗体上
		this.add(toolsPanel, BorderLayout.WEST);
		this.setVisible(true);

		// 实例化一个聊天窗口及成员控制栏目
		ChatPanel chatPanel = new ChatPanel(isManager);
		// 将栏目添加到窗体上
		this.add(chatPanel, BorderLayout.EAST);
		this.setVisible(true);

		// 给DrawPanel添加监听器
		Graphics2D graphics2d = (Graphics2D) drawPanel.getGraphics();
		// 实例化一个绘图区域的监听器
		DrawListener dl = new DrawListener(graphics2d, toolsPanel);
		drawPanel.addMouseListener(dl);
		drawPanel.addMouseMotionListener(dl);
		// 实例化一个菜单选项监听器对象
		/*
		MenuListener ml = new MenuListener(graphics2d);
		for (JMenuItem item : list) {
			item.addActionListener(ml);
		}
		 */
	}

	/**
	 * 创建菜单选项的方法
	 */
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		String[] arrayMenu = { "File", "Help" };
		String[][] arrayMenuItem = { { "New", "Open", "Save", "SaveAs","Close" },
				{ "Help"} };

		ActionListener l = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//查看点击的事菜单中的哪个按钮
				String menuAction= e.getActionCommand();

				//TODO 根据菜单栏点击的选项，进行相应操作
				switch(menuAction) { //set color
				case "New"://TODO 如果是新建，则新建
					System.out.println("menuAction="+menuAction);
					break;

				case "Open"://TODO 如果是open，通过弹窗选取路径，接下来要做的是将文件读到画板
					int select = fc.showOpenDialog(menuBar);//显示打开文件对话框  
					if(select == JFileChooser.APPROVE_OPTION)//选择的是否为“确认”  
					{  
						file = fc.getSelectedFile();  
						System.out.println("文件"+file+"被打开");  
					}  
					else  
						System.out.println("打开操作被取消");//在屏幕上输出    
					break;


				case "Save"://TODO 如果是保存
					System.out.println("menuAction="+menuAction);
					//如果目前还没有打开或保存到过任何路径，则选取一个
					if(file==null){
						System.out.println("保存操作被取消");  
						int select1 = fc.showSaveDialog(menuBar);//显示保存文件对话框  
						if(select1 == JFileChooser.APPROVE_OPTION)  
						{  
							file = fc.getSelectedFile();  
							System.out.println("文件"+file+"被保存");  
						}  
						else  
							System.out.println("保存操作被取消");  

					}else{
						//如果已有，直接存到已有路径
					}
					break;


				case "SaveAs"://TODO 如果是另存为某个路径
					System.out.println("menuAction="+menuAction);
					int select2 = fc.showSaveDialog(menuBar);//显示保存文件对话框  
					if(select2 == JFileChooser.APPROVE_OPTION)  
					{  
						file = fc.getSelectedFile();  
						System.out.println("文件"+file+"被保存");  
					}  
					else  
						System.out.println("保存操作被取消");  
					break;


				case "Close"://TODO 关闭
					System.out.println("menuAction="+menuAction);
					break;


				case "Help":
					JOptionPane.showMessageDialog(null, "Please refer to the demonstration and report"
							, "Help",JOptionPane.ERROR_MESSAGE);
					break;
				}

			}
		};

		for (int i = 0; i < arrayMenu.length; i++) {
			// 实例化一个JMenu对象
			JMenu menu = new JMenu(arrayMenu[i]);
			for (int j = 0; j < arrayMenuItem[i].length; j++) {
				// 实例化一个JMenuItem对象
				JMenuItem item = new JMenuItem(arrayMenuItem[i][j]);
				list.add(item);
				// 将JMenuItem对象添加到JMenu中
				menu.add(item);
				item.setActionCommand(arrayMenuItem[i][j]);
				// 给按钮绑定监听器
				item.addActionListener(l);
			}
			// 将JMenu添加到JMenuBar中
			menuBar.add(menu);
		}
		// 给menuBar设置大小
		menuBar.setPreferredSize(new Dimension(800, 30));
		// 给窗体设置菜单栏
		this.setJMenuBar(menuBar);
	}

}
