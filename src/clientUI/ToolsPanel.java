package clientUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 定义图形工具栏放在窗体的西边，继承了JPanel
 * 工具栏里没有什么需要对接的东西，比较简单
 * 
 */

public class ToolsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private String tool;// 表示用户选择的工具，初始值为1。数字与工具所在工具栏的序号对应
	private String size;// 表示用户选择的橡皮擦大小，初始值为2
	private JPanel panel;//左侧的整个panel
	private JButton sb;//目前选择的工具按钮
	private JButton ss;//目前选择的大小按钮
	private Color colorL;//左键存储的颜色
	private Color colorR;//右键存储的颜色
	private SocketDrawing draw1;
	private Boolean isManager;
	
	public ToolsPanel(SocketDrawing draw,boolean isManager) {
		this.tool="1";
		this.size="2";
		this.isManager=isManager;
		colorL = Color.BLACK;
		colorR = Color.WHITE;
		this.draw1 = draw;
		initUI();
		
	}
	
	private void initUI() {
		this.setPreferredSize(new Dimension(60, 0));

		// 用匿名内部类来创建一个按钮的监听工具选择面板的工具选取操作
		ActionListener l = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//将上一个已选按钮弹起
				if (sb!=null){
					sb.setEnabled(true);	
				}
				//标记选取到的tool是什么
				tool = e.getActionCommand();
				switch (tool) {
				case "3":
					draw1.straightLine();
					break;
				case "2":
					String x = inputText();
					System.out.println(x);
					draw1.drawText(x);
					break;
				case "4":
					draw1.rectangle();
					break;
				case "5":
					draw1.oval();;
					break;
				case "6":
					draw1.circle();;
					break;
				case "7":
					int y = Integer.parseInt(size);					
					draw1.eraser(y*10);
					break;
				case "1":
					draw1.freedDraw();;
					break;
				
				
				}
				 
					
				
				//将目前使用的tool按钮按下
				sb=(JButton) e.getSource();

				sb.setEnabled(false);
				if (tool.equals("2")) {
					sb.setEnabled(true);
				}
				if(panel!=null){		
				if(tool.equals("7")){
						panel.setVisible(true);		
				}else{
					panel.setVisible(false);				
					}
				}
			}
		};
		
		//panel0为绘画工具选择栏
		JPanel panel0 = new JPanel(new GridLayout(7, 1, 2, 2));
		panel0.setPreferredSize(new Dimension(42, 200));
		panel0.setBackground(new Color(220,220,220));
		for (int i = 1; i < 8; i++) {
			// 根据得到的URL来加载按钮的背景图片
			ImageIcon icon = new ImageIcon(i+".png");;
			//ImageIcon icon = new ImageIcon("1.png");
			// 实例化一个按钮对象
			JButton button = new JButton(icon);
			// 设置按钮的大小
			button.setPreferredSize(new Dimension(42, 30));
			button.setOpaque(true);
			button.setBackground(new Color(220,220,220));
			//button.setBorderPainted(false);
			// 设置按钮的动作命令
			button.setActionCommand(Integer.toString(i));
			// 给按钮绑定监听器
			button.addActionListener(l);
			// 将按钮添加到工具栏的容器中
			panel0.add(button);
			if(Integer.parseInt(tool)==i){
				sb=button;
			}
		}
		this.add(panel0);//将工具栏绑到左侧panel上
		

		
		//panel1用于存放目前选取的左右键颜色
		JPanel panel1 = new JPanel();
		// 设置Panel1的大小
		panel1.setPreferredSize(new Dimension(40, 40));
		// 设置JPanel的布局为绝对布局
		panel1.setLayout(null);
		// 实例化一个按钮颜色
		final JButton jbuL = new JButton();
		// 给按钮设置背景色
		jbuL.setBackground(Color.BLACK);
		jbuL.setOpaque(true);
		jbuL.setBorderPainted(false);
		//同理
		final JButton jbuR = new JButton();
		jbuR.setBackground(Color.WHITE);
		panel1.setBackground(new Color(220,220,220));
		jbuR.setOpaque(true);
		jbuR.setBorderPainted(false);
		//放到panel1上面
		jbuL.setBounds(6, 6, 28, 28);
		jbuR.setBounds(17, 17, 19, 19);
		panel1.add(jbuL);
		//panel1.add(jbuR);
		this.add(panel1);//panel1放入左侧
		

		//监听选取颜色操作。
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JButton button = (JButton) e.getSource();
				if (e.getButton() == 1) {// 当按下鼠标左键的时候将鼠标左键对应的颜色设置为当前值
					colorL = button.getBackground();
					draw1.cc(colorL);
					jbuL.setBackground(colorL);
				} 
			}
		};
		
		JPanel panel2 = new JPanel(new GridLayout(8, 2, 2, 2));
		// 定义一个数组，用来存储颜色选取面板
		Color[] array = { Color.BLACK,new Color(140,40,40),new Color(180,80,80),new Color(220,120,120),new Color(240,160,160),
				new Color(100,100,200),new Color(110,100,240), Color.WHITE, Color.RED, Color.GREEN,
				Color.BLUE, Color.GRAY, Color.ORANGE, Color.PINK, Color.CYAN,Color.MAGENTA
				};
		//将每个颜色注入颜色选取面板
		for (Color color : array) {
			JButton button = new JButton();
			button.setBackground(color);
			button.setOpaque(true);
			button.setBorderPainted(false);
			button.setPreferredSize(new Dimension(19, 19));
			button.addMouseListener(ma);
			panel2.add(button);
		}
		this.add(panel2);
		
		//粗细选取操作，仅在使用橡皮工具时出现，其余工具隐藏
		ActionListener l2 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ss!=null){
					ss.setEnabled(true);	
				}
				size = e.getActionCommand();
				
				int y = Integer.parseInt(size);
				
				draw1.eraser(y*10);
				
				ss=(JButton) e.getSource();
				ss.setEnabled(false);
				
			}
		};
		
		//粗细选取面板，仅在使用橡皮工具时出现，其余工具隐藏
		panel = new JPanel(new GridLayout(3, 1, 2, 2));
		panel.setPreferredSize(new Dimension(42, 66));
		//panel.setBackground(Color.WHITE);
		for (int i = 1; i < 4; i++) {
			// 根据得到的URL来加载按钮的背景图片
			ImageIcon icon = new ImageIcon("s"+i+".png");;
			// 实例化一个按钮对象
			JButton button = new JButton(icon);
			// 设置按钮的大小
			button.setPreferredSize(new Dimension(42, 30));
			button.setOpaque(true);
			button.setBackground(new Color(220,220,220));
			//button.setBorderPainted(false);
			// 设置按钮的动作命令
			button.setActionCommand(i + "");
			// 给按钮绑定监听器
			button.addActionListener(l2);
			// 将按钮添加到工具栏的容器中
			panel.add(button);
			if(Integer.parseInt(size)==i){
				ss=button;
			}
		}
		this.add(panel);
		panel.setVisible(false);
		
		//sb.doClick();//点击初始化选取的工具
		ss.doClick();//点击初始化选取的粗细
	}

	public String getTool() {
		return tool;
	}
	public String getSizes() {
		return size;
	}

	public JPanel getPanel() {
		return panel;
	}
	public Color getColorL() {
		return colorL;
	}

	public Color getColorR() {
		return colorR;
	}
	public String inputText(){
		return JOptionPane.showInputDialog("Please input your text");
	}
}