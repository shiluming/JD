package com.xn.dao;

import java.util.List;

import com.xn.model.PageBean;
import com.xn.model.URLModel;

public interface URLDao {

	public int usefullCount();
	public int unusefullCount();
	
	public List<URLModel> getUseFullData(PageBean pageBean);
	
	public List<URLModel> getunUseFullData(PageBean pageBean);
}
