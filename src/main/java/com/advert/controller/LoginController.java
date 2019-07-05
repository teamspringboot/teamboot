package com.advert.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.advert.entity.User;
import com.advert.entity.Users;
import com.advert.service.UsersService;
import com.advert.session.WebSecurityConfig;

@Controller
public class LoginController {

	@Autowired
	private UsersService usersService;

	@GetMapping("/index")
	@ResponseBody
	public Map<String,Object> index(
			@SessionAttribute(WebSecurityConfig.SESSION_KEY) String account) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", account);
		return map;
	}

	@GetMapping("/second")
	public String second() {
		return "redirect:/second.html";
	}

	@GetMapping("/login")
	public String login() {
		return "redirect:/login.html";
	}

	@PostMapping("/loginPost")
	public String loginPost(@Valid User user, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		Users users = usersService.queryUsers(user);
		if (users != null) {
			if (users.getUsername().equals(user.getUserName())
					&& users.getPassword().equals(user.getUserPwd())) {
				// 设置session
				session.setAttribute(WebSecurityConfig.SESSION_KEY,
						user.getUserName());
				return "redirect:/hello.html";
			} else {
				return "redirect:/login.html";
			}
		} else {
			return "redirect:/login.html";
		}

	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		// 移除session
		session.removeAttribute(WebSecurityConfig.SESSION_KEY);
		return "redirect:/login";
	}
}