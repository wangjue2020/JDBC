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

import com.bean.Order;
import com.util.JDBCutils;

/**
 * @Description	针对于Order 表的通用的查询操作
 * @author Wangjue
 *
 */
public class QueryForOrder {
	@Test
	public void queryForOrderTest(){
		List<Order> orders = queryForOrder("select * from `order`;");
		System.out.println(orders);
	}
	public List<Order> queryForOrder(String sql, Object...args){
		List<Order> res = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = JDBCutils.getConnection();
			ps = connection.prepareStatement(sql);
			for(int i=0; i < args.length; i++){
				ps.setObject(i+1,args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int numColumn = metaData.getColumnCount();
			while (rs.next()){
				Order o = new Order();
				for(int i = 0; i<numColumn; i++){
					Object columnValue = rs.getObject(i+1);
					String columnName = metaData.getColumnName(i+1);
					String[] splitedName = columnName.split("_");
					columnName = splitedName[0]+splitedName[1].substring(0, 1).toUpperCase()+splitedName[1].substring(1);
					Field field = Order.class.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(o, columnValue);
				}
				res.add(o);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JDBCutils.closeResource(connection, ps, rs);
		}
		return res;
	}
	@Test
	public void test(){
		Connection connection = null;
		PreparedStatement preparestatement = null;
		ResultSet rs =null;
		try{
			connection  = JDBCutils.getConnection();
			String sql = "select order_id, order_name, order_date from `order` where order_id=?;";
			preparestatement = connection.prepareStatement(sql);
			preparestatement.setObject(1, 1);
			rs = preparestatement.executeQuery();
			if(rs.next()){
				int id = (int) rs.getObject(1);
				String name = (String) rs.getObject(2);
				Date date = (Date) rs.getObject(3);
				
				Order order = new Order(id,name,date);
				System.out.println(order);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			JDBCutils.closeResource(connection, preparestatement, rs);
		}
	}
}
