package vnakhoa.vku.Model;

import java.io.File;
import java.io.Serializable;
import java.util.Vector;

public class Messenger implements Serializable {
	
	private String name;
	private String content;
	private byte[] image;
	private String event;
	private Vector<UserCalling> list;
	private UserCalling calling;

	// New Calling
	public Messenger(UserCalling calling, String event) {
		super();
		this.event = event;
		this.calling = calling;
	}

	//Calling
	public Messenger(String name, Vector<UserCalling> list, String event) {
		super();
		this.name = name;
		this.event = event;
		this.list = list;
	}

	//Send File
	public Messenger(String name, String content, byte[] image) {
		super();
		this.name = name;
		this.content = content;
		this.image = image;
	}

	//Send informaiton
	public Messenger(String name, String content, String event) {
		super();
		this.name = name;
		this.content = content;
		this.event = event;
	}

	//Send imgage
	public Messenger(String name, byte[] image) {
		super();
		this.name = name;
		this.image = image;
	}

	//Send Messenger
	public Messenger(String name, String content) {
		super();
		this.name = name;
		this.content = content;
	}

	public UserCalling getCalling() {
		return calling;
	}

	public void setCalling(UserCalling calling) {
		this.calling = calling;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	public Vector<UserCalling> getList() {
		return list;
	}

	public void setList(Vector<UserCalling> list) {
		this.list = list;
	}
	
}
