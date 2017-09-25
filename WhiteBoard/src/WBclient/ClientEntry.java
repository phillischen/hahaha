package WBclient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ClientEntry extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ClientSocket mysocket;
	private SVSocket serverSocket;
	private String host;
	private int port;

	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientEntry frame = new ClientEntry();
					//frame.host = args[0];
					//frame.port = Integer.parseInt(args[1]);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	 
	public ClientEntry() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Welcome to WhiteBoard!");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 15));
		lblNewLabel_1.setBounds(93, 60, 277, 15);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("Please choose an option");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 15));
		lblNewLabel.setBounds(93, 87, 265, 15);
		contentPane.add(lblNewLabel);
		
		JButton createCanvas = new JButton("I am a client");
		createCanvas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					//mysocket = new ClientSocket(host,port);
					dispose();
					//Canvas2 canvas = new Canvas2(mysocket);
					Canvas canvas = new Canvas();
					canvas.show();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(contentPane, "Cannot connect to the sever, please "
							+ "try again.");
					dispose();
				}							
			}
		});
		createCanvas.setBounds(90, 129, 209, 25);
		contentPane.add(createCanvas);
		
		JButton joinCanvas = new JButton("I am a user");
		joinCanvas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					//serverSocket = new SVSocket(port);
					dispose();
					Canvas canvas = new Canvas();
					canvas.loadimg();
					canvas.show();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		joinCanvas.setBounds(93, 181, 206, 25);
		contentPane.add(joinCanvas);
	}
}
