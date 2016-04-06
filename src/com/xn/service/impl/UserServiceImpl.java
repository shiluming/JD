package com.xn.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xn.dao.UserDao;
import com.xn.model.PageBean;
import com.xn.model.User;
import com.xn.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	@Override
	public User login(User user) {
		// TODO Auto-generated method stub
		return userDao.login(user);
	}
	
	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		userDao.delete(id);
	}

	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
		userDao.add(user);
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		userDao.update(user);
	}

	@Override
	public List<User> find(PageBean pageBean, User s_user) {

		return userDao.find(pageBean, s_user);
	}

	@Override
	public int count(User s_user) {
		return userDao.count(s_user);
	}

	@Override
	public User loadById(int id) {
		// TODO Auto-generated method stub
		return userDao.loadById(id);
	}

	@Override
	public User loadByUserName(String username) {
		return userDao.loadByUserName(username);
	}
}
