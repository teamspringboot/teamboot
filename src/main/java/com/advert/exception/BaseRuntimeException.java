package com.advert.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ---------------------------------------------------------------------------------
 Confidential and Proprietary                                                                
 Copyright 2008 By                                                                                     
 SGAI & Hewlett-Packard Development Company, L.P. 	              
 All Rights Reserved                                                                                  

Project Name : SGAI  MES                                                                                                                                       
 Class Name   : BaseRuntimeException.java    
 Package      : com.hp.common.exception                                                                   
 $Id: BaseRuntimeException.java 43985 2012-01-06 06:03:27Z xiangju.duan $       :                                                                  
 运行时类型异常基础类
 */
public class BaseRuntimeException extends RuntimeException implements
		MesBizException {
	private static final long serialVersionUID = 5806782400259918073L;

	private static final  Logger logger = LoggerFactory.getLogger(BaseRuntimeException.class);
	
	
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

	public BaseRuntimeException() {
		super();
	}

	public BaseRuntimeException(String message, Exception e) {
		super(message, e);
	}

	public BaseRuntimeException(String message) {
		super(message);
	}

	public BaseRuntimeException(Exception e) {
		super(e);
	}

	public BaseRuntimeException(String code, String[] params) {
		super(code);
		this.setCode(code);
		this.setParams(params);
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		if (code == null || code.length() == 0) {
			return super.getMessage();
		}
		String codeMessage = params[0];
		return codeMessage;
	}

	public String toString() {
		String s = getClass().getName();
		String message = this.getMessage();
		return (message != null) ? (s + ": " + message) : s;
	}

	

	
	
}
