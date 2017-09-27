package clientUI;

import java.net.*;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

public class ClientSocket {

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private SocketDrawing mydraw;

	ClientSocket(String serveraddress, int port, SocketDrawing d) throws UnknownHostException, IOException {
		// server address and port is read from command line argument
		socket = new Socket(serveraddress, port);
		mydraw = d;

		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}

	void makeRequest(String message) {
			// package up the string order into an object

	}
	
	void makeRequest2(BufferedImage img) {
		// package up the image into an object

	}

	void getRequest() {
		try {

			
			 unpackage the object, to string /image 
			 
			 if in the object, image != null: mydraw.paintImg((BufferedImg)img);
			 else if string != null: do below
			 

			String[] order = input.readUTF().split(",");

			if (order[0].equals("canvas")) { //when the order is about drawing
				int r = Integer.parseInt(order[2]);
				int g = Integer.parseInt(order[3]);
				int b = Integer.parseInt(order[4]);
				mydraw.cc(new Color(r, g, b));

				switch (order[1]) {
				case "line":
					mydraw.g2.drawLine(Integer.parseInt(order[5]), Integer.parseInt(order[6]),
							Integer.parseInt(order[7]), Integer.parseInt(order[8]));
					mydraw.repaint();
					break;
				case "rect":
					mydraw.g2.drawRect(Integer.parseInt(order[5]), Integer.parseInt(order[6]),
							Integer.parseInt(order[7]), Integer.parseInt(order[8]));
					mydraw.repaint();
					break;
				case "oval":
					mydraw.g2.drawOval(Integer.parseInt(order[5]), Integer.parseInt(order[6]),
							Integer.parseInt(order[7]), Integer.parseInt(order[8]));
					mydraw.repaint();
					break;
				case "circle":
					mydraw.g2.drawOval(Integer.parseInt(order[5]), Integer.parseInt(order[6]),
							Integer.parseInt(order[7]), Integer.parseInt(order[8]));
					mydraw.repaint();
					break;
				case "eraser":
					mydraw.g2.fillOval(Integer.parseInt(order[5]), Integer.parseInt(order[6]),
							Integer.parseInt(order[7]), Integer.parseInt(order[8]));
					mydraw.repaint();
					break;
				case "text":
					mydraw.g2.drawString(order[5], Integer.parseInt(order[6]), Integer.parseInt(order[7]));
					mydraw.repaint();
					break;
				case "clear":
					mydraw.clear();
					break;
				case "fetch":
					mydraw.save();
				}
			}
			else if (order[0].equals("canvas")){
				//tianqi's part
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//use if you need!
	void endConnection() {
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