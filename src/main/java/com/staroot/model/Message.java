package com.staroot.model;

public class Message {
	private String msgId;
	private String msgTitle;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	@Override
	public String toString() {
		return "Message [msgId=" + msgId + ", msgTitle=" + msgTitle + "]";
	}

}
