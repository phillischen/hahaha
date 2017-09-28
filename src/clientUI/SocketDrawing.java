package clientUI;


import clientUI.DrawerFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import javax.imageio.ImageIO;

import javax.swing.JComponent;
import org.json.JSONException;
import org.json.JSONObject;


public class SocketDrawing extends JComponent {

	private static final long serialVersionUID = 1L;
	private static Image image;
	// Graphics2D object ==> used to draw on
	protected static Graphics2D g2;
	// Mouse coordinates
	private int currentX, currentY, oldX, oldY;
	private MouseListener ml;
	private MouseMotionListener mml;
	private String currentColor;
	DrawerFrame client;

	
	SocketDrawing(BufferedImage loadimg, DrawerFrame sk) throws IOException {
		paintImg(loadimg);
                //paintImg(ImageIO.read(new File("img.png")));
		client = sk;
		black();
	}
        
	SocketDrawing(DrawerFrame sk) throws IOException {
		client = sk;
                paintImg(ImageIO.read(new File("img.png")));
                clear();
                //paintComponent(g2);
		black();
	}

        public static Graphics2D getGraphics2D(){
            return g2;
        }
        
	protected void paintComponent(Graphics g) {
		if (image == null) {
			// image to draw null ==> we create
			image = createImage(getSize().width, getSize().height);
			g2 = (Graphics2D) image.getGraphics();
			// enable antialiasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// clear draw area
			clear();
		}/*
		else {
			g2 = (Graphics2D) image.getGraphics();
			// enable antialiasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		*/
		g.drawImage(image, 0, 0, null);
	}
	
	protected static Image save() {
		return image;
	}
	
	protected void paintImg (BufferedImage loadimg) {
		//g2 = (Graphics2D) loadimg.getGraphics();
		image = loadimg;
		repaint();
		g2 = (Graphics2D) image.getGraphics();
		// enable antialiasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		black();
	}

