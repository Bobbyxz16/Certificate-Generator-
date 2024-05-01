package org.example; // Declares the package for the class

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class LoginManager {
    // Static HashMap to store user credentials
    static HashMap<String, String> users = new HashMap<>(); // Initialize a static HashMap to store username and password pairs

    // Constructor to initialize the users HashMap
    public LoginManager() { // Constructor for the LoginManager class
        LoginManager.users = new HashMap<>(); // Initialize the users HashMap
    }

    // Method to handle user login
    public void loginUser() { // Method to handle user login
        Scanner sc = new Scanner(System.in); // Create a Scanner object to read user input
        System.out.println("Do you have an account? yes/no"); // Prompt the user to enter if they have an account
        System.out.print("\nEnter input:"); // Prompt the user to enter input
        String accountVerification = sc.nextLine(); // Read the user's input

        boolean verification = false; // Flag to track if account verification is complete
        while (!verification) { // Loop until account verification is complete
            if (accountVerification.equalsIgnoreCase("yes")) { // Check if the user has an account
                System.out.println("\n-- Use your username and password to login --"); // Prompt the user to enter credentials
                verification = true; // Set the verification flag to true

            } else if (accountVerification.equalsIgnoreCase("no")) { // Check if the user doesn't have an account
                System.out.println("Do you want to continue with a guest account? yes/no"); // Prompt the user to choose a guest account
                System.out.print("\nEnter input:"); // Prompt the user to enter input
                String guestAccount = sc.nextLine(); // Read the user's input

                if (guestAccount.equalsIgnoreCase("yes")) { // Check if the user wants a guest account
                    String message = "Perfect, let's go directly to certificate creation"; // Message to be displayed
                    TypeOfAccount.printWordsOneByOne(message); // Print the message one word at a time
                    PdfGenerator.generatePDF(); // Call the generate a PDF certificate method
                    verification = true; // Set the verification flag to true
                    System.exit(0); // Exit the program

                } else if (guestAccount.equalsIgnoreCase("no")) { // Check if the user doesn't want a guest account
                    System.out.println("1. Go back to menu\n2. Exit"); // Prompt the user with options
                    System.out.print("\nEnter input:"); // Prompt the user to enter input
                    String choice = sc.nextLine(); // Read the user's input

                    if (choice.equals("1")) { // Check if the user wants to go back to the menu
                        LoginManager login = new LoginManager(); // Create a new LoginManager instance
                        Main main = new Main(); // Create a new Main instance
                        main.Menu(login); // Display the menu
                        return; // Return from the method

                    } else if (choice.equals("2")) { // Check if the user wants to exit
                        System.out.println("Thank you for using our System!");
                        System.exit(0); // Exit the program

                    } else { // If the user enters an invalid choice
                        System.out.println("Invalid choice. Please enter 1 or 2."); // Print an error message
                    }

                } else { // If the user enters an invalid input
                    System.out.println("Invalid input. Please enter 'yes' or 'no'."); // Print an error message
                    System.out.print("\nEnter input:"); // Prompt the user to enter input
                    guestAccount = sc.nextLine(); // Read the user's input
                }

            } else { // If the user enters an invalid input
                System.out.println("Invalid input. Please enter 'yes' or 'no'."); // Print an error message
                System.out.print("\nEnter input:"); // Prompt the user to enter input
                accountVerification = sc.nextLine(); // Read the user's input
            }
        }

        boolean loggedIn = false; // Flag to track if the user is logged in
        while (!loggedIn) { // Loop until the user is logged in
            System.out.print("Enter your username: "); // Prompt the user to enter their username
            String username = sc.nextLine(); // Read the user's input
            System.out.print("Enter your password: "); // Prompt the user to enter their password
            String password = sc.nextLine(); // Read the user's input

            if (users.containsKey(username) && users.get(username).equals(password)) { // Check if the username and password are correct
                System.out.println("Login successful!"); // Print a success message
                RegistrationManager.UserRegistration.setCurrentUser(username); // Set the current user in RegistrationMaganer.UserRegistration
                Bank.UserRegistration.setCurrentUser(username); // Set the current user in Bank.UserRegistration
                loggedIn = true; // Set the loggedIn flag to true

            } else { // If the username or password is incorrect
                System.out.println("Invalid username or password\n\n"); // Print an error message
                System.out.println("1. Try again\n2. Go to register\n3. Exit"); // Prompt the user with options
                System.out.print("\nEnter input:"); // Prompt the user to enter input
                String input = sc.nextLine(); // Read the user's input

                if (input.equals("1")) { // Check if the user wants to try again
                    loginUser(); // Call the loginUser method recursively

                } else if (input.equals("2")) { // Check if the user wants to register
                    RegistrationManager.registerUser(); // Call the registerUser method in RegistrationMaganer
                    return; // Return from the method

                } else if (input.equals("3")) { // Check if the user wants to exit
                    System.out.println("Thank you for using our System!");
                    System.exit(0); // Exit the program

                } else { // If the user enters an invalid choice
                    System.out.println("Invalid choice. Please enter 1, 2, or 3."); // Print an error message
                }
            }

            int accountType = RegistrationManager.UserRegistration.userAccounts.get(username); // Get the account type for the current user

            TypeOfAccount typeOfAccount = new TypeOfAccount(1, 2, 3); // Create a new TypeOfAccount instance

            switch (accountType) { // Switch statement based on the account type
                case 1: // Normal account
                    typeOfAccount.getNormalAccount();
                    break;
                case 2: // Pro account
                    typeOfAccount.getProAccount(); // Get the pro account value
                    break;
                case 3: // Designer account
                    typeOfAccount.getDesignerAccount(); // Get the designer account value
                    break;
                default: // If the account type is unknown
                    System.out.println("Unknown account type"); // Print an error message
                    break;
            }

            saveUserData(); // Save the user data
        }

        String message = "Let's create a certificate"; // Message to be displayed
        TypeOfAccount.printWordsOneByOne(message); // Print the message one word at a time
    }

    // Method to load user data from a file
    public void loadUserData() { // Method to load user data
        Path userPath = Paths.get(System.getProperty("user.dir")); // Get the current working directory path
        try (BufferedReader br = new BufferedReader(new FileReader(userPath + "\\src\\main\\java\\org\\example\\Login_userdata"))) { // Open the userdata file for reading
            String line; // Variable to store each line of the file
            while ((line = br.readLine()) != null) { // Loop through each line of the file
                String[] parts = line.split(","); // Split the line by commas
                if (parts.length >= 3) { // Check if the line has at least three parts
                    String username = parts[0]; // Extract the username
                    String userData = parts[1]; // Extract the user data (Password)
                    int account = Integer.parseInt(parts[2]); // Extract the account type
                    users.put(username, userData); // Add the username and user data to the users HashMap
                    RegistrationManager.UserRegistration.userAccounts.put(username, account); // Add the username and account type to the userAccounts HashMap
                }
            }
        } catch (FileNotFoundException | RuntimeException e) { // Catch FileNotFoundException or RuntimeException
            System.err.println("User data file not found: " + e.getMessage()); // Print an error message
        } catch (IOException e) { // Catch any other IOException
            System.err.println("Error reading user data: " + e.getMessage()); // Print an error message
        }
    }

    // Method to save user data to a file
    public static void saveUserData() { // Method to save user data
        Path userPath = Paths.get(System.getProperty("user.dir")); // Get the current working directory path
        try (PrintWriter pw = new PrintWriter(new FileWriter(userPath + "\\src\\main\\java\\org\\example\\Login_userdata", true))) { // Open the userdata file for writing (append mode)
            for (Map.Entry<String, String> entry : users.entrySet()) { // Loop through each entry in the users HashMap
                String username = entry.getKey(); // Get the username (key)
                String userData = entry.getValue(); // Get the user data (value) = (Password)
                int account = RegistrationManager.UserRegistration.userAccounts.get(username); // Get the account type for the user
                pw.println(username + "," + userData + "," + account); // Write the username, user data, and account type to the file
            }
        } catch (IOException e) { // Catch any IOException
            System.err.println("Error saving user data: " + e.getMessage()); // Print an error message
        }
    }
}