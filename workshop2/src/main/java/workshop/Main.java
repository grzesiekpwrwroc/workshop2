package workshop;

import Entity.User;
import Entity.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {

    static String createTable = "CREATE TABLE users (\n" +
            "    ID INT(11) AUTO_INCREMENT,\n" +
            "    email VARCHAR(255) UNIQUE,\n" +
            "    username VARCHAR(255),\n" +
            "    password VARCHAR(60),\n" +
            "    PRIMARY KEY (ID)\n" +
            ")";

    public static void main(String[] args) throws SQLException {

        Connection conn = DBUtil.getConnection();
        //DBUtil.createTable(conn, createTable);
       //User user = new User("Grzegorz","grzegorz.jarus@gmail.com","BardzoMocneHasło");
        User user2 = new User("Paulina","paulina.kowalska@onet.com","123456");
        //user2.setId(4);
        UserDAO userDAO = new UserDAO();
       // userDAO.create(user);
       //userDAO.create(user2);
        userDAO.read(3);
        userDAO.read(1);
        userDAO.update(user2);
       // userDAO.update(userDAO.read(3));
        //UserDAO.createUser(conn);
        userDAO.delete(3);
        UserDAO.printUsers(userDAO.findAll());
        DBUtil.disconnect(conn);
    }
}
