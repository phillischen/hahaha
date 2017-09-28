package clientUI;

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
import javax.imageio.ImageIO;

import javax.swing.JComponent;

public class SingleDrawing extends JComponent {

	private static final long serialVersionUID = 1L;
	private Image image;
	// Graphics2D object ==> used to draw on
	private Graphics2D g2;
	// Mouse coordinates
	private int currentX, currentY, oldX, oldY;
	private MouseListener ml;
	private MouseMotionListener mml;
	//private SVSocket serverSocket;

	
	public SingleDrawing(BufferedImage loadimg) {
		paintImg(loadimg);
		black();
	}

	public SingleDrawing(){
            try {
                paintImg(ImageIO.read(new File("img.png")));
                black();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
		
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
					//serverSocket.makeRequest("FD,"+oldX+","+oldY+","+currentX+","+currentY);
					// refresh draw area to repaint
                                        drawFreeDraw(oldX, oldY, currentX, currentY);
					//repaint();
					// store current coords x,y as olds x,y
					oldX = currentX;
					oldY = currentY;
				}
			}
		};
		addMouseMotionListener(mml);

	}
        
        protected void drawFreeDraw(int _oldX, int _oldY, int _currentX, int _currentY){
            
            g2.drawLine(_oldX, _oldY, _currentX, _currentY);
            repaint();
            System.out.println("repainted");
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
				//g2.drawRect(oldX, oldY, Math.abs(currentX - oldX), Math.abs(currentY - oldY));
				//repaint();
				//System.out.println("repainted");
                                drawRectangle(oldX, oldY, currentX, currentY);
			}
		};

		addMouseListener(ml);

	}
        
        protected void drawRectangle(int _oldX, int _oldY, int _currentX, int _currentY){
            
            g2.drawRect(_oldX, _oldY, Math.abs(_currentX - _oldX), Math.abs(_currentY - _oldY));
            repaint();
            System.out.println("Rect repainted");
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
				//g2.drawOval(oldX, oldY, Math.abs(currentX - oldX), Math.abs(currentY - oldY));
				//repaint();
				//System.out.println("repainted");
                                drawOval(oldX, oldY, currentX, currentY);
			}
		};

		addMouseListener(ml);

	}
        
        protected void drawOval(int _oldX, int _oldY, int _currentX, int _currentY){
            
            g2.drawOval(_oldX, _oldY, Math.abs(_currentX - _oldX), Math.abs(_currentY - _oldY));
            repaint();
            System.out.println("repainted");
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
				//int diameter = Math.max(Math.abs(currentX - oldX), Math.abs(currentY - oldY));
				System.out.println("REALEASE = " + currentX + ", " + currentY);
                                drawCircle(oldX, oldY, currentX, currentY);
				//g2.drawOval(oldX, oldY, diameter, diameter);
				//repaint();
				//System.out.println("repainted");
			}
		};

		addMouseListener(ml);

	}
        
        protected void drawCircle(int _oldX, int _oldY, int _currentX, int _currentY){
            
            int diameter = Math.max(Math.abs(_currentX - _oldX), Math.abs(_currentY - _oldY));
            g2.drawOval(_oldX, _oldY, diameter, diameter);
            repaint();
            System.out.println("repainted");
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
                                drawStraightLine(oldX, oldY, currentX, currentY);
			}
		};

		addMouseListener(ml);

	}
        
        protected void drawStraightLine(int _oldX, int _oldY, int _currentX, int _currentY){
            
            g2.drawLine(_oldX, _oldY, _currentX, _currentY);
            repaint();
            System.out.println("repainted");
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
					// draw line if g2 context not null
					//g2.fillOval(oldX, oldY, size, size);
					// refresh draw area to repaint
					//repaint();
                                    toErase(oldX, oldY, size);
					// store current coords x,y as olds x,y
					oldX = currentX;
					oldY = currentY;
				}
			}
		};
		addMouseMotionListener(mml);

	}
        
        protected void toErase(int _oldX, int _oldY, int _size){
            g2.setPaint(Color.white);
            g2.fillOval(_oldX, _oldY, _size, _size);
            repaint();
        }

	protected void drawText(String input) {
		removeMouseListener(ml);

		ml = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				// save coord x,y when mouse is pressed
				oldX = e.getX();
				oldY = e.getY();
				//g2.drawString(input, oldX, oldY);
				//repaint();
				//System.out.println("draw text repaint");
                                toText(oldX, oldY, input);

			}
		};

		addMouseListener(ml);
	}
        
        protected void toText(int _oldX, int _oldY, String _input){
            g2.drawString(_input, _oldX, _oldY);
            repaint();
            System.out.println("draw text repaint");
        }

	// now we create exposed methods
	public void clear() {
		g2.setPaint(Color.white);
		// draw white on entire draw area to clear
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(Color.black);
		repaint();
	}

	public void cc(Color color) {
		g2.setPaint(color);
		String x = color.getRed()+","+color.getGreen()+","+color.getBlue();
		System.out.println(x);
		
		
	}
	public void red() {
		g2.setPaint(Color.red);
	}

	public void darkred() {
		g2.setPaint(Color.red.darker());
	}

	public void black() {
		g2.setPaint(Color.black);
		 
	}

	public void green() {
		g2.setPaint(Color.green);
	}

	public void darkgreen() {
		g2.setPaint(Color.green.darker());
	}

	public void blue() {
		g2.setPaint(Color.blue);
	}

	public void darkblue() {
		g2.setPaint(Color.blue.darker());
	}

	public void lightgrey() {
		g2.setPaint(Color.lightGray);
	}

	public void magenta() {
		g2.setPaint(Color.magenta);
	}

	public void orange() {
		g2.setPaint(Color.orange);
	}

	public void darkorange() {
		g2.setPaint(Color.orange.darker());
	}

	public void pink() {
		g2.setPaint(Color.pink);
	}

	public void darkpink() {
		g2.setPaint(Color.pink.darker());
	}

	public void white() {
		g2.setPaint(Color.white);
	}

	public void yellow() {
		g2.setPaint(Color.yellow);
	}

	public void darkyellow() {
		g2.setPaint(Color.yellow.darker());
	}

}
