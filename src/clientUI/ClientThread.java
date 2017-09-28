package clientUI;

/*
 * 
 */

import static clientUI.ClientSocket.doRequest;
import static clientUI.ClientSocket.sendToAll;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import javax.imageio.ImageIO;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class ClientThread extends Thread{
    
    private Thread thread;
    private Socket clientSocket;
    private DataInputStream input;
    private DataOutputStream output;
    private JSONObject request, arguments;
    private String method, color, inputText, image;
    private int oldX, oldY, currentX, currentY, size;
    private int num;
    private BufferedImage img;
    
    public ClientThread(Socket client, int counter){
        try {
            this.clientSocket = client;
            this.num = counter;
            output = new DataOutputStream(clientSocket.getOutputStream());
            input = new DataInputStream(clientSocket.getInputStream());
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void start(){
        try {
            System.out.println("CLIENT: " + (String)input.readUTF());
            output.writeUTF(imgToString(ClientSocket.getImg()));
            //output.writeUTF("Server: Hi Client " + this.num + "!!!");
            //output.flush();
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } 
    }
    
    public String imgToString(BufferedImage image) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos);
        byte[] imageBytes = bos.toByteArray();

        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(imageBytes);
    }
    

    
    public void run(){
        synchronized(this){
            while(true){
                try {
                    if(input.available() > 0){
                        request = new JSONObject(input.readUTF());
                        System.out.println(request.toString());
                        
                        JSONObject broadcast = new JSONObject();
                        broadcast.put("broadcast", request);
                        sendToAll(broadcast.toString());
                        parseCommand(request);
                    }
                } catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }
        }
    }
    
    
    public void sendMessage(String message){
        try {
            output.writeUTF(message);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    private void parseCommand(JSONObject command){
        System.out.println(command);
        try {
            if(command.getString("kind").equals("newClient")){
                //output.writeUTF(img);
            }else {
                image = command.getString("image");
                
                method = command.getString("method");
                System.out.println(method);
                arguments = command.getJSONObject("arguments");
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
                        color = arguments.getString("color");
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
                        color = arguments.getString("color");
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
                doRequest(method, color, oldX, oldY, currentX, currentY, size, inputText, image);
            }
        }catch (JSONException e) {
            System.err.println(e.getMessage());
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        
    }
    
    
    
}

