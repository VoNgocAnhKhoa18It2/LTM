package vnakhoa.vku.Model;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
	
	private String name;
	private String userIP;
	private Socket socket;

	public User(String name, String userIP, Socket socket) {
		super();
		this.name = name;
		this.userIP = userIP;
		this.socket = socket;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
