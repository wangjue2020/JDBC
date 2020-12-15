package com.prepared.statement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.bean.Customer;
import com.util.JDBCutils;

/**
 * @Desciption 正对于Customers表的查询操作
 * @author Wangjue
 *
 */
public class QueryForCustomer {
	/**
	 * @Description 针对customers表的通用的查询操作
	 */
	public List<Customer> queryForCustomers(String sql, Object... args) {
		ArrayList<Customer> res = new ArrayList<>();
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		try {
			connection = JDBCutils.getConnection();
			prepareStatement = connection.prepareStatement(sql);
			for (int i = 1; i <= args.length; i++) {
				prepareStatement.setObject(i, args[i - 1]);
			}
			resultSet = prepareStatement.executeQuery();
			// 获取结果集的元数据
			ResultSetMetaData metaData = resultSet.getMetaData();
			// 通过ResultSetMetaData获取结果集中的列数
			int numColumn = metaData.getColumnCount();
			while (resultSet.next()) {
				Customer c = new Customer();
				for (int i = 0; i < numColumn; i++) {
					// 获取列值
					Object columnValue = resultSet.getObject(i + 1);
					// 获取每个列的列名
					String columnName = metaData.getColumnName(i + 1);
					// 给customer 对象指定的columnName属性，赋值为columnValue，通过反射
					Field field = Customer.class.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(c, columnValue);
				}

				res.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(connection, prepareStatement, resultSet);
		}
		return res;
	}

	@Test
	public void testQueryForCustomers() {

		List<Customer> res = queryForCustomers("select id, name from customers where id >=?;", 1);
		System.out.println(res);
	}

	@Test
	public void test() {
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultset = null;
		try {
			connection = JDBCutils.getConnection();
			String sql = "select id, name, email, birth from customers where id = ?;";
			prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setObject(1, 1);
			// 执行,并返回结果集
			resultset = prepareStatement.executeQuery();
			// 处理结果集
			// next(): 判断结果集的下一条是否有数据，如果有数据返回true，并指针下移；如果返回false，指针不会下移
			if (resultset.next()) {
				// 获取当前这条数据的各个字段值
				int id = resultset.getInt(1);
				String name = resultset.getString(2);
				String email = resultset.getString(3);
				Date birthday = resultset.getDate(4);

				Customer customer = new Customer(id, name, email, birthday);
				System.out.println(customer);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(connection, prepareStatement, resultset);
		}
	}
}
