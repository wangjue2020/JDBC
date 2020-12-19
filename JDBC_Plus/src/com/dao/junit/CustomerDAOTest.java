package com.dao.junit;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import org.junit.Test;

import com.bean.Customer;
import com.dao.impl.CustomerDAOimpl;
import com.utils.JDBCutils;

public class CustomerDAOTest {
	private CustomerDAOimpl dao = new CustomerDAOimpl();
	@Test
	public void testInsert() {
		Connection connection = null;
		try {
			connection = JDBCutils.getConnection();
			Customer customer = new Customer(1, "Jenny", "Jenny@gmail.com", new Date(43534646435L));
			dao.insert(connection, customer);
			System.out.println("添加成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDBCutils.closeResource(connection, null);
	}

	@Test
	public void testDeletedById(){
		Connection connection = null;
		try {
			connection = JDBCutils.getConnection();
			dao.deletedById(connection, 23);
			System.out.println("delete successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDBCutils.closeResource(connection, null);
	}

	@Test
	public void testUpdateConnectionCustomer() {
		System.out.println("===============testUpdateConnectionCustomer(Start)==================");
		Connection connection = null;
		try {
			connection = JDBCutils.getConnection();
			Customer c = dao.getCustomerById(connection, 21);
			c.setBirth(new Date(43534646435L));
			dao.update(connection, c);
			Customer c1 = dao.getCustomerById(connection, 21);
			System.out.println(c1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDBCutils.closeResource(connection, null);

		System.out.println("===============testUpdateConnectionCustomer(End)==================");
	}

	@Test
	public void testGetCustomerById() {
		System.out.println("===============testGetCustomerById(Start)==================");
		Connection connection= null;
		try {
			connection = JDBCutils.getConnection();
			Customer c = dao.getCustomerById(connection, 21);
			System.out.println(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDBCutils.closeResource(connection, null);
		System.out.println("===============testGetCustomerById(End)==================");
		
	}

	@Test
	public void testGetALL() {
		System.out.println("===============testGetALL(Start)==================");
		Connection connection = null;
		try {
			connection = JDBCutils.getConnection();
			List<Customer> customers = dao.getALL(connection);
			for ( Customer c: customers){
				System.out.println(c);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDBCutils.closeResource(connection, null);

		System.out.println("===============testGetALL(End)==================");
	}

	@Test
	public void testGetCount() {
		System.out.println("===============testGetCount(Start)==================");
		Connection connection = null;
		try {
			connection = JDBCutils.getConnection();
			Long count = dao.getCount(connection);
			System.out.println(count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDBCutils.closeResource(connection, null);
		System.out.println("===============testGetCount(End)==================");
		
	}

	@Test
	public void testGetMaxBirth() {
		System.out.println("===============testGetMaxBirth(Start)==================");
		Connection conn = null;
		try {
			conn = JDBCutils.getConnection();
			Date maxBirth = dao.getMaxBirth(conn);
			System.out.println(maxBirth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDBCutils.closeResource(conn, null);
		System.out.println("===============testGetMaxBirth(End)==================");
	}

}
