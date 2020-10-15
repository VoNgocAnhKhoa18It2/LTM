package vnakhoa.vku.Client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.github.sarxos.webcam.Webcam;

import vnakhoa.vku.Model.CallingMessenger;
import vnakhoa.vku.Model.Messenger;

public class SendThread extends Thread{
	
	public Vector<Socket> listClient;
	public Webcam webcam;

	public SendThread(Vector<Socket> listClient, Webcam webcam) {
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
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		}
	}
	
	public void sendMessage(Socket socket) throws IOException {
        OutputStream ops = socket.getOutputStream();
        ObjectOutputStream ots = new ObjectOutputStream(ops);
        ots.writeObject(new CallingMessenger(IMG(webcam.getImage())));
        ots.flush();
        
    }
	
	private byte [] IMG(Image image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(getBufferedImage(image), "JPEG", baos);
        } catch (IOException ex) {
            //handle it here.... not implemented yet...
        }
        return baos.toByteArray();
    }
     
    private BufferedImage getBufferedImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //Graphics2D g2d = bi.createGraphics();
        //g2d.drawImage(image, 0, 0, null);
        return bi;
    }
}
