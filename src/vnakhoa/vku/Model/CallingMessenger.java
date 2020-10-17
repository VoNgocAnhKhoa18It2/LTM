package vnakhoa.vku.Model;

import java.awt.Image;
import java.io.Serializable;

public class CallingMessenger implements Serializable {
	private Image img;

	public CallingMessenger(Image img) {
		super();
		this.img = img;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}
	
}
