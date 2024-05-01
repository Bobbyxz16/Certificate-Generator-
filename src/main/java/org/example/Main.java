package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Create an instance of Main class
        Main menu = new Main();
        // Create an instance of LoginManager class
        LoginManager login = new LoginManager();
        // Create a Scanner object for user input
        Scanner sc = new Scanner(System.in);

        // Load user data from file/database
        login.loadUserData();
        // Display the main menu and handle user actions
        menu.Menu(login);

        // Close the Scanner object to release resources
        sc.close();
    }
    public void Menu(LoginManager loginManager) {
        // Display welcome message
        System.out.println("\nWelcome to CERTIGRAPH!");
        // Create a Scanner object for user input
        Scanner sc = new Scanner(System.in);

        boolean election = false;

        // Main menu loop
        while (!election) {
            // Display menu options
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");

            // Check if the input is an integer
            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                sc.nextLine(); // Consume the newline after the integer

                // Process the selected option
                switch (choice) {
                    case 1:
                        // Call method to register a new user
                        RegistrationManager.registerUser();
                        election = true; // Exit the loop after registration
                        break;
                    case 2:
                        // Call method to login user
                        loginManager.loginUser();
                        // Call method to display home page based on account type
                        TypeOfAccount.homePage(sc);
                        election = true; // Exit the loop after login
                        System.exit(0); // Exit the program after login
                        break;
                    case 3:
                        // Save user data and exit the program
                        loginManager.saveUserData();
                        System.out.println("Thank you for using our System!");
                        election = true; // Exit the loop after choosing exit
                        System.exit(0); // Exit the program
                        break;
                    default:
                        // Display error message for invalid choice
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } else {
                // If the input is not an integer, clear the scanner buffer and display an error message
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Clear the scanner buffer
            }
        }
        // Close the Scanner object to release resources
        sc.close();
    }
}
