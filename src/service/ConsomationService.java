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

    public ConsomationService(UserService userService, ConsomationRepository consomationRepository, Connection connection) {
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
                .filter(user -> {
                    try {
                        return calculerImpactTotal(user) > 3000;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public double averageImpactByPeriod(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

        List<Consomation> consumptions = consomationRepository.getUserConsomations(user);

        List<LocalDate> dates = DateUtils.dateLitRange(startDate, endDate);

        List<Consomation> consumptionsInPeriod = consumptions.stream()
                .filter(consumption -> !(consumption.getStartDate().isAfter(endDate) || consumption.getEndDate().isBefore(startDate)))
                .collect(Collectors.toList());

        double totalImpact = consumptionsInPeriod.stream()
                .mapToDouble(Consomation::calculImpact)
                .sum();

        return totalImpact / dates.size();
    }

    public List<User> detectInactiveUsers(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<User> users = userRepository.afficherUsers();
        List<User> inactiveUsers = new ArrayList<>();

        for (User user : users) {
            List<Consomation> consumptions = consomationRepository.getUserConsomations(user);
            boolean isActive = consumptions.stream()
                    .anyMatch(consumption -> !(consumption.getStartDate().isAfter(endDate) || consumption.getEndDate().isBefore(startDate)));
            if (!isActive) {
                inactiveUsers.add(user);
            }
        }
        return inactiveUsers;
    }

    public void generateDailyConsomationReport(int userId) throws SQLException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

        List<Consomation> consumptions = consomationRepository.getUserConsomations(user);

        if (consumptions.isEmpty()) {
            System.out.println("No consomation data for user " + user.getName());
            return;
        }

        consumptions.forEach(consumption -> {
            LocalDate startDate = consumption.getStartDate();
            LocalDate endDate = consumption.getEndDate();

            List<LocalDate> dateRange = DateUtils.dateLitRange(startDate, endDate);

            long totalDays = dateRange.size();
            double dailyConsomation = consumption.getValue() / totalDays;
            dateRange.forEach(date ->
                    System.out.println("On " + date + ", daily consomtion is " + dailyConsomation + " mg")
            );
        });
    }

    public void generateWeeklyConsomationReport(int userId) throws SQLException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

        List<Consomation> consumptions = consomationRepository.getUserConsomations(user);

        if (consumptions.isEmpty()) {
            System.out.println("No consomation data for user " + user.getName());
            return;
        }

        consumptions.forEach(consumption -> {
            LocalDate startDate = consumption.getStartDate();
            LocalDate endDate = consumption.getEndDate();

            LocalDate currentStartDate = startDate;
            while (currentStartDate.isBefore(endDate)) {
                LocalDate weekEndDate = currentStartDate.plusWeeks(1).minusDays(1);
                if (weekEndDate.isAfter(endDate)) weekEndDate = endDate;

                List<LocalDate> weeklyRange = DateUtils.dateLitRange(currentStartDate, weekEndDate);
                long totalDays = weeklyRange.size();
                double weeklyConsomation = consumption.getValue() / (double) totalDays;

                System.out.println("From " + currentStartDate + " to " + weekEndDate + ", weekly consomation is " + weeklyConsomation + " mg");

                currentStartDate = weekEndDate.plusDays(1);
            }
        });
    }

    public void generateMonthlyConsomationReport(int userId) throws SQLException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

        List<Consomation> consumptions = consomationRepository.getUserConsomations(user);

        if (consumptions.isEmpty()) {
            System.out.println("No consomation data for user " + user.getName());
            return;
        }

        consumptions.forEach(consumption -> {
            LocalDate startDate = consumption.getStartDate();
            LocalDate endDate = consumption.getEndDate();

            LocalDate currentStartDate = startDate.withDayOfMonth(1);
            while (currentStartDate.isBefore(endDate)) {
                LocalDate monthEndDate = currentStartDate.withDayOfMonth(currentStartDate.lengthOfMonth()).minusDays(1);
                if (monthEndDate.isAfter(endDate)) monthEndDate = endDate;

                List<LocalDate> monthlyRange = DateUtils.dateLitRange(currentStartDate, monthEndDate);
                long totalDays = monthlyRange.size();
                double monthlyConsomation = consumption.getValue() / (double) totalDays;

                System.out.println("From " + currentStartDate + " to " + monthEndDate + ", monthly consomation is " + monthlyConsomation + " mg");

                currentStartDate = monthEndDate.plusDays(1).withDayOfMonth(1);
            }
        });
    }
    public List<Consomation> getUserConsomations(User user) throws SQLException {
        return consomationRepository.getUserConsomations(user);
    }



}
