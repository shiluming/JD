package com.xn.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.xn.dao.URLDao;
import com.xn.model.PageBean;
import com.xn.model.Product;
import com.xn.model.URLModel;

@Repository("urlDao")
public class URLDaoImpl implements URLDao {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public int usefullCount() {
		String sql = "select count(*) from tb_fetchurls";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public int unusefullCount() {
		String sql = "select count(*) from tb_url";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}
	@Override
	public List<URLModel> getUseFullData(PageBean pageBean) {
		StringBuffer sb=new StringBuffer("select * from tb_fetchurls t1");

		if(pageBean != null) {
			sb.append(" limit "+pageBean.getPage()+","+pageBean.getPageSize());
		}
		
		final List<URLModel> proList = new ArrayList<>();
		jdbcTemplate.query(sb.toString(), new Object[]{}, new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				URLModel pro = new URLModel();
				pro.setId(rs.getInt("id"));
				pro.setUrl(rs.getString("fetchurl"));
				pro.setTime(rs.getString("fetchtime"));
				pro.setItemid(rs.getString("itemid"));
				pro.setThreadname(rs.getString("threadname"));
				proList.add(pro);
			}
			
		});
		if(proList.size()==0) {
		}
		return proList;
	}

	@Override
	public List<URLModel> getunUseFullData(PageBean pageBean) {
		StringBuffer sb=new StringBuffer("select * from tb_url t1");

		if(pageBean != null) {
			sb.append(" limit "+pageBean.getPage()+","+pageBean.getPageSize());
		}
		
		final List<URLModel> proList = new ArrayList<>();
		jdbcTemplate.query(sb.toString(), new Object[]{}, new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				URLModel pro = new URLModel();
				pro.setId(rs.getInt("id"));
				pro.setUrl(rs.getString("url"));
				pro.setTime(rs.getString("time"));
				pro.setThreadname(rs.getString("threadname"));
				proList.add(pro);
			}
			
		});
		if(proList.size()==0) {
		}
		return proList;
	}

}
