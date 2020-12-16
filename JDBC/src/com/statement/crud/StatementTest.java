package com.statement.crud;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import org.junit.Test;

/**
 * 除了解决Statement的拼串问题、sql问题之外，PreparedStatement还有哪些好处？
 * 1、PreparedStatement操作Blob数据，而Statement做不到
 * 2、PreparedStatement可以实现更高效的批量操作（只需要检验一次给预编译，之后只需要传值）
 * @author Wangjue
 *
 */
public class StatementTest {

	// 使用Statement的弊端：需要拼写sql语句，并且存在SQL注入的问题
	// 如何避免出现sql注入，只要用PreparedStatement（从statement扩展而来）取代Statement
	@Test
	public void testLogin() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Username:");
		String user = scanner.next();
		System.out.print("Password:");
		String password = scanner.next();
		//当username 为 "1'or" ，password为 "=1 or '1' ='1"是就会出现sql注入的问题，sql语句就变成了恒成立的语句
		//select user, password from user_table where user ='1' or 'and password='=1 or '1' ='1';
		/*
		 * select user, password 
		 * from user_table 
		 * where user ='1' 
		 * or 
		 * 'and password='=1 
		 * or '1' ='1';
		 */
		String sql="select user, password from user_table where user ='"+user+"' and password='"+password+"';";
		User u = get(sql, User.class);
		if ( u!= null){
			System.out.println(u);
		}else{
			System.out.println("no such user");
		}
	}

	// 使用Statement实现对数据表的查询操作
	public <T> T get(String sql, Class<T> clazz) {
		T t = null;

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 1.加载配置文件
			InputStream is = StatementTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
			Properties pros = new Properties();
			pros.load(is);

			// 2.读取配置信息
			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");

			// 3.加载驱动
			Class.forName(driverClass);

			// 4.获取连接
			conn = DriverManager.getConnection(url, user, password);

			st = conn.createStatement();

			rs = st.executeQuery(sql);

			// 获取结果集的元数据
			ResultSetMetaData rsmd = rs.getMetaData();

			// 获取结果集的列数
			int columnCount = rsmd.getColumnCount();

			if (rs.next()) {

				t = clazz.newInstance();

				for (int i = 0; i < columnCount; i++) {
					// //1. 获取列的名称
					// String columnName = rsmd.getColumnName(i+1);

					// 1. 获取列的别名
					String columnName = rsmd.getColumnLabel(i + 1);

					// 2. 根据列名获取对应数据表中的数据
					Object columnVal = rs.getObject(columnName);

					// 3. 将数据表中得到的数据，封装进对象
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(t, columnVal);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

}
