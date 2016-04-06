package com.xn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * 数据库池类
 * @author Administrator
 *
 */
public class DBPools implements DataSource{

	private static Logger logger = Logger.getLogger(DBPools.class);
	
	private static LinkedList<Connection> list = new LinkedList<>();

	/**
	 * 静态代码块，在加载该类时，自动创建一个数据库连接池
	 */
	static {
		InputStream in = DBPools.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties prop = new Properties();
		try {
			prop.load(in);
			int initSize = Integer.parseInt(prop.getProperty("jdbcPoolInitSize"));
			for(int i=0;i<initSize;i++) {
				Connection con = DBUtils.getConnection();
				list.add(con);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("数据库连接池创建失败！！" + " 连接池大小 :" + list.size());
		}
	}
	
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	//线程池获取连接
	@Override
	public synchronized Connection getConnection() {

		if(list.size() > 0) {
			final Connection con = list.removeFirst();
			if(con == null) {
				logger.error(" 在数据库连接池获取连接时出现错误！！");
			}
			return (Connection) Proxy.newProxyInstance(DBPools.class.getClassLoader(), new Class[]{Connection.class},
					new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							// TODO Auto-generated method stub
							if(!method.getName().equals("close")) {
								return method.invoke(con, args);
							} else {
								list.add(con);
								logger.debug( con + " 连接还给数据库连接池了  !现在数据库连接池大小是： " + list.size());
								return null;
							}
						}
					});
		} else  {
			logger.error("数据库连接池没有连接 !!");
			return null;
		}
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
