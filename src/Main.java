import config.DatabaseConnection;
import domain.*;
import repository.ConsomationRepository;
import repository.UserRepository;
import service.ConsomationService;
import service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException {

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection = dbConnection.getConnection();

        UserRepository userRepository = new UserRepository(DatabaseConnection.getInstance().getConnection());
        UserService userService = new UserService(userRepository);
        ConsomationRepository consomationRepository = new ConsomationRepository(DatabaseConnection.getInstance().getConnection());
        ConsomationService consomationService = new ConsomationService(userService,consomationRepository);

        Scanner inp = new Scanner(System.in);
        System.out.println("====CO2eq====");

        int choice;

        do {
            System.out.println("+-----------------------------------------------------------------+");
            System.out.println("|                           Menu                                  |");
            System.out.println("+-----------------------------------------------------------------+");
            System.out.println("| 1  - Add User                                                   |");
            System.out.println("| 2  - Show All Users                                             |");
            System.out.println("| 3  - Update User                                                |");
            System.out.println("| 4  - Delete User                                                |");
            System.out.println("| 5  - Find User by ID                                            |");
            System.out.println("| 6  - Add Consomation                                            |");
            System.out.println("| 7  - Show User Consomations Details                             |");
            System.out.println("| 8  - Filter Users Exceeding 3000 kg CO2                         |");
            System.out.println("| 9  - Detect Inactive Users in a Period                           |");
            System.out.println("| 10 - Calculate Average Consomation Impact per User in a Period  |");
            System.out.println("| 11 - Sort Users by Consomation Impact                           |");
            System.out.println("| 12 - Generate Report                                            |");
            System.out.println("| 13 - Exit                                                       |");
            System.out.println("+-----------------------------------------------------------------+");

            choice = inp.nextInt();
            inp.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter user name:");
                    String name = inp.nextLine();

                    System.out.println("Enter age:");
                    int age = inp.nextInt();
                    inp.nextLine();
                    try {
                        userService.addUser(name, age);
                        System.out.println("User added");
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Here is the list of users:");
                    try {
                        List<User> users = userService.afficherUsers();
                        if (users.isEmpty()) {
                            System.out.println("No users found");
                        } else {
                            users.stream()
                                    .sorted(Comparator.comparing(User::getId))
                                    .forEachOrdered(user -> System.out.println("ID: " + user.getId() + " Name: " + user.getName() + " Age: " + user.getAge()));
                        }
                    } catch (SQLException e) {
                        System.out.println("Error displaying users: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("Update User");
                    System.out.println("Enter user ID to update:");
                    int idUpdate = inp.nextInt();
                    inp.nextLine();

                    System.out.println("Enter new name:");
                    String newName = inp.nextLine();

                    System.out.println("Enter new age:");
                    int newAge = inp.nextInt();
                    inp.nextLine();

                    try {
                        boolean isUpdate = userService.updateUser(idUpdate, newName, newAge);
                        if (isUpdate) {
                            System.out.println("User updated successfully");
                        } else {
                            System.out.println("User not found");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error updating user: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("Delete User");
                    System.out.println("Enter user ID to delete:");
                    int idDelete = inp.nextInt();
                    inp.nextLine();

                    boolean isDelete = userService.deleteUser(idDelete);
                    if (isDelete) {
                        System.out.println("User deleted successfully");
                    } else {
                        System.out.println("User not found");
                    }
                    break;

                case 5:
                    System.out.println("Find User by ID");
                    System.out.println("Enter user ID:");
                    int idUser = inp.nextInt();
                    inp.nextLine();

                    Optional<User> foundUser = userService.findById(idUser);
                    if (foundUser.isPresent()) {
                        User user = foundUser.get();
                        System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Age: " + user.getAge());
                    } else {
                        System.out.println("User not found.");
                    }
                    break;

                case 6:
                    System.out.println("Enter user id :");
                    int userId = inp.nextInt();
                    inp.nextLine();

                    System.out.println("Enter Value :");
                    int value = inp.nextInt();
                    inp.nextLine();

                    System.out.println("Enter start date (YYYY-MM-DD) :");
                    LocalDate startDate = LocalDate.parse(inp.nextLine());

                    System.out.println("Enter end date (YYYY-MM-DD)");
                    LocalDate endDate = LocalDate.parse(inp.nextLine());

                    System.out.println("Enter consumption type : \n 1. Transport \n 2. Alimentation \n 3. Logement");
                    int consomationT = inp.nextInt();
                    inp.nextLine();

                    Consomation consomation = null;

                    switch (consomationT) {
                        case 1:
                            System.out.println("Enter transport type :\n 1. Voiture \n 2. Train");
                            int typeT = inp.nextInt();
                            inp.nextLine();
                            System.out.println("Enter distance in km:");
                            double distance = inp.nextDouble();
                            consomation = new Transport(value, startDate, endDate, distance,
                                    typeT == 1 ? TypeVehicule.VOITURE : TypeVehicule.TRAIN, consomationT);
                            break;
                        case 2:
                            System.out.println("Enter Alimentation type :\n 1. Viande \n 2. Légume");
                            int typeA = inp.nextInt();
                            inp.nextLine();
                            System.out.println("Enter poids :");
                            Double poids = inp.nextDouble();
                            consomation = new Alimentation(value, startDate, endDate,poids,
                                    typeA == 1 ? TypeAliment.VIANDE : TypeAliment.LEGUME,consomationT);
                            break;
                        case 3:
                            System.out.println("Enter Logement type : \n 1. Électricité \n 2. Gaz");
                            int typeL = inp.nextInt();
                            inp.nextLine();
                            System.out.println("Enter energy consumption:");
                            double energyConsumption = inp.nextDouble();
                            consomation = new Logement(value, startDate, endDate,
                                    typeL == 1 ? TypeEnergie.ELECTRICITE : TypeEnergie.GAZ, energyConsumption, consomationT);
                            break;
                        default:
                            System.out.println("Invalid consumption type");
                            return;
                    }

                    if (consomation != null) {
                        Optional<Consomation> result = null;
                        try {
                            result = consomationService.addConsomation(consomation, userId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        if (result.isPresent()) {
                            System.out.println("Consomation added Scfly");
                        } else {
                            System.out.println("ERROR");
                        }
                    }
                    break;

                case 7:
                    System.out.println("Show User Consomations Details");
                    System.out.println("Enter user ID:");
                    int idUserConsDetails = inp.nextInt();
                    inp.nextLine();

                    Optional<User> foundUserDetails = userService.findById(idUserConsDetails);

                    if (foundUserDetails.isEmpty()) {
                        System.out.println("User not found.");
                        break;
                    }

                    try {
                        List<Consomation> consomations = consomationService.getUserConsomations(foundUserDetails.get());

                        if (!consomations.isEmpty()) {
                            consomations.forEach(consomation -> {
                                if (consomation != null) {
                                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                    System.out.println(consomation);
                                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                } else {
                                    System.out.println("Consomation is null");
                                }
                            });
                        } else {
                            System.out.println("No consomation data for this user.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Error displaying consomations: " + e.getMessage());
                    }
                    break;

                case 8:
                    System.out.println("Filter Users Exceeding 3000 kg of CO2");
                    consomationService.calculateConsomationImpact()
                            .forEach(user -> System.out.println("ID: " + user.getId() + " Name: " + user.getName() + " Age: " + user.getAge()));
                    break;

                case 9:
                    System.out.println("Detect Inactive Users in a Period");
                    System.out.println("Enter start date (YYYY-MM-DD):");
                    LocalDate startPeriod = LocalDate.parse(inp.nextLine());

                    System.out.println("Enter end date (YYYY-MM-DD):");
                    LocalDate endPeriod = LocalDate.parse(inp.nextLine());

                    try {
                        List<User> inactiveUsers = consomationService.detectInactiveUsers(startPeriod, endPeriod);
                        if (inactiveUsers.isEmpty()) {
                            System.out.println("No inactive users in the period");
                        } else {
                            System.out.println("Inactive users between " + startPeriod + " and " + endPeriod + ":");
                            inactiveUsers.forEach(user -> System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Age: " + user.getAge()));
                        }
                    } catch (SQLException e) {
                        System.err.println("Error detecting inactive users: " + e.getMessage());
                    }
                    break;

                case 10:
                    System.out.println("Calculate Average Consomation Impact per User in a Period");
                    System.out.println("Enter user ID:");
                    int idUserAverage = inp.nextInt();
                    inp.nextLine();

                    System.out.println("Enter start date (YYYY-MM-DD):");
                    LocalDate startDateAverage = LocalDate.parse(inp.nextLine());

                    System.out.println("Enter end date (YYYY-MM-DD):");
                    LocalDate endDateAverage = LocalDate.parse(inp.nextLine());

                    double averageImpact = consomationService.averageConsomationByPeriod(idUserAverage, startDateAverage, endDateAverage);

                    System.out.println("Average consomation from " + startDateAverage + " to " + endDateAverage + " : " + averageImpact + " kg CO2");
                    break;

                case 11:
                    System.out.println("Sort Users by Consomation Impact");
                    List<User> allUsers = userService.afficherUsers();
                    allUsers.stream()
                            .sorted((u1, u2) -> Double.compare(consomationService.calculateTotalImpact(u2), consomationService.calculateTotalImpact(u1)))
                            .forEach(user -> {
                                System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Age: " + user.getAge() + ", Impact: " + consomationService.calculateTotalImpact(user) + " kg CO2");
                            });
                    break;

                case 12:
                    System.out.println("Generate Report");

                    int reportChoice;
                    do {
                        System.out.println("+-----------------------------+");
                        System.out.println("|         Report              |");
                        System.out.println("+-----------------------------+");
                        System.out.println("| 1 - Daily Report            |");
                        System.out.println("| 2 - Weekly Report           |");
                        System.out.println("| 3 - Monthly Report          |");
                        System.out.println("| 4 - Exit                    |");
                        System.out.println("+-----------------------------+");
                        reportChoice = inp.nextInt();
                        inp.nextLine();

                        switch (reportChoice) {
                            case 1:
                                System.out.println("Daily Report");
                                System.out.println("Enter user ID:");
                                int userIdDaily = inp.nextInt();
                                inp.nextLine();
                                consomationService.dailyConsomationReport(userIdDaily);
                                break;
                            case 2:
                                System.out.println("Weekly Report");
                                System.out.println("Enter user ID:");
                                int userIdWeekly = inp.nextInt();
                                inp.nextLine();
                                consomationService.weeklyConsomationReport(userIdWeekly);
                                break;
                            case 3:
                                System.out.println("Monthly Report");
                                System.out.println("Enter user ID:");
                                int userIdMonthly = inp.nextInt();
                                inp.nextLine();
                                consomationService.monthlyConsomationReport(userIdMonthly);
                                break;
                            case 4:
                                System.out.println("Exiting report menu.");
                                break;
                            default:
                                System.out.println("Invalid choice");
                                break;
                        }
                    } while (reportChoice != 4);
                    break;

                case 13:
                    System.out.println("Exiting application");
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;
            }

        } while (choice != 13);
    }
    }
