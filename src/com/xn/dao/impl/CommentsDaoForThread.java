package com.xn.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.xn.utils.DBPools;

public class CommentsDaoForThread {

	
	private DBPools connectionPools = new DBPools();
	private Logger logger = Logger.getLogger(CommentsDaoForThread.class);
	public CommentsDaoForThread() {
		
	}
	
	public void addComments(String contents, String itemid){
		String sql = "update tb_comments set commentcontents=CONCAT(commentcontents,?),starttime=? where itemid=?";
		String sql2 = "update tb_iteminfo set flag=? where itemid=?";
		Connection con = connectionPools.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			PreparedStatement pst2 = con.prepareStatement(sql2);
			pst2.setBoolean(1, true);
			pst2.setString(2, itemid);
			pst2.execute();
			
			pst.setString(1, contents);
			pst.setString(2, new SimpleDateFormat("yyyy:MM:dd hh:mm:ss").format(new Date()));
			pst.setString(3, itemid);
			pst.execute();
		} catch (SQLException e) {
			logger.error("  评论插入数据库失败  ");
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
