package com.advert.exportexcel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advert.pageing.AppServiceHelper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class ExportExcel2007ByDefault implements IExportExcel {
	private static final Logger logger = LoggerFactory
			.getLogger(ExportExcelByDefault.class);

	private int lastColumns = 4;// 最后一行总列数
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

	private XSSFCellStyle styleTableName = null; // 表格名称样式
	private XSSFCellStyle styleHeader = null; // 表头样式
	private XSSFCellStyle styleData = null; // 数据样式
	private XSSFCellStyle styleNumber = null; // 数字样式
	private XSSFCellStyle styleDate = null; // 日期样式

	private XSSFCreationHelper helper = null;

	private XSSFCellStyle styleFooterLeft = null;// 靠左

	private XSSFCellStyle styleFooterRight = null;// 靠右

	private XSSFCellStyle styleFooterCenter = null;// 居中

	private boolean needFooter = false;

	private XSSFWorkbook workbook;

	private void initCellFormat(XSSFWorkbook workbook) {
		try {
			this.workbook = workbook;
			helper = workbook.getCreationHelper();
			styleTableName = workbook.createCellStyle();
			styleTableName.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			styleTableName.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleTableName.setLocked(true);
			styleTableName.setWrapText(true);
			styleTableName.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT
					.getIndex());
			setCellBorder(styleTableName);
			XSSFFont font = workbook.createFont();
			styleTableName.setFont(font);
			font.setFontName("DFBK-W5-FPG");
			font.setFontHeightInPoints((short) 14);// 设置字体大小
			font.setBold(true);

			styleHeader = workbook.createCellStyle();
			styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			styleHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleHeader.setLocked(true);
			styleHeader.setWrapText(true);
			styleHeader.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT
					.getIndex());
			setCellBorder(styleHeader);
			XSSFFont headerFont = workbook.createFont();
			styleHeader.setFont(headerFont);
			headerFont.setFontName("DFBK-W5-FPG");
			headerFont.setFontHeightInPoints((short) 11);// 设置字体大小
			headerFont.setBold(true);

			styleData = workbook.createCellStyle();
			styleData.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleData.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setCellBorder(styleData);
			XSSFFont dataFont = workbook.createFont();
			styleData.setFont(dataFont);
			dataFont.setFontName("DFBK-W5-FPG");
			dataFont.setFontHeightInPoints((short) 10);// 设置字体大小

			styleNumber = workbook.createCellStyle();
			styleNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			styleNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setCellBorder(styleNumber);
			XSSFFont numFont = workbook.createFont();
			styleNumber.setFont(numFont);
			numFont.setFontName("DFBK-W5-FPG");
			numFont.setFontHeightInPoints((short) 10);// 设置字体大小

			XSSFDataFormat df = workbook.createDataFormat();
			styleNumber.setDataFormat(df.getFormat("0"));

			styleDate = workbook.createCellStyle();
			styleDate.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			styleDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setCellBorder(styleDate);
			XSSFFont dateFont = workbook.createFont();
			styleDate.setFont(dateFont);
			dateFont.setFontName("DFBK-W5-FPG");
			dateFont.setFontHeightInPoints((short) 10);// 设置字体大小

			df = workbook.createDataFormat();
			styleDate.setDataFormat(df.getFormat("yyyy-MM-dd HH:mm:ss"));

			XSSFCellStyle styleFloat = getStyleFloat();

			df = workbook.createDataFormat();
			// styleFloat.setDataFormat(df.getFormat("0.0000"));

			XSSFFont footerFont = workbook.createFont();
			footerFont.setFontName("DFBK-W5-FPG");
			footerFont.setFontHeightInPoints((short) 10);// 设置字体大小
			footerFont.setBold(true);

			styleFooterLeft = workbook.createCellStyle();
			styleFooterLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleFooterLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleFooterLeft.setFont(footerFont);

			styleFooterRight = workbook.createCellStyle();
			styleFooterRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			styleFooterRight
					.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleFooterRight.setFont(footerFont);

			styleFooterCenter = workbook.createCellStyle();
			styleFooterCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			styleFooterCenter
					.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleFooterCenter.setFont(footerFont);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setCellBorder(XSSFCellStyle cellStyle) {
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	}

	/**
	 * Excel工作簿中生成sheet(只有一页的时候)
	 * 
	 * @param title
	 * @return
	 */
	public ByteArrayOutputStream createExportXlsFileStream() {
		long time1 = System.currentTimeMillis();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			XSSFWorkbook wb = new XSSFWorkbook();
			// 创建第一个sheet
			XSSFSheet sheet = wb.createSheet("mySheet");
			this.getExportData(wb, sheet);
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long time2 = System.currentTimeMillis();
		logger.debug("Create export stream time consuming: " + (time2 - time1)
				+ " ms");
		return os;
	}

	/**
	 * 压缩xls文件
	 * 
	 * @param num
	 * @return
	 */
	public ByteArrayOutputStream createZipFileStream(int num) {
		// 压缩后zip文件名
		String zipFileName = this.getFilePath()
				+ this.getLocalFileNamePattern() + ".zip";
		// 压缩目标，是一个目录
		String inputFileName = this.getFilePath()
				+ this.getLocalFileNamePattern();

		try {
			ZipUtil.ZipFile(zipFileName, inputFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(zipFileName));
			int count;
			byte data[] = new byte[1024 * 1024];
			while ((count = in.read(data, 0, 1024 * 1024)) != -1) {
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
	 * 
	 * @param title
	 * @return
	 */
	public void createLocalExportFile(int i) {
		long time1 = System.currentTimeMillis();
		// 创建文件存放路径
		this.createDir(this.getFilePath());
		this.createDir(this.getFilePath() + this.getLocalFileNamePattern());
		String xlsFileName = this.getFilePath()
				+ this.getLocalFileNamePattern() + this.getSysSeparator()
				+ this.getLocalFileNamePattern() + "_" + (i + 1) + ".xlsx";

		try {
			OutputStream os = new FileOutputStream(xlsFileName);
			XSSFWorkbook wb = new XSSFWorkbook();
			// 创建第一个sheet
			XSSFSheet sheet = wb.createSheet("mySheet");
			this.getExportData(wb, sheet);
			wb.write(os);
			// 关闭输出流
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long time2 = System.currentTimeMillis();
		// logger.debug("创建本地文件 " + xlsFileName + " 用时：" + (time2-time1) +
		// "毫秒");
	}

	private void getExportData(XSSFWorkbook wb, XSSFSheet sheet) {
		// 初始化样式
		initCellFormat(wb);
		// 设置Excel的标题
		// this.generateTableTitle(sheet);
		// 设置表头
		this.generateTableHead(sheet);
		// 输出数据
		this.generateTableData(sheet);
		// if(needFooter){
		// //插入一行空行，便于文档使用人员进行列的合计等操作
		// this.generateBlankRow(sheet);
		// //插入备注行
		// this.generateCommentRow(wb, sheet);
		// //插入制表人行
		// this.generateTableFooter(sheet);
		// }

	}

	private void generateTableTitle(XSSFSheet sheet) {

		int totalColumns = this.getHeader().size();
		// 设置Excel文档标题
		String title = this.excelTitle;

		XSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(30);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalColumns));
		XSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(styleTableName);
		titleCell.setCellValue(title);
	}

	private void generateTableFooter(XSSFSheet sheet) {
		XSSFRow footRow;
		int rows = sheet.getLastRowNum();
		footRow = sheet.createRow(rows + 1);
		XSSFCell exportByTitleCell = footRow.createCell(0);
		exportByTitleCell.setCellStyle(styleFooterRight);
		exportByTitleCell.setCellValue("制表人:");
		XSSFCell exportByCell = footRow.createCell(1);
		exportByCell.setCellStyle(styleFooterLeft);
		exportByCell.setCellValue(this.getStrUserName());

		XSSFCell exportDateTitleCell = footRow.createCell(2);
		exportDateTitleCell.setCellStyle(styleFooterRight);
		exportDateTitleCell.setCellValue("制表日期:");
		XSSFCell exportDateCell = footRow.createCell(3);
		exportDateCell.setCellStyle(styleFooterLeft);
		exportDateCell.setCellValue((new Date()).toLocaleString());
	}

	private int generateCommentRow(XSSFWorkbook wb, XSSFSheet sheet) {
		int totalColumns = this.getHeader().size();
		// footer
		int rows = sheet.getLastRowNum() + 1;
		XSSFRow footRow = sheet.createRow(rows);
		XSSFCell commentCell = footRow.createCell(0);
		commentCell.setCellStyle(styleFooterCenter);
		commentCell.setCellValue("备注:");

		footRow = sheet.createRow(rows + 1);
		footRow.createCell(1);
		CellRangeAddress commentTitleRegion = new CellRangeAddress(rows,
				rows + 1, 0, 0);
		sheet.addMergedRegion(commentTitleRegion);
		setRegionBorder(CellStyle.BORDER_THIN, commentTitleRegion, sheet, wb);

		CellRangeAddress commentRegion = new CellRangeAddress(rows, rows + 1,
				1, totalColumns);
		sheet.addMergedRegion(commentRegion);
		setRegionBorder(CellStyle.BORDER_THIN, commentRegion, sheet, wb);
		return rows;
	}

	private static void setRegionBorder(int border, CellRangeAddress region,
			Sheet sheet, Workbook wb) {
		RegionUtil.setBorderBottom(border, region, sheet, wb);
		RegionUtil.setBorderLeft(border, region, sheet, wb);
		RegionUtil.setBorderRight(border, region, sheet, wb);
		RegionUtil.setBorderTop(border, region, sheet, wb);

	}

	/**
	 * 生成数据
	 * 
	 * @param sheet
	 * 
	 */

	private void generateTableData(XSSFSheet sheet) {

		Map<Integer, Integer> columnWidthMap = new HashMap<Integer, Integer>();
		XSSFDataFormat df = sheet.getWorkbook().createDataFormat();

		// 整数格式
		List dList = this.getDataList();
		int startRow = sheet.getLastRowNum() + 1;
		for (int i = this.getDataStart(); i < this.getDataEnd(); i++) {
			XSSFRow row = sheet.createRow(startRow);
			// 插入序号列
			XSSFCell dataCell = row.createCell(0);
			dataCell.setCellStyle(styleNumber);
			dataCell.setCellValue(i + 1);

			Object entity = dList.get(i);
			int k = 1;
			Map<String, String> header = this.getHeader();
			Iterator it = header.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) it.next();
				String key = entry.getKey();
				Object dataObj = null;
				if (entity != null) {
					if (entity instanceof Map) {// Map直接取值
						dataObj = ((Map) entity).get(key);
					} else {
						dataObj = getDataFromEntity(entity, key);
					}
				}
				String data = null;
				if (dataObj == null) {
					data = "";
				} else {
					data = formatDataObj(dataObj, key);
				}

				// 计算列的最大宽度
				Integer maxDataLength = columnWidthMap.get(k);
				if (maxDataLength == null) {
					maxDataLength = data.length();
					columnWidthMap.put(k, maxDataLength);
				} else if (maxDataLength < data.length()) {
					maxDataLength = data.length();
					columnWidthMap.put(k, maxDataLength);
				}

				dataCell = row.createCell(k);
				try {
					int dotPos = data.indexOf(".");
					if (dotPos > 0) {
						Integer decimalLenth = data.length() - dotPos - 1;
						// XSSFWorkbook currWb = sheet.getWorkbook();
						// XSSFCellStyle styleFloat = currWb.createCellStyle();
						// styleFloat.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						// styleFloat
						// .setVerticalAlignment(HorizontalAlignment.CENTER);
						// setCellBorder(styleFloat);
						// XSSFFont floatFont = currWb.createFont();
						// styleFloat.setFont(floatFont);
						// floatFont.setFontName("DFBK-W5-FPG");
						// floatFont.setFontHeightInPoints((short) 10);// 设置字体大小

						XSSFDataFormat floatDataFormat = helper
								.createDataFormat();
						BigDecimal decimalVal = new BigDecimal(data);
						double fNum = decimalVal.setScale(decimalLenth,
								BigDecimal.ROUND_HALF_UP).doubleValue();
						// styleFloat.setDataFormat(floatDataFormat.getFormat("0."
						// + StringUtils.repeat('0', decimalLenth)));
						dataCell.setCellStyle(styleData);
						dataCell.setCellValue(fNum);
					} else {
						if ((dataObj instanceof Number)) {
							Long fNum = Long.parseLong(data);
							dataCell.setCellStyle(styleNumber);
							dataCell.setCellValue(fNum);
						} else if ((dataObj instanceof java.sql.Timestamp)) {
							java.sql.Timestamp timeStampObj = (java.sql.Timestamp) dataObj;
							Date dt = new Date(timeStampObj.getTime());
							dataCell.setCellStyle(styleDate);
							dataCell.setCellValue(dt);
						} else {
							dataCell.setCellStyle(styleData);
							dataCell.setCellValue(data);
						}
					}
				} catch (Exception e) {
					dataCell.setCellStyle(styleData);
					dataCell.setCellValue(data);
				}

				k++;
			}
			startRow++;
		}

		for (int columnCnt = 0; columnCnt < this.getHeader().size(); columnCnt++) {
			Integer length = columnWidthMap.get(columnCnt);
			if (length == null) {
				length = 5;
			}
			length = length > 255 ? 200 : length;
			length = length < 15 ? 15 : length;
			sheet.autoSizeColumn(columnCnt + 1);
			// sheet.setColumnWidth(columnCnt, length*256);
		}
	}

	private XSSFCellStyle getStyleFloat() {
		XSSFCellStyle styleFloat = workbook.createCellStyle();
		styleFloat.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleFloat.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		setCellBorder(styleFloat);
		XSSFFont floatFont = workbook.createFont();
		styleFloat.setFont(floatFont);
		floatFont.setFontName("DFBK-W5-FPG");
		floatFont.setFontHeightInPoints((short) 10);// 设置字体大小
		return styleFloat;
	}

	private Map<String, Map<String, String>> commonTypesMap = null;

	private String formatDataObj(Object obj, String key) {
		if (obj instanceof Date) {
			String format = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(obj);
		} else {
			
			String data = obj.toString().trim();
			/*CacheConfig cacheConfig = (CacheConfig) AppServiceHelper
					.findBean("cacheConfig");
			if (this.getConvertMap().containsKey(key)) {
				String commonCode = this.getConvertMap().get(key);
				if (commonTypesMap == null) {
					commonTypesMap = cacheConfig.getCommTypeAndRefDetailMap();
				}
				if (commonTypesMap.containsKey(commonCode)) {
					Map<String, String> itemsMap = commonTypesMap
							.get(commonCode);
					if (itemsMap.containsKey(data)) {
						data = itemsMap.get(data);
					}
				}
			}*/
			return data;
		}
	}

	private Map<String, Object> refectionMap = new HashMap<String, Object>();

	private Object getDataFromEntity(Object entity, String colName) {
		Class clazz = null;
		PropertyDescriptor pd = null;
		if (refectionMap.containsKey(entity.getClass().getName() + "Clazz")) {
			clazz = (Class) refectionMap.get(entity.getClass().getName()
					+ "Clazz");
		} else {
			clazz = entity.getClass();
			refectionMap.put(entity.getClass().getName() + "Clazz", clazz);
		}
		Object val = null;
		try {
			if (refectionMap.containsKey(colName)) {
				pd = (PropertyDescriptor) refectionMap.get(colName);
			} else {
				pd = new PropertyDescriptor(colName, clazz);
				refectionMap.put(colName, pd);
			}
			val = pd.getReadMethod().invoke(entity);
			// ochuan 对象类型为数字类型为空时则显示为0 Double Long BigDecimal
			if (val == null) {
				if (pd.getPropertyType().getName().equals("java.lang.Double")
						|| pd.getPropertyType().getName()
								.equals("java.lang.Long")
						|| pd.getPropertyType().getName()
								.equals("java.math.BigDecimal")) {
					val = "0.0";
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			// e.printStackTrace();
		}
		return val;
	}

	/**
	 * 在文档中插入一行空行
	 */
	private void generateBlankRow(XSSFSheet sheet) {
		int rows = sheet.getLastRowNum() + 1;
		// 生成第一行
		XSSFRow row = sheet.createRow(rows);
		Map<String, String> header = this.getHeader();
		if (header != null) {
			XSSFCell blankCell = row.createCell(0);
			blankCell.setCellStyle(styleData);
			blankCell.setCellValue("");
			int j = 1;
			Iterator it = header.entrySet().iterator();
			while (it.hasNext()) {
				it.next();
				blankCell = row.createCell(j);
				blankCell.setCellStyle(styleData);
				blankCell.setCellValue("");
				j++;
			}
			;
		}
	}

	/**
	 * 生成表头
	 * 
	 * @param sheet
	 */
	private void generateTableHead(XSSFSheet sheet) {
		Map<String, String> header = this.getHeader();
		int rows = sheet.getLastRowNum();
		if (header != null) {
			// 生成第一行
			XSSFRow row = sheet.createRow(rows);

			XSSFCell headerCell = row.createCell(0);
			headerCell.setCellStyle(styleHeader);
			headerCell.setCellValue("序号");

			// 给这一行的第一列赋值
			int j = 1;
			Iterator it = header.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) it.next();
				String value = entry.getValue();
				headerCell = row.createCell(j);
				headerCell.setCellStyle(styleHeader);
				headerCell.setCellValue(value);
				j++;
			}
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param dirPath
	 */
	private void createDir(String dirPath) {
		File file = new File(dirPath);
		if (!file.isDirectory()) {
			file.mkdir();
			// logger.debug("创建" + dirPath + "目录.");
		} else {
			// logger.debug(dirPath + "目录已经存在.");
		}
	}

	/**
	 * 删除生成的文件
	 */
	private void deleteFileAndDir() {
		String xlsFilePath = this.getFilePath()
				+ this.getLocalFileNamePattern();
		this.deleteFile(new File(xlsFilePath));
		String zipFilePath = this.getFilePath()
				+ this.getLocalFileNamePattern() + ".zip";
		this.deleteFile(new File(zipFilePath));
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	private void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法
				// logger.debug("删除文件" + file.getName());
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
					// logger.debug("删除文件" + files[i].getName());
				}
				file.delete();
				// logger.debug("删除目录" + file.getName());
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
		if (localFileNamePattern == null) {
			localFileNamePattern = this.getFileName()
					+ System.currentTimeMillis();
		}
		return localFileNamePattern;
	}

	public void setLocalFileNamePattern(String localFileNamePattern) {
		this.localFileNamePattern = localFileNamePattern;
	}

	public String getFilePath() {
		filePath = this.getBasePath() + this.getSysSeparator()
				+ "tempExcelExport" + this.getSysSeparator();
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
