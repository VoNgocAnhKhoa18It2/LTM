package vnakhoa.vku.Model;

import java.io.Serializable;

public class UserCalling implements Serializable {
	private String name;
	private String address;
	private String port;
	
	public UserCalling(String name, String address, String port) {
		super();
		this.name = name;
		this.address = address;
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "UserCalling [name=" + name + ", address=" + address + ", port=" + port + "]";
	}
}
