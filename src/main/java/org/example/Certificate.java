package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

import java.util.Scanner;

public class Certificate {

    private final String name; // Stores the name associated with the certificate
    private final String description; // Stores the description associated with the certificate
    private final String title; // Stores the title associated with the certificate
    private final int templateChoice; // Stores the user's template choice

    // Constructor to initialize Certificate object with provided details
    public Certificate(String name, String description, String title, int templateChoice) {
        this.name = name;
        this.description = description;
        this.title = title;
        this.templateChoice = templateChoice;
    }

    // Getter method for retrieving the name associated with the certificate
    public String getName() {
        return name;
    }

    // Getter method for retrieving the description associated with the certificate
    public String getDescription() {
        return description;
    }

    // Getter method for retrieving the title associated with the certificate
    public String getTitle() {
        return title;
    }

    // Getter method for retrieving the user's template choice
    public int getTemplateChoice() {
        return templateChoice;
    }

    // Method to create a new certificate
    public static Certificate createCertificate() {
        Scanner scanner = new Scanner(System.in); // Scanner object for user input
        System.out.println("\n\n-|- TEMPLATE SELECTION -|-"); // Display message for template selection
        System.out.println("\n*Brief description of each template"); // Display brief description of templates
        System.out.println("\n1. Brown box with serif Georgia font\n" +
                "\n2. [Premium] The completion of an HTML and CSS course, \n   with a double-bordered box in blue and yellow.\n" +
                "\n3. [Blocked]The review of a master's degree by a professor, \n   featuring a blue-bordered box and a signature.\n" +
                "\n4. It's a certification of an achievement, \n   with a rustic black and yellow floral border.\n" +
                "\n5. [Premium] It's a certificate of participation in a sports tournament,\n   featuring a blue-bordered box and an illustrated image of the sports.\n");

        // Retrieve current user's username
        String username = RegistrationManager.UserRegistration.getCurrentUser();
        // Retrieve current user's account type
        Integer accountType = RegistrationManager.UserRegistration.userAccounts.get(username);

        // accountType = 1 -> Normal Account
        // accountType = 2 -> Pro Account
        // accountType = 3 -> Designer Account

        boolean pdfChoose = false; // Flag to determine if PDF creation is allowed
        int choose; // From th templates options
        do {
            System.out.print("Please enter your choice (1-5): "); // Prompt for template choice
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 5."); // Error message for invalid input
                scanner.next(); // Consume the invalid input
            }
            choose = scanner.nextInt(); // Read user's template choice

            if (choose < 1 || choose > 5) {
                System.out.println("Invalid choice. Please try again."); // Error message for invalid template choice
                continue;
            }

            // Check if user has access to premium templates based on account type
            if (username != null && accountType != null) {
                if (accountType == 2 || accountType == 3) {
                    pdfChoose = true; // Allow PDF creation for premium account types
                } else if ((choose == 1 || choose == 3 || choose == 4) && (accountType == 1)) {
                    pdfChoose = true; // Allow PDF creation for standard account types with selected templates
                } else if ((choose == 2 || choose == 5) && (accountType == 1)) {
                    System.out.println("You don't have access to premium templates."); // Message for standard account types attempting to access premium templates
                }
            } else {
                if (choose == 1 || choose == 4) {
                    pdfChoose = true; // Allow PDF creation for unregistered users or users with limited access
                } else if (choose == 3) {
                    System.out.println("You have to create an account to access these templates"); // Message for unregistered users attempting to access premium templates
                } else {
                    System.out.println("You don't have access to premium templates."); // Message for unregistered users or standard account types attempting to access premium templates
                }
            }
        } while (!pdfChoose);

        scanner.nextLine(); // Consume the newline character

        String name = "";
        String description = "";
        String title = "";

        // Prompt user for additional details based on template choice
        if (choose == 1) {
            System.out.print("\nName: ");
            name = scanner.nextLine();
            System.out.print("Description: ");
            description = scanner.nextLine();
            System.out.print("Title: ");
            title = scanner.nextLine();
        } else if (choose == 2) {
            System.out.print("\nName: ");
            name = scanner.nextLine();
        } else if (choose == 3) {
            System.out.print("\nName: ");
            name = scanner.nextLine();
            System.out.print("Description: ");
            description = scanner.nextLine();
        } else if (choose == 4) {
            System.out.print("\nName: ");
            name = scanner.nextLine();
        } else if (choose == 5) {
            System.out.print("\nName: ");
            name = scanner.nextLine();
            System.out.print("Title: ");
            title = scanner.nextLine();
        }

        return new Certificate(name, description, title, choose); // Create and return the Certificate object with provided details
    }
}
