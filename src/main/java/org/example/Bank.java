package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

import java.util.Scanner;

public class Bank {
    private final String cardNumber; // Stores the card number
    private final String cardExpiry; // Stores the card's expiry date
    private final int cardCVC; // Stores the card's CVC (Card Verification Code)
    private final String cardHolderName; // Stores the cardholder's name

    // Constructor to initialize the Bank object with card details
    public Bank(String cardNumber, String cardExpiry, int cardCVC, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardCVC = cardCVC;
        this.cardHolderName = cardHolderName;
    }

    // Getter method for retrieving the card number
    public String getCardNumber() {
        return cardNumber;
    }

    // Getter method for retrieving the card's expiry date
    public String getCardExpiry() { return cardExpiry; }

    // Getter method for retrieving the card's CVC
    public int getCardCVC() {
        return cardCVC;
    }

    // Getter method for retrieving the card holder's name
    public String getCardHolderName() {
        return cardHolderName;
    }

    /**
     * Interface for handling data persistence.
     */
    public interface DataPersistence {
        void saveBankData(Bank bank); // Method to save bank data
        Bank loadBankData(String username); // Method to load bank data based on username
    }

    /**
     * Class handling user interaction and payment process.
     */
    public static class PaymentProcessor {
        private final Scanner scanner; // Scanner object for user input
        private final DataPersistence dataPersistence; // Object for data persistence operations

        // Constructor to initialize PaymentProcessor with scanner and data persistence objects
        public PaymentProcessor(Scanner scanner, DataPersistence dataPersistence) {
            this.scanner = scanner;
            this.dataPersistence = dataPersistence;
        }

        // Getter method for data persistence object
        public DataPersistence getDataPersistence() {
            return dataPersistence;
        }

        /**
         * Performs the payment process, requesting card details from the user and saving the data.
         *
         * @param currentUser The name of the current user.
         * @return The bank card details.
         */
        public Bank processPayment(String currentUser) {
            // Display messages
            System.out.println("Thank you for your account choice");
            System.out.println("Let's proceed to the checkout");
            System.out.println("\n\n-Payment-");

            // Read card details from the user
            String cardNumber = readCardNumber();
            String cardExpiry = readCardExpiry();
            int cardCVC = readCardCVC();
            String cardHolderName = readCardHolderName(currentUser);

            // Create a Bank object with the provided card details
            Bank bank = new Bank(cardNumber, cardExpiry, cardCVC, cardHolderName);

            // Save the bank data using data persistence object
            dataPersistence.saveBankData(bank);

            return bank; // Return the Bank object
        }

        // Method to read card number from the user
        private String readCardNumber() {
            String cardNumber;
            do {
                System.out.print("Card Number (14 digits): "); // Prompt for input
                cardNumber = scanner.nextLine(); // Read input from user
                if (cardNumber.length() != 14) {
                    System.out.println("Invalid card number. It should be 14 digits long."); // Error message for invalid input
                }
            } while (cardNumber.length() != 14); // Loop until valid input is provided
            return cardNumber; // Return the card number
        }

        // Method to read card expiry date from the user
        private String readCardExpiry() {
            String cardExpiry;
            do {
                System.out.print("Card Expiry (MM/YY): "); // Prompt for input
                cardExpiry = scanner.nextLine(); // Read input from user
                if (!cardExpiry.matches("\\d{2}/\\d{2}")) { // Input expected = 10/21
                    System.out.println("Invalid expiry format. Should be MM/YY."); // Error message for invalid input format
                }
            } while (!cardExpiry.matches("\\d{2}/\\d{2}")); // Loop until valid input is provided
            return cardExpiry; // Return the card expiry date
        }

        // Method to read card CVC from the user
        private int readCardCVC() {
            int cardCVC;
            do {
                System.out.print("Card CVC (3 digits): "); // Prompt for input
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number."); // Error message for invalid input type
                    scanner.next(); // Consume invalid input
                }
                cardCVC = scanner.nextInt(); // Read input from user
                scanner.nextLine(); // Consume newline character
                if (String.valueOf(cardCVC).length() != 3) {
                    System.out.println("Invalid CVC. It should be 3 digits long."); // Error message for invalid input length
                }
            } while (String.valueOf(cardCVC).length() != 3); // Loop until valid input is provided
            return cardCVC; // Return the card CVC
        }

        // Method to read cardholder's name from the user
        private String readCardHolderName(String currentUser) {
            String cardHolderName;
            do {
                System.out.print("Card Holder Name: "); // Prompt for input
                cardHolderName = scanner.nextLine(); // Read input from user
                if (cardHolderName.isEmpty() || cardHolderName.matches("[a-zA-Z ]+")) {
                    UserRegistration.setCurrentUser(cardHolderName); // Set the currently logged-in user's username
                    break;
                } else {
                    System.out.println("Invalid card holder name. Please enter a name or leave it blank."); // Error message for invalid input
                }
            } while (true); // Loop until valid input is provided
            return cardHolderName.isEmpty() ? currentUser : cardHolderName; // Return the cardholder's name
        }

        // Method to load bank data based on username
        public Bank loadBankData(String username) {
            return dataPersistence.loadBankData(username); // Load bank data using data persistence object
        }
    }

    // Class to handle user registration
    public static class UserRegistration {
        private static String currentUser; // Stores the currently logged-in user's username

        // Method to retrieve currently logged-in user's username
        public static String getCurrentUser() {
            return currentUser; // Return the currently logged-in user's username
        }

        // Method to set the currently logged-in user's username
        public static void setCurrentUser(String cardHolderName) {
            currentUser = cardHolderName; // Set the currently logged-in user's username
        }
    }
}
