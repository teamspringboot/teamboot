package com.advert.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advert.entity.Users;
import com.advert.service.UsersService;

@RestController
public class UsersController {
	private final static Map<String,Object> map = new HashMap<String,Object>();
	@Autowired
	private UsersService usersService;
	@PostMapping("/users")
	public Map<String,Object> addUsers(Users users){
		map.clear();
		try{
			usersService.insert(users);
			map.put("status", "200");
			map.put("message", "注册成功");
		}catch(Exception e){
			map.put("status", "500");
			map.put("message", "注册失败"+e.getMessage());
		}
		return map;
	}
}
