package service;
import domain.User;
import domain.Consomation;

import repository.ConsomationRepository;

import java.util.Optional;

public class ConsomationService {
    private UserService userService;
    private ConsomationRepository consomationRepository;

    public ConsomationService(UserService userService, ConsomationRepository consomationRepository) {
        this.userService = userService;
        this.consomationRepository = consomationRepository;
    }

    public Optional<Consomation> addConsomation(Consomation consomation, User user) {
        if (user == null) {
            return Optional.empty();
        }
        consomation.setUser(user);
        return consomationRepository.addConsomation(consomation);
    }
}
