package vnakhoa.vku.Model;


import java.io.Serializable;

import javax.swing.ImageIcon;

public class CallingMessenger implements Serializable {
	private ImageIcon img;

	public CallingMessenger(ImageIcon img) {
		super();
		this.img = img;
	}

	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
	
}
