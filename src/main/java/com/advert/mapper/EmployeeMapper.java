package com.advert.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.annotation.Transactional;

import com.advert.entity.Employee;
import com.advert.pageing.BaseMapper;
public interface EmployeeMapper extends BaseMapper<Employee>{
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);
    List<Employee> query(Map<String,Object> params);
}