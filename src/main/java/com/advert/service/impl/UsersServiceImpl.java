package com.advert.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advert.entity.User;
import com.advert.entity.Users;
import com.advert.mapper.UsersMapper;
import com.advert.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {

	private final static Map<String,Object> map = new HashMap<String,Object>();
	@Autowired
	private UsersMapper usersMapper;
	@Override
	public void insert(Users users) {
		usersMapper.insert(users);
		
	}
	@Override
	public Users queryUsers(User user) {
		map.clear();
		map.put("username", user.getUserName());
		map.put("password", user.getUserPwd());
		return usersMapper.selectByPrimaryKey(map);
	}

}
