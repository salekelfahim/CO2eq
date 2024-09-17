package service;

import config.DatabaseConnection;
import domain.User;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UserService {
    private Connection connection = DatabaseConnection.getInstance().getConnection();
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = new UserRepository(connection);
    }

    public void addUser(String name, int age) throws SQLException {
        User nouvUser = new User(name, age);
        userRepository.addUser(nouvUser);

    }

    public List<User> afficherUsers() throws SQLException {
        return userRepository.afficherUsers();
    }

    public boolean updateUser(int id, String name, int age) throws SQLException {
        User user = new User(id, name, age);
        return userRepository.updateUser(user);

    }

    public Optional<User> findUserById(int id) throws SQLException {
        return userRepository.findById(id);
    }


    public boolean deleteUser(int id) throws SQLException {
        return userRepository.deleteUser(id);
    }

    public Optional<User> findById(int id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                System.out.println("ID: " + user.get().getId() + ", Name: " + user.get().getName() + ", Age: " + user.get().getAge());
            } else {
                System.out.println("User not found with ID: " + id);
            }
            return user;
        } catch (SQLException e) {
            System.out.println("Error fetching user by ID: " + e.getMessage());
            return Optional.empty();
        }
    }
}
