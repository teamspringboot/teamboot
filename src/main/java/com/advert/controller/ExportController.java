package com.advert.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.advert.exportexcel.ExportExcel;
import com.advert.util.entity.AbstractEntity;
import com.github.pagehelper.PageInfo;


public class ExportController<T extends AbstractEntity> extends BaseController<T> {

	private static final  Logger logger = LoggerFactory.getLogger(ExportController.class);
	/**
	 * 导出全部数据
	 * @param excelTitle
	 * @param excelName
	 * @param excel2007
	 * @return
	 */
	@RequestMapping("/exportExcel")
    public String exportExcel(@RequestParam(value="excelTitle", defaultValue="导出数据列表") String excelTitle,
    		@RequestParam(value="excelName", defaultValue="export_data_") String excelName,
    		@RequestParam(value="excel2007", defaultValue="true") Boolean excel2007) {
    	try {
    		List<T> dataList = getData();
    		this.exportExcelWrap(excelTitle, excelName, excel2007, dataList);
    		return "exportExcel success";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("exportExcel  error log start---------");
			logger.error(e.getMessage(), e);
			logger.error(e.getMessage(), e.getStackTrace());
			logger.error("exportExcel  error log end---------");
			return "exportExcel failure";
		}
    }
	/**
	 * 导出当前页数据
	 * @param excelTitle
	 * @param excelName
	 * @param excel2007
	 * @return
	 */
	@RequestMapping("/exportExcelByPage")
    public String exportExcelByPage(@RequestParam(value="excelTitle", defaultValue="导出数据列表") String excelTitle,
    		@RequestParam(value="excelName", defaultValue="export_data_") String excelName,
    		@RequestParam(value="excel2007", defaultValue="true") Boolean excel2007) {
    	try {
    		List<T> dataList = getDataByPage();
    		this.exportExcelWrap(excelTitle, excelName, excel2007, dataList);
    		return "exportExcel success";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("exportExcel  error log start---------");
			logger.error(e.getMessage(), e);
			logger.error(e.getMessage(), e.getStackTrace());
			logger.error("exportExcel  error log end---------");
			return "exportExcel failure";
		}
    }
	/**
	 * updated by wangfei 代码迁移,方便子类自定义导出excel方法的调用
	 */
	protected void exportExcelWrap(String excelTitle, String excelName, Boolean excel2007, List dataList) {
		OutputStream os = null;
		ByteArrayOutputStream baos = null;
		try {
    		String realPath = request.getSession().getServletContext().getRealPath("/");   	
    		String fileName =excelName+ System.currentTimeMillis();
    		LinkedHashMap<String,String>  exportColumns = this.getExportColumns();
    		Map<String, String> convertMap = this.getConvertMap();
    		ExportExcel exportExcel = new ExportExcel();
    		exportExcel.setFileName(fileName);
    		exportExcel.setRealPath(realPath);
    		exportExcel.setExcelTitle(excelTitle);
    		baos = exportExcel.getExportStream(dataList, exportColumns, convertMap,excel2007);
    		response.reset();
    		response.setContentType("application/vnd.ms-excel; charset=utf-8");
    		response.setHeader("Content-Disposition",
    				"attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + exportExcel.getFileSurrfix());
			// add by liuxing@20190315 START
			// 修复打开excel时报"部分内容有问题,尝试恢复"提示的问题
			response.setCharacterEncoding("UTF-8");
			response.setContentLength(baos.size());
			// add by liuxing@20190315 END
    		os = response.getOutputStream();
    		baos.writeTo(os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    /**
     * 查询数据
     * @return
     */
    protected List<T> getData() {
    	Map<String, Object> params = getParams();
    	List<T> list = (List<T>)getBaseService().find(params);
    	return list;
    }
    
    protected List<T> getDataByPage() {
    	Map<String, Object> params = getParams();
    	PageInfo<T> page = (PageInfo<T>)getBaseService().findByPage(params);
    	List<T> list = page.getList();
    	return list;
    }
    /**
     * 获取导出列数组
     * @return
     */
    private LinkedHashMap<String,String> getExportColumns() {
    	LinkedHashMap<String,String> colsMap = new LinkedHashMap<String,String>();
    	Map<String, Object> params = preparePageParams();
    	if (params.containsKey("col")) {
    		String exportCols = (String)params.get("col");//eMsg,matId,planId,nextSubBacklogCodeDesc 对应的实体类名称
    		String exportColsDesc = (String)params.get("desc");//异常,钢板号,计划号,下道工序		     页面显示名称
    		String[] columns = exportCols.split(",");
    		String[] descs = exportColsDesc.split(",");
    		for(int i=0;i<columns.length;i++) {
    			colsMap.put(columns[i], descs[i]);
    		}
    	}
    	return colsMap;
    }
    
    private Map<String, String> getConvertMap() {
    	Map<String, String> convertMap = new HashMap<String, String>();
    	Map<String, Object> params = getParams();
    	if (params.containsKey("convertCol")) {
    		String convertCol = (String)params.get("convertCol");
    		String[] columns = convertCol.split(",");
    		for(int i=0;i<columns.length;i++) {
        		String[] pair = columns[i].split(":");
        		convertMap.put(pair[0], pair[1]);
    		}
    	}
    	//logger.debug(convertMap);
    	return convertMap;
    }
    /**
     * 获取查询参数集合
     * @return
     */
    private Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		Set<Map.Entry<String, String>> p = qm.entrySet();
		for (Map.Entry<String, String> me : p) {
			String name = me.getKey();
			String value = me.getValue();
			if (StringUtils.isNotEmpty(value)) {
				//加入逗号还原数组查询
				String commaName=name+"Comma";
				value=value.replace("，", ",");
				String[] commaValueArr=value.split(",");
				params.put(commaName, commaValueArr);
				params.put(name, value);//new String(value.getBytes("ISO-8859-1"), "UTF-8"));
			}
		}
		return params;
    }
}
