package Entity;

import org.mindrot.jbcrypt.BCrypt;
import workshop.DBUtil;

import java.sql.*;
import java.util.Arrays;

public class UserDAO {

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String GET_USER_QUERY = "Select * From users Where ID = ?";
    private static final String FIND_ALL_QUERY = "Select * From users ";

    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ? where id = ?";

    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";


    public static void createUser(Connection conn) throws SQLException {
        PreparedStatement preStmt =
                conn.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);

        ResultSet rs = preStmt.getGeneratedKeys();
        if (rs.next()) {
            long id = rs.getLong(1);
            System.out.println("Inserted ID: " + id);
        }


    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public User create(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int id) {

        User user = null;

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(GET_USER_QUERY);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();



//            if(resultSet.next()){
//                User user = new User();
//
//            }
            while (resultSet.next()) {
                System.out.println("read(id) - Znaleziono użytkownika o id = " + id);
                user = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
                user.setId(id);
                System.out.println(user);

            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
        if (user != null) {
            System.out.println("Zwrócony user: " + user);
        } else System.out.println("read (id) - Nie ma w bazie użytkownika o id= " + id + " Zwrócony user : " + user);

        return user;

    }

    public void update(User user) {

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(UPDATE_USER_QUERY);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());


            statement.executeUpdate();
          //  ResultSet resultSet = statement.executeQuery();
            if(statement.executeUpdate()==1){
                System.out.println("Update()user - Zaktualizowano dane użytkownika o id: " + user.getId());
            }
            else System.out.println("Update(user) - Nie ma w bazie danych użytkownika o id: "+ user.getId());

        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, id);
            statement.executeUpdate();
            if(statement.executeUpdate()==1){
            System.out.println("Delete() - Usunięto użytkownika o id: "+ id);}
            else System.out.println("Delete() - Nie ma w bazie danych użytkownika o id: "+ id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers; // Zwracamy nową tablicę.
    }

    public User[] findAll(){

        User[] users = new User[0];

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(FIND_ALL_QUERY);
           // statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                System.out.println("read(id) - Znaleziono użytkownika o id = " + resultSet.getInt(1) );
                user.setUserName(resultSet.getString(3));
                user.setEmail(resultSet.getString(2));
                user.setPassword(resultSet.getString(4));
                user.setId(resultSet.getInt(1));
                users = addToArray(user,users);

            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return users;

    }

    public static void printUsers(User[] users){
        for (User user :users) {
            System.out.println(user);
        }
    }





}
