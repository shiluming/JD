package com.xn.dao;

import java.util.List;

import com.xn.model.IndexModel;
import com.xn.model.PageBean;

public interface IndexDao {

	public void changeFlag(String id);
	
	public void add(IndexModel iModel);
	
	public int count();
	
	public List<IndexModel> getData(PageBean pageBean,String current);
}
