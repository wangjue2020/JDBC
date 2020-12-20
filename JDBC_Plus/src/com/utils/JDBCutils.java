package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

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
	 * @Description 使用c3p0的数据库连接池技术
	 * @return
	 * @throws SQLException 
	 */
	//数据库连接池只需要提供一个即可
	
	private static 	ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0"); 
	public static Connection getConnectionC3P0() throws SQLException{ 
		Connection connection = cpds.getConnection();
		
		return connection; 
	}
	/**
	 * @Description 使用DBCP的数据库连接池技术
	 * @return
	 */
	private static DataSource source;
	static {
		try {
			Properties pros = new Properties();
			//方式一：
//		InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
			//方式二：
			File file = new File("src/dbcp.properties");
			FileInputStream is = new FileInputStream(file);
			pros.load(is);
			source = BasicDataSourceFactory.createDataSource(pros);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection getConnectionDBCP() throws Exception{
		
		Connection connection = source.getConnection();
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
