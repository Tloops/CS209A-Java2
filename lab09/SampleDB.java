package lab9;

import java.sql.*;

public class SampleDB {
	//
	static Connection con = null;

	private static void openDB(String dbPath) {
		try {
			// CLASSPATH must be properly set, for instance on
			// a Linux system or a Mac:
			// $ export CLASSPATH=.:sqlite-jdbc-version-number.jar
			// Alternatively, run the program with
			// $ java -cp .:sqlite-jdbc-version-number.jar BasicJDBC
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			System.err.println("Cannot find the driver.");
			System.exit(1);
		}
		try {
			con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			con.setAutoCommit(false);
			System.err.println("Successfully connected to the database.");
		} catch (Exception e) {
			System.err.println("openDB" + e.getMessage());
			System.exit(1);
		}
	}

	private static void closeDB() {
		if (con != null) {
			try {
				con.close();
				con = null;
			} catch (Exception e) {
				// Forget about it
			}
		}
	}

	private static void querySqlite_master() {
		// We query the sqlite_master table that contains
		// the names of all other tables in the database,
		// and for each table we count how many rows it
		// contains.
		if (con != null) {
			try {
				Statement stmt1;
				ResultSet rs1;
				Statement stmt2;
				ResultSet rs2;
				int tabcnt = 0;
				stmt1 = con.createStatement();
				rs1 = stmt1.executeQuery("select name from sqlite_master" + " where type='table'");
				while (rs1.next()) {
					stmt2 = con.createStatement();
					rs2 = stmt2.executeQuery("select count(*) from " + rs1.getString(1));
					if (rs2.next()) {
						System.out.println(rs1.getString(1) + ":\t" + rs2.getInt(1) + " rows");
					}
					rs2.close();
					tabcnt++;
				}
				rs1.close();
				if (tabcnt == 0) {
					System.out.println("No tables in the file");
				}
				con.commit();
			} catch (Exception e) {
				System.err.println("querySqlite_master:" + e.getMessage());
				System.exit(1);
			}
		}
	}

	public static void loadData() {
		if (con != null) {
			try {
				// create a database connection
				Statement statement = con.createStatement();
				statement.setQueryTimeout(30); // set timeout to 30 sec.

				statement.executeUpdate("drop table if exists person");
				// create table
				statement.executeUpdate("create table person (id integer, name string)");
				// insert records
				statement.executeUpdate("insert into person values(1, 'leo')");
				statement.executeUpdate("insert into person values(2, 'yui')");
				ResultSet rs = statement.executeQuery("select * from person");
				while (rs.next()) {
					// read the result set
					System.out.println("name = " + rs.getString("name"));
					System.out.println("id = " + rs.getInt("id"));
				}
				rs.close();
				con.commit();

			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				System.err.println("loadData:" + e.getMessage());
			}
		}
	}

	public static void queryWithParameters() {
		if (con != null) {
			try {
				String SQL = "insert into person values(?, ?)";
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setInt(1, 3);
				pstmt.setString(2, "alice");
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					// read the result set
					System.out.print("id = " + rs.getInt("id"));
					System.out.println("   name = " + rs.getString("name"));
				}
			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				System.err.println("queryWithParameters:" + e.getMessage());
			}
		}

	}

	public static void queryExecuteBatch() {
		if (con != null) {
			try {
				// Create SQL statement
				String SQL = "INSERT INTO person (id, name) " + "VALUES(?, ?)";

				// Create PrepareStatement object
				PreparedStatement pstmt = con.prepareStatement(SQL);

				for (int i = 101; i < 201; i++) {
					// Set the variables
					pstmt.setInt(1, i);
					pstmt.setString(2, "Robot" + i);

					// Add it to the batch
					pstmt.addBatch();
				}

				// Create an int[] to hold returned values
				int[] count = pstmt.executeBatch();
				System.out.println(count);
				// Explicitly commit statements to apply changes
				con.commit();

			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				System.err.println("queryWithParameters:" + e.getMessage());
			}
		}

	}

	public static void printPerson() {
		if (con != null) {
			try {
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery("select * from person");
				while (rs.next()) {
					// read the result set
					System.out.println("name = " + rs.getString("name"));
					System.out.println("id = " + rs.getInt("id"));
				}
				rs.close();
			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				System.err.println("printPerson:" + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			openDB(args[0]);
			loadData();
			querySqlite_master();
			queryWithParameters();
			queryExecuteBatch();
			printPerson();
			querySqlite_master();
			closeDB();
		}
	}
}
