package com.exercise;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.junit.Test;

import com.util.JDBCutils;

/**
 * 向表中添加一组数据
 * 
 * @author Wangjue
 *
 */
public class Exercise1 {

	@Test
	public void test() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("name:");
		String name = scanner.nextLine();
		System.out.print("email:");
		String email = scanner.nextLine();
		System.out.print("Birthdate(yyyy-mm-dd):");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		Date date = null;
		try {
			date = new Date(sdf.parse(scanner.nextLine()).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} finally{
			scanner.close();
		}
		if (date == null) {
			String sql = "insert into customers(name, email) values (?, ?);";
			modifyTable(sql, name, email);
		} else {
			String sql = "insert into customers(name, email, birth) values ( ?, ? ,?);";
			modifyTable(sql, name, email, date);
		}
		
	}

	public void modifyTable(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JDBCutils.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(connection, ps);
		}
	}
}
