package application;

import application.entities.Admin;
import application.entities.Installer;
import application.entities.User;
import application.services.*;

import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    public static Logger logger = LoggerUtility.getLogger();
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

       
        User currentUser;
        String email;
        String password;
        int validationStatus;
        boolean iterator = true;

        while (iterator) {
            MessagesGenerator.listGenerator("signingList");
            String choice = scanner.nextLine();

            switch (choice) {

                case "1" -> {
                    logger.info("Enter your email: ");
                    email = scanner.nextLine();
                    logger.info("Enter your password: ");
                    password = scanner.nextLine();
                    validationStatus= MainUtility.signUpUtility(email,password);
                    logger.info(MessagesGenerator.SigningMessages(validationStatus));
                }
                case "2" -> {
                    logger.info("Enter your email: ");
                    email = scanner.nextLine();
                    logger.info("Enter your password: ");
                    password = scanner.nextLine();

                    validationStatus = ValidationUser.validation(email, password);
                    currentUser=MainUtility.signInUtility(email,password,validationStatus );

                    logger.info(MessagesGenerator.SigningMessages(validationStatus));

                    if (currentUser != null && currentUser.isSignInStatus())
                    {
                        DatabaseService databaseService = new DatabaseService();
                        logger.info("Welcome dear " + currentUser.getProfileObject().getName());
                        switch (currentUser.getRole()){

                            case 'u' -> MainUtility.userUtility(databaseService,currentUser);
                            case 'a' ->{
                                Admin currentAdmin =(Admin) currentUser;
                                MainUtility.adminUtility(databaseService,currentAdmin);
                            }
                            case 'i' ->{

                                Installer currentInstaller =(Installer) currentUser;
                                MainUtility.installerUtility(databaseService,currentInstaller);
                            }
                            default ->
                              logger.severe("Error: something went wrong, please run application again!\n");
                        }
                    }
                    else
                    {
                        logger.severe(MessagesGenerator.SigningMessages(5));
                    }
                }
                case "3" -> {
                    iterator=false;
                    logger.info("Good bye, have a nice day.");
                }
                default -> logger.severe("\nInvalid choice!, Please enter 1, 2, or 3.\n");
            }
        }
    }
}