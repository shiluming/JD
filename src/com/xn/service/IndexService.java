package com.xn.service;

import java.util.List;

import com.xn.model.IndexModel;
import com.xn.model.PageBean;
import com.xn.model.Product;

public interface IndexService {

	public void changeFlag(String id);
	
	public String deleteIndex();
	
	public String createIndex();
	
	public void addIndex(IndexModel iModel);
	
	public int count();
	
	public List<IndexModel> getData(PageBean pageBean,String current);
	
	public String search(String searchField,String query);
	
	public List<Product> searchForObject(String searchField,String query);
}
