package com.advert.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.advert.entity.Employee;
import com.advert.exception.Response;
import com.advert.importexcel.ImportExcel;
import com.advert.service.EmployeeServiceInter;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/emp")
public class EmployeeController extends ExportController<Employee>{

	@Autowired
	private EmployeeServiceInter employeeServiceInter;
	
	@RequestMapping(value="/employee",method=RequestMethod.GET)
	public Map<String,Object> query(){
		Map<String, Object> params = preparePageParams();
		return employeeServiceInter.query(params);
	}
	
	@RequestMapping(value="/employee",method=RequestMethod.POST)
	public Response add(){
		 employeeServiceInter.insertSelective();
		 return new Response().success();
	}
	
	@RequestMapping(value="/page",method=RequestMethod.GET)
	public PageInfo<Employee> queryPage(){
		Map<String, Object> paramMap = preparePageParams();
		PageInfo<Employee> page = employeeServiceInter.findByPage(paramMap);
		return page;
	}
	@RequestMapping("/upLoadSlab")
	public Response upLoadMess(@RequestParam("slabUpload") MultipartFile file) {
		String importFlag = "upLoadSlab";
		List<String[]> list = null;
		try {
			list = ImportExcel.readExcel(file, importFlag);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//校验
		//employeeServiceInter.insertSelective(list);
		return new Response().success(); 
	}
}
