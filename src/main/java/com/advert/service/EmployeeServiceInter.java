package com.advert.service;

import java.util.Map;

import com.advert.entity.Employee;
import com.advert.exception.DaoException;
import com.advert.pageing.BaseService;

public interface EmployeeServiceInter extends BaseService<Employee>{

	public Map<String,Object> query(Map<String,Object> params);
	public void insertSelective() throws DaoException;
}
