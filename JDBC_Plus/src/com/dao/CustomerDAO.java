package com.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.bean.Customer;

/*
 * 此接口用于规范针对于Customers表的常用操作
 */
public interface CustomerDAO {
	/**
	 * @Description  insert into customer table
	 * @param conn
	 * @param cust
	 */
	void insert(Connection conn, Customer cust);
	/**
	 * @Description delete customer by id
	 * @param conn
	 * @param id
	 */
	void deletedById(Connection conn, int id);
	/**
	 * @Description update customer table
	 * @param conn
	 * @param cust
	 */
	void update(Connection conn, Customer cust);
	/**
	 * @Description get a customer by id
	 * @param conn
	 * @param id
	 * @return Customer
	 */
	Customer getCustomerById(Connection conn, int id);
	/**
	 * @Description get all customers
	 * @param conn
	 * @return List<Customer>
	 */
	List<Customer> getALL(Connection conn);
	/**
	 * @Description return number of customers
	 * @param conn
	 * @return Long 
	 */
	Long getCount(Connection conn);
	/**
	 * 
	 * @param conn
	 * @return max birthday in customers
	 */
	Date getMaxBirth(Connection conn);
}
