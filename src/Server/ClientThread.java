package Server;

/*
 * 
 */

import static Server.ServerSocket.sendToAll;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;


public class ClientThread extends Thread{
    
    private Thread thread;
    private Socket clientSocket;
    private DataInputStream input;
    private DataOutputStream output;
    private JSONObject request, arguments;
    private String method, color, inputText,in;
    private int oldX, oldY, currentX, currentY;
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
            output.writeUTF("Server: Hi Client " + this.num + "!!!");
            //output.flush();
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } 
    }
    
    public void run(){
        synchronized(this){
            while(true){
                try {
                    if(input.available() > 0){
                        request = new JSONObject(input.readUTF());
                        System.out.println(request.toString());
                        //parseCommand(request);
                        sendToAll("From client " + this.num + ": " + request.toString());
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
        try {
            if(command.has("newClient")){
                //output.writeUTF(img);
            }else if(command.has("oldClient")){
                method = command.getString("method");
                arguments = new JSONObject(command.getString("arguments"));
                switch(method){
                    case "color":
                        color = arguments.getString("color");
                        break;
                    case "line":
                    case "rect":
                    case "oval":
                    case "circle":
                    case "eraser":
                        oldX = arguments.getInt("oldX");
                        oldY = arguments.getInt("oldX");
                        currentX = arguments.getInt("currentX");
                        currentY = arguments.getInt("currentY");
                        break;
                    case "text":
                        oldX = arguments.getInt("oldX");
                        oldY = arguments.getInt("oldX");
                        inputText = arguments.getString("input");
                        break;
                    case "clear":
                        break;
                    default:
                        break;
                }
            }
        }catch (JSONException e) {
            System.err.println(e.getMessage());
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        
    }
    
    
}

