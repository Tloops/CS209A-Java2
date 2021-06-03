package lab9;

import org.apache.commons.csv.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class GradeQuery {

    static Connection con = null;
    static List<CSVRecord> records = null;

    private static void openDB(String dbPath) {
        try {
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

    public static void loadCSV(String csvPath){
        final String[] HEADERS = {"Name", "Grade"};
        Reader in = null;
        try {
            in = new FileReader(csvPath);
            CSVParser parser = CSVFormat.RFC4180.withHeader(HEADERS)
                    .withFirstRecordAsHeader().withQuoteMode(QuoteMode.ALL).parse(in);
            records = parser.getRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData() {
        if (con != null) {
            try {
                // create a database connection
                Statement statement = con.createStatement();
                statement.setQueryTimeout(30); // set timeout to 30 sec.

                statement.executeUpdate("drop table if exists student_grade");
                // create table
                statement.executeUpdate("create table student_grade (name string, grade integer)");
                // insert records
//                statement.executeUpdate("insert into student_grade values(1, 'leo')");
//                statement.executeUpdate("insert into student_grade values(2, 'yui')");
                for (CSVRecord record: records) {
                    String name = record.get("Name");
                    int grade = Integer.parseInt(record.get("Grade"));
                    if (con != null) {
                        try {
                            String SQL = "insert into student_grade values(?, ?)";
                            PreparedStatement pstmt = con.prepareStatement(SQL);
                            pstmt.setString(1, name);
                            pstmt.setInt(2, grade);
                            pstmt.executeUpdate();
                        } catch (SQLException e) {
                            System.err.println("queryWithParameters:" + e.getMessage());
                        }
                    }
                }
                con.commit();

//                ResultSet rs = statement.executeQuery("select * from student_grade");
//                while (rs.next()) {
//                    System.out.println("name = " + rs.getString("name"));
//                    System.out.println("grade = " + rs.getInt("grade"));
//                }
//                rs.close();

            } catch (SQLException e) {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println("loadData:" + e.getMessage());
            }
        }
    }

    public static void queryName(String name){
        if (con != null) {
            try {
                String SQL = "select * from student_grade where name = ?";
                PreparedStatement pstmt = con.prepareStatement(SQL);
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("name") + "'s grade is " + rs.getInt("grade"));
                }
            } catch (SQLException e) {
                System.err.println("queryWithParameters:" + e.getMessage());
            }
        }
    }

    public static void queryGradeBound(int lowerBound, int upperBound){
        if (con != null) {
            try {
                String SQL = "select * from student_grade where grade > ? and grade < ?";
                PreparedStatement pstmt = con.prepareStatement(SQL);
                pstmt.setInt(1, lowerBound);
                pstmt.setInt(2, upperBound);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("name") + ", " + rs.getInt("grade"));
                }
            } catch (SQLException e) {
                System.err.println("queryWithParameters:" + e.getMessage());
            }
        }
    }

    public static void queryTop(int top){
        if (con != null) {
            try {
                String SQL = "select * from student_grade order by grade desc";
                PreparedStatement pstmt = con.prepareStatement(SQL);
                ResultSet rs = pstmt.executeQuery();
                int count = 0;
                int currentValue = -1;
                while (rs.next()) {
                    int grade = rs.getInt("grade");
                    String name = rs.getString("name");
                    if(currentValue != grade) {
                        currentValue = grade;
                        count++;
                        if(count > top)
                            break;
                    }
                    System.out.println(name + ", " + grade);
                }
            } catch (SQLException e) {
                System.err.println("queryWithParameters:" + e.getMessage());
            }
        }
    }

    public static void queryService(){
        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.next();
            if (command.equalsIgnoreCase("quit")){
                break;
            }
            if (command.equalsIgnoreCase("NAME")) {
                String name = in.nextLine().trim();
                queryName(name);
            } else if (command.equalsIgnoreCase("GRADE")){
                String[] upperAndLowerBound = in.nextLine().trim().split(" ");
                int lowerBound = Integer.parseInt(upperAndLowerBound[0]);
                int upperBound = Integer.parseInt(upperAndLowerBound[1]);
                queryGradeBound(lowerBound, upperBound);
            } else if (command.equalsIgnoreCase("TOP")){
                int bound = Integer.parseInt(in.nextLine().trim());
                queryTop(bound);
            }
        }
    }

    public static void main(String[] args) {
        openDB("StudentDB.sqlite");
        loadCSV("StudentsGrade.csv");
        loadData();
        queryService();
        closeDB();
    }

}
