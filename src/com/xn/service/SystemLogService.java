package com.xn.service;

import java.util.List;

import com.xn.model.LogModel;
import com.xn.model.PageBean;

public interface SystemLogService {

	public void add(LogModel log);
	
	public List<LogModel> list(PageBean pageBean, String query);
	
	public int count(String query);
	
	public void delete(int id);
}
