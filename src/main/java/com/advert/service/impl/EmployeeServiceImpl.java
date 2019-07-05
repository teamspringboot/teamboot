package com.advert.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advert.entity.Employee;
import com.advert.exception.DaoException;
import com.advert.mapper.EmployeeMapper;
import com.advert.pageing.PageingService;
import com.advert.service.EmployeeServiceInter;

@Service
@Transactional
/**
 * 事物控制,需要添加@Transactional注解,每个方法需要抛出或捕捉运行时异常(RuntimeException)
 * @author Administrator
 *
 */
public class EmployeeServiceImpl extends PageingService<Employee> implements EmployeeServiceInter{

	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Override
	public Map<String,Object> query(Map<String,Object> params) {
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			List<Employee> employeeList = employeeMapper.query(params);
			map.put("list", employeeList);
			map.put("message", "查询成功！");
		}catch(Exception e){
			map.put("message", "查询失败！"+e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
	@Override
	public void insertSelective() throws DaoException {
		for(int i=1;i<=10;i++){
			Employee employee = new Employee();
			employee.setClasses("测试祝明classes"+i);
			employee.setOffice("测试祝明office"+i);
			employee.setSalary(new BigDecimal("8959.56").add(new BigDecimal(i)));
			employeeMapper.insertSelective(employee);
		}
		int num=9/0;
	}
	
}
