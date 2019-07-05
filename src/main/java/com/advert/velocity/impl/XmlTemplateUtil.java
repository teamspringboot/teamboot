package com.advert.velocity.impl;

import org.apache.commons.lang.StringUtils;

/**
 * ---------------------------------------------------------------------------------
 Confidential and Proprietary                                                                
 Copyright 2008 By                                                                                     
 SGAI & Hewlett-Packard Development Company, L.P. 	              
 All Rights Reserved                                                                                  

 Project Name : SGAI  MES                                                                                                                                       
 Class Name   : XmlTemplateUtil.java    
 Package      : com.hp.common.velocity.service.impl                                                                   
 @version     $Id$                                                          
 @author hxuan
 @since  Mar 8, 2010 
 */
public class XmlTemplateUtil {
    private static final String SEPRATOR = "_";
    private static final String OPENT_MSG = "<TEL_ID>";
    private static final String CLOSE_MSG = "</TEL_ID>";
    
    /**
     * get msgTypeId in xml template. 
     * @param xml a xml message send by jms.
     * @param pdiId
     * @return
     * @author chaoming.yang
     * @since  2010-2-2
     */
    public static String getMsgTypeId(String xml,String pdiId){
        String msgTypeId = pdiId;
//        if(pdiId.indexOf(SEPRATOR)>0){
            msgTypeId=StringUtils.substringBetween(xml,OPENT_MSG,CLOSE_MSG).trim();
//        }
        
        return msgTypeId;
    }    
}
