package io.muic.ooc.webapp.service;

import io.muic.ooc.webapp.model.User;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Setter
public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user(username, password, display_name) VALUES (?, ?, ?)";
    private static final String SELECT_USER_SQL = "SELECT * FROM tbl_user WHERE username = ?";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM tbl_user";

    private static UserService service;

    private DatabaseConnectionService databaseConnectionService;

    private UserService() {

    }

    public static UserService getInstance() {
        if (service == null) {
            service = new UserService();
            service.setDatabaseConnectionService(DatabaseConnectionService.getInstance());
        }
        return service;
    }

    // create new user
    public void createUser(String username, String password, String displayName) throws UserServiceException {
        // password need to be hashed and salted so we will need bcrypt library

        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);
                ) {

            ps.setString(1, username);
            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            ps.setString(3, displayName);
            ps.executeUpdate();
            connection.commit();
        } catch(SQLIntegrityConstraintViolationException e) {
            throw new UsernameNotUniqueException(String.format("Username '%s' already exists.", username));
        }
        catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }

    }

    // find user by username
    public User findUser(String username) {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_USER_SQL);
                ) {

            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("display_name")
            );
        } catch (SQLException throwables) {
            return null;
        }
    }

    // list all users
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL_USERS_SQL);
                ) {

            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                users.add(
                        new User(resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("display_name"))
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    // delete user
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // update user by user id
    public void updateUserById(long id, String displayName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void changePassword(String newPassword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        UserService userService = UserService.getInstance();
        try {
            userService.createUser("admin", "123456", "Admin");
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
//        List<User> users = userService.findAll();
//        for (User user : users) {
//            System.out.println(user.getUsername());
//        }
    }
}
