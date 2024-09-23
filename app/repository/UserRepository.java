// UserRepository.java
package repository;

import play.db.Database;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;

public class UserRepository {

    private final Database database;

    @Inject
    public UserRepository(Database database) {
        this.database = database;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = database.getConnection()) {
            String query = "SELECT * FROM User";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        User user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setName(resultSet.getString("name"));
                        user.setEmail(resultSet.getString("email"));
                        users.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            throw new UserRepositoryException("Error while retrieving users", e);
        }
        return users;
    }

    /*                                 This code is for testing custom defined exception  UserRepositoryException                  */

//    public List<User> getAllUsers() {
//        throw new UserRepositoryException("Simulated exception in getAllUsers method");
//    }

    public void addUser(User user) {
        try (Connection connection = database.getConnection()) {
            String query = "INSERT INTO `User` (`id`, `name`, `email`) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, user.getId());
                statement.setString(2, user.getName());
                statement.setString(3, user.getEmail());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new UserRepositoryException("Error while adding a user", e);
        }
    }

    public void removeUser(int id) {
        try (Connection connection = database.getConnection()) {
            String query = "DELETE FROM `User` WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new UserRepositoryException("Error while removing a user", e);
        }
    }

    public void update(User user) {
        try (Connection connection = database.getConnection()) {
            String query = "UPDATE `User` SET `name`=?, `email`=? WHERE `id`=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getEmail());
                statement.setInt(3, user.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new UserRepositoryException("Error while updating a user", e);
        }
    }

    public User getUserById(int id) {
        User user = null;
        try (Connection connection = database.getConnection()) {
            String query = "SELECT * FROM `User` WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setName(resultSet.getString("name"));
                        user.setEmail(resultSet.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new UserRepositoryException("Error while retrieving a user by ID", e);
        }
        return user;
    }
}