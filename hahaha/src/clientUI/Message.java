package clientUI;

import java.awt.Image;
import java.io.Serializable;

class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String order;
	private Image img;
	
	Message(String ord){
		this.order= ord;
	}
	Message(Image img){
		this.img= img;
	}
}
