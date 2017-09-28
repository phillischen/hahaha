package clientUI;

import java.net.*;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.net.ServerSocketFactory;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

public class ClientSocket {

        //Declare the port number
        private static int port = 4444;
        private static ArrayList<ClientThread> clientList = new ArrayList<ClientThread>();
        //Identifies the user number connected
        private static int counter = 0;
        private static SocketDrawing mydraw;
        private static Graphics2D g2;
        private static String[] colorList;
        
        public static void main(String[] args) {
            
            try{
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

                                //传入的参数没问题才能开始窗口构造
                                DrawerFrame frame = new DrawerFrame(args[0],args[1],args[2]);
                                frame.initUI();
                                mydraw = frame.getMydraw();
                               // g2 = frame.getGraphics2D();
                                
                        }
                }
                //create socket at port x
                ServerSocketFactory factory = ServerSocketFactory.getDefault();
                java.net.ServerSocket server = factory.createServerSocket(port);

                System.out.println("Waiting for client connection...on port " + port);

                //wait for connections
                while(true){
                    Socket client = server.accept();
                    counter++;
                    System.out.println("Client " + counter + ": Applying for connection!");

                    ClientThread t = new ClientThread(client, counter);
                    clientList.add(t);
                    t.start();
                }


            }catch(IOException e){
                System.err.println("Problem appears to read the server socket.");
            }

        }

        public static BufferedImage getImg() throws IOException{
            BufferedImage img = ImageIO.read(new File("img.png"));
            //clientSocket.makeRequest(img);
            return img;
        }
	
	void makeRequest2(BufferedImage img) {
		// package up the image into an object

	}
        
        public static void getRequest(){
        
    }
        public static void sendToAll(String message){
            for(ClientThread client : clientList)
                client.sendMessage(message);
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
    
    public static void doRequest(String method, String color, int oldX, int oldY, int currentX, int currentY, int size, String inputText, String image){
        System.out.println("doRequest");
        
        colorList = color.split(",");
        mydraw.cc(new Color(Integer.parseInt(colorList[0]),Integer.parseInt(colorList[1]),Integer.parseInt(colorList[2])));
        
        switch(method){
            case "color":
                break;
            case "freeDraw":
                mydraw.g2.drawLine(oldX, oldY, currentX, currentY);
		mydraw.repaint();
                break;
            case "line":
                mydraw.g2.drawLine(oldX, oldY, currentX, currentY);
		mydraw.repaint();
                break;
            case "rect":
                mydraw.g2.drawRect(Math.min(oldX, currentX), Math.min(oldY, currentY), Math.abs(currentX - oldX), Math.abs(currentY - oldY));
		mydraw.repaint();
                break;
            case "oval":
                mydraw.g2.drawOval(Math.min(oldX, currentX), Math.min(oldY, currentY), Math.abs(currentX - oldX), Math.abs(currentY - oldY));
		mydraw.repaint();
                break;
            case "circle":
                int diameter = Math.max(Math.abs(currentX - oldX), Math.abs(currentY - oldY));
		mydraw.g2.drawOval(Math.min(oldX, currentX), Math.min(oldY, currentY), diameter, diameter);
                mydraw.repaint();
                break;
            case "eraser":
                mydraw.g2.setPaint(Color.white);
                mydraw.g2.fillOval(oldX, oldY, size, size);
                mydraw.repaint();
                break;
            case "text":
                mydraw.g2.drawString(inputText, oldX, oldY);
		mydraw.repaint();
                break;
            case "clear":
                mydraw.clear();
                if(!image.isEmpty()){
                    mydraw.paintImg(decodeToImage(image));
                }
                break;
            default:
                break;
        }
    }
    
    
}