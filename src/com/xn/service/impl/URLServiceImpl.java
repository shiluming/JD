package com.xn.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xn.dao.URLDao;
import com.xn.model.PageBean;
import com.xn.model.URLModel;
import com.xn.service.URLService;

@Service("urlSerive")
public class URLServiceImpl implements URLService{

	@Resource
	private URLDao urlDao;
	
	@Override
	public int usefullCount() {
		return urlDao.usefullCount();
	}

	@Override
	public int unusefullCount() {
		return urlDao.unusefullCount();
	}

	@Override
	public List<URLModel> getUseFullData(PageBean pageBean) {
		return urlDao.getUseFullData(pageBean);
	}

	@Override
	public List<URLModel> getunUseFullData(PageBean pageBean) {
		return urlDao.getunUseFullData(pageBean);
	}

	
}
