package workshop;

import java.sql.*;

public class DBUtil {


    private static final String DB_URL = "jdbc:mysql://localhost:3306/workshop2?useSSL=false&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "coderslab";

    private static final String DELETE_QUERY = "DELETE FROM tableName where id = ?";

    public static void main(String[] args) {

        String createDB = "CREATE DATABASE products_ex CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

        String createTable = "CREATE TABLE users (\n" +
                "    ID INT(11) AUTO_INCREMENT,\n" +
                "    email VARCHAR(255) UNIQUE,\n" +
                "    username VARCHAR(255),\n" +
                "    password VARCHAR(60),\n" +
                "    PRIMARY KEY (ID)\n" +
                ")";




        String sql1 = "CREATE TABLE users (user_id INT AUTO_INCREMENT,"
                + " user_name VARCHAR(255),"
                + " user_email VARCHAR(255) UNIQUE, "
                + " PRIMARY KEY(user_id))";
        String sql2 = "SELECT * FROM products WHERE PRICE>11";
        String sql3 = "DROP TABLE users";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/products_ex?allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC",
                    "root", "coderslab");
            Statement stat = conn.createStatement();
            {

                stat.execute(sql2);

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM clients");
                while (rs.next()) {
                    String firstName = rs.getString("NAME");
                    int id = rs.getInt("ID");
                    System.out.println(id + " " + firstName);
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static Connection connect() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        finally {
//            if(conn!=null){
//                try{
//                    conn.close();
//                }catch (SQLException e) { e.printStackTrace(); }
//            }
//
//        }
        return conn;

    }

    public static void disconnect(Connection conn) //throws SQLException{
    {
        try {
            conn.close();
            //conn=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void createTable(Connection conn, String sql) throws SQLException {
        //Connection conn = DBUtil.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }

    public static void insert(Connection conn, String query, String... params) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void remove(Connection conn, String tableName, int id) {
        try (PreparedStatement statement =
                     conn.prepareStatement(DELETE_QUERY.replace("tableName", tableName));) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void printData(Connection conn, String query, String... columnNames) throws SQLException {

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                for (String columnName : columnNames) {
                    System.out.print(resultSet.getString(columnName) + ", ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

