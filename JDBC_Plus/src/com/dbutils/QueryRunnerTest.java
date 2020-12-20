package com.dbutils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import com.bean.Customer;
import com.utils.JDBCutils;

/**
 * @Description commons-dbutils 是Apache 组织提供的一个开源的JDBC工具类库，封装了针对于数据库的增删改查操作
 * @author Wangjue
 *
 */
public class QueryRunnerTest {
	//测试插入
	@Test
	public void testInsert(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnectionDruid();
			String sql="insert into customers (name, email, birth) values (?, ? ,?)";
			int insertCount = runner.update(conn, sql, "Jack", "Jack@yahoo.com", "2020-01-01"	);
			System.out.println("inserted "+ insertCount+ "rows");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally	{
			JDBCutils.closeResource(conn, null);
		}
	}
	//测试查询
	/*
	 * BeanHandler: 是ResultSetHandler接口的实现类， 用于封装表中的一条记录
	 */
	@Test 
	public void testQuery(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnectionDruid();
			String sql = "select id, name, email, birth from customers where id = ?";
			BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
			Customer customer = runner.query(conn, sql, handler , 3);
			System.out.println(customer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDBCutils.closeResource(conn, null);
	}
	@Test 
	public void testQuery1(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			String sql = "select id, name, email, birth from customers where id < ?";
			conn = JDBCutils.getConnectionDruid();
			BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
			List<Customer> customers = runner.query(conn, sql, handler , 3);
			System.out.println(customers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	
	//测试查询
	/*
	 * MapHandler: 是ResultSetHandler接口的实现类， 用于封装表中的一条记录
	 * {name=林志玲, birth=1984-06-12, id=3, email=linzl@gmail.com}
	 * 将一个object 封装成map
	 * MapListHandler return List<Map<String, Object>>, 将多个object 封装成多个Map 存在list中，每个map对应一个object
	 * [{name=汪峰, birth=2010-02-02, id=1, email=wf@126.com}, {name=王菲, birth=1988-12-26, id=2, email=wangf@163.com}]
	 */
	@Test 
	public void testQuery2(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnectionDruid();
			String sql = "select id, name, email, birth from customers where id = ?";
			MapHandler handler = new MapHandler();
			Map<String, Object> map = runner.query(conn, sql, handler, 3);
			System.out.println(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	@Test 
	public void testQuery3(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnectionDruid();
			String sql = "select id, name, email, birth from customers where id < ?";
			MapListHandler handler = new MapListHandler();
			List<Map<String, Object>> maps = runner.query(conn, sql, handler, 3);
			System.out.println(maps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	//[1,"汪峰","wf@126.com","2010-02-02"]
	//将一个object 封装成array
	//ArrayListHandler return List<Object[]>, 将多个object 封装成多个array 存在list中，每个array对应一个object
	@Test 
	public void testQuery4(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnectionDruid();
			String sql = "select id, name, email, birth from customers where id < ?";
			ArrayHandler handler = new ArrayHandler();
			Object[] result = runner.query(conn, sql, handler, 3);
			for(Object o : result){
				System.out.println(o);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	/*
	 * ScalarHandler: 用于查询特殊值
	 */
	@Test 
	public void testQuery5(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnectionDruid();
			String sql = "select count(*) from customers";
			ScalarHandler handler = new ScalarHandler();
			Long count = (Long) runner.query(conn, sql, handler);
			System.out.println(count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	
	@Test 
	public void testQuery6(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnectionDruid();
			String sql = "select max(birth) from customers";
			ScalarHandler handler = new ScalarHandler();
			Date date = (Date) runner.query(conn, sql, handler);
			System.out.println(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	/*
	 * 自定义ResultSetHandler 的实现类
	 */
	@Test 
	public void testQuery7(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnectionDruid();
			String sql = "select id, name, email, birth from customers where id = ?";
			ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>(){

				@Override
				public Customer handle(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					if(rs.next()){
						int id = rs.getInt("id");
						String name = rs.getString("name");
						String email = rs.getString("email");
						Date birth =rs.getDate("birth");
						return new Customer(id, name, email, birth);
					}
					return null;
				}
				
			};
			Customer c = runner.query(conn, sql, handler,3);
			System.out.println(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);
		}
	}
}
