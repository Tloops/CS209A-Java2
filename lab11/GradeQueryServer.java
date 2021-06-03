package labBsocket;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.List;
public class GradeQueryServer {

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
                // insert record
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

            } catch (SQLException e) {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println("loadData:" + e.getMessage());
            }
        }
    }

    public static String queryName(String name){
        StringBuilder sb = new StringBuilder();
        if (con != null) {
            try {
                String SQL = "select * from student_grade where name = ?";
                PreparedStatement pstmt = con.prepareStatement(SQL);
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    sb.append(rs.getString("name")).append("'s grade is ")
                            .append(rs.getInt("grade")).append('\n');
                }
            } catch (SQLException e) {
                System.err.println("queryWithParameters:" + e.getMessage());
            }
        }
        return sb.toString();
    }

    public static String queryGradeBound(int lowerBound, int upperBound){
        StringBuilder sb = new StringBuilder();
        if (con != null) {
            try {
                String SQL = "select * from student_grade where grade > ? and grade < ?";
                PreparedStatement pstmt = con.prepareStatement(SQL);
                pstmt.setInt(1, lowerBound);
                pstmt.setInt(2, upperBound);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    sb.append(rs.getString("name")).append(", ").append(rs.getInt("grade")).append('\n');
                }
            } catch (SQLException e) {
                System.err.println("queryWithParameters:" + e.getMessage());
            }
        }
        return sb.toString();
    }

    public static String queryTop(int top){
        StringBuilder sb = new StringBuilder();
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
                    sb.append(name).append(", ").append(grade).append('\n');
//                    System.out.println(name + ", " + grade);
                }
            } catch (SQLException e) {
                System.err.println("queryWithParameters:" + e.getMessage());
            }
        }
        return sb.toString();
    }

    public static void queryService() throws IOException {
        int portNumber = 8888;
        PrintWriter out = null;
        BufferedReader in = null;
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server is OK, is waiting for connect...");
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Hava a connect");

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine, outputLine;
                // Wait for input
                if ((inputLine = in.readLine()) != null) {
                    System.out.println("Received " + inputLine);
                    String[] command = inputLine.split("\\s+");
                    String response = "";
                    if (command[0].equalsIgnoreCase("quit")){
                        response = "goodbye";
                    }
                    else if (command[0].equalsIgnoreCase("NAME")) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < command.length; i++) {
                            sb.append(command[i]);
                            sb.append(" ");
                        }
                        String name = sb.toString().trim();
                        response = queryName(name);
                    }
                    else if (command[0].equalsIgnoreCase("GRADE")){
                        int lowerBound = Integer.parseInt(command[1]);
                        int upperBound = Integer.parseInt(command[2]);
                        response = queryGradeBound(lowerBound, upperBound);
                    }
                    else if (command[0].equalsIgnoreCase("TOP")){
                        int bound = Integer.parseInt(command[1]);
                        response = queryTop(bound);
                    }
                    out.println(response);
                    System.out.println("Command processed");
                }
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println(
                    "Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }

        }
    }

    public static void main(String[] args) throws IOException {
        openDB("StudentDB.sqlite");
        loadCSV("StudentsGrade.csv");
        loadData();
        queryService();
        closeDB();
    }

}
