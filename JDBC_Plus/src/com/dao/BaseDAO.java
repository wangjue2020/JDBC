package com.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.utils.JDBCutils;

/*
 * 封装了针对于数据表的通用的操作
 */
public abstract class BaseDAO {
	//通用的增删改
	public int update(Connection connection, String sql, Object... args) {
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
	//通用的查询操作，用于返回数据表中的一条记录（Version2.0 考虑上事务）
	public <T> List<T> getListOfInstance(Connection connection, Class<T> clazz,String sql, Object...args){
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
				for (int i = 0; i < numColumn; i++) {
					Object columnValue = rs.getObject(i + 1);
					// metaData.getColumnName()：获取列名
					// metaData.getColumnLabel()：获取别名
					String columnName = metaData.getColumnLabel(i + 1);
					Field field = clazz.getDeclaredField(columnName);
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
	
	public <T> T getInstance(Connection connection, Class<T> clazz,String sql, Object...args){
		PreparedStatement ps = null;
		ResultSet rs = null;
		T newInstance = null;
		try {
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int numColumn = metaData.getColumnCount();
			if(rs.next()) {
				newInstance = clazz.newInstance();
				for (int i = 0; i < numColumn; i++) {
					Object columnValue = rs.getObject(i + 1);
					// metaData.getColumnName()：获取列名
					// metaData.getColumnLabel()：获取别名
					String columnName = metaData.getColumnLabel(i + 1);
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(newInstance, columnValue);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(null, ps, rs);
		}
		return newInstance;
	}
	
	public <E> E getValue(Connection conn, String sql, Object...args){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for ( int i =0 ; i < args.length; i++){
				ps.setObject(i+1, args[i]);
			}
			rs = ps.executeQuery();
			if ( rs.next()){
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JDBCutils.closeResource(null, ps, rs);
		}
		return null;
	}
}
