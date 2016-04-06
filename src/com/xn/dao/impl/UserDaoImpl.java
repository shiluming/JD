package com.xn.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.xn.dao.UserDao;
import com.xn.model.PageBean;
import com.xn.model.User;
import com.xn.utils.StringUtils;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public User login(User user) {
		String sql = "select * from tb_user where username=? and password=?";
		final User resultUser = new User();
		jdbcTemplate.query(sql, new Object[]{user.getUserName(),user.getPassWord()},
				new RowCallbackHandler(){

					@Override
					public void processRow(ResultSet rs) throws SQLException {
						// TODO Auto-generated method stub
						resultUser.setId(rs.getInt("id"));
						resultUser.setUserName(rs.getString("username"));
						resultUser.setPassWord(rs.getString("password"));
						resultUser.setAddress(rs.getString("address"));
						resultUser.setRole(rs.getString("role"));
					}
			
		});
		return resultUser;
	}

	@Override
	public String test() {
		// TODO Auto-generated method stub
		return null;
	}
	
	 
	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		String sql = "delete from tb_user where id=?";
		jdbcTemplate.update(sql,new Object[]{id});
	}

	@Override
	public void add(User user) {
		String sql = "insert into tb_user values(null,?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{user.getUserName()
				,user.getPassWord()
				,user.getAddress()
				,user.getTellPhone()
				,user.getRole()});
	}

	@Override
	public void update(User user) {
		String sql = "update tb_user set username=?,password=?,role=?,tell=?,address=? where id=?";
		jdbcTemplate.update(sql, new Object[]{user.getUserName()
				,user.getPassWord()
				,user.getRole()
				,user.getTellPhone()
				,user.getAddress()
				,user.getId()});
	}

	@Override
	public List<User> find(PageBean pageBean, User s_user) {
		StringBuffer sb=new StringBuffer("select * from tb_user t1");
		if(s_user!=null){
			if(StringUtils.isNotEmpty(s_user.getUserName())){
				sb.append(" where t1.username like '%"+s_user.getUserName()+"%'");
			}
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		final List<User> userList=new ArrayList<User>();
		jdbcTemplate.query(sb.toString(), new Object[]{}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				User user=new User();
				user.setId(rs.getInt("id"));
				user.setUserName(rs.getString("username"));
				user.setPassWord(rs.getString("password"));
				user.setAddress(rs.getString("address"));
				user.setRole(rs.getString("role"));
				user.setTellPhone(rs.getString("tell"));
				userList.add(user);
			}
		});
		return userList;
	}

	@Override
	public int count(User s_user) {
		StringBuffer sb=new StringBuffer("select count(*) from tb_user t1");
		if(s_user!=null){
			if(StringUtils.isNotEmpty(s_user.getUserName())){
				sb.append(" where userName like '%"+s_user.getUserName()+"%'");
			}
		}
		return jdbcTemplate.queryForObject(sb.toString(), Integer.class);
	}

	@Override
	public User loadById(int id) {
String sql="select * from tb_user t1 where t1.id=?";
		
		final User user=new User();
		jdbcTemplate.query(sql, new Object[]{id}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				user.setId(rs.getInt("id"));
				user.setUserName(rs.getString("username"));
				user.setPassWord(rs.getString("password"));
				user.setAddress(rs.getString("address"));
				user.setTellPhone(rs.getString("tell"));
				user.setRole(rs.getString("role"));
			}
		});
		return user;
	}

	@Override
	public User loadByUserName(String username) {
		String sql = "select * from tb_user where username=?";
		final User user = new User();
		jdbcTemplate.query(sql, new Object[]{username}, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				user.setUserName(rs.getString("username"));
				user.setPassWord(rs.getString("password"));
				user.setAddress(rs.getString("address"));
				user.setRole(rs.getString("role"));
				user.setTellPhone(rs.getString("tell"));
			}
		});
		return user;
	}
	
	
}
