package com.advert.velocity;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.w3c.dom.Document;

import com.advert.exception.ApplicationException;



/**
 * ---------------------------------------------------------------------------------
 Confidential and Proprietary                                                                
 Copyright 2008 By                                                                                     
 SGAI & Hewlett-Packard Development Company, L.P. 	              
 All Rights Reserved                                                                                  

 Project Name : SGAI  MES                                                                                                                                       
 Class Name   : GenerateXmlService.java    
 Package      : com.hp.common.velocity.service                                                                   
 @version     $Id$                                                          
 @author yangchaoming
 @since  2008-12-11 
 */
/**
 * ---------------------------------------------------------------------------------
 Confidential and Proprietary                                                                
 Copyright 2008 By                                                                                     
 SGAI & Hewlett-Packard Development Company, L.P. 	              
 All Rights Reserved                                                                                  

 Project Name : SGAI  MES                                                                                                                                       
 Class Name   : GenerateXmlService.java    
 Package      : com.hp.common.velocity.service                                                                   
 @version     $Id$                                                          
 @author yangchaoming
 @since  2008-12-11 
 */
public interface GenerateXmlService {

    /**
     * Velocity的上下文中添加对象
     * 
     * @param Key
     * @param obj
     * @author yangchaoming
     * @since  2008-12-11 
     */
    public void addObject2VCtx(String Key, Object obj,VelocityContext vc);

    /**
     * 根据上下文及模板生成Xml并解析为字符串
     * 
     * @param 模板文件
     * @param 电文对象
     * @return 生成Xml
     * @author yangchaoming
     * @throws ParseErrorException 
     * @throws ResourceNotFoundException 
     * @throws FileNotFoundException 
     * @throws UnsupportedEncodingException 
     * @throws Exception 
     * @since  2008-12-11 
     */
    public String genXmlByTemplate(String templateFile,VelocityContext vc) throws ResourceNotFoundException,
            ParseErrorException, UnsupportedEncodingException,
            FileNotFoundException, Exception;
    
    /**
     * 根据上下文及模板生成消息
     * 
     * @param 要得到的消息标识
     * @return
     * @author yangchaoming
     * @throws ApplicationException 
     * @since  2008-12-12 
     */
    public String genXmlByMsId(String msId,VelocityContext vc) throws ApplicationException;
    
    /**
     * 清空Velocity上下文
     * 
     * @author yangchaoming
     * @since  2008-12-11 
     */
    public void clearVCtx(VelocityContext vc);

    /**
     * @param fileName
     * @throws FileNotFoundException
     * @author yangchaoming
     * @since  2008-12-11 
     */
    public void delete() throws FileNotFoundException;
    
    /**
     * 把字符串解析成Dom对象
     * 
     * @param 字符串
     * @return Document对象
     * @author yangchaoming
     * @since  2009-1-5
     */
    public Document parStr2Dom(String xmlString);
    
    /**
     * 根据模板字符串生成 xml
     * @param templateStr
     * @return
     * @author sjk
     * @since  2009-4-29
     */
    public String genXmlByTemplateStr(String templateStr,VelocityContext vc);

}