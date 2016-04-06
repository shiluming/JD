package com.xn.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xn.dao.InterfaceDao;
import com.xn.model.InterfaceModel;
import com.xn.service.InterfaceService;

@Service("interfaceService")
public class InterfaceServiceImpl implements InterfaceService {

	@Resource
	private InterfaceDao interfaceDao;
	
	@Override
	public List<InterfaceModel> list() {
		return interfaceDao.list();
	}

	@Override
	public boolean open(String name) {
		return interfaceDao.open(name);
	}

	@Override
	public void btn(String sw, String id) {
		interfaceDao.btn(sw, id);
	}

}
