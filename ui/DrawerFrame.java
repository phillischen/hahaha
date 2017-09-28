package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;



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
	private static String ip = "localhost";
	private static int port = 4444;
	private String userName="goudan";
        private static Graphics2D graphics2d;
        private static SocketDrawing mydraw;
	
	private List<JMenuItem> list;// list用于存放菜单选项方便给每个菜单选项添加监听器
	private File file;//当前打开或保存到的文件路径
        
        private static JSONObject arguments, content;
        private static String message = "";
        private static String method, color, inputText;
        private static int oldX, oldY, currentX, currentY, size;
        private static DrawerFrame frame;
        private static ToolsPanel toolsPanel;
	/**
	 * 程序的入口主函数
	 */
        
        public SocketDrawing getMydraw(){
            return mydraw;
        }
        
        public Graphics2D getGraphics2D(){
            return this.graphics2d;
        }
        
        public ToolsPanel getToolsPanel(){
            return toolsPanel;
        }
        
	public static void main(String[] args) throws IOException {
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
					//System.out.println(args);

					
					//传入的参数没问题才能开始窗口构造
					frame = new DrawerFrame(args[0],args[1],args[2]);
					//frame.initUI();
                                        clientConnection();
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
	public DrawerFrame(String a,String b,String c) throws IOException {
		this.list = new ArrayList<JMenuItem>();
		ip = a;
		port = Integer.parseInt(b);
		userName=c;
                mydraw = new SocketDrawing(frame);
	}

        public static void clientConnection(){
            try{

                Socket socket = new Socket(ip, port);
                //output and input stream
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF("I want to connect!");
                message = input.readUTF();
                System.out.println(message);
                //graphics2d = mydraw.getGraphics2D();
                if(!message.isEmpty()){
                    mydraw = new SocketDrawing(decodeToImage(message), frame);
                }
                frame.initUI();

                //for testing
                output.writeUTF(command("text","",210,220,0,0,0,"This is Text222").toString());

                while(!socket.isClosed()){
                    if(input.available() > 0){
                        message = input.readUTF();
                        System.out.println(message);
                        parseCommand(new JSONObject(message));
                    }
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                input.close();
                output.close();
                socket.close();

            }catch(UnknownHostException e){
                System.err.println("Please enter the correct hostname to connect!");
            }catch(ConnectException e){
                System.err.println("Please enter the correct port number to connect!");
            }catch(IOException e){
                System.err.println(e.getMessage());
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } catch (JSONException e) {
                System.err.println(e.getMessage());
            } 
        }
        
        public static BufferedImage decodeToImage(String imageString) {
 
            BufferedImage image = null;
            byte[] imageByte;
            try {
                BASE64Decoder decoder = new BASE64Decoder();
                imageByte = decoder.decodeBuffer(imageString);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                image = ImageIO.read(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
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
		this.setIconImage(new ImageIcon("draw.png").getImage());

		// 调用创建菜单选项的方法在窗体上创建菜单选项
		createMenuBar();		
		//add the canvas to the UI panel
		this.add(mydraw, BorderLayout.CENTER);

		// 实例化一个图形选择工具条
		ToolsPanel toolsPanel = new ToolsPanel(mydraw,isManager);
		// 将图形选择工具条添加到窗体上
		this.add(toolsPanel, BorderLayout.WEST);
		this.setVisible(true);

		// 实例化一个聊天窗口及成员控制栏目
		ChatPanel chatPanel = new ChatPanel(isManager);
		// 将栏目添加到窗体上
		this.add(chatPanel, BorderLayout.EAST);	
		
		this.setVisible(true);
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
        
        public static JSONObject commandNewClient(String image){
            JSONObject command = new JSONObject();
            try {
                command.put("kind", "newClient");
                command.put("image", image);
            } catch (JSONException e) {
                System.err.println(e.getMessage());
            }
            return command;
        }
    
    public static JSONObject command(String method, String color, int oldX, int oldY, int currentX, int currentY, int size, String inputText){
        JSONObject command = new JSONObject();
        JSONObject arguments = new JSONObject();
        
        try {
                switch(method){
                    case "color":
                        arguments.put("color", color);
                        break;
                    case "freeDraw":
                    case "line":
                    case "rect":
                    case "oval":
                    case "circle":
                        arguments.put("oldX", oldX);
                        arguments.put("oldY", oldY);
                        arguments.put("currentX", currentX);
                        arguments.put("currentY", currentY);
                        break;
                    case "eraser":
                        arguments.put("oldX", oldX);
                        arguments.put("oldY", oldY);
                        arguments.put("size", size);
                        break;
                    case "text":
                        arguments.put("oldX", oldX);
                        arguments.put("oldY", oldY);
                        arguments.put("inputText", inputText);
                        break;
                    case "clear":
                        break;
                    default:
                        break;
                }
                
                command.put("kind", "oldClient");
                command.put("method", method);
                command.put("arguments", arguments);

        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
        return command;
    }
    
    private static void parseCommand(JSONObject command){
        System.out.println(command);
        try {
            if(command.has("broadcast")){
                content = command.getJSONObject("broadcast");
                method = content.getString("method");
                System.out.println(method);
                arguments = content.getJSONObject("arguments");
                System.out.println(arguments.toString());
                switch(method){
                    case "color":
                        color = arguments.getString("color");
                        break;
                    case "freeDraw":
                    case "line":
                    case "rect":
                    case "oval":
                    case "circle":
                        oldX = arguments.getInt("oldX");
                        oldY = arguments.getInt("oldY");
                        currentX = arguments.getInt("currentX");
                        currentY = arguments.getInt("currentY");
                        break;
                    case "eraser":
                        oldX = arguments.getInt("oldX");
                        oldY = arguments.getInt("oldY");
                        size = arguments.getInt("size");
                        break;
                    case "text":
                        oldX = arguments.getInt("oldX");
                        oldY = arguments.getInt("oldX");
                        inputText = arguments.getString("inputText");
                        break;
                    case "clear":
                        break;
                    default:
                        break;
                }
                
                System.out.println(method);
                System.out.println(color);
                System.out.println(oldX);
                System.out.println(oldY);
                System.out.println(currentX);
                System.out.println(currentY);
                System.out.println(size);
                System.out.println(inputText);
                doRequest(method, color, oldX, oldY, currentX, currentY, size, inputText);
            }
        }catch (JSONException e) {
            System.err.println(e.getMessage());
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        
    }
    
    public static void getArgument(JSONObject message) {
                    // package up the string order into an object
        System.out.println(message.toString());

    }
    
    public static void doRequest(String method, String color, int oldX, int oldY, int currentX, int currentY, int size, String inputText){
        System.out.println("doRequest");
        switch(method){
            case "color":
                switch(color) { //set color
                    case "red":
                        mydraw.red();
                        break;
                    case "darkred":
                        mydraw.darkred();
                        break;
                    case "black":
                        mydraw.black();
                        break;
                    case "green":
                        mydraw.green();
                        break;
                    case "darkgreen":
                        mydraw.darkgreen();
                        break;
                    case "blue":
                        mydraw.blue();
                        break;
                    case "darkblue":
                        mydraw.darkblue();
                        break;
                    case "lightgray":
                        mydraw.lightgray();
                        break;
                    case "magenta":
                        mydraw.magenta();
                        break;
                    case "orange":
                        mydraw.orange();
                        break;
                    case "darkorange":
                        mydraw.darkorange();
                        break;
                    case "pink":
                        mydraw.pink();
                        break;
                    case "darkpink":
                        mydraw.darkpink();
                        break;
                    case "white":
                        mydraw.white();
                        break;
                    case "yellow":
                        mydraw.yellow();
                        break;
                    case "darkyellow":
                        mydraw.darkyellow();
                        break;
                    default:
                        System.out.println("color lable wrong!");
                        break;
                }
                break;
            case "freeDraw":
                graphics2d.drawLine(oldX, oldY, currentX, currentY);
		mydraw.repaint();
                break;
            case "line":
                graphics2d.drawLine(oldX, oldY, currentX, currentY);
		mydraw.repaint();
                break;
            case "rect":
                graphics2d.drawRect(oldX, oldY, Math.abs(currentX - oldX), Math.abs(currentY - oldY));
		mydraw.repaint();
                break;
            case "oval":
                graphics2d.drawOval(oldX, oldY, Math.abs(currentX - oldX), Math.abs(currentY - oldY));
		mydraw.repaint();
                break;
            case "circle":
                int diameter = Math.max(Math.abs(currentX - oldX), Math.abs(currentY - oldY));
		graphics2d.drawOval(oldX, oldY, diameter, diameter);
                mydraw.repaint();
                break;
            case "eraser":
                graphics2d.setPaint(Color.white);
                graphics2d.fillOval(oldX, oldY, size, size);
                mydraw.repaint();
                break;
            case "text":
                graphics2d.drawString(inputText, oldX, oldY);
		mydraw.repaint();
                break;
            case "clear":
                mydraw.clear();
                break;
            default:
                break;
        }
    }
    
}
