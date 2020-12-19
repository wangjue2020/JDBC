package com.connection;

import java.beans.PropertyVetoException;
import java.sql.Connection;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Test {
	//方式一
	@Test
	public void testGetConnection() throws Exception{
		//获c3p0数据库连接池
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true" );
		cpds.setUser("root");                                  
		cpds.setPassword("admin");  
		//通过设置相关的参数，对数据库连接池进行管理
		//设置初始时数据库连接池中的连接数
		cpds.setInitialPoolSize(10);
		
		Connection connection = cpds.getConnection();
		System.out.println(connection);
	}
	//方式二： 使用配置文件
	
}
