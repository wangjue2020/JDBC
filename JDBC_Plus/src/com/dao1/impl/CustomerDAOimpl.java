package com.dao1.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.bean.Customer;
import com.dao1.BaseDAO;
import com.dao1.CustomerDAO;

public class CustomerDAOimpl extends BaseDAO<Customer> implements CustomerDAO{

	@Override
	public void insert(Connection conn, Customer cust) {
		// TODO Auto-generated method stub
		String sql = "insert into Customers (name, email, birth) values ( ?, ? ,?);";
		String name = cust.getName();
		String email = cust.getEmail();
		Date birth = cust.getBirth();
		update(conn, sql, name, email, birth);
	}

	@Override
	public void deletedById(Connection conn, int id) {
		// TODO Auto-generated method stub
		String sql = "delete from Customers where id = ?";
		update(conn, sql, id);
	}

	@Override
	public void update(Connection conn, Customer cust) {
		// TODO Auto-generated method stub
		String sql = "update customers set name = ?, email=? , birth=? where id=?";
		update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
	}

	@Override
	public Customer getCustomerById(Connection conn, int id) {
		// TODO Auto-generated method stub
		String sql = "select id, name, email, birth from customers where id = ?";
		Customer instance = getInstance(conn, sql, id);
		return instance;
	}

	@Override
	public List<Customer> getALL(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "select id, name, email, birth from customers";
		List<Customer> customers = getListOfInstance(conn,  sql);
		return customers;
	}

	@Override
	public Long getCount(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from Customers";
		return getValue(conn, sql);
	}

	@Override
	public Date getMaxBirth(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "select max(birth) from customers";
		return getValue(conn, sql);
	}

}
