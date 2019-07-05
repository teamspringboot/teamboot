package com.advert.util.excel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;


public class ExcelUtil {        
    private static final int BUFFER_SIZE = 16 * 1024;

    
    /**
     * 把文件拷贝到指定的路径
     * @param srcFile
     * @param directory
     * @param fileName
     * @throws FileNotFoundException
     * @throws IOException
     * @author hxuan
     * @since  Jul 9, 2010
     */
    public static void copyExcel(File srcFile, String directory, String fileName)throws FileNotFoundException,IOException {
        // 判断路径是否存在，不存在则创建路径
        File destinationFile = new File(directory);
        boolean createDirResult = true;
        if (!destinationFile.isDirectory()) {
            createDirResult = destinationFile.mkdirs();
        }
        
        if(!createDirResult){
            throw new IOException("could not create directory.");
        }
        
        // 存放文件模板的绝对路径        
        String createdFile = directory + "/" + fileName +".xls";
        destinationFile = new File(createdFile);

        copyFile(srcFile, destinationFile);// 上传模板        
    }
    
    private static void copyFile(File src, File dst)throws FileNotFoundException, IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src),
                    BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dst),
                    BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (in.read(buffer) > 0) {
                out.write(buffer);
            }
        } finally {
            if (null != in) {
                in.close();
            }
            if (null != out) {
                out.close();
            }
        }
    }
    
    /**
     * 从excel表格中取值,对于单元格是公式的直接获取公式
     * @param cell
     * @return
     * @author hxuan
     * @since  Jul 9, 2010
     */
    public static String getCellValue(HSSFCell cell) {
        String value = null;
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_NUMERIC:
            value = String.valueOf(cell.getNumericCellValue()).trim();
            break;
        case HSSFCell.CELL_TYPE_STRING:
            value = cell.getStringCellValue();
            break;
        case HSSFCell.CELL_TYPE_FORMULA:
            value = cell.getCellFormula();
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            value = String.valueOf(cell.getBooleanCellValue());
            break;
        }

        return value;
    }     
    
    
   
   /**
    *   从excel表格中取值,对于单元格是公式的获取计算后的值
    * @param fe
    * @param cell
    * @return
    */
    public static String getCellValueFormula(FormulaEvaluator fe, Cell cell) {	
        String value = null;
        if (cell == null) {
            return null;
        }
        int type=cell.getCellType();
        
        switch (type) {
        case HSSFCell.CELL_TYPE_NUMERIC:
        	value = String.valueOf(cell.getNumericCellValue()).trim();
            break;
        case HSSFCell.CELL_TYPE_STRING:
            value = cell.getStringCellValue();
            break;
        case HSSFCell.CELL_TYPE_FORMULA:
        	Cell tmpCell=fe.evaluateInCell(cell);
        	value = String.valueOf(tmpCell.getNumericCellValue()).trim();
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            value = String.valueOf(cell.getBooleanCellValue());
            break;
        }
        return value;
    }     
    
}
