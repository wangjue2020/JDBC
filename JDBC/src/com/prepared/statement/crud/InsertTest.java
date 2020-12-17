package com.prepared.statement.crud;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import com.util.JDBCutils;

/**
 * 使用PreparedStatement实现批量数据的操作
 * 
 * update、delete本身就具备批量操作的效果 此时的批量操作，主要指的是批量插入 insert,
 * 使用PreparedStatement如何实现更高效的批量插入？
 * 
 * 
 * 题目： 向goods 表中插入20000条数据 create table goods ( id int primary key
 * auto_increment, name varchar(25) );
 * 
 * 方式一：使用statement Connection conn = JDBCutil.getConnection(); Statement st =
 * conn.createStatemtn(); for(int i =1; i <= 20000; i++){ String sql = "insert
 * into goods(name) values('name_"+i+"')"; st.execute(); }
 * 
 * 方式二：使用PreparedStatement： 见code
 * 
 * @author Wangjue
 *
 */
public class InsertTest {
	// 批量插入方式二：
	@Deprecated
	@Test
	public void testInsert() {
		Connection connection = null;
		PreparedStatement s = null;
		try {
			connection = JDBCutils.getConnection();
			String sql = "insert into goods(name) values(?)";
			s = connection.prepareStatement(sql);
			for (int i = 1; i <= 20000; i++) {
				s.setObject(1, "name_" + i);
				s.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(connection, s);
		}
	}

	/*
	 * 批量插入的方式三：
	 * 
	 * 1、addBatch()、executeBatch()、clearBatch()
	 * 2、mysql服务器默认是关闭批处理的，我们需要通过一个参数，上mysql开启批处理的支持
	 * ?rewriteBatchedStatements=true 写在配置文件的url后面
	 * 3、使用更新的mysql驱动：mysql-connector-java-5.1.37-bin.jar
	 */
	@Deprecated
	@Test
	public void testInsert1() {
		Connection connection = null;
		PreparedStatement s = null;
		try {
			connection = JDBCutils.getConnection();
			String sql = "insert into goods(name) values(?)";
			s = connection.prepareStatement(sql);
			for (int i = 1; i <= 20000; i++) {
				s.setObject(1, "name_" + i);
				// 1."攒“ sql
				s.addBatch();
				if (i % 500 == 0) {
					// 2、执行batch
					s.executeBatch();
					// 3、清空batch
					s.clearBatch();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(connection, s);
		}
	}

	/*
	 * 批量插入的方式四：Optimal method
	 */
	@Test
	public void testInsert2() {
		Connection connection = null;
		PreparedStatement s = null;
		try {
			connection = JDBCutils.getConnection();
			// 设置不允许自动提交数据
			connection.setAutoCommit(false);
			String sql = "insert into goods(name) values(?)";
			s = connection.prepareStatement(sql);
			for (int i = 1; i <= 20000; i++) {
				s.setObject(1, "name_" + i);
				// 1."攒“ sql
				s.addBatch();
				if (i % 500 == 0) {
					// 2、执行batch
					s.executeBatch();
					// 3、清空batch
					s.clearBatch();
				}
			}
			//提交数据
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(connection, s);
		}
	}
}
