package com.xn.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.xn.dao.DataDao;
import com.xn.model.PageBean;
import com.xn.model.Product;

@Repository("dataDao")
public class DataDaoImpl implements DataDao{

	private static Logger logger = Logger.getLogger(DataDaoImpl.class);
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Product> getData(PageBean pageBean, Product s_product) {
		StringBuffer sb=new StringBuffer("select * from tb_fetchurls t1,tb_iteminfo t2 where t1.itemid=t2.itemid");
		if(s_product != null) {
			sb.append(" and t2.itemid="+s_product.getItemid());
		}
		if(pageBean != null) {
			sb.append(" limit "+pageBean.getPage()+","+pageBean.getPageSize());
		}
		final List<Product> proList = new ArrayList<Product>();
		jdbcTemplate.query(sb.toString(), new Object[]{}, new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				Product pro = new Product();
				pro.setId(rs.getInt("id"));
				pro.setItemid(rs.getString("itemid"));
				pro.setItemname(rs.getString("itemname"));
				pro.setItemprice(rs.getString("price"));
				pro.setItemShop(rs.getString("shop")+" ");
				pro.setCommentCounts(rs.getString("commentcounts"));
				pro.setOthers(rs.getString("fetchurl"));
				proList.add(pro);
			}
			
		});
		if(proList.size()==0) {
			logger.info("数据库没有 id = " + s_product.getItemid() + " 的商品数据!!");
		}
		return proList;
	}

	@Override
	public int count() {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM tb_iteminfo t1, tb_fetchurls t2 WHERE t1.itemid=t2.itemid");
		return jdbcTemplate.queryForObject(sb.toString(), Integer.class);
	}

	@Override
	public List<Product> getDataForComments(Product p) {
		StringBuffer sb = new StringBuffer("select * from tb_iteminfo where flag=0");
		if(p!=null) {
			sb.append(" and itemid="+p.getItemid());
		}
		sb.append(" limit 10");
		List<Product> proList = new ArrayList<>();
		
		jdbcTemplate.query(sb.toString(), new Object[]{}, new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				Product pro = new Product();
				//pro.setId(rs.getInt("id"));
				pro.setItemid(rs.getString("itemid"));
				pro.setItemname(rs.getString("itemname"));
				pro.setItemprice(rs.getString("price"));
				pro.setItemShop(rs.getString("shop")+" ");
				pro.setCommentCounts(rs.getString("commentcounts"));
				proList.add(pro);
			}
			
		});
		return proList;
	}

	@Override
	public void addComments(String contents,String itemid) {
		
		String sql = "update tb_comments set commentcontents=CONCAT(commentcontents,'?') where itemid=?";
		jdbcTemplate.update(sql, new Object[]{contents,itemid});
	}

	
}
