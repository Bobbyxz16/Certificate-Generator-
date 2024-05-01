package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implementation of the data persistence interface for handling bank data.
 */
public class HandleBankData implements Bank.DataPersistence {

    private final Path userDataPath; // Path to the folder where user data is stored

    /**
     * Constructor to initialize the HandleBankData object with the user data path.
     */
    public HandleBankData() {
        userDataPath = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "org", "example", "Bank_Userdata");
    }

    /**
     * Method to save bank data to a file.
     *
     * @param bank The Bank object containing the data to be saved.
     */
    @Override
    public void saveBankData(Bank bank) {
        Path filePath = userDataPath.resolve(bank.getCardHolderName() + "_BankDetails.txt"); // File path for saving bank data

        // Try-with-resources block to automatically close resources after use
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toFile()))) {
            // Write bank data to the file
            writer.println("Card number: " + bank.getCardNumber());
            writer.println("Card expiry: " + bank.getCardExpiry());
            writer.println("Card CVC: " + bank.getCardCVC());
            writer.println("Card holder name: " + bank.getCardHolderName());
            System.out.println("Bank data saved for username: " + bank.getCardHolderName()); // Log success message
        } catch (IOException e) {
            throw new RuntimeException("Error saving bank data: " + e.getMessage()); // Throw runtime exception for error in saving data
        }
    }

    /**
     * Method to load bank data from a file.
     *
     * @param username The username for which bank data needs to be loaded.
     * @return The Bank object containing the loaded bank data.
     */
    @Override
    public Bank loadBankData(String username) {
        String currentUser = Bank.UserRegistration.getCurrentUser(); // Retrieve current user's username
        Path filePath = userDataPath.resolve(currentUser + "_BankDetails.txt"); // File path for loading bank data
        File file = filePath.toFile();

        // Check if the file exists
        if (!file.exists()) {
            System.out.println("Bank data not found for username: " + username); // Log message for missing bank data
            return null; // Return null or default Bank object if data is not found
        }

        // Try-with-resources block to automatically close resources after use
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String number = null;
            String expiryDate = null;
            int cvcCode = 0;
            String holderName = null;
            String line;
            // Read lines from the file and extract bank data
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Card number: ")) {
                    number = line.substring("Card number: ".length());
                } else if (line.startsWith("Card expiry: ")) {
                    expiryDate = line.substring("Card expiry: ".length());
                } else if (line.startsWith("Card CVC: ")) {
                    cvcCode = Integer.parseInt(line.substring("Card CVC: ".length()));
                } else if (line.startsWith("Card holder name: ")) {
                    holderName = line.substring("Card holder name: ".length());
                } else {
                    // Throw specific exception for invalid format
                    throw new RuntimeException("Invalid bank data format");
                }
            }
            // Create and return a Bank object with the loaded bank data
            if (number != null && expiryDate != null && holderName != null) {
                return new Bank(number, expiryDate, cvcCode, holderName);
            } else {
                throw new RuntimeException("Invalid bank data format"); // Throw exception if data format is invalid
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Bank data not found for username: " + username); // Throw exception if file is not found
        } catch (IOException e) {
            // Log the specific IO exception message for debugging
            System.err.println("Error reading bank data: " + e.getMessage());
            throw new RuntimeException("Error reading bank data"); // Throw exception for error in reading data
        }
    }
}
