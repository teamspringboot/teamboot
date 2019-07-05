/*
 *  Confidential and Proprietary                                                                
 Copyright 2008 By                                                                                     
 SGAI & Hewlett-Packard Development Company, L.P. 	              
 All Rights Reserved                                                                                  

 Project Name : SGAI  MES                                                                                                                                       
 Class Name   : MessageProvider.java    
 Package      : com.sgai.message.vo  
 */
package com.advert.message;


/**
 * this is a generic message interface for L2, L3, and L4.
 @version     $Id$                                                          
 @author sunf
 @since  2008-11-12 
 */
public interface MessageProvider extends Messaging {
	public static final String DATE_PATTERN="yyyyMMddHHmmss";
	/**
	 * returns the message id.
	 * @return returns the message id
	 */
	public String getMessageId();
	/**
	 * sets the message id.
	 * @param msgId a message id to be set
	 */
	public void setMessageId(String msgId);
	
	/**
	 * returns the message provider.
	 * @return a String indicates the message provider.
	 */
	public String getMessageProvider();
}
