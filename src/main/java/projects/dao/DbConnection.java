package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projects.exception.DbException;

public class DbConnection {
	
//Constants
	private static final String SCHEMA = "projects";
	private static final String USER = "projects";
	private static final String PASSWORD = "projects";
	private static final String HOST = "localhost";
	private static final int PORT = 3306;

		
	public static Connection getConnection() {
		String uri = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false", HOST, PORT, SCHEMA, USER, PASSWORD);
		System.out.println(" ");
		System.out.println("============================================================================================================");
		System.out.println("|| Connecting with url: " + uri + " ||");
			
		try {
			Connection conn = DriverManager.getConnection(uri);		//asked the driver to look up the connection for us
			System.out.println("|| Successfully obtained connection!                                                                      ||");//and asked driver to make the connection
			System.out.println("============================================================================================================");

			return conn;
		} catch (SQLException e) {
			System.out.println("|| ERROR getting connection                                                                                ||");
			System.out.println("============================================================================================================");
			throw new DbException(e);
		}
	}
}
