package com.transaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.bean.User;
import com.utils.JDBCutils;

/*
 * 1、什么叫数据库事务？
 * 事务：一组逻辑操作单元，是数据从一种状态变换到另一种状态。
 * 			-- 一组逻辑操作单元：一个或多个DML操作
 * 
 * 2、事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 * 		当在一个事务中执行多个操作时，要么所有的事务都被提交（commit），那么这些修改就永久保存下来； 要么数据库管理
 * 		系统将放弃所做的所有修改，整个事务回滚（rollback）到最初状态
 * 3、数据一旦提交，就不可回滚
 * 4、哪些操作会导致数据的自动提交？
 * 		-- DDL 操作一旦执行，都会自动提交， set autocommit = false； 对DDL语句无效
 * 		-- DML 默认情况下，一旦执行，就会自动提交
 * 				我们可以通过set autocommit = false 的方式取消DML操作的自动提交
 * 		-- 默认在关闭连接时，会自动的提交数据
 */

public class TransactionTest {
	/*
	 * 针对于数据表user_table来说： 
	 * AA用户给BB用户转账100 
	 * 
	 * update user_table set balance = balance - 100 where user = 'AA'; 
	 * update user_table set balance = balance + 100 where user = 'BB';
	 */
	@Test
	public void testUpdate() {
			String sql1 = "update user_table set balance = balance - 100 where user = ?;";
			generalUpdate(sql1, "AA");
			//模拟网络异常
//			System.out.println(10/0);抛出异常导致只有AA的账户扣了钱，但是钱没有到BB账户里
			String sql2 = "update user_table set balance = balance + 100 where user = ?;";
			generalUpdate(sql2, "BB");
			
			System.out.println("转账成功");
	}
	@Test
	public void testgeneralUpdate1() {
		Connection connection = null;
		try {
			connection = JDBCutils.getConnection();
			//取消数据的自动提交功能
			connection.setAutoCommit(false);
			String sql1 = "update user_table set balance = balance - 100 where user = ?;";
			generalUpdate1(connection, sql1, "AA");
//			Thread.sleep(100000000000000000L);
			//模拟网络异常
			System.out.println(10/0);//抛出异常导致只有AA的账户扣了钱，但是钱没有到BB账户里
			String sql2 = "update user_table set balance = balance + 100 where user = ?;";
			generalUpdate1(connection, sql2, "BB");
			
			System.out.println("转账成功");
			//2、提交数据
			
			connection.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				connection .rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			//修改其为自动提交数据
			//主要用于在应用数据库连接池的情境下，不关闭连接，而是选择恢复自动提交以便下一次使用
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JDBCutils.closeResource(connection, null);
		}
	}
	// 通用的增删改操作------version 2.0(including transaction)
	// sql中占位符的个数与可变形参的个数一致
	public int generalUpdate1(Connection connection, String sql, Object... args) {
		PreparedStatement preparedStatement = null;
		try {
			
			// 2、预编译sql语句，返回preparedStatement实例
			preparedStatement = connection.prepareStatement(sql);
			// 3、填充占位符
			for (int i = 1; i <= args.length; i++) {
				preparedStatement.setObject(i, args[i - 1]);
			}
			// 4、执行
			return preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5、资源的关闭
			JDBCutils.closeResource(null, preparedStatement);
		}
		return 0;
	}

	// 通用的增删改操作------version 1.0
	// sql中占位符的个数与可变形参的个数一致
	@Deprecated
	public int generalUpdate(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			// 1、获取数据库的连接
			connection = JDBCutils.getConnection();
			// 2、预编译sql语句，返回preparedStatement实例
			preparedStatement = connection.prepareStatement(sql);
			// 3、填充占位符
			for (int i = 1; i <= args.length; i++) {
				preparedStatement.setObject(i, args[i - 1]);
			}
			// 4、执行
			return preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5、资源的关闭
			JDBCutils.closeResource(connection, preparedStatement);
		}
		return 0;
	}
	
	@Test
	public void testTransactionSelect() throws Exception{
		Connection connection  = JDBCutils.getConnection();
		connection.setTransactionIsolation(1);
		//获取当前连接的隔离级别
		System.out.println(connection.getTransactionIsolation());
		//设置数据库的隔离级别
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		connection.setAutoCommit(false);
		String sql = "select user, password, balance from user_table where user = ?";
		List<User> instance = getInstance(connection, User.class, sql, "CC");
		System.out.println(instance);
	}
	@Test
	public void testTransactionUpdate() throws Exception{
		Connection connection  = JDBCutils.getConnection();
		String sql = "update user_table set balance =? where user=?";
		generalUpdate1(connection, sql,5000,"CC");
		
		Thread.sleep(15000);
		System.out.println("修改结束");
		
	}
	//通用的查询操作，用于返回数据表中的一条记录（Version2.0 考虑上事务）
	public <T> List<T> getInstance(Connection connection, Class<T> clazz,String sql, Object...args){
		List<T> res = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int numColumn = metaData.getColumnCount();
			while (rs.next()) {
				T newInstance = clazz.newInstance();
//				Order o = new Order();
				for (int i = 0; i < numColumn; i++) {
					Object columnValue = rs.getObject(i + 1);
					// metaData.getColumnName()：获取列名
					// metaData.getColumnLabel()：获取别名
					String columnName = metaData.getColumnLabel(i + 1);
					Field field = clazz.getDeclaredField(columnName);
//					Field field = Order.class.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(newInstance, columnValue);
				}
				res.add(newInstance);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(null, ps, rs);
		}
		return res;
	}
}
