package com.advert.exportexcel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advert.pageing.AppServiceHelper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;



public class ExportExcelByDefault  implements IExportExcel {
	private static final  Logger logger = LoggerFactory.getLogger(ExportExcelByDefault.class);
	private WritableCellFormat wcfTableName = null; //表格名称样式
	private WritableCellFormat wcfHeader = null; //表头样式
	private WritableCellFormat wcfDeptName = null; //单位名称样式

	private WritableCellFormat wcfBKLeft = null;//边框
	private WritableCellFormat wcfBKCenter = null;//边框居中
	private WritableCellFormat wcfBKRight = null;//边框靠右
	
	private WritableCellFormat wcfLeft = null;//无边框
	private WritableCellFormat wcfCenter = null;//居中
	private WritableCellFormat wcfRight = null;//靠右
	
	private int lastColumns  = 4;//最后一行总列数
	private List dataList;
	private Map<String, String> header;
	private String fileName;
	private String excelTitle;
	private String strUserName;
	private String localFileNamePattern;
	private String basePath;
	private String filePath;
	private String sysSeparator;
	private int dataStart;
	private int dataEnd;
	private Map<String, String> convertMap;
	private Map<String, String> convertTableMap;
	private boolean needFooter = true;

	private void initCellFormat() {
		 try {
	      //定义表格名称的单元格格式
	      WritableFont tableNameFont = new WritableFont(WritableFont.createFont("DFBK-W5-FPG"), 14, WritableFont.BOLD); //加粗字体
	      wcfTableName = new WritableCellFormat(tableNameFont);
	      wcfTableName.setLocked(true);
		  wcfTableName.setAlignment(jxl.format.Alignment.CENTRE); //设置水平居中对齐
		  wcfTableName.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); //设置垂直居中对齐
		  wcfTableName.setWrap(true);
		  wcfTableName.setBackground(jxl.format.Colour.GRAY_25);
		  wcfTableName.setBorder(jxl.format.Border.ALL,
		          jxl.format.BorderLineStyle.THIN);
		
	      //定义表头、除单位名称外的单元格格式
	      WritableFont headFont = new WritableFont(WritableFont.createFont("DFBK-W5-FPG"), 11, WritableFont.BOLD); //不加粗字体
	      wcfHeader = new WritableCellFormat(headFont);
	      wcfHeader.setLocked(true);
	      wcfHeader.setBorder(jxl.format.Border.ALL,
	                          jxl.format.BorderLineStyle.THIN); //设置边框
	      wcfHeader.setAlignment(jxl.format.Alignment.CENTRE); //设置水平居中对齐
	      wcfHeader.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); //设置垂直居中对齐
	      wcfHeader.setWrap(true);
	      wcfHeader.setBackground(jxl.format.Colour.GRAY_50);

