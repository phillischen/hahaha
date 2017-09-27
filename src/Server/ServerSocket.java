package Server;

/*
 * 
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.net.ServerSocketFactory;
import org.json.JSONObject;

public class ServerSocket {
    
    //Declare the port number
    private static int port = 4444;
    private static ArrayList<ClientThread> clientList = new ArrayList<ClientThread>();
    //Identifies the user number connected
    private static int counter = 0;
    
    private static JSONObject dict_json;
    
    public static JSONObject getDict_json(){
        return dict_json;
    }
    
    public static void main(String[] args) {
        
        //object that will store the parsed command line arguments
        //CmdLineArgs argsBean = new CmdLineArgs();
        //parser provided by args4j
        //CmdLineParser parser = new CmdLineParser(argsBean);
        
        try{
            
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
    
    
    public static void sendToAll(String message){
        for(ClientThread client : clientList)
            client.sendMessage(message);
    }
    
    
    
    
}
