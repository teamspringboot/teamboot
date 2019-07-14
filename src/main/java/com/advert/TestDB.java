package com.advert;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.advert.service.EmployeeServiceInter;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class TestDB {
	@Autowired
	private EmployeeServiceInter employeeServiceInter;
	@Test
	public void test() {
		Map<String,Object> map = employeeServiceInter.query(new HashMap<String,Object>());
		System.err.println(map);
	}
}
