package com.xn.dao;

import java.util.List;

import com.xn.model.PageBean;
import com.xn.model.User;

public interface UserDao {

	public User login(User user);
	
	public String test();
	
	public void delete(int id);
	
	public void add(User user);

	public void update(User user);
	
	public List<User> find(PageBean pageBean,User s_user);
	
	public int count(User s_user);
	
	public User loadById(int id);
	
	public User loadByUserName(String username);
	
}
