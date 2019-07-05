package com.advert.message;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class L4NMessageHeader implements Serializable {
	@XStreamAlias("MessageTypeId")
	private String messageTypeId;
	@XStreamAlias("MessageId")
	private String messageId;
	@XStreamAlias("SendDate")
	private String sendDate;
	@XStreamAlias("SendTime")
	private String sendTime;
	@XStreamAlias("Sender")
	private String sender;

	@XStreamAlias("Flag")
	private String flag;
	@XStreamAlias("Msg")
	private String msg;
	@XStreamAlias("SvcName")
	private String svcName;
	@XStreamAlias("UUID")
	private String uuid;

	public String getMessageTypeId() {
		return messageTypeId;
	}

	public void setMessageTypeId(String messageTypeId) {
		this.messageTypeId = messageTypeId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getSendDateTime() {
		String date = this.getSendDate();
		String time = this.getSendTime();
		if (date == null || date.trim().length() == 0) {
			return new Date();
		} else {
			if (time == null || time.trim().length() == 0) {
				time = "000000";
			}
		}

		DateFormat formatter = new SimpleDateFormat(
				MessageProvider.DATE_PATTERN);
		return formatter.parse(date + time, new ParsePosition(0));
	}

}
