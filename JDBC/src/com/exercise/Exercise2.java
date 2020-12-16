package com.exercise;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import org.junit.Test;

import com.bean.Student;
import com.util.JDBCutils;

public class Exercise2 {
	//完成学生信息的删除功能
	@Test
	public void testDelete(){
		Scanner scanner = new Scanner(System.in);
		int i =0;
		int res = 0;
		String sql = "delete from examstudent where examcard = ?;";
		while (res == 0){
			if (i!=0){
				System.out.println("查无此人，请重新输入");
			}
			System.out.print("请输入学生的考号：");
			String examId = scanner.next();
			if("q".equals(examId))
				return;
			res = modifyTable(sql, examId);
			i++;
		}
		System.out.println("删除成功");
		scanner.close();
	}
	//向examstudent表中添加一条记录
	/*
	 * Type:
	 * IDCard:
	 * ExamCard:
	 * StudentName:
	 * Location:
	 * Grade:
	 * 
	 */
	@Test
	public void testInsert(){
		Scanner scanner = new Scanner(System.in);
		System.out.print("四级/六级：");
		int type = scanner.nextInt();
		System.out.print("ID number:");
		String idCard =  scanner.next();
		System.out.print("ExamCard:");
		String examCard = scanner.next();
		System.out.print("Name:");
		String studentName = scanner.next();
		System.out.print("Location:");
		String location = scanner.next();
		System.out.print("grade:");
		int grade = scanner.nextInt();
		String sql = "insert into examstudent (type, IDCard, ExamCard, StudentName, Location, Grade) values (?, ? , ?, ?, ?, ?);";
		int res = modifyTable(sql, type, idCard, examCard, studentName, location, grade);
		if (res != 0)
			System.out.println("添加成功");
		else 
			System.out.print("添加失败");
		scanner.close();
		
	}
	//根据身份证号或准考证号查询学生的基本信息
	@Test
	public void testQueryByIdOrExamId(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("请选择您要输入的类型：\na:准考证号\nb:身份证号");
		String ans = scanner.next();
		if ( !"a".equals(ans) && !"b".equals(ans)){
			System.out.println("您的输入有误，请重新进入程序。");
			return;
		}
		Student result = null;
		String sql ="select FlowId flowId, Type examType, IDCard id, ExamCard examId, StudentName name, Location location, Grade grade"
				+ " from examstudent where ExamCard = ? or IDCard = ?;";
		if ("a".equals(ans)){
			System.out.print("请输入准考证号：");
			String examId = scanner.next();
			result = queryByIdOrExamId(sql, examId, null);
		}else{
			System.out.print("请输入身份证号：");
			String id = scanner.next();
			result = queryByIdOrExamId(sql, null,id );
		}
		if( result == null)
			System.out.println("查无此人，请重新进入程序。");
		else{
			System.out.println("===========查询结果============");
			System.out.println(result);
		}
		scanner.close();
	}
	public Student queryByIdOrExamId(String sql, Object...args){
		Connection connection = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try{
			connection = JDBCutils.getConnection();
			s = connection.prepareStatement(sql);
			for(int i =0; i< args.length; i++){
				s.setObject(i+1, args[i]);
			}
			rs = s.executeQuery();
			if ( rs.next()){
				Student stu = new Student();
				ResultSetMetaData rsm = rs.getMetaData();
				int columnNum = rsm.getColumnCount();
				for ( int i = 0; i < columnNum; i++){
					String columnLabel = rsm.getColumnLabel(i+1);
					Object columnValue = rs.getObject(i+1);
					Field field = Student.class.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(stu, columnValue);
				}
				return stu;
				
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			JDBCutils.closeResource(connection, s, rs);
		}
		return null;
	}
	public int modifyTable(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JDBCutils.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(connection, ps);
		}
		return 0;
	}
	
}
