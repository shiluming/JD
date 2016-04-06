package com.xn.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xn.dao.DataAnalyseDao;
import com.xn.service.DataAnalyseService;

@Service("dataAnalyseService")
public class DataAnalyseServiceImpl implements DataAnalyseService {

	@Resource
	private DataAnalyseDao dataAnalyseDao;
	
	@Override
	public int[] priceAnalyse() {
		
		return dataAnalyseDao.priceAnalyse();
	}

}
