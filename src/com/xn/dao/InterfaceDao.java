package com.xn.dao;

import java.util.List;

import com.xn.model.InterfaceModel;

public interface InterfaceDao {

	public List<InterfaceModel> list();
	
	public boolean open(String name);
	
	public void btn(String sw,String id);
}
