package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class RegistrationManager {
    // Define private fields to store user registration information
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String username;
    private final String password;

    // Constructor to initialize user registration information
    public RegistrationManager(String firstName, String lastName, String email, String username, String password) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getter methods for accessing user registration information
    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Static inner class to handle user registration data
    public static class UserRegistration {
        // Map to store user accounts (username -> account type)
        public static final Map<String, Integer> userAccounts = new HashMap<>(); // Static map to store user accounts
        // Static variable to store the currently logged-in user's username
        private static String currentUser; // Variable to store the currently logged-in user's username

        // Method to retrieve currently logged-in user's username
        public static String getCurrentUser() {
            return currentUser; // Returning the currently logged-in user's username
        }

        // Method to set the currently logged-in user's username
        public static void setCurrentUser(String username) {
            currentUser = username; // Setting the currently logged-in user's username
        }
    }

    public static RegistrationManager registerUser() {
        LoginManager loginManager = new LoginManager(); // Creating an instance of LoginManager
        System.out.println("\n-- REGISTRATION --");
        Scanner sc = new Scanner(System.in); // Creating Scanner object for user input

        System.out.print("Enter Firstname: "); // Prompting user to enter first name
        String firstname = getStringOnly(sc); // Calling method to get only letters for first name

        System.out.print("Enter Lastname: "); // Prompting user to enter last name
        String lastname = getStringOnly(sc); // Calling method to get only letters for last name

        String email;
        do {
            System.out.print("Enter email: "); // Prompting user to enter email
            email = sc.nextLine(); // Reading user input for email
            if (!isValidEmail(email)) { // Checking if the entered email is valid
                System.out.println("Invalid email format. Please enter a valid email."); // Displaying error message for invalid email format
            }
        } while (!isValidEmail(email)); // Loop until a valid email is entered

        System.out.print("Enter username: "); // Prompting user to enter username
        String username = sc.nextLine();

        System.out.print("Enter password: "); // Prompting user to enter password
        String password = sc.nextLine();

        // Select account type for the user
        int accountType = TypeOfAccount.accountSelection(); // Calling method to select account type
        System.out.println("Registration successful!"); // Displaying registration success message
        RegistrationManager newUser = new RegistrationManager(firstname, lastname, email, username, password); // Creating a new user instance
        RegistrationManager.saveProfile(newUser); // Saving user profile

        // Set the current user before saving profile and login data
        UserRegistration.setCurrentUser(username); // Setting the current user

        // Save user data and login credentials
        LoginManager.users.put(username, password); // Saving username and password to users map
        UserRegistration.userAccounts.put(username, accountType); // Saving username and account type to userAccounts map
        LoginManager.saveUserData(); // Saving user data

        // Provide options to go back to the menu or exit
        int selection;
        do {
            System.out.println("\n\n1. Go back to menu\n2. Exit"); // Displaying menu options
            System.out.println("Enter Input: "); // Prompting user to enter input

            // Check if the input is a number
            while (!sc.hasNextInt()) { // Loop until user enters a number
                System.out.println("Please enter a number."); // Displaying error message for non-numeric input
                sc.next(); // Consume the invalid input
            }

            selection = sc.nextInt(); // Reading user input
            sc.nextLine(); // Consume newline character

            if (selection == 1) { // If user chooses to go back to the menu
                Main main = new Main(); // Creating an instance of Main
                main.Menu(loginManager); // Displaying the menu
            } else if (selection != 2) { // If user chooses an invalid option
                System.out.println("Invalid input. Please enter 1 or 2."); // Displaying error message for invalid input
            }
        } while (selection != 2); // Loop until user chooses to exit

        System.out.println("Thank you for using our System!");
        // Exit the program
        System.exit(0); // Exiting the program

        return newUser; // Returning the newly registered user
    }

    // Method to validate email format
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"; // Regular expression for email validation
        return email.matches(emailRegex); // Checking if email matches the regex pattern
    }

    // Method to input string only
    public static String getStringOnly(Scanner sc) {
        String input;
        do {
            input = sc.nextLine(); // Reading user input
            if (!input.matches("^[a-zA-Z\\s]+$")) { // Checking if input contains only letters and spaces
                System.out.print("Please enter only letters and spaces: "); // Displaying error message for non-letter/space input
            }
        } while (!input.matches("^[a-zA-Z\\s]+$")); // Loop until input contains only letters and spaces
        return input; // Returning the validated input
    }

    // Method to save user profile data to a text file
    public static void saveProfile(RegistrationManager user) {
        // Get the current working directory
        Path userPath = Paths.get(System.getProperty("user.dir")); // Getting current working directory

        // Write user profile data to a text file
        if (user != null) { // Checking if user object is not null
            try (PrintWriter writer = new PrintWriter(new FileWriter(userPath + "\\src\\main\\java\\org\\example\\Profile_UserData\\" + user.getUsername() + "_RegistrationData.txt"))) { // Creating PrintWriter to write to file
                writer.println("Username: " + user.getUsername()); // Writing username to file
                writer.println("First Name: " + user.getFirstName()); // Writing first name to file
                writer.println("Last Name: " + user.getLastName()); // Writing last name to file
                writer.println("Email: " + user.getEmail()); // Writing email to file
                writer.println("Password: " + user.getPassword()); // Writing password to file
                writer.println(); // Adding an empty line for separation

                System.out.println("Profile saved for username: " + user.getUsername()); // Displaying success message
            } catch (IOException e) { // Handling IO exception
                System.err.println("Error saving profile: " + e.getMessage()); // Displaying error message
            }
        }
    }

    // Method to load user profile data from a text file
    public static void loadProfile() {
        // Get the current working directory
        Path userPath = Paths.get(System.getProperty("user.dir")); // Getting current working directory
        String username = UserRegistration.getCurrentUser(); // Getting current user's username
        StringBuilder profileData = new StringBuilder(); // Creating StringBuilder to store profile data

        // Read user profile data from a text file
        try (BufferedReader br = new BufferedReader(new FileReader(userPath + "\\src\\main\\java\\org\\example\\Profile_UserData\\" + username + "_RegistrationData.txt"))) { // Creating BufferedReader to read from file
            String line;
            while ((line = br.readLine()) != null) { // Looping through each line in the file
                profileData.append(line).append("\n"); // Appending each line to StringBuilder
            }
        } catch (FileNotFoundException e) { // Handling file not found exception
            System.out.println("Profile not found for username: " + username); // Displaying message for profile not found
        } catch (IOException e) { // Handling IO exception
            System.err.println("Error reading profile: " + e.getMessage()); // Displaying error message
        }

        // Display user profile data
        if (profileData.length() > 0) { // Checking if profile data exists
            System.out.println("\nRegistration details for " + username + ":"); // Displaying username
            System.out.println(profileData); // Displaying profile data
        }
    }
}



