package com.xn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;


public class DBUtils {
	
	private static Logger logger = Logger.getLogger(DBUtils.class);
	
	/**
	 * ��ȡ���ݿ�����
	 * @return
	 */
	public static Connection getConnection() {
		Connection con = null;
		InputStream in = DBUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties prop = new Properties();
		try {
			prop.load(in);
			String driver = prop.getProperty("jdbc.driverClassName");
			String url = prop.getProperty("jdbc.url");
			String username = prop.getProperty("jdbc.username");
			String password = prop.getProperty("jdbc.password");
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("��ȡ�����ļ�ʧ�ܣ���");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("��ȡ�����ļ�ʧ�ܣ���");
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("�ر������ļ���ʧ�ܣ���");
		}
		if(con==null) {
			logger.error("��ȡ���ݿ�����ʧ�� ����");
		}
		return con;
	}
	/**
	 * �ر����ݿ�����
	 * @param con
	 */
	public static void close(Connection con) {
		try {
			if(con!=null) 
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("�ر����ݿ�����ʧ�ܣ���");
		}
	}
}
