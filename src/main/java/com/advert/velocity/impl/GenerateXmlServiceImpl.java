package com.advert.velocity.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.dom4j.io.DOMReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.advert.exception.ApplicationException;
import com.advert.velocity.GenerateXmlService;




/**
 * ---------------------------------------------------------------------------------
 * 获取电文                                                                                     
 *
 */
@Service
public class GenerateXmlServiceImpl implements GenerateXmlService {
    private static final Log logger = LogFactory
            .getLog(GenerateXmlServiceImpl.class);
    private static final String MSG_FILE_PATH = "temp";
    private static final String TEMPLET_FILE_PATH = "/velocity/";

    @Autowired
    private VelocityEngine velocityEngine;
    private String fileName;

    /* (non-Javadoc)
     * @see com.hp.common.velocity.service.GenerateXmlService#generateXml(org.apache.velocity.VelocityContext, java.lang.String, java.lang.String)
     */
    public String genXmlByTemplate(String templateFile,VelocityContext vc)
            throws ResourceNotFoundException, ParseErrorException, Exception {
        // 模板文件的路径名
        templateFile = TEMPLET_FILE_PATH + templateFile;

        // 根据上下文的fileName命名xml临时文件的文件名
        fileName = MSG_FILE_PATH + (String) vc.get("fileName") + ".xml";

//        Template template = velocityEngine.getTemplate(templateFile);
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                new FileOutputStream(fileName), "UTF-8"));
//        template.merge(vc, writer);
//        writer.close();
        
        InputStream in = this.getClass().getResourceAsStream(templateFile); 
        InputStreamReader read = new InputStreamReader(in, "UTF-8");
        BufferedReader reader = new BufferedReader(read);
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        read.close();
        
        StringWriter w = new StringWriter(); 
    	velocityEngine.evaluate(vc, w, "", sb.toString());
        w.flush();
        w.close();
        return String.valueOf(w);
//        return getStringFromXml(fileName);
    }

    /* (non-Javadoc)
     * @see com.hp.common.velocity.service.GenerateXmlService#genXmlFromStirng(java.lang.String)
     */
    /*public String genXmlByMsId(String msId,VelocityContext vc)throws ApplicationException{
    	ObjConfig conf = objConfigService.findByObjId(msId);
        // 如果数据库未配置相应的电文的模板抛异常
        if (null == conf||StringUtil.isBlank(conf.getXmlTemplate())) {
            throw new ApplicationException(
                    "error.cod.service.pdi.template.config.not.find",new String[]{msId}
                            );
        }

        // 解析模板并输出
        StringWriter w = new StringWriter();        
        try {
        	velocityEngine.evaluate(vc, w, "", conf.getXmlTemplate());
            w.flush();
            w.close();
        } catch (ResourceNotFoundException e1) {
            e1.printStackTrace();
            throw new ApplicationException(
                    "error.code.service.velocity.template.file.can.not.find",
                    new String[] { msId});
        } catch (ParseErrorException e1) {
            e1.printStackTrace();
            throw new ApplicationException(
                    "error.code.service.velocity.template.file.can.not.parse",
                    new String[] { msId });
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            throw new ApplicationException(
                    "error.code.service.velocity.template.encode.can.not.support",
                    new String[] { msId });
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new ApplicationException(
                    "error.code.service.velocity.can.not.generate.xml",
                    new String[] { msId });
        }
       
        return String.valueOf(w);
    }*/

    /* (non-Javadoc)
     * @see com.hp.common.velocity.service.GenerateXmlService#addObject2Ctx(java.lang.String, java.lang.Object)
     */
    public void addObject2VCtx(String Key, Object obj,VelocityContext vc) {
        // 为context做参数赋值，该参数在模板中可直接访问
        vc.put(Key, obj);
    }

    /* (non-Javadoc)
     * @see com.hp.common.velocity.service.GenerateXmlService#clearVCtx()
     */
    public void clearVCtx(VelocityContext vc) {
        // 清空上下文
        Object[] temp = vc.getKeys();
        try {
            for (Object key : temp) {
                if(vc.containsKey(key)){
                    vc.put(key.toString(), null); 
                }
                
            }
        } catch (Exception e) {
            vc = null;
            e.printStackTrace();
        }

    }

    /* (non-Javadoc)
     * @see com.hp.common.velocity.service.GenerateXmlService#delete(java.lang.String)
     */
    public void delete() throws FileNotFoundException {
        File f = new File(fileName);
        if (f.exists()) {
            f.delete();
        }
    }

    /**
     * 解析Xml文件
     * 
     * @param Xml文件的路径
     * @param 根据xml要生成的电文（.jav)
     * @return 字符串
     * @author yangchaoming
     * @since  2008-12-11 
     */
    public String getStringFromXml(String fileName) throws SAXException,
            IOException {
        // DOM读取xml文件
        Document docc = null;
        DocumentBuilder builder = getDomBuilder();
        try {
            docc = builder.parse(fileName);
        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // 返回解析后的XML
        return (new DOMReader().read(docc)).asXML();
    }

    // 获得DocumentBuilder
    private DocumentBuilder getDomBuilder() {
        // DOM读取xml文件
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }

        return builder;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

   

    /* (non-Javadoc)
     * @see com.hp.common.velocity.service.GenerateXmlService#parStr2Dom(java.lang.String)
     */
    public Document parStr2Dom(String xmlString) {
        Document doc = null;
        StringReader sr = new StringReader(xmlString);
        InputSource iSrc = new InputSource(sr);
        DocumentBuilder builder = getDomBuilder();
        try {
            doc = builder.parse(iSrc);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

	@Override
	public String genXmlByMsId(String msId, VelocityContext vc)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String genXmlByTemplateStr(String templateStr, VelocityContext vc) {
		// TODO Auto-generated method stub
		return null;
	}

    /* (non-Javadoc)
     * @see com.hp.common.velocity.service.GenerateXmlService#genXmlByTemplateStr(java.lang.String)
     */
    /*public String genXmlByTemplateStr(String templateStr,VelocityContext vc) {
        // 如果数据库未配置相应的电文抛异常
        if (null == templateStr) {
            throw new ResourceNotFoundException(
                    "Can't find right confuration for message,the type is:"
                            + "");
        }
        // 如果数据库未配置对应电文的模板抛异常
        if (StringUtil.isBlank(templateStr)) {
            throw new ResourceNotFoundException(
                    "Can't find Template from message the type is:" + "");
        }
        StringWriter w = new StringWriter();
        VelocityEngine velocityEngine = (VelocityEngine) AppServiceHelper
				.findBean("velocityEngine");
		velocityEngine.evaluate(vc, w, "", templateStr);
        try {
            w.close();
        } catch (IOException e) {
            logger
                    .warn("close stream exception,found method genXmlByTemplateStr ,please");
        }

        return String.valueOf(w);
    }*/
}