	protected void freedDraw() {
		setDoubleBuffered(false);
		removeMouseListener(ml);
		removeMouseMotionListener(mml);

		ml = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				// save coord x,y when mouse is pressed
				oldX = e.getX();
				oldY = e.getY();
			}
		};
		addMouseListener(ml);

		mml = new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				// coord x,y when drag mouse
				currentX = e.getX();
				currentY = e.getY();

				if (g2 != null) {
					// draw line if g2 context not null
					//g2.drawLine(oldX, oldY, currentX, currentY);
                                        client.getArgument(argumentList("line", currentColor, oldX, oldY, currentX, currentY, 0, "", ""));
					//clientSocket.makeRequest("canvas,line,"+currentColor+","+oldX+","+oldY+","+currentX+","+currentY);
					// refresh draw area to repaint
					//repaint();
					// store current coords x,y as olds x,y
					oldX = currentX;
					oldY = currentY;
				}
			}
		};
		addMouseMotionListener(mml);

	}
	
	public void rectangle() {
		// int x = getDistance();
		removeMouseListener(ml);
		removeMouseMotionListener(mml);
		// setDoubleBuffered(false);
		ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				oldX = e.getX();
				oldY = e.getY();
				System.out.println("PRE = " + oldX + ", " + oldY);
			}

			public void mouseReleased(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				System.out.println("REALEASE = " + currentX + ", " + currentY);
                                client.getArgument(argumentList("rect", currentColor, oldX, oldY, currentX, currentY, 0, "", ""));
				//clientSocket.makeRequest("canvas,rect,"+currentColor+","+oldX+","+oldY+","+
				//Math.abs(currentX - oldX)+","+Math.abs(currentY - oldY));
				//g2.drawRect(oldX, oldY, Math.abs(currentX - oldX), Math.abs(currentY - oldY));
				//repaint();
				System.out.println("rectangle repainted");
			}
		};

		addMouseListener(ml);

	}

	protected void oval() {

		removeMouseListener(ml);
		removeMouseMotionListener(mml);
		// setDoubleBuffered(false);
		ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				oldX = e.getX();
				oldY = e.getY();
				System.out.println("PRE = " + oldX + ", " + oldY);
			}

			public void mouseReleased(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				System.out.println("REALEASE = " + currentX + ", " + currentY);
                                client.getArgument(argumentList("oval", currentColor, oldX, oldY, currentX, currentY, 0, "", ""));
				//clientSocket.makeRequest("canvas,oval,"+currentColor+","+oldX+","+oldY+","+
				//		Math.abs(currentX - oldX)+","+Math.abs(currentY - oldY));
				//g2.drawOval(oldX, oldY, Math.abs(currentX - oldX), Math.abs(currentY - oldY));
				//repaint();
				System.out.println("oval repainted");
			}
		};

		addMouseListener(ml);

	}

	protected void circle() {

		removeMouseListener(ml);
		removeMouseMotionListener(mml);
		// setDoubleBuffered(false);
		ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				oldX = e.getX();
				oldY = e.getY();
				System.out.println("PRE = " + oldX + ", " + oldY);
			}

			public void mouseReleased(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				int diameter = Math.max(Math.abs(currentX - oldX), Math.abs(currentY - oldY));
				System.out.println("REALEASE = " + currentX + ", " + currentY);
                                client.getArgument(argumentList("circle", currentColor, oldX, oldY, currentX, currentY, 0, "", ""));
				//clientSocket.makeRequest("canvas,circle,"+currentColor+","+oldX+","+oldY+","+
				//		diameter+","+diameter);
				//g2.drawOval(oldX, oldY, diameter, diameter);
				//repaint();
				System.out.println("repainted");
			}
		};

		addMouseListener(ml);

	}

	protected void straightLine() {

		removeMouseListener(ml);
		removeMouseMotionListener(mml);
		// setDoubleBuffered(false);
		ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				oldX = e.getX();
				oldY = e.getY();
				System.out.println("PRE = " + oldX + ", " + oldY);
			}

			public void mouseReleased(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				System.out.println("REALEASE = " + currentX + ", " + currentY);
				client.getArgument(argumentList("line", currentColor, oldX, oldY, currentX, currentY, 0, "", ""));
                                //clientSocket.makeRequest("canvas,line,"+currentColor+","+oldX+","+oldY+","+
				//		currentX+","+currentY);
				//g2.drawLine(oldX, oldY, currentX, currentY);
				//repaint();
				System.out.println("repainted");
			}
		};

		addMouseListener(ml);

	}

	protected void eraser(int size) {
		removeMouseListener(ml);
		removeMouseMotionListener(mml);

		ml = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				// save coord x,y when mouse is pressed
				oldX = e.getX();
				oldY = e.getY();
			}
		};
		addMouseListener(ml);

		mml = new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				// coord x,y when drag mouse
				currentX = e.getX();
				currentY = e.getY();
				g2.setPaint(Color.white);
				currentColor = "255,255,255";
				if (g2 != null) {
                                        client.getArgument(argumentList("eraser", currentColor, oldX, oldY, 0, 0, size, "", ""));
					//clientSocket.makeRequest("canvas,eraser,"+currentColor+","+oldX+","+oldY+","+size+","+size);
					//g2.fillOval(oldX, oldY, size, size);
					
					//repaint();
					
					oldX = currentX;
					oldY = currentY;
				}
			}
		};
		addMouseMotionListener(mml);

	}

	protected void drawText(String input) {
		removeMouseListener(ml);

		ml = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				// save coord x,y when mouse is pressed
				oldX = e.getX();
				oldY = e.getY();
                                
                                client.getArgument(argumentList("text", currentColor, oldX, oldY, 0, 0, 0, input, ""));
				//clientSocket.makeRequest("canvas,text,"+currentColor+","+input+","+oldX+","+oldY);
				//g2.drawString(input, oldX, oldY);
				//repaint();
				System.out.println("draw text repaint");

			}
		};

		addMouseListener(ml);
	}

	// now we create exposed methods
	public void clear() {
		g2.setPaint(Color.white);
		// draw white on entire draw area to clear
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(Color.black);
		currentColor = "0,0,0";
		repaint();
	}
	
	public void cc(Color color) {
		g2.setPaint(color);
		String x = color.getRed()+","+color.getGreen()+","+color.getBlue();
                currentColor = x;
		System.out.println("RBG:"+x);
		
		
	}

	public void black() {
		g2.setPaint(Color.black);
		currentColor = "0,0,0";
	}


	public void white() {
		g2.setPaint(Color.white);
		currentColor = "255,255,255";
	}

        
        public static JSONObject argumentList(String method, String color, int oldX, int oldY, int currentX, int currentY, int size, String inputText, String image){
            JSONObject command = new JSONObject();
            JSONObject arguments = new JSONObject();

            try {
                    arguments.put("type", "canvas");

                    arguments.put("color", color);
                    arguments.put("oldX", oldX);
                    arguments.put("oldY", oldY);
                    arguments.put("currentX", currentX);
                    arguments.put("currentY", currentY);
                    arguments.put("size", size);
                    arguments.put("inputText", inputText);
                    
                    command.put("image", image);
                    command.put("kind", "oldClient");
                    command.put("method", method);
                    command.put("arguments", arguments);

            } catch (JSONException e) {
                System.err.println(e.getMessage());
            }
            return command;
        }
        

}