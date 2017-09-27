package clientUI;

import java.net.*;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class ClientSocket {
	
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private Drawing2 mydraw;
	

	ClientSocket(String serveraddress, int port) throws UnknownHostException, IOException{
			//server address and port is read from command line argument
			socket = new Socket(serveraddress, port);

			input = new DataInputStream(socket.getInputStream());
		    output = new DataOutputStream(socket.getOutputStream());
	}
	
	void makeRequest(String message) {
		try {

			output.writeUTF(message);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	    	
	}
	
	void getRequest() {
		try {
			
			/*change input object to string /image
			if image: mydraw.paintImg(img);
			if string: do below
			*/
			String[] order = input.readUTF().split(",");
			
			switch(order[2]) { //set color
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
			}
			
			switch(order[1]) {
			case "line":
				mydraw.g2.drawLine(Integer.parseInt(order[3]),Integer.parseInt(order[4]),
						Integer.parseInt(order[5]),Integer.parseInt(order[6]));
				mydraw.repaint();
				break;
			case "rect":
				mydraw.g2.drawRect(Integer.parseInt(order[3]),Integer.parseInt(order[4]),
						Integer.parseInt(order[5]),Integer.parseInt(order[6]));
				mydraw.repaint();
				break;
			case "oval":
				mydraw.g2.drawOval(Integer.parseInt(order[3]),Integer.parseInt(order[4]),
						Integer.parseInt(order[5]),Integer.parseInt(order[6]));
				mydraw.repaint();
				break;
			case "circle":
				mydraw.g2.drawOval(Integer.parseInt(order[3]),Integer.parseInt(order[4]),
						Integer.parseInt(order[5]),Integer.parseInt(order[6]));
				mydraw.repaint();
				break;
			case "eraser":
				mydraw.g2.fillOval(Integer.parseInt(order[3]),Integer.parseInt(order[4]),
						Integer.parseInt(order[5]),Integer.parseInt(order[6]));
				mydraw.repaint();
				break;
			case "text":
				mydraw.g2.drawString(order[3],Integer.parseInt(order[4]),
						Integer.parseInt(order[5]));
				mydraw.repaint();
				break;
			case "clear":
				mydraw.clear();
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Manually close the connection
	void endConnection () {
		try {
			socket.close();
			input.close();
			output.close();
			System.out.println("conection ended!");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}

