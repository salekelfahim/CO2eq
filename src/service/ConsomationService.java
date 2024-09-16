package service;
import domain.User;
import domain.Consomation;

import repository.ConsomationRepository;

import java.sql.SQLException;
import java.util.Optional;

public class ConsomationService {
    private UserService userService;
    private ConsomationRepository consomationRepository;

    public ConsomationService(UserService userService, ConsomationRepository consomationRepository) {
        this.userService = userService;
        this.consomationRepository = consomationRepository;
    }

    public Optional<Consomation> addConsomation(Consomation consomation,int userID) throws SQLException {
        Optional<User> user = userService.findUserById(userID);
        if (user.isEmpty()){
            return Optional.empty();
        }
        consomation.setUser(user.get());
        return consomationRepository.addConsomation(consomation);
    }
}
