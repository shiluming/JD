package com.xn.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.xn.dao.IndexDao;
import com.xn.model.IndexModel;
import com.xn.model.PageBean;
import com.xn.model.Product;

@Repository("indexDao")
public class IndexDaoImpl implements IndexDao {
	
	private static Logger logger = Logger.getLogger(IndexDaoImpl.class);
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void add(IndexModel iModel) {
		String sql = "insert into tb_index values(null,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{iModel.getCreateTime()
				,iModel.getIndexDir()
				,iModel.getIndexNum()
				,iModel.getSpareTime()
				,iModel.getIndexUser()
				,iModel.isBuckup()
				,iModel.getBuckupDir()
				,iModel.isCurrent()});
	}

	@Override
	public int count() {
		String sql = "select count(*) from tb_index";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public List<IndexModel> getData(PageBean pageBean,String current) {
		StringBuffer sb=new StringBuffer("select * from tb_index");
		if(current==null) {
			current = "100";
		}
		if(current.equals("1")) {
			sb.append(" where iscurrent=true");
		}
		if(pageBean != null) {
			sb.append(" limit "+0+","+pageBean.getPageSize());
		}
		System.out.println(sb.toString());
		final List<IndexModel> IList = new ArrayList<IndexModel>();
		jdbcTemplate.query(sb.toString(), new Object[]{}, new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				IndexModel io = new IndexModel();
				io.setId(rs.getInt("id"));
				io.setCreateTime(rs.getString("createtime"));
				io.setBuckup(rs.getBoolean("isbuckup"));
				io.setBuckupDir(rs.getString("buckupdir"));
				io.setIndexDir(rs.getString("indexdir"));
				io.setIndexNum(rs.getInt("indexNum"));
				io.setSpareTime(rs.getString("sparetime"));
				io.setIndexUser(rs.getString("indexuser"));
				io.setCurrent(rs.getBoolean("iscurrent"));
				IList.add(io);
			}
			
		});
		return IList;
	}

	@Override
	public void changeFlag(String id) {
		// TODO Auto-generated method stub
		String sql = "update tb_index set iscurrent=0 where id=?";
		jdbcTemplate.update(sql, new Object[]{id});
	}

}
