package WBclient;

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

import javax.swing.JComponent;

public class Drawing2 extends JComponent {

	private static final long serialVersionUID = 1L;
	private Image image;
	// Graphics2D object ==> used to draw on
	protected Graphics2D g2;
	// Mouse coordinates
	private int currentX, currentY, oldX, oldY;
	private MouseListener ml;
	private MouseMotionListener mml;
	private String currentColor;
	ClientSocket clientSocket;

	
	public Drawing2(BufferedImage loadimg, ClientSocket sk) {
		paintImg(loadimg);
		clientSocket = sk;
	}
	

	public Drawing2(ClientSocket sk) {
		clientSocket = sk;
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
	
	protected Image save() {
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
					clientSocket.makeRequest("canvas,line,"+currentColor+","+oldX+","+oldY+","+
					currentX+","+currentY);
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
				clientSocket.makeRequest("canvas,rect,"+currentColor+","+oldX+","+oldY+","+
				Math.abs(currentX - oldX)+","+Math.abs(currentY - oldY));
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
				clientSocket.makeRequest("canvas,oval,"+currentColor+","+oldX+","+oldY+","+
						Math.abs(currentX - oldX)+","+Math.abs(currentY - oldY));
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
				clientSocket.makeRequest("canvas,circle,"+currentColor+","+oldX+","+oldY+","+
						diameter+","+diameter);
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
				clientSocket.makeRequest("canvas,line,"+currentColor+","+oldX+","+oldY+","+
						currentX+","+currentY);
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
				if (g2 != null) {
					clientSocket.makeRequest("canvas,eraser,white,"+oldX+","+oldY+","+size+","+size);
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
				clientSocket.makeRequest("canvas,text,"+currentColor+","+input+","+oldX+","+oldY);
				//g2.drawString(input, oldX, oldY);
				//repaint();
				System.out.println("draw text repaint");

			}
		};

		addMouseListener(ml);
	}

	// now we create exposed methods
	protected void clear() {
		g2.setPaint(Color.white);
		// draw white on entire draw area to clear
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(Color.black);
		currentColor = "black";
		repaint();
	}

	public void red() {
		g2.setPaint(Color.red);
		currentColor = "red";
	}

	public void darkred() {
		g2.setPaint(Color.red.darker());
		currentColor = "darkred";
	}

	public void black() {
		g2.setPaint(Color.black);
		currentColor = "black";
	}

	public void green() {
		g2.setPaint(Color.green);
		currentColor = "green";
	}

	public void darkgreen() {
		g2.setPaint(Color.green.darker());
		currentColor = "darkgreen";
	}

	public void blue() {
		g2.setPaint(Color.blue);
		currentColor = "blue";
	}

	public void darkblue() {
		g2.setPaint(Color.blue.darker());
		currentColor = "darkblue";
	}

	public void lightgray() {
		g2.setPaint(Color.lightGray);
		currentColor = "lightgray";
	}

	public void magenta() {
		g2.setPaint(Color.magenta);
		currentColor = "magenta";
	}

	public void orange() {
		g2.setPaint(Color.orange);
		currentColor = "orange";
	}

	public void darkorange() {
		g2.setPaint(Color.orange.darker());
		currentColor = "darkorange";
	}

	public void pink() {
		g2.setPaint(Color.pink);
		currentColor = "pink";
	}

	public void darkpink() {
		g2.setPaint(Color.pink.darker());
		currentColor = "darkpink";
	}

	public void white() {
		g2.setPaint(Color.white);
		currentColor = "white";
	}

	public void yellow() {
		g2.setPaint(Color.yellow);
		currentColor = "yellow";
	}

	public void darkyellow() {
		g2.setPaint(Color.yellow.darker());
		currentColor = "darkyellow";
	}

}