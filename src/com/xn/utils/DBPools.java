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
 * ���ݿ����
 * @author Administrator
 *
 */
public class DBPools implements DataSource{

	private static Logger logger = Logger.getLogger(DBPools.class);
	
	private static LinkedList<Connection> list = new LinkedList<>();

	/**
	 * ��̬����飬�ڼ��ظ���ʱ���Զ�����һ�����ݿ����ӳ�
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
			logger.error("���ݿ����ӳش���ʧ�ܣ���" + " ���ӳش�С :" + list.size());
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

	//�̳߳ػ�ȡ����
	@Override
	public synchronized Connection getConnection() {

		if(list.size() > 0) {
			final Connection con = list.removeFirst();
			if(con == null) {
				logger.error(" �����ݿ����ӳػ�ȡ����ʱ���ִ��󣡣�");
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
								logger.debug( con + " ���ӻ������ݿ����ӳ���  !�������ݿ����ӳش�С�ǣ� " + list.size());
								return null;
							}
						}
					});
		} else  {
			logger.error("���ݿ����ӳ�û������ !!");
			return null;
		}
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
