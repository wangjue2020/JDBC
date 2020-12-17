package com.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.bean.Customer;
import com.util.JDBCutils;

/**
 * @Description 	测试使用PreparedStatement操作Blob类型的数据
 * @author Wangjue
 *
 */
public class BlobTest {
	//向数据表Customers中插入Blob类型的字段
	@Test
	public void insertBlob(){
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JDBCutils.getConnection();
			String sql = "insert into customers (name, email, birth, photo) values(?,?,?,?);";
			ps = connection.prepareStatement(sql);
			ps.setObject(1, "John");
			ps.setObject(2, "john@gmail.ca");
			ps.setObject(3, "1990-01-01");
			FileInputStream is = new FileInputStream(new File("screenshot.png"));
			ps.setBlob(4, is);
			ps.execute();
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			JDBCutils.closeResource(connection, ps);
		}
	}
	//查询数据表中的Blob类型的字段
	@Test
	public void queryBlob(){
		Connection connection = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		InputStream binaryStream = null;
		FileOutputStream fos = null;
		try{
			connection = JDBCutils.getConnection();
			String sql = "select id, name, email, birth, photo from customers where id = ?;";
			s = connection.prepareStatement(sql);
			s.setObject(1, 22);
			rs = s.executeQuery();
			if ( rs.next()){
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				Date birth = rs.getDate(4);
				Customer cust = new Customer(id, name, email, birth);
				//将Blob类型的字段下载下来，以文件的方式保存到本地
				Blob blob = rs.getBlob(5);
				binaryStream = blob.getBinaryStream();
				fos = new FileOutputStream("screenshot_downloaded.png");
				byte[] arr = new byte[1024];
				int len;
				while ((len = binaryStream.read(arr)) != -1){
					fos.write(arr, 0, len);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		} finally{
			JDBCutils.closeResource(connection, s, rs);
			try {
				if (binaryStream != null)
					binaryStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if( fos != null)
					fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
