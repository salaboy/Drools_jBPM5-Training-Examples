package org.plugtree.training.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private String content;
	private Date timestamp;
	private boolean processed = false;

	public Message(String userName, String content) {
		this.userName = userName;
		this.content = content;
		this.timestamp = new Date();
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public boolean isProcessed() {
		return processed;
	}

}
