package com.advert.exportexcel;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public interface IExportExcel {
	/**
	 * Excel工作簿中生成sheet(只有一页的时候)
	 * @param title
	 * @return
	 */
	public ByteArrayOutputStream createExportXlsFileStream();
	/**
	 * 压缩xls文件
	 * @param num
	 * @return
	 */
	public ByteArrayOutputStream createZipFileStream(int num);
	/**
	 * Excel工作簿中生成sheet(包含多页的时候，生成本地文件打包，供用户下载)
	 * @param title
	 * @return
	 */
	public void createLocalExportFile(int i);
	
	
	public List getDataList();
	public void setDataList(List dataList);

	public String getLocalFileNamePattern();

	public void setLocalFileNamePattern(String localFileNamePattern);
	public String getFilePath();

	public void setFilePath(String filePath);

	public String getSysSeparator();

	public void setSysSeparator(String sysSeparator);

	public String getBasePath();

	public void setBasePath(String basePath);
	public int getDataStart();
	public void setDataStart(int dataStart);
	public int getDataEnd();
	public void setDataEnd(int dataEnd);

	public String getStrUserName();

	public void setStrUserName(String strUserName);

	public String getFileName();

	public void setFileName(String fileName);
	public Map<String, String> getHeader();
	public void setHeader(Map<String, String> header);
	public String getExcelTitle() ;
	public void setExcelTitle(String excelTitle);
	public Map<String, String> getConvertMap();
	public void setConvertMap(Map<String, String> convertMap);
	public void setNeedFooter(boolean needFooter);

	void setConvertTableMap(Map<String, String> convertTableMap);
}
