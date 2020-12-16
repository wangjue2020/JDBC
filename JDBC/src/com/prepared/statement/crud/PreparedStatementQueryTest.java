package com.prepared.statement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.bean.Customer;
import com.bean.Order;
import com.util.JDBCutils;

/**
 * @Description 使用PreparedStatement实现针对于不同表的通用的查询操作
 * @author Wangjue
 *
 */
public class PreparedStatementQueryTest {
	
	@Test
	public void test(){
		String sql="select id, name, email from customers where id >=?;";
		List<Customer> customers = getInstance(Customer.class,sql, 1);
		System.out.println(customers.toString());
		
		String sql1 = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id=?;";
		List<Order> orders = getInstance(Order.class, sql1, 1);
		System.out.println(orders);
		
	}
	public <T> List<T> getInstance(Class<T> clazz,String sql, Object...args){
		List<T> res = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = JDBCutils.getConnection();
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
			JDBCutils.closeResource(connection, ps, rs);
		}
		return res;
	}
}
