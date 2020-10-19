package vnakhoa.vku.Client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BoxView;
import javax.swing.tree.DefaultMutableTreeNode;

import vnakhoa.vku.Model.Messenger;
import vnakhoa.vku.Model.User;
import vnakhoa.vku.Server.Event;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ScrollPaneConstants;
import javax.swing.ImageIcon;
import java.awt.SystemColor;
import javax.swing.JLayeredPane;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Chat extends JPanel {
	
	private JTextField txtContent;
	public static JPanel mess;
	Socket socket;
	ArrayList<String> limited;
	int count = 0;
	public static JButton btnCall;
	
	public ImageIcon reIcon(String path, JComponent obj ) {
		ImageIcon img = new ImageIcon(path);
		Image im = img.getImage().getScaledInstance(obj.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon anh = new ImageIcon(im);
		return anh;
	}
	
	public String filedialog() {
		String filename = null;
		try {
			FileDialog fd = new FileDialog(new Dialog(new Frame() ), "Open", FileDialog.LOAD);
			File file1 = null;
			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		    File[] roots = fileSystemView.getRoots();
		    for (File fileSystemRoot : roots) {
		        DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
		        file1 = (File) node.getUserObject();
		        File[] files = fileSystemView.getFiles(file1, true); 
		    }
		    fd.setDirectory(file1.getAbsolutePath());
			fd.setVisible(true);
			filename = fd.getDirectory()+fd.getFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filename;
	}
	
	private void addDataLimited() {
		limited = new ArrayList<String>();
		limited.add("cc");
		limited.add("vl");
		limited.add("cl");
		limited.add("đm");
		limited.add("đéo");
		limited.add("dm");
		limited.add("lol");
	}
	
	private String checkLimited(String s) {
		int i = 0;
		String[] arr = s.split(" ");
		s = "";
		for (int j = 0; j < arr.length; j++) {
			if (limited.indexOf(arr[j].toLowerCase()) != -1) {
				arr[j] = String.join("", Collections.nCopies(limited.get(limited.indexOf(arr[j].toLowerCase())).length(), "*"));
				i++;
			}
			s = s+" "+arr[j];
		}
		if (i != 0) {
			count= count + 1;
			switch (count) {
			case 1:
				Client.lblVP.setForeground(new Color(255, 204, 51));
				Client.lblVP.setText(String.valueOf(count));
				JOptionPane.showMessageDialog(null, "Bạn đã lần "+count+" vi phạm văn hóa chat room", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
				break;
			case 2:
				Client.lblVP.setForeground(new Color(255, 204, 51));
				Client.lblVP.setText(String.valueOf(count));
				JOptionPane.showMessageDialog(null, "Bạn đã lần "+count+" vi phạm văn hóa chat room", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
				break;
			case 3:
				Client.lblVP.setForeground(new Color(255, 0, 0));
				Client.lblVP.setText(String.valueOf(count));
				JOptionPane.showMessageDialog(null, "Bạn đã lần "+count+" vi phạm văn hóa chat room", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
				break;
			}
		}		
		return s.trim();
	}
	
	public Chat(User user) {
		setBackground(new Color(245, 245, 245));
		Client.lblVP.setText(String.valueOf(count));
		
		addDataLimited();
		this.socket = user.getSocket();
		setAutoscrolls(true);
		setLayout(new BorderLayout(0, 0));
		
		JLayeredPane panel = new JLayeredPane();
		panel.setBackground(new Color(245, 245, 245));
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnSend = new JButton("");
		btnSend.setBackground(new Color(245, 245, 245));
		btnSend.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSend.setPreferredSize(new Dimension(33, 10));
		btnSend.setIconTextGap(1);
		btnSend.setIcon(new ImageIcon("img\\icons8-email-send-25.png"));
		btnSend.setBorder(null);
		btnSend.setBounds(395, 398, 30, 27);
		panel.add(btnSend, JLayeredPane.DEFAULT_LAYER);
		
		txtContent = new JTextField();
		txtContent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
					String content = txtContent.getText().trim();
					if (content.length() > 0) {
						try {
							String s = checkLimited(content);
							if (count > 3) {
								reportServer(user.getName());
							} else {
								Messenger messenger = new Messenger("", s);
								addMessenger(messenger);
								Client.check = true;
								ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
								objectOutputStream.writeObject(new Messenger(user.getName(), s));
								objectOutputStream.flush();
								txtContent.setText("");
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} 
	            }
			}
		});
		txtContent.setBorder(null);
		txtContent.setBackground(Color.WHITE);
		txtContent.setBounds(0, 398, 392, 27);
		panel.add(txtContent,JLayeredPane.DEFAULT_LAYER);
		txtContent.setColumns(10);
		
		JScrollPane scrolIcon = new JScrollPane();
		scrolIcon.setPreferredSize(new Dimension(200, 200));
		scrolIcon.setMaximumSize(new Dimension(200, 200));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(526, 387));
		scrollPane.setOpaque(false);
		scrollPane.setBounds(0, 0, 526, 387);
		panel.add(scrollPane,JLayeredPane.DEFAULT_LAYER);
		
		mess = new JPanel();
		mess.setBackground(new Color(255, 255, 255));
		mess.setLayout(new BoxLayout(mess, BoxLayout.PAGE_AXIS));
		
		scrollPane.setViewportView(mess);
		
		scrolIcon.setOpaque(true);
		scrolIcon.setVisible(false);
		scrolIcon.setBorder(null);
		scrolIcon.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrolIcon.setBounds(0, 0, 200, 200);
		panel.add(scrolIcon,JLayeredPane.POPUP_LAYER);
		
		JPanel pnlIcon = new JPanel();
		pnlIcon.setAutoscrolls(true);
		pnlIcon.setBorder(new LineBorder(new Color(245, 245, 245), 5));
		scrolIcon.setViewportView(pnlIcon);
		pnlIcon.setBackground(new Color(245, 245, 245));
		pnlIcon.setLayout(new GridLayout(0, 3, 5, 5));
		
		addIcon(pnlIcon,user.getName());
		
		JButton btnsendIcon = new JButton("");
		btnsendIcon.setBounds(427, 398, 30, 27);
		panel.add(btnsendIcon);
		btnsendIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (scrolIcon.isVisible()) {
						scrolIcon.setVisible(false);
						return;
					}
					JComponent source = (JComponent) arg0.getSource();
					scrolIcon.setLocation(new Point(source.getX()- scrolIcon.getWidth()/2 -10,
			                  source.getY() - scrolIcon.getHeight()-10));
					scrolIcon.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		btnsendIcon.setIcon(new ImageIcon("img\\icons8-smiling-25.png"));
		btnsendIcon.setBackground(new Color(245, 245, 245));
		btnsendIcon.setBorder(null);
		
		JButton btngim = new JButton("");
		btngim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					JFileChooser chooser = new JFileChooser(System.getProperty("user.home")+"/Desktop");
					int result = chooser.showOpenDialog(null);
					if (result == chooser.APPROVE_OPTION) { 
						File file = chooser.getSelectedFile();
						if (file.exists()) {
							if (file.length() / 1024 > 50) {
								JOptionPane.showMessageDialog(null, "File đã vượt giới han dung lương quy định");
								return;
							}
							FileInputStream imgg = new FileInputStream(file);
    						byte i[] = new byte[(int) file.length()];
    						imgg.read(i, 0, (int) file.length());
    						Messenger messenger = new Messenger("",file.getName(),i );
    						addMessenger(messenger);
    						ObjectOutputStream objectOutputStream;
    						objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    						objectOutputStream.writeObject(new Messenger(user.getName(),file.getName(),i));
    						objectOutputStream.flush();
    						Client.check = true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btngim.setIcon(new ImageIcon("img\\icons8-attach-25.png"));
		btngim.setBorder(null);
		btngim.setBackground(new Color(245, 245, 245));
		btngim.setBounds(461, 398, 30, 27);
		panel.add(btngim);
		
		btnCall = new JButton("");
		btnCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Calling calling = new Calling(socket,Client.port);
					calling.setExtendedState(JFrame.MAXIMIZED_BOTH); 
					calling.setVisible(true);
					calling.setLocationRelativeTo(null);
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					objectOutputStream.writeObject(new Messenger(user.getName(),"",Event.CREATE_CALLING));
					objectOutputStream.flush();
					Client.check = true;
					Client.calling = true;
					ChatContent chatContent = new ChatContent("Bạn",socket);
					mess.add(chatContent);
					mess.revalidate();
					int height = (int)mess.getPreferredSize().getHeight();
					Rectangle rect = new Rectangle(0,height,10,10);
					mess.scrollRectToVisible(rect);
					btnCall.setEnabled(false);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		btnCall.setIcon(new ImageIcon("img\\icons8-phone-25.png"));
		btnCall.setBorder(null);
		btnCall.setBackground(new Color(245, 245, 245));
		btnCall.setBounds(494, 398, 30, 27);
		panel.add(btnCall);
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String content = txtContent.getText().trim();
				if (content.length() > 0) {
					try {
						String s = checkLimited(content);
						if (count > 3) {
							reportServer(user.getName());
						} else {
							Messenger messenger = new Messenger("", s);
							addMessenger(messenger);
							Client.check = true;
							ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
							objectOutputStream.writeObject(new Messenger(user.getName(), s));
							objectOutputStream.flush();
							txtContent.setText("");
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}	
		});
	}
	
	public void addIcon(JPanel pnlIcon, String name) {
		 File dir = new File("icon");
         if (dir.isDirectory()) {
       	  File[] files = dir.listFiles();
             if (files.length > 0) {
           	 for (File f : files) {
           		JLabel l = new JLabel();
    			l.setPreferredSize(new Dimension(50, 50));
    			l.setOpaque(true);
    			l.setSize(new Dimension(50, 50));
    			l.setMinimumSize(new Dimension(50, 50));
    			l.setMaximumSize(new Dimension(50, 50));
    			l.setHorizontalAlignment(SwingConstants.CENTER);
    			l.setSize(50,50);
    			l.setIcon((f.length() < 1500) ? new ImageIcon("icon\\"+f.getName()) : reIcon("icon\\"+f.getName(), l));
    			l.addMouseListener(new MouseAdapter() {
    				@Override
    				public void mouseClicked(MouseEvent arg0) {
    					try {
    						FileInputStream imgg = new FileInputStream(f);
    						byte i[] = new byte[(int) f.length()];
    						imgg.read(i, 0, (int) f.length());
    						Messenger messenger = new Messenger("",i );
    						addMessenger(messenger);
    						ObjectOutputStream objectOutputStream;
    						objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    						objectOutputStream.writeObject(new Messenger(name,i));
    						objectOutputStream.flush();
    						Client.check = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
    				}
    			});
    			pnlIcon.add(l);
                }
			}
         }
	}
	
	public ImageIcon reIcon(String path, JLabel lblimg) {
		ImageIcon img = new ImageIcon(path);
		Image im = img.getImage().getScaledInstance(lblimg.getWidth(), lblimg.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon anh = new ImageIcon(im);
		return anh;
	}
	
	protected void reportServer( String name ) throws Exception {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectOutputStream.writeObject(new Messenger(name,"" ,Event.REPORT_LIMITED));
		objectOutputStream.flush();
		Client.messagesThread.stop();
		socket.close();
		System.exit(0);
	}

	public static void addMessenger(Messenger messenger) {
		ChatContent chatContent = null;
		if (messenger.getImage() != null && messenger.getContent() != null) {
			chatContent = new ChatContent(messenger.getName(),messenger.getContent(), messenger.getImage());
		} else if (messenger.getContent() != null) {
			chatContent = new ChatContent(messenger.getName(),messenger.getContent());
		} else {
			chatContent = new ChatContent(messenger.getName(),messenger.getImage());
		}
		mess.add(chatContent);
		mess.revalidate();
		int height = (int)mess.getPreferredSize().getHeight();
		Rectangle rect = new Rectangle(0,height,10,10);
		mess.scrollRectToVisible(rect);
	}
}