	      //定义单位名称单元格格式
	      WritableFont deptNameFont = new WritableFont(WritableFont.createFont("DFBK-W5-FPG"), 10, WritableFont.NO_BOLD); //不加粗字体
	      wcfDeptName = new WritableCellFormat(deptNameFont);
	      //wcfDeptName.setBorder(jxl.format.Border.ALL,  jxl.format.BorderLineStyle.THIN); //设置边框
	      wcfDeptName.setAlignment(jxl.format.Alignment.LEFT); //设置水平居中对齐
	      wcfDeptName.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); //设置垂直居中对齐
	      wcfDeptName.setWrap(true);  
	     
	      //边框
	      WritableFont BK = new WritableFont(WritableFont.createFont("DFBK-W5-FPG"), 10, WritableFont.NO_BOLD); //不加粗字体
	      wcfBKLeft = new WritableCellFormat(BK);
	      wcfBKLeft.setBorder(jxl.format.Border.ALL,  jxl.format.BorderLineStyle.THIN); 
	      
	      wcfBKCenter= new WritableCellFormat(BK);
	      wcfBKCenter.setBorder(jxl.format.Border.ALL,  jxl.format.BorderLineStyle.THIN); 
	      wcfBKCenter.setAlignment(jxl.format.Alignment.CENTRE);
	      wcfBKCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	      
	      wcfBKRight= new WritableCellFormat(BK);
	      wcfBKRight.setBorder(jxl.format.Border.ALL,  jxl.format.BorderLineStyle.THIN); 
	      wcfBKRight.setAlignment(jxl.format.Alignment.RIGHT);
	      wcfBKRight.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	      
	      WritableFont WCF = new WritableFont(WritableFont.createFont("DFBK-W5-FPG"), 10, WritableFont.BOLD); //加粗字体
	      wcfLeft = new WritableCellFormat(WCF);
	      wcfLeft.setAlignment(jxl.format.Alignment.LEFT);
	      wcfLeft.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	      
	      wcfCenter= new WritableCellFormat(WCF);
	      wcfCenter.setAlignment(jxl.format.Alignment.CENTRE);
	      wcfCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	      
	      wcfRight= new WritableCellFormat(WCF);
	      wcfRight.setAlignment(jxl.format.Alignment.RIGHT);
	      wcfRight.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	      
		 } catch (WriteException e) {
				e.printStackTrace();
		}
	}
	
	
	/**
	 * Excel工作簿中生成sheet(只有一页的时候)
	 * @param title
	 * @return
	 */
	public ByteArrayOutputStream createExportXlsFileStream()  {
		long time1 = System.currentTimeMillis();
		ByteArrayOutputStream os =  new ByteArrayOutputStream();
		try {			
			WritableWorkbook workbookStream = Workbook.createWorkbook(os);
			WritableSheet sheet = workbookStream.createSheet("mySheet", 0);
			this.getExportData(sheet);
			sheet.setName("Page1");
			workbookStream.write();
			workbookStream.close();
			workbookStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		long time2 = System.currentTimeMillis();
		logger.debug("Create export stream time consuming: " + (time2-time1) + " ms");
		return os;
	}
	/**
	 * 压缩xls文件
	 * @param num
	 * @return
	 */
	public ByteArrayOutputStream createZipFileStream(int num) {		
		//压缩后zip文件名
		String zipFileName = this.getFilePath() + 
							 this.getLocalFileNamePattern() + ".zip";
		//压缩目标，是一个目录
		String inputFileName = this.getFilePath() + 
							   this.getLocalFileNamePattern();
		
		try {
			ZipUtil.ZipFile(zipFileName, inputFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ByteArrayOutputStream os =  new ByteArrayOutputStream();
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFileName));   
			int count;   
	        byte data[] = new byte[1024*1024];   
	        while ((count = in.read(data, 0, 1024*1024)) != -1) {   
	            os.write(data, 0, count);   
	        }
	        in.close();
	        this.deleteFileAndDir();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return os;
	}
	/**
	 * Excel工作簿中生成sheet(包含多页的时候，生成本地文件打包，供用户下载)
	 * @param title
	 * @return
	 */
	public void createLocalExportFile(int i)  {
		long time1 = System.currentTimeMillis();
		//创建文件存放路径
		this.createDir(this.getFilePath());
		this.createDir(this.getFilePath() + this.getLocalFileNamePattern());
		String xlsFileName = this.getFilePath() +
							 this.getLocalFileNamePattern() +
							 this.getSysSeparator() +
							 this.getLocalFileNamePattern()+ "_" + (i +1) + ".xls";
		
		try {
			WritableWorkbook workbookStream = Workbook.createWorkbook(new File(xlsFileName));
			WritableSheet sheet = workbookStream.createSheet("mySheet", 0);
			this.getExportData(sheet);
			sheet.setName("Page" + (i+1));
			workbookStream.write();
			workbookStream.close();
			workbookStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		long time2 = System.currentTimeMillis();
		//logger.debug("创建本地文件 " + xlsFileName + " 用时：" + (time2-time1) + "毫秒");
	}
	
	private void getExportData(WritableSheet sheet) 
		throws RowsExceededException, WriteException {
		
		int totalColumns = this.getHeader().size();
		//初始化样式
		initCellFormat();
		Label l;
		//设置Excel文档标题
		String title = this.excelTitle;
		l = new Label(0, 0, title, wcfTableName);
		sheet.addCell(l); 
		sheet.mergeCells(0, 0, totalColumns, 0); 
		//设置表头
		this.generateTableHead(sheet);
		//输出数据
		this.generateTableData(sheet);

		if(needFooter){
			//插入一行空行，便于文档使用人员进行列的合计等操作
			this.generateBlankRow(sheet);
			
			//footer
			int rows = sheet.getRows();
			l = new Label(0, rows, "备注:", wcfBKCenter);
			sheet.addCell(l);
			sheet.mergeCells(0, rows, 0, rows +2);
			l = new Label(1, rows, "", wcfBKCenter);
			sheet.addCell(l);
			sheet.mergeCells(1, rows, totalColumns, rows +2);
			l = new Label(this.lastColumns-4, rows +3, "制表人:", wcfRight);
			sheet.addCell(l);
			l = new Label(this.lastColumns-3, rows +3, this.getStrUserName() , wcfLeft);
			sheet.addCell(l);
			l = new Label(this.lastColumns-2, rows +3, "制表日期:", wcfRight);
			sheet.addCell(l);
			l = new Label(this.lastColumns-1, rows +3, (new Date()).toLocaleString(), wcfLeft);
			sheet.addCell(l);
		}
	}
	
	/**
	 * 生成数据
	 * @param sheet
	 */
	private void generateTableData(WritableSheet sheet)  
							throws RowsExceededException, WriteException{
		//整数格式
		jxl.write.NumberFormat nFormat = new jxl.write.NumberFormat("0");  
		jxl.write.WritableCellFormat nCellFormat = new jxl.write.WritableCellFormat(nFormat);							
		nCellFormat.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
		nCellFormat.setAlignment(nCellFormat.getAlignment().RIGHT);
		
		Map<Integer,jxl.write.WritableCellFormat> fcellFormatMap = new HashMap<Integer,jxl.write.WritableCellFormat>();
		//小数格式
		jxl.write.NumberFormat fFormat = new jxl.write.NumberFormat("0.0000");  
		jxl.write.WritableCellFormat fCellFormat = new jxl.write.WritableCellFormat(fFormat);
		fCellFormat.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
		
		jxl.write.DateFormat dateFormat=new jxl.write.DateFormat("yyyy-MM-dd HH:mm:ss");
		jxl.write.WritableCellFormat dateCellFormat = new jxl.write.WritableCellFormat(dateFormat);								
		dateCellFormat.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
		
		List dList = this.getDataList();
		int rows = sheet.getRows() - this.getDataStart();
		//logger.debug(SystemProperties.commonTypeItemsMap);
//		logger.debug("start:" + this.getDataStart());
//		logger.debug("end:" + this.getDataEnd());
		for (int i=this.getDataStart();i<this.getDataEnd();i++) {			
			//插入序号列
			jxl.write.WritableCellFormat rownum = new jxl.write.WritableCellFormat(nFormat);
			rownum.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
			rownum.setAlignment(jxl.format.Alignment.CENTRE);//设置水平居中对齐
			rownum.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); //设置垂直居中对齐
			jxl.write.Number sequenceCell = new jxl.write.Number(0,i+rows,i+1,rownum); 
			sheet.addCell(sequenceCell);
			
			Object entity = dList.get(i); 
			int k=1;
			Map<String, String> header = this.getHeader();
			Iterator it = header.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, String> entry =  (Entry<String, String>) it.next();
				String key = entry.getKey();
				Object dataObj = null;
				if(entity instanceof Map){//Map直接取值
					dataObj = ((Map)entity).get(key);
				}else{
					dataObj = getDataFromEntity(entity, key);
				}
				String data = null;
				if (dataObj==null) {
					data = "";
				} else {
					data = formatDataObj(dataObj,key);
				}
				int length = data.length();
				length = length<15?15:length;
				if (sheet.getColumnWidth(k)<length) {
					sheet.setColumnView(k, length);
				}
				try {
					int dotPos= data.indexOf(".");
					if (dotPos>0) {
						Integer decimalLenth=data.length()-dotPos-1; 
						jxl.write.WritableCellFormat cellFormat = fcellFormatMap.get(decimalLenth);
						if(cellFormat==null){
							jxl.write.NumberFormat numFormat = new jxl.write.NumberFormat("0."+StringUtils.repeat('0', decimalLenth));  
							cellFormat = new jxl.write.WritableCellFormat(numFormat);
							fCellFormat = new jxl.write.WritableCellFormat(cellFormat);
							fCellFormat.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
						}						
						Float fNum = Float.parseFloat(data);					
						jxl.write.Number numCell = new jxl.write.Number(k,i+rows,fNum,fCellFormat); 
						sheet.addCell(numCell);
					} else {
						if ((dataObj instanceof java.lang.Number) && !data.startsWith("0")) {
							Long fNum = Long.parseLong(data);
							jxl.write.Number numCell = new jxl.write.Number(k,i+rows,fNum,nCellFormat); 
							sheet.addCell(numCell);
						} else if((dataObj instanceof java.sql.Date)){
							java.sql.Date dtObj = (java.sql.Date)dataObj;
							java.util.Date dt=new java.util.Date (dtObj.getTime());
							jxl.write.DateTime dateCell=new jxl.write.DateTime(k,i+rows,dt,dateCellFormat);
							sheet.addCell(dateCell);	
						}else if((dataObj instanceof java.sql.Timestamp)){
							java.sql.Timestamp timeStampObj = (java.sql.Timestamp)dataObj;
							java.util.Date dt=new java.util.Date (timeStampObj.getTime());
							jxl.write.DateTime dateCell=new jxl.write.DateTime(k,i+rows,dt,dateCellFormat);
							sheet.addCell(dateCell);	
						}else{
							Label l = new Label(k , i+rows , data, getAlignFormat(dataObj, key));
							sheet.addCell(l);
						}
					}
				} catch (Exception e) {
					Label l = new Label(k , i+rows , data, getAlignFormat(dataObj, key));
					sheet.addCell(l);
				}				
				k++;
			}
		}
	}
	
	private String formatDataObj(Object obj, String key) {
		if (obj instanceof java.util.Date) {
			String format = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			String dateStr = sdf.format(obj);
			/**updated by wangfei 
			 * 当格式化之后为 yyyy-MM-dd 00:00:00时,只保留前一部分
			 * 但是会出现将含有时分秒00:00:00的数据格式化的情况
			 */
			
			if (dateStr.contains("00:00:00")) {
				dateStr = dateStr.substring(0, 10);
			}
			return dateStr;			
		} else {
			String data = obj.toString().trim();
			/*CacheConfig cacheConfig = (CacheConfig) AppServiceHelper.findBean("cacheConfig");
			if (this.getConvertMap().containsKey(key)) {
				String commonCode = this.getConvertMap().get(key);
				Map<String, Map<String, String>> commTypeMap = cacheConfig.getCommTypeAndRefDetailMap();
				if(commTypeMap.containsKey(commonCode)) {
					Map<String, String> itemsMap = commTypeMap.get(commonCode);
					if (itemsMap.containsKey(data)) {
						data = itemsMap.get(data);
					}
				}
			} */
			return data;
		}
	}
	private Map<String, Object> refectionMap = new HashMap<String, Object>();
	private Object getDataFromEntity(Object entity, String colName) {
		Class clazz = null;
		java.beans.PropertyDescriptor pd = null;
		if(refectionMap.containsKey(entity.getClass().getName()+ "Clazz")) {
			clazz = (Class)refectionMap.get(entity.getClass().getName()+ "Clazz");
		} else {
			clazz = entity.getClass();
			refectionMap.put(entity.getClass().getName() + "Clazz", clazz);
		}
		Object val = null;
		try {
			if (refectionMap.containsKey(colName)) {
				pd =  (java.beans.PropertyDescriptor)refectionMap.get(colName);
			} else {
				pd =  new PropertyDescriptor(colName, clazz);
				refectionMap.put(colName, pd);
			}
			val = pd.getReadMethod().invoke(entity);
			//ochuan 对象类型为数字类型为空时则显示为0 Double Long BigDecimal
			if(val==null){
				if (pd.getPropertyType().getName().equals("java.lang.Double")
						|| pd.getPropertyType().getName().equals("java.lang.Long")
						|| pd.getPropertyType().getName().equals("java.math.BigDecimal")) {
					val = 0;
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return val;
	}
	/**
	 * 在文档中插入一行空行
	 */
	private void generateBlankRow(WritableSheet sheet) 
							throws RowsExceededException, WriteException{
		int rows = sheet.getRows();
		Map<String, String> header = this.getHeader();
		if (header!=null) {
        	Label l = new Label(0, rows , "", wcfHeader);
			sheet.addCell(l);		
			int j=1;
			Iterator it = header.entrySet().iterator();
			while(it.hasNext()){
            	it.next();
            	l = new Label(j, rows , "", wcfHeader);
				sheet.addCell(l);		
				j++;
			};
		}
	}
	
	/**
	 * 生成表头
	 * @param sheet
	 */
	private void generateTableHead(WritableSheet sheet)  
		throws RowsExceededException, WriteException{
		Map<String, String> header = this.getHeader();
		if (header!=null) {
			Label l = new Label(0, 1, "序号", wcfHeader);
			sheet.addCell(l);
			
			int j=1;
			Iterator it = header.entrySet().iterator();
            while(it.hasNext()) {
            	Map.Entry<String, String> entry =  (Entry<String, String>) it.next();
				String value = entry.getValue();
				//sheet.setColumnView(j, value==null? 15:value.length()*3);
				l = new Label(j, 1, value, wcfHeader);
				sheet.addCell(l);
				j++;
			}
		}
	}

	/**
	 * 根据数据类型设定对齐方式 
	 * @param data
	 * @return
	 */
	private WritableCellFormat getAlignFormat(Object data, String key) {
		if (data==null) return wcfBKCenter;
		if (data instanceof java.lang.Number && !this.getConvertMap().containsKey(key)) {
			if (data.toString().trim().indexOf(".")>0) {
				return wcfBKRight;
			} else {
				return wcfBKCenter;
			}
		} else {
			return wcfBKLeft;
		}
	}
	/**
	 * 合并sheet到一个工作簿
	 * @param osList
	 * @return
	 */
	public ByteArrayOutputStream mergeSheets(List<ByteArrayOutputStream> osList) {
		ByteArrayOutputStream osStream = null;
		if (osList.size()==1) {
			osStream = osList.get(0);
		} else if (osList.size()>1) {
			
			WritableWorkbook workbookStream = null;
			try {
				
				for (int i=0;i<osList.size();i++) {
					ByteArrayOutputStream osi = osList.get(i);
					
					if (osStream!=null) {
						ByteArrayInputStream baseInputStream = new ByteArrayInputStream(osStream.toByteArray());
						Workbook baseWorkBook = Workbook.getWorkbook(baseInputStream);
						osStream =  new ByteArrayOutputStream();
						workbookStream = Workbook.createWorkbook(osStream,baseWorkBook);
					} else {
						osStream =  new ByteArrayOutputStream();
						workbookStream = Workbook.createWorkbook(osStream);
					}
					
					ByteArrayInputStream inputStream = new ByteArrayInputStream(osi.toByteArray());
					Workbook wb = Workbook.getWorkbook(inputStream);
					Sheet sheet = wb.getSheet(0);
					
					WritableSheet wSheet = workbookStream.importSheet("Page" + (i + 1), i, sheet);
					
					workbookStream.write();
					workbookStream.close();
					workbookStream = null;
					wb.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return osStream;
	}
	
	/**
	 * 合并sheet到一个工作簿
	 * @param osList
	 * @return
	 */
	public ByteArrayOutputStream writeLocalFlie(List<ByteArrayOutputStream> osList) {
		ByteArrayOutputStream osStream = null;
		if (osList.size()>1) {
			WritableWorkbook workbookStream = null;
			try {
				for (int i=0;i<osList.size();i++) {
					ByteArrayOutputStream osi = osList.get(i);
					ByteArrayInputStream baseInputStream = new ByteArrayInputStream(osi.toByteArray());
					Workbook baseWorkBook = Workbook.getWorkbook(baseInputStream);
					workbookStream = Workbook.createWorkbook(new File("d:\\excel\\data" + i + ".xls"),baseWorkBook);
					workbookStream.write();
					workbookStream.close();
					workbookStream = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		osStream = new ByteArrayOutputStream();
		return osStream;
	}
	/**
	 * 创建目录
	 * @param dirPath
	 */
	private void createDir(String dirPath) {
		File file = new File(dirPath);
		if (!file.isDirectory()) { 
			file.mkdir();
			//logger.debug("创建" + dirPath + "目录.");
		} else {
			//logger.debug(dirPath + "目录已经存在.");
		}
	}
	/**
	 * 删除生成的文件
	 */
	private void deleteFileAndDir() {
		String xlsFilePath = this.getFilePath() + this.getLocalFileNamePattern();
		this.deleteFile(new File(xlsFilePath));
		String zipFilePath = this.getFilePath() + this.getLocalFileNamePattern() + ".zip";
		this.deleteFile(new File(zipFilePath));
	}
	/**
	 * 删除文件
	 * @param file
	 */
	private void deleteFile(File file){ 
	   if(file.exists()){                    //判断文件是否存在
		   if(file.isFile()){                    //判断是否是文件
			   file.delete();                       //delete()方法
			   //logger.debug("删除文件" + file.getName());
		   } else if(file.isDirectory()){              //否则如果它是一个目录
			   File files[] = file.listFiles();               //声明目录下所有的文件 files[];
			   for(int i=0;i<files.length;i++){            //遍历目录下所有的文件
				   this.deleteFile(files[i]);             //把每个文件 用这个方法进行迭代
				   //logger.debug("删除文件" + files[i].getName());
			   }
			   file.delete();
			   //logger.debug("删除目录" + file.getName());
		   } 
		   
	   } else { 
		   logger.debug("所删除的文件不存在！"); 
	   } 
	} 

	public List getDataList() {
		return dataList;
	}
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public String getLocalFileNamePattern() {
		if (localFileNamePattern==null) {
			localFileNamePattern = this.getFileName() + System.currentTimeMillis();
		}
		return localFileNamePattern;
	}

	public void setLocalFileNamePattern(String localFileNamePattern) {
		this.localFileNamePattern = localFileNamePattern;
	}

	public String getFilePath() {
		filePath = this.getBasePath() + 
				   this.getSysSeparator() + 
				   "tempExcelExport" +
				   this.getSysSeparator();
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSysSeparator() {
		sysSeparator = System.getProperty("file.separator");
		return sysSeparator;
	}

	public void setSysSeparator(String sysSeparator) {
		this.sysSeparator = sysSeparator;
	}

	public String getBasePath() {
		basePath = System.getProperty("user.dir");
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public int getDataStart() {
		return dataStart;
	}
	public void setDataStart(int dataStart) {
		this.dataStart = dataStart;
	}
	public int getDataEnd() {
		return dataEnd;
	}
	public void setDataEnd(int dataEnd) {
		this.dataEnd = dataEnd;
	}

	public String getStrUserName() {
		return strUserName;
	}

	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Map<String, String> getHeader() {
		return header;
	}
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	public String getExcelTitle() {
		return excelTitle;
	}
	public void setExcelTitle(String excelTitle) {
		this.excelTitle = excelTitle;
	}
	public Map<String, String> getConvertMap() {
		return convertMap;
	}
	public void setConvertMap(Map<String, String> convertMap) {
		this.convertMap = convertMap;
	}


	public boolean isNeedFooter() {
		return needFooter;
	}

	public void setNeedFooter(boolean needFooter) {
		this.needFooter = needFooter;
	}


	public Map<String, String> getConvertTableMap() {
		return convertTableMap;
	}

	@Override
	public void setConvertTableMap(Map<String, String> convertTableMap) {
		this.convertTableMap = convertTableMap;
	}
}
