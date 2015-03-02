import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.Statement;

public class Database {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/javaDB";
	static final String USER = "root";
	static final String PASS = "explore_lk";
	Logger logger = Logger.getLogger("Database");

	Connection conn = null;

	public Database() {
		try {
			Class.forName(JDBC_DRIVER);
			this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
			logger.log(Level.INFO, "Connection Made");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Unable to load driver");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Unable to make connection");
			e.printStackTrace();
		}

	}

	public void CreateTableUser() {
		Statement stmt = null;

		String sql = "CREATE TABLE User "
				+ "(id INTEGER not NULL AUTO_INCREMENT, "
				+ " name VARCHAR(255), " + " password VARCHAR(255), "
				+ " PRIMARY KEY ( id ))";
		try {
			stmt = (Statement) this.conn.createStatement();
			stmt.executeUpdate(sql);
			logger.log(Level.INFO, "USER Table Created");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, "Can't Create User Table ");
		}

	}

	public void InsertUser(String name, String password) {
		Statement stmt = null;
		String sql = "INSERT INTO User (name, password) " + "VALUES (" + "'"
				+ name + "'" + ", " + "" + "'" + password + "'" + ")";
		try {
			stmt = (Statement) this.conn.createStatement();
			stmt.executeUpdate(sql);
			logger.log(Level.INFO, "Values Inserted");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, "Can't Insert Values In User Table");
		}

	}

	public HashMap<String, String> GetRecordOfUser() {
		Statement stmt = null;
		HashMap<String, String> map = new HashMap<String, String>();
		String sql = "SELECT name, password FROM User";
		try {
			stmt = (Statement) this.conn.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			while (set.next()) {
				String name = set.getString("name");
				String password = set.getString("password");
				map.put(name, password);
				logger.log(Level.INFO, "Values Retrieved From User Table: "
						+ name + "->" + password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, "Can't Retrieve Values From User Table");
		}
		return map;
	}

	public String GetRecordOfSpecficUser(String password) {
		Statement stmt = null;
		String name = null;
		String sql = "SELECT name, password FROM User WHERE password = " + "'"
				+ password + "' ";
		try {
			stmt = (Statement) this.conn.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			while (set.next()) {
				name = set.getString("name");
				logger.log(Level.INFO, "Value Retrieved From User Table: "
						+ name + "->" + password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, "Can't Retrieve Value From User Table");
		}
		return name;
	}
	public void Close()
	{
		try {
			this.conn.close();
			logger.log(Level.INFO, "Connection closed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unable to close connection");
		}
	}
	public static void main(String[] args) {
		Database db = new Database();
		db.CreateTableUser();
		db.InsertUser("natesh", "password");
		db.InsertUser("rachit", "password1");
		db.GetRecordOfUser();
		db.GetRecordOfSpecficUser("password");
	}

}
