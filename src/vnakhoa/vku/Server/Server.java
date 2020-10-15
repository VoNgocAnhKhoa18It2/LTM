package vnakhoa.vku.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JOptionPane;

import vnakhoa.vku.Client.CallingClient;
import vnakhoa.vku.Model.Messenger;
import vnakhoa.vku.Model.User;
import vnakhoa.vku.Model.UserCalling;
import vnakhoa.vku.Server.Server.Manageuser;

public class Server {
	
	ServerSocket serverSocket;
	Vector<String> users = new Vector<String>();
	Vector<Manageuser> clients = new Vector<Manageuser>();
	int portCalling = 4000;
	static Vector<UserCalling> listCalling = new Vector<UserCalling>();
	
	public static void main(String[] args) throws Exception {
		 new Server().createserver();
	}
	
	public void createserver() throws Exception {
        serverSocket = new ServerSocket(1201);
        JOptionPane.showMessageDialog(null, "Server started", "Server", JOptionPane.INFORMATION_MESSAGE);
        while (true) {
            Socket client = serverSocket.accept();
            if (client.isConnected()) {
            	System.out.println("Co nguoi ket noi");
				Manageuser c = new Manageuser(client);
			}
        }
    }
	
    public void sendtoall(Messenger message) throws IOException {
        for (Manageuser c : clients) {
        	if(!c.user.getName().equals(message.getName())) {
        		c.sendMessage(message,c.user.getSocket());
        	}
        }
    }
    
	
	class Manageuser extends Thread {
		int port ;
        User user;
        
        public Manageuser(Socket client) throws Exception {
        	InputStream input = client.getInputStream();
        	ObjectInputStream objInput = new ObjectInputStream(input);
            Messenger messenger = (Messenger) objInput.readObject();
            if (users.indexOf(messenger.getContent()) != -1) {
            	sendMessage(new Messenger(Event.NOTIFICATION_ERROR,"Tên người dùng tồn tại"),client);
            	return;
			} else {
				port = portCalling+=1;
				sendMessage(new Messenger(Event.NOTIFICATION_OK,"Well come "+messenger.getName(),String.valueOf(portCalling)),client);
				this.user = new User(messenger.getName(),messenger.getContent(),client);
	            clients.add(this);
	            users.add(user.getName());
	            sendtoall(new Messenger("Server", this.user.getName()+" đã tham gia vao nhóm chat", Event.NEW_USER));
	            sendtoall(new Messenger("Server", users.toString() , Event.UPDATE_USER)); 
	            if (listCalling.size() > 0) {
	            	sendMessage(new Messenger("Server", user.getName()+ " đã tạo 1 cuộc hội thoại",Event.CALLING_NOTIFICATION),client);
				}
	            start();
			}
        }
        public void sendMessage(Messenger messenger, Socket socket) throws IOException {
            OutputStream ops = socket.getOutputStream();
            ObjectOutputStream ots = new ObjectOutputStream(ops);
            ots.writeObject(messenger);
            ots.flush();
            
        }
        @Override
        public void run() {
        	Messenger messenger = null;
            try {
                while (true) {
                	if (!user.getSocket().isClosed()) {
                		InputStream input = user.getSocket().getInputStream();
                    	ObjectInputStream objInput = new ObjectInputStream(input);
                    	messenger = (Messenger) objInput.readObject();
                        if (messenger.getEvent() != null) {
                        	boolean stop = false;
                        	switch (messenger.getEvent()) {
                        	case Event.CREATE_CALLING:
								UserCalling callingClient = new UserCalling(user.getName(),user.getUserIP(),String.valueOf(port));
								listCalling.add(callingClient);
								System.out.println(listCalling.size());
								sendtoall(new Messenger(user.getName(), user.getName()+ " đã tạo 1 cuộc hội thoại",Event.CALLING_NOTIFICATION));
								break;
							case Event.CALLING_ACCEPT:
								UserCalling calling = new UserCalling(user.getName(),user.getUserIP(),String.valueOf(port));
								sendMessage(new Messenger("Server",listCalling,Event.CALLING_ACCEPT),user.getSocket());
								sendtoall(new Messenger(calling,Event.NEW_CALLING));
								listCalling.add(calling);
								break;
							case Event.DICONNECT:
                                clients.remove(this);
                                users.remove(messenger.getName());
                                sendtoall(new Messenger("Server", messenger.getName()+ " đã thoát",Event.IFM_DICONNECT));
                                sendtoall(new Messenger("Server", users.toString() , Event.UPDATE_USER));
                                stop = true;
                                break;
							case Event.REPORT_LIMITED:
								clients.remove(this);
                                users.remove(messenger.getName());
                                sendtoall(new Messenger("Server", messenger.getName()+ " đã bị click vì đã vi phạm văn hóa Chat Room",Event.IFM_DICONNECT));
                                sendtoall(new Messenger("Server", users.toString() , Event.UPDATE_USER));
                                stop = true;
                                break;   
							}
                        	if (stop) {
								return;
							}
                        } else {
                        	sendtoall(messenger); 
                        }
					}
                }
            } 
            catch (Exception ex) {
            	if (user.getSocket() == null) {
            		clients.remove(this);
                    users.remove(user.getName());
				}
            }
        }
    } 
}
