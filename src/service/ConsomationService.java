package service;
import domain.User;
import domain.Consomation;
import repository.ConsomationRepository;
import repository.UserRepository;
import Utils.DateUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ConsomationService {
    private UserService userService;
    private ConsomationRepository consomationRepository;
    private Connection connection;
    private UserRepository userRepository;

    public ConsomationService(UserService userService, ConsomationRepository consomationRepository) {
        this.userService = userService;
        this.consomationRepository = consomationRepository;
        this.connection = connection;
        this.userRepository = new UserRepository(connection);
    }

    public Optional<Consomation> addConsomation(Consomation consomation, int userID) throws SQLException {
        Optional<User> user = userService.findUserById(userID);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        consomation.setUser(user.get());
        return consomationRepository.addConsomation(consomation);
    }

    public double calculerImpactTotal(User user) throws SQLException {
        List<Consomation> consomations = consomationRepository.getUserConsomations(user);
        return consomations.stream()
                .mapToDouble(Consomation::calculImpact)
                .sum();
    }

    public List<User> CalculConsomationImpact() throws SQLException {
        List<User> users = userRepository.afficherUsers();
        return users.stream()
                .filter(e -> {
                    try {
                        return calculerImpactTotal(e) > 3000;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public double moyenneConsByPeriode(int userId, LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("Date de début doit être avant la date de fin");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur avec l'ID " + userId + " n'existe pas."));

        List<Consomation> consomations = consomationRepository.getUserConsomations(user);

        List<LocalDate> dates = DateUtils.dateLitRange(dateDebut, dateFin);

        List<Consomation> consomationsPeriode = consomations.stream()
                .filter(consomation -> !(consomation.getStartDate().isAfter(dateFin) || consomation.getEndDate().isBefore(dateDebut)))
                .collect(Collectors.toList());

        double impactTotal = consomationsPeriode.stream()
                .mapToDouble(Consomation::calculImpact)
                .sum();

        return impactTotal / dates.size();
    }

    public List<User> detectInactiveUsers(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        List<User> users = userRepository.afficherUsers();
        List<User> inactiveUsers = new ArrayList<>();

        for (User user : users) {
            List<Consomation> consomations = consomationRepository.getUserConsomations(user);
            boolean isActive = consomations.stream()
                    .anyMatch(consomation -> !(consomation.getStartDate().isAfter(dateFin) || consomation.getEndDate().isBefore(dateDebut)));
            if (!isActive) {
                inactiveUsers.add(user);
            }
        }
        return inactiveUsers;
    }

    public void rapportConsomationDaily(int userId) throws SQLException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur avec l'ID " + userId + " n'existe pas."));

        List<Consomation> consomations = consomationRepository.getUserConsomations(user);

        if (consomations.isEmpty()) {
            System.out.println("Pas de consommation pour cet utilisateur " + user.getName());
            return;
        }

        consomations.forEach(consomation -> {
            LocalDate startDate = consomation.getStartDate();
            LocalDate endDate = consomation.getEndDate();

            List<LocalDate> rangeDate = DateUtils.dateLitRange(startDate, endDate);

            long totalDays = rangeDate.size();
            double consomationDaily = consomation.getValue() / totalDays;
            rangeDate.forEach(date ->
                    System.out.println("Le " + date + ", consommation de " + consomationDaily + " mg")
            );
        });
    }




}
