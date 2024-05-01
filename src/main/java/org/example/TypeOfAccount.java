package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

import java.util.Scanner;

public class TypeOfAccount {
    private final int normalAccount;
    private final int proAccount;
    private final int designerAccount;

    // Constructor to initialize account types
    public TypeOfAccount(int normalAccount, int proAccount, int designerAccount) {
        this.normalAccount = normalAccount;
        this.proAccount = proAccount;
        this.designerAccount = designerAccount;
    }


    // Getter methods for account types
    public int getNormalAccount() {
        System.out.println("\nYou are using a Normal Account\n");
        return normalAccount;
    }

    public int getProAccount() {
        System.out.println("\nYou are using a Pro Account\n");
        return proAccount;
    }

    public int getDesignerAccount() {
        System.out.println("\nYou are using a Designer Account\n");
        return designerAccount;
    }

    // Method to select account type
    public static int accountSelection() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\n- ACCOUNT SELECTION -");
        System.out.println("1. Normal Account\n2. Pro Account\n3. Designer Account");
        System.out.print("\nEnter input:");

        int accountType = 0; // Variable to store the selected account type
        boolean choose = false;
        Bank.PaymentProcessor paymentProcessor = new Bank.PaymentProcessor(sc, new HandleBankData());
        DesignerUser designer = new DesignerUser();

        while (!choose) {
            // Check if the input is an integer
            if (sc.hasNextInt()) {
                accountType = sc.nextInt();

                try {
                    Thread.sleep(1100); // Delay after input
                } catch (InterruptedException e) {
                }

                switch (accountType) {
                    case 1:
                        System.out.println("\nYou sign up with a normal account");
                        choose = true;
                        break;
                    case 2:
                        handleProAccount(paymentProcessor); // Handle pro account
                        choose = true;
                        break;
                    case 3:
                        sc.nextLine();
                        handleDesignerAccount(paymentProcessor, designer); // Handle designer account
                        choose = true;
                        break;
                    default:
                        System.out.println("Incorrect input. Please choose again");
                        break;
                }
            } else {
                System.out.println("Incorrect input. Please enter an integer.");
                sc.nextLine(); // Clear the input buffer
            }
        }

