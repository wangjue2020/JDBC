package com.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DruidTest {
	@Test
	public void getConnection() throws Exception{
		Properties properties = new Properties();
		
		InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
		properties.load(resourceAsStream);
		DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
	
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
	}
}
