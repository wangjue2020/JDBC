package com.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

public class DBCPTest {
	/**
	 * @throws SQLException 
	 * @Description 测试DBCP的数据库连接池技术
	 */
	//方式一：不推荐
	@Test
	@Deprecated
	public void testGetConnection() throws SQLException{
		//创建了DBCP的数据库连接池
		BasicDataSource sd = new BasicDataSource();
		//设置基本信息
		sd.setDriverClassName("com.mysql.jdbc.Driver");
		sd.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true" );
		sd.setUsername("root");
		sd.setPassword("admin");
		
		//还可以设置其他涉及数据库连接池管理的相关属性
		sd.setInitialSize(10);
		sd.setMaxActive(10);
		// ....
		Connection conn = sd.getConnection();
		System.out.println(conn);
	}
	//方式二：使用配置文件
	@Test
	public void testGetConnection1() throws Exception{
		Properties pros = new Properties();
		//方式一：
//		InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
		//方式二：
		File file = new File("src/dbcp.properties");
		FileInputStream is = new FileInputStream(file);
		pros.load(is);
		DataSource dataSource = BasicDataSourceFactory.createDataSource(pros);
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
	}
}
