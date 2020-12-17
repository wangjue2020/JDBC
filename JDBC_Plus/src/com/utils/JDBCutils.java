package com.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Description 操作数据库的工具类
 * @author Wangjue
 *
 */
public class JDBCutils {
	/**
	 * @Description	获取数据库的连接
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception{
		InputStream resourceAsStream =ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(resourceAsStream);
		String url = properties.getProperty("url");
		String username = properties.getProperty("user");
		String password = properties.getProperty("password");
		String driverClass = properties.getProperty("driverClass");
		Class.forName(driverClass);
		Connection connection = DriverManager.getConnection(url,username, password);
		return connection;
	}
	/**
	 * @Description 关闭连接和Statement的操作
	 * @param connection
	 * @param s
	 */
	public static void closeResource(Connection connection, Statement s){
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (s != null)
				s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @Description 关闭连接和Statement的操作
	 * @param connection
	 * @param s
	 */
	public static void closeResource(Connection connection, Statement s, ResultSet rs){
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (s != null)
				s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(rs != null)
				rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
