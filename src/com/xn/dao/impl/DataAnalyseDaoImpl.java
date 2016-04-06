package com.xn.dao.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.xn.dao.DataAnalyseDao;
@Repository("dataAnalyseDao")
public class DataAnalyseDaoImpl implements DataAnalyseDao {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int[] priceAnalyse() {
		int[] result = new int[7];
		String sql_others = "select count(*) from tb_iteminfo where price<=0";
		String sql_500 = "select count(*) from tb_iteminfo where price>0 and price <=500";
		String sql_1000 = "select count(*) from tb_iteminfo where price>500 and price <=1000";
		String sql_3000 = "select count(*) from tb_iteminfo where price>1000 and price <=3000";
		String sql_5000 = "select count(*) from tb_iteminfo where price>3000 and price <=5000";
		String sql_10000 = "select count(*) from tb_iteminfo where price>5000 and price <=10000";
		String sql_10000_up = "select count(*) from tb_iteminfo where price>10000";
		result[0] = jdbcTemplate.queryForObject(sql_others, Integer.class);
		result[1] = jdbcTemplate.queryForObject(sql_500, Integer.class);
		result[2] = jdbcTemplate.queryForObject(sql_1000, Integer.class);
		result[3] = jdbcTemplate.queryForObject(sql_3000, Integer.class);
		result[4] = jdbcTemplate.queryForObject(sql_5000, Integer.class);
		result[5] = jdbcTemplate.queryForObject(sql_10000, Integer.class);
		result[6] = jdbcTemplate.queryForObject(sql_10000_up, Integer.class);
		return result;
	}

}
