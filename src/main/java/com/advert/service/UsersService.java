package com.advert.service;

import com.advert.entity.User;
import com.advert.entity.Users;


public interface UsersService {

	public void insert(Users users);
	public Users queryUsers(User user);
}
