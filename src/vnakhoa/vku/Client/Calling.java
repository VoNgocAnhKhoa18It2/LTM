package vnakhoa.vku.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import vnakhoa.vku.Model.CallingMessenger;
import vnakhoa.vku.Model.Messenger;
import vnakhoa.vku.Server.Event;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Component;

public class Calling extends JFrame {

	private JPanel contentPane;
	Webcam webCam;
	public static JPanel pnlClient;
	public Vector<Socket> listClient = new Vector<Socket>();
	private JLayeredPane layeredPane;
	private JPanel pnlChatVideo;
	private JLabel lblNewLabel;
	WebcamPanel webcamPanel;
	Socket client;
	
	
	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public Calling(Socket socket,int port) {
		this.client = socket;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 833, 550);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 245, 245));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		layeredPane = new JLayeredPane();
		contentPane.add(layeredPane,BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		layeredPane.setLayer(panel, 300);
		panel.setVisible(false);
		layeredPane.setLayout(new BorderLayout(0, 0));
		panel.setPreferredSize(new Dimension(300, 300));
		layeredPane.add(panel,BorderLayout.CENTER,JLayeredPane.POPUP_LAYER);
		FlowLayout fl_panel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		fl_panel.setAlignOnBaseline(true);
		panel.setLayout(fl_panel);
		
		lblNewLabel = new JLabel("New label");
		panel.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		scrollPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.add(scrollPane, BorderLayout.EAST);
		
		
		webCam = Webcam.getDefault();
		webCam.setViewSize(WebcamResolution.VGA.getSize());

		webcamPanel = new WebcamPanel(webCam);
		webcamPanel.setImageSizeDisplayed(true);
		webcamPanel.setFPSDisplayed(true);
		webcamPanel.setMirrored(true);
		webcamPanel.setDisplayDebugInfo(true);
		
		webcamPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				panel.setVisible(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panel.setVisible(false);
			}
		});
		layeredPane.add(webcamPanel,BorderLayout.CENTER,JLayeredPane.DEFAULT_LAYER);
		
		pnlClient = new JPanel();
		pnlClient.setPreferredSize(new Dimension(320,webcamPanel.getHeight()));
		pnlClient.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		scrollPane.setViewportView(pnlClient);
		pnlClient.setLayout(new GridLayout(0, 2, 5, 5));
		new ServerThread(port);
	}
	
	class ServerThread extends Thread {
		ServerSocket serverSocket;
		public ServerThread(int port) {
			try {
				serverSocket = new ServerSocket(port);
				start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		@Override
		public void run() {
			try {	
				while(true) {
					Socket client = serverSocket.accept(); 
					System.out.println("Co nguoi ket noi");
					SendClient sendClient = new SendClient(client);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class SendClient extends Thread{
		Socket socket;
		
		public SendClient(Socket socket) {
			super();
			this.socket = socket;
			start();
		}

		@Override
		public void run() {
			boolean loop = true;
			while (loop) {
				try {
					sendMessage(socket);
				} catch (Exception e) {
					if (socket == null) {
						loop = false;
					}
					loop = false;
					e.printStackTrace();
				}
			}
		}
		
		public void sendMessage(Socket socket) throws Exception {
	        OutputStream ops = socket.getOutputStream();
	        ObjectOutputStream ots = new ObjectOutputStream(ops);
	        ots.writeObject(new CallingMessenger(webcamPanel.getImage()));
	        ots.flush();
	    }
		
		private byte[] IMG(Image image) {
			try {
				BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = bi.createGraphics();
				g2.drawImage(image,0,0,null);
				g2.dispose();
				ByteArrayOutputStream bStream = new ByteArrayOutputStream();
				ImageIO.write(bi, "jpg", bStream);
				return bStream.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	    }
	}
}