        return accountType;
    }

    // Method to handle pro account
    private static void handleProAccount(Bank.PaymentProcessor paymentProcessor) {
        Main menu = new Main();
        LoginManager login = new LoginManager();
        Scanner sc = new Scanner(System.in);
        int select = 0;

        while (select != 1 && select != 2 && select !=3) {
            System.out.println("\nTo proceed you will need to insert a payment method");
            System.out.println("\n1. Go back to Menu\n2. Go back to Registration\n3. Accept");
            System.out.print("Enter Input:");
            select = sc.nextInt();

            if (select == 1) {
                menu.Menu(login);
            } else if (select == 2) {
                RegistrationManager.registerUser();
            } else if (select == 3) {
                System.out.println("Thank you for trusting CERTIGRAPH.");
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
        System.out.println("\nYou sign up with a pro account");
        String currentUser = Bank.UserRegistration.getCurrentUser();
        Bank bankDetails = paymentProcessor.processPayment(currentUser);
        displayBankDetails(bankDetails, "Monthly payment of 19 pounds", paymentProcessor);
    }

    // Method to handle designer account
    private static void handleDesignerAccount(Bank.PaymentProcessor paymentProcessor, DesignerUser designer) {
        Main menu = new Main();
        LoginManager login = new LoginManager();
        Scanner sc = new Scanner(System.in);
        int select = 0;

        while (select != 1 && select != 2 && select !=3) {
            System.out.println("\nTo proceed you will need to insert a payment method");
            System.out.println("\n1. Go back to Menu\n2. Go back to Registration\n3. Accept");
            System.out.print("Enter Input:");
            select = sc.nextInt();

            if (select == 1) {
                menu.Menu(login);
            } else if (select == 2) {
                RegistrationManager.registerUser();
            } else if (select == 3) {
                System.out.println("Thank you for trusting CERTIGRAPH.");
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
        System.out.println("\nYou sign up with a designer account");
        String currentUser = Bank.UserRegistration.getCurrentUser();
        Bank bankDetails = paymentProcessor.processPayment(currentUser);
        displayBankDetails(bankDetails, "Monthly payment of 29 pounds", paymentProcessor);
        designer.Designer_Account(); // Call Designer_Account method
    }

    // Method to display bank details and save data
    private static void displayBankDetails(Bank bankDetails, String message, Bank.PaymentProcessor paymentProcessor) {
        System.out.println("\nCard Number: " + bankDetails.getCardNumber());
        System.out.println("Card Expiry: " + bankDetails.getCardExpiry());
        System.out.println("Card CVC: " + bankDetails.getCardCVC());
        System.out.println("Card Holder Name: " + bankDetails.getCardHolderName());
        System.out.println("\n" + message);
        paymentProcessor.getDataPersistence().saveBankData(bankDetails); // Save bank data
    }

    // Method to display home page
    public static void homePage(Scanner sc) {
        String username = Bank.UserRegistration.getCurrentUser();
        if (RegistrationManager.UserRegistration.userAccounts.containsKey(username)) {
            Integer accountType = RegistrationManager.UserRegistration.userAccounts.get(username);
            if (accountType != null) {
                if (accountType == 3) { // Designer account
                    System.out.println("\n-- HOME PAGE --");
                    System.out.println("\n1. Upload Certificate\n2. Create Certificate\n3. Profile\n4. Exit");
                    System.out.print("\nEnter input: ");
                    int homePageSelection = sc.nextInt();
                    sc.nextLine(); // Consume the newline character

                    while (true) {
                        if (homePageSelection == 1) {
                            DesignerUser.Designer_Account(); // Call Designer_Account method
                            System.out.println("\n1.Go to home page\n2.Exit");
                            int choose;

                            do {
                                System.out.println("Enter Input: ");
                                choose = sc.nextInt();
                                if (choose == 1) {
                                    TypeOfAccount.homePage(sc); // Go to home page
                                } else if (choose == 2) {
                                    System.out.println("Thank you for using our System!");
                                    System.exit(0); // Exit program
                                } else {
                                    System.out.println("Invalid input. Please try again.");
                                }
                            } while (choose != 1 && choose != 2);
                            break;
                        } else if (homePageSelection == 2) {
                            PdfGenerator.generatePDF(); // Generate PDF
                            System.exit(0); // Exit program
                        } else if (homePageSelection == 3) {
                            profile(sc); // Display profile
                            break;
                        } else if (homePageSelection == 4) {
                            System.exit(0); // Exit program
                        } else {
                            System.out.println("Incorrect Input");
                            System.out.println("\nPlease enter 1, 2, 3 or 4");
                            homePageSelection = sc.nextInt();
                            sc.nextLine(); // Consume the newline character
                        }
                    }
                } else { // Normal or pro account
                    System.out.println("\n-- HOME PAGE --");
                    System.out.println("\n1. Create Certificate\n2. Profile\n3. Exit");
                    System.out.print("\nEnter input: ");
                    int homePageSelection = sc.nextInt();
                    sc.nextLine(); // Consume the newline character

                    while (true) {
                        if (homePageSelection == 1) {
                            PdfGenerator.generatePDF(); // Generate PDF
                            System.exit(0); // Exit program
                        } else if (homePageSelection == 2) {
                            profile(sc); // Display profile
                            break;
                        } else if (homePageSelection == 3) {
                            System.exit(0); // Exit program
                        } else {
                            System.out.println("Incorrect Input");
                            System.out.println("\nPlease enter 1, 2 or 3");
                            homePageSelection = sc.nextInt();
                            sc.nextLine(); // Consume the newline character
                        }
                    }
                }
            } else {
                System.out.println("Error: User account type not found.");
            }
        } else {
            System.out.println("Error: User account not found.");
        }
    }

    // Method to display profile and handle user input
    public static void profile(Scanner sc) {
        System.out.println("\n-- PROFILE --");
        RegistrationManager.loadProfile(); // Load profile

        Bank.PaymentProcessor paymentProcessor = new Bank.PaymentProcessor(sc, new HandleBankData());
        String currentUser = Bank.UserRegistration.getCurrentUser();
        Bank bankCard = paymentProcessor.loadBankData(currentUser); // Load bank data
        if (bankCard != null) {
            System.out.println("\nBank details:");
            System.out.println("Card number: " + bankCard.getCardNumber());
            System.out.println("Card expiry: " + bankCard.getCardExpiry());
            System.out.println("Card CVC: " + bankCard.getCardCVC());
            System.out.println("Card holder name: " + bankCard.getCardHolderName());
        } else {
            System.out.println("\nThis account don't require any payment.");
        }

        int profileChoose;

        do {
            System.out.println("\n1. Go to home page\n2. Exit");
            System.out.print("\nEnter input:");

            // Check if the input is a number
            while (!sc.hasNextInt()) {
                System.out.println("Please enter a number.");
                sc.next(); // Consume the invalid input
            }

            profileChoose = sc.nextInt();
            sc.nextLine(); // Consume newline character

            if (profileChoose == 1) {
                TypeOfAccount.homePage(sc); // Go to home page
            } else if (profileChoose != 2) {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        } while (profileChoose != 2);

        System.out.println("Thank you for using our System!");
        System.exit(0); // Exit program
    }

    // Method to print words one by one with a delay
    public static void printWordsOneByOne(String message) {
        String[] words = message.split(" ");
        for (String word : words) {
            System.out.print(word + " ");
            try {
                Thread.sleep(800); // Delay between each word
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted state
                e.printStackTrace();
            }
        }
    }
}