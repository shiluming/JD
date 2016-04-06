package com.xn.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.xn.dao.InterfaceDao;
import com.xn.model.IndexModel;
import com.xn.model.InterfaceModel;

@Repository("interfaceDao")
public class InterfaceDaoImpl implements InterfaceDao {

	@Resource
	private JdbcTemplate jddbcTemplate;
	
	@Override
	public List<InterfaceModel> list() {
		final List<InterfaceModel> list = new ArrayList<>();
		String sql = "select * from tb_interface";
		jddbcTemplate.query(sql, new Object[]{}, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				InterfaceModel iM = new InterfaceModel();
				iM.setId(rs.getInt("id"));
				iM.setName(rs.getString("name"));
				iM.setType(rs.getString("type"));
				iM.setAddress(rs.getString("address"));
				iM.setFlag(rs.getBoolean("flag"));
				list.add(iM);
			}
		});
		return list;
	}

	@Override
	public boolean open(String name) {
		String sql = "select flag from tb_interface where id="+name;
		return jddbcTemplate.queryForObject(sql, Boolean.class);
	}

	@Override
	public void btn(String sw,String id) {
		String sql = "update tb_interface set flag=? where id=?";
		jddbcTemplate.update(sql,new Object[]{sw,id});
	}

}
