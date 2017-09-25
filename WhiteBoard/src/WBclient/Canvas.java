package WBclient;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Canvas {

  JButton clearBtn, fdrawBtn, blueBtn, eraserBtn, redBtn, rectBtn;
  Drawing myDraw;
  //SVSocket serverSocket;
  BufferedImage img;
  ActionListener actionListener = new ActionListener() {

  public void actionPerformed(ActionEvent e) {
      if (e.getSource() == clearBtn) {
        myDraw.clear();
      } else if (e.getSource() == fdrawBtn) {
    	  myDraw.black();
    	  myDraw.freedDraw();
      } else if (e.getSource() == blueBtn) {
        //myDraw.blue();
    	 
    	 try {
    		File fl = new File("img.png");
			ImageIO.write((BufferedImage)myDraw.save(), "png", fl);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	 
    	/*
    	  FileOutputStream fos;
		try {
			Image tosave;
			tosave = myDraw.save();
			fos = new FileOutputStream("img");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(tosave);
			oos.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
			
      } else if (e.getSource() == eraserBtn) {
        //myDraw.eraser(20);
    	  BufferedImage img;
    	  try {
    		
			img = ImageIO.read(new File("img.png"));
			myDraw.paintImg(img);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //System.out.println("eraser done");
      } else if (e.getSource() == redBtn) {
        myDraw.red();
      } else if (e.getSource() == rectBtn) {
    	  myDraw.black();
    	  myDraw.drawText("MMP!");
      }
    }
  };
  /*
  public static void main(String[] args) {
    new Canvas().show();
  }
  public Canvas(SVSocket sk) {
	  serverSocket = sk;
	  
  }
  
	*/
  public Canvas() {
	 	  
  }
  
  void loadimg() {
	  try {
		
		img = ImageIO.read(new File("img.png"));
		//clientSocket.makeRequest(img);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();}
  }
  
  void show() {
    // create main frame
    JFrame frame = new JFrame("My canvas");
    Container content = frame.getContentPane();
    // set layout on content pane
    content.setLayout(new BorderLayout());
    // create draw area
    
    myDraw = new Drawing(img);

    // add to content pane
    content.add(myDraw, BorderLayout.CENTER);

    // create controls to apply colors and call clear feature
    JPanel controls = new JPanel();

    clearBtn = new JButton("Clear");
    clearBtn.addActionListener(actionListener);
    fdrawBtn = new JButton("Free draw");
    fdrawBtn.addActionListener(actionListener);
    blueBtn = new JButton("Save");
    blueBtn.addActionListener(actionListener);
    eraserBtn = new JButton("Eraser");
    eraserBtn.addActionListener(actionListener);
    redBtn = new JButton("Red");
    redBtn.addActionListener(actionListener);
    rectBtn = new JButton("Rectangle");
    rectBtn.addActionListener(actionListener);

    // add to panel
    controls.add(eraserBtn);
    controls.add(blueBtn);
    controls.add(fdrawBtn);
    controls.add(redBtn);
    controls.add(rectBtn);
    controls.add(clearBtn);

    // add to content pane
    content.add(controls, BorderLayout.NORTH);

    frame.setSize(600, 600);
    // can close frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // show the swing paint result
    frame.setVisible(true);

    // Now you can try our Swing Paint !!! Enjoy 😀
  }

}