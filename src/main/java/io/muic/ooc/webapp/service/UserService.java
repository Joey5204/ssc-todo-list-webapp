package io.muic.ooc.webapp.service;

import io.muic.ooc.webapp.model.Task;
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
    private static final String SELECT_TASKS_BY_USERNAME_SQL = "SELECT * FROM tbl_task WHERE username = ?";
    private static final String INSERT_TASK_SQL = "INSERT INTO tbl_task(name, description, status, username) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_TASK_SQL = "UPDATE tbl_task SET name = ?, description = ?, status = ? WHERE id = ?";
    private static final String DELETE_TASK_SQL = "DELETE FROM tbl_task WHERE id = ?";
    private static final String SELECT_TASK_BY_ID_SQL = "SELECT * FROM tbl_task WHERE id = ? AND username = ?";


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

    public List<Task> findTasksByUsername(String username) {
        List<Task> tasks = new ArrayList<>();
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_TASKS_BY_USERNAME_SQL);
        ) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();

            // Debugging information before the loop
            System.out.println("Executed SQL Query: " + SELECT_TASKS_BY_USERNAME_SQL);
            System.out.println("Username parameter: " + username);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String taskName = resultSet.getString("name");
                String taskDescription = resultSet.getString("description");
                boolean isCompleted = resultSet.getBoolean("status");

                // Debugging information inside the loop
                System.out.println("Retrieved Task - ID: " + id + ", Name: " + taskName +
                        ", Description: " + taskDescription + ", Status: " + (isCompleted ? "Completed" : "Incomplete"));

                tasks.add(new Task(id, taskName, taskDescription, isCompleted));
            }

            // Debugging information after the loop
            System.out.println("Tasks found for username '" + username + "': " + tasks.size());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tasks;
    }

    public Task getTaskById(long id, String username) throws SQLException {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_TASK_BY_ID_SQL);
        ) {
            ps.setLong(1, id);
            ps.setString(2, username);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return new Task(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("status")
                );
            }
        }
        return null; // Task not found
    }

    public void createTask(String name, String description, boolean status, String username) throws SQLException {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_TASK_SQL);
        ) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setBoolean(3, status);
            ps.setString(4, username);
            ps.executeUpdate();
            connection.commit();
        }
    }

    public void updateTask(long id, String name, String description, boolean status) throws SQLException {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(UPDATE_TASK_SQL);
        ) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setBoolean(3, status);
            ps.setLong(4, id);
            ps.executeUpdate();
            connection.commit();
        }
    }

    public void deleteTask(long id) throws SQLException {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_TASK_SQL);
        ) {
            ps.setLong(1, id);
            ps.executeUpdate();
            connection.commit();
        }
    }


    public static void main(String[] args) {
        UserService userService = UserService.getInstance();
        try {
            // Create a test user (if not already created)
            userService.createUser("test_task", "123456", "test_task");

            // Create a few tasks for the test user
            userService.createTask("Test Task 1", "Description for Task 1", false, "test_task");
            userService.createTask("Test Task 2", "Description for Task 2", true, "test_task");

            // Retrieve the tasks for the test user
            List<Task> tasks = userService.findTasksByUsername("test_task");

            // Print out the tasks to check if they were retrieved correctly
            System.out.println("Tasks for user 'test_task':");
            for (Task task : tasks) {
                System.out.println("ID: " + task.getId() + ", Name: " + task.getName() +
                        ", Description: " + task.getDescription() + ", Status: " + (task.isStatus() ? "Completed" : "Incomplete"));
            }
        } catch (UserServiceException | SQLException e) {
            e.printStackTrace();
        }
    }
}
