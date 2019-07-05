package com.advert.exportexcel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {
	private static final  Logger logger = LoggerFactory.getLogger(ExportExcel.class);
	private static final int readSize = 1024 * 1024;
	public static void zip(ZipOutputStream out, File f, String base, boolean first) throws Exception {
	  if (first) {
	    if (f.isDirectory()) {
	      out.putNextEntry(new org.apache.tools.zip.ZipEntry("/"));
	      base = base + f.getName();
	      first = false;
	    } else
	      base = f.getName();
	  }
	  if (f.isDirectory()) {
	    File[] fl = f.listFiles();
	    base = base + "/";
	    for (int i = 0; i < fl.length; i++) {
	      zip(out, fl[i], base + fl[i].getName(), first);
	    }
	  } else {
	    out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
	    BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));   
		int count;   
        byte data[] = new byte[readSize];   
        while ((count = in.read(data, 0, readSize)) != -1) {   
            out.write(data, 0, count);   
        }  
	    in.close();
	  }
	}
	
	public static void ZipFile(String zipFileName, String inputFileName)
	    throws Exception {
	  long time1 = System.currentTimeMillis();
	  org.apache.tools.zip.ZipOutputStream out = new org.apache.tools.zip.ZipOutputStream(
	      new FileOutputStream(zipFileName));
	  //设置的和文件名字格式一样或开发环境编码设置一样的话就能正常显示了
	  out.setEncoding("gbk");
	  File inputFile = new File(inputFileName);
	  zip(out, inputFile, "", true);
	  logger.debug("zip done");
	  out.close();
	  long time2 = System.currentTimeMillis();
	  logger.debug("压缩导出文件耗时：" + (time2-time1) + "毫秒");
	}
}