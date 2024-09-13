import config.DatabaseConnection;
import domain.*;
import service.ConsomationService;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection = dbConnection.getConnection();

        Scanner inp = new Scanner(System.in);
        int choice;

        do {
            System.out.println("+------------------------------------------+");
            System.out.println("|                   Menu                   |");
            System.out.println("+------------------------------------------+");
            System.out.println("| 1 - Add User                             |");
            System.out.println("| 2 - Display All Users                    |");
            System.out.println("| 3 - Add Consomation                      |");
            System.out.println("| 4 - Exit                                 |");
            System.out.println("+------------------------------------------+");

            choice = inp.nextInt();
            inp.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter user ID:");
                    int id = inp.nextInt();
                    inp.nextLine();

                    System.out.println("Enter user name:");
                    String name = inp.nextLine();

                    System.out.println("Enter age:");
                    int age = inp.nextInt();
                    inp.nextLine();
                    break;

                case 2:
                    System.out.println("Here is the list of users:");
                    break;

                case 3:
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
                        Optional<Consomation> result = ConsomationService.addConsomation(consomation, userId);
                        if (result.isPresent()) {
                            System.out.println("Consomation added Scfly");
                        } else {
                            System.out.println("ERROR");
                        }
                    }
                    break;

                case 4:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }

        } while (choice != 3);
    }
    }
