package com.xlibrserver;

import com.xlibrpkg.BookData;
import com.xlibrpkg.Log;
import com.xlibrpkg.UserData;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnect implements Serializable {
	private static DBConnect s_Instance = null;

	private static Connection s_Connection;
	private static PreparedStatement s_PreparedStatement;
	private static ResultSet s_ResultSet;
	private static Statement s_Statement;
	private static int userID;

	private DBConnect() {
		s_Connection = null;
		s_PreparedStatement = null;
		s_ResultSet = null;

		try {
			s_Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xlibr","root","H!vxtK2zzL^#Ps_9");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean Login(String _username, String _password) {
		try {
			String statement = "SELECT * FROM user_data WHERE user_name='" + _username + "' AND pass_word='" + _password +"'";
			s_PreparedStatement = s_Connection.prepareStatement(statement);
			s_ResultSet = s_PreparedStatement.executeQuery();

			int count = 0;
			while(s_ResultSet.next()) {
				userID = s_ResultSet.getInt(1);
				XLibrconnect.s_UserRole = s_ResultSet.getInt("user_role");
				count++;
			}
			Log.getInstance();
			Log.INFO("There are " + count + " users that mach the received data");

			if (count == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean Signup(UserData _user) {
		try {
			String statement = "SELECT * FROM user_data WHERE user_name='" + _user.getUsername() + "' OR email='" + _user.getEmail() + "'";
			s_PreparedStatement = s_Connection.prepareStatement(statement);
			s_ResultSet = s_PreparedStatement.executeQuery();

			int count = 0;
			while(s_ResultSet.next())
				count++;
			Log.getInstance();
			Log.INFO("There are " + count + " users that mach the received data");

			if (count > 0)
				return false;
			else {
				String insert = "INSERT INTO user_data (user_name, first_name, last_name, email, address, pass_word, user_role)" +
						"VALUES (\"" + _user.getUsername()  + "\", " + "\"" + _user.getFirstname()  + "\", " + "\"" + _user.getLastname()  + "\", " + "\"" + _user.getEmail()  + "\", " + "\"" + _user.getAddress()  + "\", " + "\"" + _user.getPassword()  + "\", " +  0  + ");";

				s_Statement = s_Connection.createStatement();
				s_Statement.executeUpdate(insert);

				String userid = "SELECT * FROM user_data WHERE user_name='" + _user.getUsername() + "' AND pass_word='" + _user.getPassword() +"'";
				s_PreparedStatement = s_Connection.prepareStatement(userid);
				s_ResultSet = s_PreparedStatement.executeQuery();

				while(s_ResultSet.next()) {
					userID = s_ResultSet.getInt(1);
				}

				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static List<BookData> GetBooks() {
		List<BookData> bookList = new ArrayList<BookData>();
		BookData book = new BookData();

		try {
			String statement = "SELECT * FROM book_data";
			s_PreparedStatement = s_Connection.prepareStatement(statement);
			s_ResultSet = s_PreparedStatement.executeQuery();

			while(s_ResultSet.next()) {
				book.id = s_ResultSet.getInt("id");
				book.title = s_ResultSet.getString("title");
				book.author = s_ResultSet.getString("author");
				book.publisher = s_ResultSet.getString("publisher");
				book.synopsis = s_ResultSet.getString("synopsis");
				book.releaseYear = s_ResultSet.getInt("release_year");
				bookList.add(book);
				book = new BookData();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bookList;
	}

	public static List<BookData> GetUserBooks() {
		List<BookData> bookList = new ArrayList<BookData>();
		BookData book = new BookData();

		try {
			Log.WARN("" + userID);
			String statement = "SELECT * FROM book_data JOIN user_book ON book_data.id=book_id WHERE user_id=" + userID;
			s_PreparedStatement = s_Connection.prepareStatement(statement);
			s_ResultSet = s_PreparedStatement.executeQuery();

			while(s_ResultSet.next()) {
				book.id = s_ResultSet.getInt("id");
				book.title = s_ResultSet.getString("title");
				book.author = s_ResultSet.getString("author");
				book.publisher = s_ResultSet.getString("publisher");
				book.synopsis = s_ResultSet.getString("synopsis");
				book.releaseYear = s_ResultSet.getInt("release_year");
				bookList.add(book);
				book = new BookData();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bookList;
	}

	public static void BorrowBook(int _bookID) {
		String insert = "INSERT INTO user_book (user_id, book_id)" +
				"VALUES (" + userID + ", " + _bookID +" );";

		try {
			s_Statement = s_Connection.createStatement();
			s_Statement.executeUpdate(insert);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void AddBook() {

	}

	public static void GetBookData() {

	}


	public static DBConnect getInstance() {
		if (s_Instance == null)
			s_Instance = new DBConnect();

		return s_Instance;
	}
}
