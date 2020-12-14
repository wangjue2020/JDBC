package com.prepared.statement.crud;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Properties;

import org.junit.Test;

/*
 * 使用PreparedStatement来替代Statement， 实现对数据表的增删改查操作
 * 
 * 增删改，查
 */
public class PreparedStatementUpdateTest {

	// 向customers表中添加一条记录
	@Test
	public void insertTest() {
		// 建立连接
		Connection conn = null;
		PreparedStatement prepareStatement = null;
		try {
			InputStream resourceAsStream = PreparedStatementUpdateTest.class.getClassLoader()
					.getResourceAsStream("jdbc.properties");
			Properties properties = new Properties();
			properties.load(resourceAsStream);
			String url = properties.getProperty("url");
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");
			String driverClass = properties.getProperty("driverClass");

			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, user, password);
			System.out.println(conn);

			// 预编译sql语句， “？”为占位符
			String sql = "insert into customers(name, email, birth)values(?,?,?)";
			prepareStatement = conn.prepareStatement(sql);
			// 填充占位符
			prepareStatement.setString(1, "哪吒");
			prepareStatement.setString(2, "nezha@gmail.com");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date parse = sdf.parse("1000-01-01");
			prepareStatement.setDate(3, new Date(parse.getTime()));

			// 执行sql
			prepareStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 资源的关闭
			try {
				if(prepareStatement != null)
					prepareStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
