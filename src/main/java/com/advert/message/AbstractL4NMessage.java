/*
 *  Confidential and Proprietary                                                                
 Copyright 2008 By                                                                                     
 SGAI & Hewlett-Packard Development Company, L.P. 	              
 All Rights Reserved                                                                                  

 Project Name : SGAI  MES                                                                                                                                       
 Class Name   : AbstractL4Message.java    
 Package      : com.hp.message.vo      
 */
package com.advert.message;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * this is designed to define a parent message for others to extend.
 @version     $Id$                                                          
 @author sunf
 @since  2008-9-4 
 */
public abstract class AbstractL4NMessage implements Messaging {
	
	private static final String xmlVersionDeclaration="<?xml version=\"1.0\" encoding=\"utf-8\"?>";

	private L4NMessageHeader messageHeader;	
	
	@XStreamOmitField
	private String messageId;
	
	public AbstractL4NMessage() {
		this.messageHeader=new L4NMessageHeader();
		this.messageHeader.setMessageTypeId(this.getMessageTypeId());
	}
	
	public L4NMessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(L4NMessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public String getMessageId() {
		return this.messageId;
	}

	public Date getSendDateTime() {
		return this.messageHeader.getSendDateTime();
	}

	public String getSender() {
		return this.messageHeader.getSender();
	}
	
	public void setMessageId(String msgId) {
		this.messageId = msgId;
	}
	
	public String getXmlVersionDeclaration() {
		return xmlVersionDeclaration;
	}

	public String toString() {
		return new ToStringBuilder(this).append(this.messageHeader).toString();
	}
}
