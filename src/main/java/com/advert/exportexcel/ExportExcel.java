package com.advert.exportexcel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class ExportExcel {
	private static final  Logger logger = LoggerFactory.getLogger(ExportExcel.class);
	private String fileName;
	private String realPath;
	private String excelTitle;
	private int perSheetRows = 10000;
	private String fileSurrfix;
	private boolean neetFooter = true;
	
	/**
	 * 获取导出文件数据流
	 * @param xml
	 * @return
	 */
	public ByteArrayOutputStream getExportStream(List list, LinkedHashMap<String,String> header,
												Map<String, String> convertMap,boolean isExcel2007) {
		long time1 = System.currentTimeMillis();
		String suffix = ".xls";
		IExportExcel exportExcelByDefault = null;		
		if(isExcel2007){
			exportExcelByDefault = new ExportExcel2007ByDefault();
			suffix = ".xlsx";
		}else{
			exportExcelByDefault = new ExportExcelByDefault();
			suffix = ".xls";
		}
		fileSurrfix = suffix;
		int dataSize = list.size();
		String fileSeparator = System.getProperty("file.separator");
		String fileName = this.getFileName() + suffix;
		fileName = this.getRealPath() + "exceltemplate" + fileSeparator + fileName;
		int num = (dataSize%perSheetRows==0) ? (int)Math.floor(dataSize/perSheetRows) : (int)Math.floor(dataSize/perSheetRows)+1;
		
		
		exportExcelByDefault.setDataList(list);
		exportExcelByDefault.setNeedFooter(this.neetFooter);
		exportExcelByDefault.setHeader(header);
		exportExcelByDefault.setExcelTitle(this.getExcelTitle());
		exportExcelByDefault.setFileName(this.getFileName());
		exportExcelByDefault.setConvertMap(convertMap);
		ByteArrayOutputStream os = null;
		if (!this.fileIsExist(fileName)) {
			//将数据传给导出Excel的工具类
			exportExcelByDefault.setFileName(this.getFileName());
			exportExcelByDefault.setStrUserName("登录人");
			//多个sheet打zip包，一个sheet直接返回xsl
			if (num>1) {
				//生成本地文件
				for (int j=0;j<num;j++) {
					exportExcelByDefault.setDataStart((j*perSheetRows));
					exportExcelByDefault.setDataEnd(((j+1)*perSheetRows)>dataSize?dataSize:((j+1)*perSheetRows));					
					//创建工作簿
					exportExcelByDefault.createLocalExportFile(j);
					this.setFileSurrfix(".zip");
				}
				//将本地文件打包
				os = exportExcelByDefault.createZipFileStream(num);
				//删除本地文件				
			} else {
				exportExcelByDefault.setDataStart(0);
				exportExcelByDefault.setDataEnd(list.size());
				exportExcelByDefault.setDataList(list);
				//创建工作簿
				os = exportExcelByDefault.createExportXlsFileStream();
				this.setFileSurrfix(suffix);
			}
		} else {
			//采用模板导出时，在此实现
		}
		
		long time2 = System.currentTimeMillis();
		logger.debug("Total export data rows: " + dataSize + " .");
		logger.debug("Total time consuming：" + (time2-time1) + " ms");
		return os;
	}

	/**
	 * 获取导出文件数据流
	 * @param xml
	 * @return
	 */
	public ByteArrayOutputStream getExportStream(List list, LinkedHashMap<String,String> header,
												 Map<String, String> convertMap, Map<String, String> convertTableMap, boolean isExcel2007) {
		long time1 = System.currentTimeMillis();
		String suffix = ".xls";
		IExportExcel exportExcelByDefault = null;
		if(isExcel2007){
			exportExcelByDefault = new ExportExcel2007ByDefault();
			suffix = ".xlsx";
		}else{
			exportExcelByDefault = new ExportExcelByDefault();
			suffix = ".xls";
		}
		fileSurrfix = suffix;
		int dataSize = list.size();
		String fileSeparator = System.getProperty("file.separator");
		String fileName = this.getFileName() + suffix;
		fileName = this.getRealPath() + "exceltemplate" + fileSeparator + fileName;
		int num = (dataSize%perSheetRows==0) ? (int)Math.floor(dataSize/perSheetRows) : (int)Math.floor(dataSize/perSheetRows)+1;


		exportExcelByDefault.setDataList(list);
		exportExcelByDefault.setNeedFooter(this.neetFooter);
		exportExcelByDefault.setHeader(header);
		exportExcelByDefault.setExcelTitle(this.getExcelTitle());
		exportExcelByDefault.setFileName(this.getFileName());
		exportExcelByDefault.setConvertMap(convertMap);
		exportExcelByDefault.setConvertTableMap(convertTableMap);
		ByteArrayOutputStream os = null;
		if (!this.fileIsExist(fileName)) {
			//将数据传给导出Excel的工具类
			exportExcelByDefault.setFileName(this.getFileName());
			exportExcelByDefault.setStrUserName("登录人");
			//多个sheet打zip包，一个sheet直接返回xsl
			if (num>1) {
				//生成本地文件
				for (int j=0;j<num;j++) {
					exportExcelByDefault.setDataStart((j*perSheetRows));
					exportExcelByDefault.setDataEnd(((j+1)*perSheetRows)>dataSize?dataSize:((j+1)*perSheetRows));
					//创建工作簿
					exportExcelByDefault.createLocalExportFile(j);
					this.setFileSurrfix(".zip");
				}
				//将本地文件打包
				os = exportExcelByDefault.createZipFileStream(num);
				//删除本地文件
			} else {
				exportExcelByDefault.setDataStart(0);
				exportExcelByDefault.setDataEnd(list.size());
				exportExcelByDefault.setDataList(list);
				//创建工作簿
				os = exportExcelByDefault.createExportXlsFileStream();
				this.setFileSurrfix(suffix);
			}
		} else {
			//采用模板导出时，在此实现
		}

		long time2 = System.currentTimeMillis();
		logger.debug("Total export data rows: " + dataSize + " .");
		logger.debug("Total time consuming：" + (time2-time1) + " ms");
		return os;
	}
	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	private boolean fileIsExist(String fileName) {
	   boolean isExist =false;
	   File f = new File(fileName);
	   if (f.exists()) {
		   isExist =true;
	   }
	   return isExist;
    }
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getFileSurrfix() {
		return fileSurrfix;
	}

	public void setFileSurrfix(String fileSurrfix) {
		this.fileSurrfix = fileSurrfix;
	}
	
	public String getExcelTitle() {
		return excelTitle;
	}
	public void setExcelTitle(String excelTitle) {
		this.excelTitle = excelTitle;
	}
	public boolean isNeetFooter() {
		return neetFooter;
	}
	public void setNeetFooter(boolean neetFooter) {
		this.neetFooter = neetFooter;
	}	
	
}