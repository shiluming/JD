package com.xn.dao;

import java.util.List;

import com.xn.model.PageBean;
import com.xn.model.Product;

public interface DataDao {

	public List<Product> getData(PageBean pageBean, Product s_product);
	
	public int count();
	
	public List<Product> getDataForComments(Product p);	
	
	public void addComments(String contents,String itemid);
	
}
