package com.panxiantong.gomoku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBC {
	public static void upload() {
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "7777";
		String address = "panxiantong.com:3306";
		String dbName = "greystone";

		String url = "jdbc:mysql://" + address + "/" + dbName + "?autoReconnect=true&useSSL=false&"
				+ "useUnicode=true&characterEncoding=UTF-8";

		// 下面执行sql语句
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, userName, password);
			Statement s = conn.createStatement();

			String sql = "INSERT INTO score_table (name,score1,score2) VALUES ('oneS', '1', '2');";

			s.execute(sql);

			// 关闭声明
			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			// 关闭链接对象
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		upload();
	}

}