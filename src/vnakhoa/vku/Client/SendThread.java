package vnakhoa.vku.Client;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.stream.IIOByteBuffer;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import vnakhoa.vku.Model.CallingMessenger;
import vnakhoa.vku.Model.Messenger;

public class SendThread extends Thread{
	
	public Vector<Socket> listClient;
	public WebcamPanel webcam;

	public SendThread(Vector<Socket> listClient, WebcamPanel webcam) {
		super();
		this.listClient = listClient;
		this.webcam = webcam;
		start();
	}

	@Override
	public void run() {
		while (true) {
			if (listClient.size() > 0) {
				listClient.forEach((socket) -> {
					try {
						sendMessage(socket);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
		}
	}
	
	public void sendMessage(Socket socket) throws Exception {
        OutputStream ops = socket.getOutputStream();
        ObjectOutputStream ots = new ObjectOutputStream(ops);
        ots.writeObject(new CallingMessenger(IMG(webcam.getImage())));
        ots.flush();
    }
	
	private byte[] IMG(Image image) throws Exception {
		BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(image,0,0,null);
		g2.dispose();
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ImageIO.write(bi, "jpg", bStream);
		return bStream.toByteArray();
    }
    
}
