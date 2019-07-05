package com.advert.exception;

/**
 * ---------------------------------------------------------------------------------
 Confidential and Proprietary                                                                
 Copyright 2008 By                                                                                     
 SGAI & Hewlett-Packard Development Company, L.P. 	              
 All Rights Reserved                                                                                  

Project Name : SGAI  MES                                                                                                                                       
 Class Name   : BaseCheckException.java    
 Package      : com.hp.common.exception                                                                   
 $Id: BaseCheckException.java 9648 2008-09-03 06:13:57Z xia.li2 $       :                                                                  
 检测类型异常基础类
 */
public class BaseCheckException extends Exception implements MesBizException{
	private static final long serialVersionUID = -6358717097463016627L;

	private String code;

	private String[] params;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}
	
	/**
	 * 默认构造参数
	 */
	public BaseCheckException() {
		super();
	}

	/**
	 * 提供错误消息和异常构造参数
	 * @param message
	 * @param e
	 */
	public BaseCheckException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * 只有错误消息，没有传递异常
	 * @param message
	 * @param e
	 */
	public BaseCheckException(String code,String[] params) {
		super(code);
		this.setCode(code);
		this.setParams(params);
	}
}
