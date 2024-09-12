import config.DatabaseConnection;
import java.sql.Connection;
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
                    System.out.println("...");
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
