package com.xn.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.xn.dao.SystemLog;
import com.xn.model.LogModel;
import com.xn.model.PageBean;
import com.xn.model.User;
import com.xn.utils.StringUtils;

@Repository("systemLog")
public class SystemLogImpl implements SystemLog {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void add(LogModel log) {
		String sql = "insert into tb_systemlog values(null,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{log.getUsername()
				,log.getIp()
				,log.getAddress()
				,log.getDate()
				,log.getOthers()
				,log.getType()});
	}
	
	@Override
	public List<LogModel> list(PageBean pageBean,String query) {
		StringBuffer sb=new StringBuffer("select * from tb_systemlog t1");
		if(query!=null){
			if(StringUtils.isNotEmpty(query)){
				sb.append(" where t1.username like '%"+query+"%'");
			}
		}
		
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		
		final List<LogModel> list=new ArrayList<LogModel>();
		jdbcTemplate.query(sb.toString(), new Object[]{}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				LogModel lModel=new LogModel();
				lModel.setId(rs.getInt("id"));
				lModel.setUsername(rs.getString("username"));
				lModel.setAddress(rs.getString("address"));
				lModel.setIp(rs.getString("ip"));
				lModel.setDate(rs.getString("date"));
				lModel.setOthers(rs.getString("others"));
				lModel.setType(rs.getString("type"));
				list.add(lModel);
			}
		});
		return list;
	}

	@Override
	public int count(String query) {
		StringBuffer sb=new StringBuffer("select count(*) from tb_systemlog t1");
		if(query!=null){
			if(StringUtils.isNotEmpty(query)){
				sb.append(" where username like '%"+query+"%'");
			}
		}
		return jdbcTemplate.queryForObject(sb.toString(), Integer.class);
	}

	@Override
	public void delete(int id) {
		String sql = "delete from tb_systemlog where id=?";
		jdbcTemplate.update(sql,new Object[]{id});
	}

	
}
