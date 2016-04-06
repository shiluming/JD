package com.xn.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xn.dao.SystemLog;
import com.xn.model.LogModel;
import com.xn.model.PageBean;
import com.xn.service.SystemLogService;

@Service("systemLogService")
public class SystemLogServiceImpl implements SystemLogService {

	@Resource
	private SystemLog systemLog;
	@Override
	public void add(LogModel log) {
		systemLog.add(log);
	}

	@Override
	public List<LogModel> list(PageBean pageBean, String query) {
		return systemLog.list(pageBean, query);
	}

	@Override
	public int count(String query) {
		return systemLog.count(query);
	}

	@Override
	public void delete(int id) {
		systemLog.delete(id);
	}

}
