package com.xn.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xn.dao.DataDao;
import com.xn.model.PageBean;
import com.xn.model.Product;
import com.xn.service.DataService;

@Service("dataService")
public class DataServiceImpl implements DataService {

	@Resource
	private DataDao dataDao;
	
	@Override
	public List<Product> getData(PageBean pageBean, Product s_product) {
		return dataDao.getData(pageBean, s_product);
	}

	@Override
	public int count() {
		return dataDao.count();
	}

	@Override
	public List<Product> getDataForComments(Product p) {
		
		return dataDao.getDataForComments(p);
	}

	@Override
	public void addComments(String contents, String itemid) {
		
	}

}
