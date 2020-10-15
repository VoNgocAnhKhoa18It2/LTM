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

public class CallingClient extends JLayeredPane {
	
	JLabel lblImg;
	Socket socket;
	/**
	 * Create the panel.
	 * @throws Exception 
	 */
	public CallingClient(String address, int port,String name) throws  Exception {
		setAutoscrolls(true);
		setMaximumSize(new Dimension(32767, 150));
		setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.CYAN, Color.GREEN));
		setPreferredSize(new Dimension(150, 150));
		setLayout(null);
		
		lblImg = new JLabel("New label");
		lblImg.setBounds(0, 0, 150, 150);
		lblImg.setOpaque(true);
		lblImg.setBackground(Color.RED);
		lblImg.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblImg);
		
		JLabel lblName = new JLabel("khoa");
		setLayer(lblName, 300);
		lblName.setBounds(0, 114, 150, 36);
		lblName.setBorder(new EmptyBorder(5, 5, 5, 0));
		lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblName.setText(name);
		add(lblName);
		
		socket = new Socket(address,port);
		ReceiveThread receiveThread = new ReceiveThread();
		receiveThread.start();
	}
	
	public ImageIcon reIcon(Image path) {
		ImageIcon img = new ImageIcon(path);
		Image im = img.getImage().getScaledInstance(lblImg.getWidth(), lblImg.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon anh = new ImageIcon(im);
		return anh;
	}
	
	class ReceiveThread extends Thread {

		@Override
		public void run() {
			try {
				while (true) {
					InputStream inputStream = socket.getInputStream();
					ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
					CallingMessenger calling  = (CallingMessenger) objectInputStream.readObject();
					lblImg.setIcon(reIcon(calling.getImg()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
