package vnakhoa.vku.Client;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;

import vnakhoa.vku.Model.CallingMessenger;
import vnakhoa.vku.Model.UserCalling;

public class CallingClient extends JLayeredPane {
	
	JLabel lblImg;
	Socket socket;
	UserCalling calling;
	/**
	 * Create the panel.
	 * @throws Exception 
	 */
	public CallingClient(UserCalling calling) throws  Exception {
		this.calling = calling;
		setAutoscrolls(true);
		setMaximumSize(new Dimension(32767, 150));
		setPreferredSize(new Dimension(150, 150));
		setLayout(null);
		
		lblImg = new JLabel("New label");
		lblImg.setBounds(0, 0, 150, 150);
		lblImg.setOpaque(true);
		lblImg.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblImg);
		
		JLabel lblName = new JLabel("");
		setLayer(lblName, 300);
		lblName.setBounds(0, 114, 150, 36);
		lblName.setBorder(new EmptyBorder(5, 5, 5, 0));
		lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblName.setText(calling.getName());
		add(lblName);
		
		socket = new Socket(calling.getAddress(),Integer.parseInt(calling.getPort()));
		if (socket.isConnected()) {
			new ReceiveThread().start();
			
		} else {
			System.out.println("Ket noi that bai");
		}
	}
	
	public ImageIcon reIcon(ImageIcon path) {
		Image im = path.getImage().getScaledInstance(lblImg.getWidth(), lblImg.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon anh = new ImageIcon(im);
		return anh;
	}
	
	class ReceiveThread extends Thread {

		@Override
		public void run() {
			CallingMessenger calling = null;
			System.out.println("Ket noi thanh cong");
			try {
				while (true) {
					InputStream inputStream = socket.getInputStream();
					ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
					calling = (CallingMessenger) objectInputStream.readObject();
					lblImg.setIcon(reIcon(calling.getImg()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
