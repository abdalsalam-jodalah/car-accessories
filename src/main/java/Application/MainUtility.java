package Application;
import Application.DataBase.Premetive_Objects.ResultSetResultHandler;
import Application.Entities.*;
import Application.Services.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static Application.Main.scanner;
import static Application.Services.MessagesGenerator.logger;

public class MainUtility {
    public static void userUtility(DatabaseService databaseService, User currentUser){
        boolean iterator =true;
        while (iterator) {
            MessagesGenerator.listGenerator("userList");
            int option = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            switch ( option ) {
                case 1 -> {
                    MessagesGenerator.listGenerator("browsProductsList");
                    int browsOption = scanner.nextInt();
                    scanner.nextLine();  // Consume the newline
                    currentUser.browsProducts(browsOption, databaseService);
                }
                case 2 -> currentUser.showDetails(logger);
                case 3 -> {
                    MessagesGenerator.listGenerator("editProfile");
                    int optionIn = scanner.nextInt();
                    scanner.nextLine();  // Consume the newline
                    currentUser.editDetails(optionIn, logger, scanner);
                }
                case 4 -> currentUser.viewInstallationRequests();
                case 5 -> currentUser.viewRequisitesHistory();
                case 6 ->{
                    try{

                        ArrayList<Date> datesArray = new ArrayList<>();
                        Calendar calendar = Calendar.getInstance();
                        for(int i=0; i<5; i++){
                            calendar.add(Calendar.DAY_OF_MONTH, 1); // Add one day
                            Date date = calendar.getTime();
                            datesArray.add(date);
                        }
                        logger.info("Please enter the request ID: ");
                        int requestID = scanner.nextInt();

                        logger.info("\nPlease select the product you want to install\n");
                        ResultSet rs = Product.getAllProductsNames(databaseService);
                        logger.info("ID" + "  " + "Name\n");
                        while ( rs.next() )
                            logger.info(rs.getInt(1) + "  " + rs.getString(2) + "\n");
                        logger.info("\n");
                        int productID = scanner.nextInt();

                        logger.info("\nPlease select one of the available dates\n");
                        for(int i=0; i<datesArray.size(); i++)
                            logger.info((i+1) + "- " + datesArray.get(i) + "\n");
                        int dateIndex = scanner.nextInt() - 1;
                        Date date = datesArray.get(dateIndex);

                        logger.info("\nPlease enter the description, what exactly do you want to do:\n");
                        scanner.nextLine();
                        String description = scanner.nextLine();

                        Request request = new Request(requestID, productID, currentUser.getEmail(), new Date(), description);
                        boolean done = currentUser.makeRequest(request);
                        if(done){
                            logger.info("\nRequest Added Successfully, you can check your email for further information\n");
                            EmailSender.sendEmail("s12027747@stu.najah.edu", "Installation Request", "Added successfully");
                            datesArray.remove(dateIndex);
                        }
                        else
                            logger.info("Sorry, something went wrong!");

                    }catch ( Exception e ){
                        e.printStackTrace();
                    }
                }
                case 7 -> {
                    try{
                        logger.info("Please enter the request ID you want to remove:\n");
                        ResultSet rs = databaseService.executeQuery("SELECT * FROM Request WHERE userId ='" + currentUser.getEmail() + "'", new ResultSetResultHandler());
                        while ( rs.next() )
                            logger.info(rs.getInt(1) + "  " + rs.getInt(2) + "   " + rs.getString(3) + "  " + rs.getString(4) + "  " + rs.getString(5) + "\n");
                        logger.info("\n");
                        int requestID = scanner.nextInt();

                        boolean done = currentUser.removeRequest(requestID);
                        if(done){
                            logger.info("\nRequest Removed Successfully, you can check your email for further information\n");
                            EmailSender.sendEmail("s12027747@stu.najah.edu", "Installation Request", "Removed successfully");
                        }
                        else{
                            logger.info("Sorry, something went wrong!");

                        }

                    }catch ( Exception e ){
                        e.printStackTrace();
                    }
                }
                case 8 -> iterator = false;
                default -> logger.info("Invalid choice! \nPlease enter 1, 2, ... 6.\n");
            }
        }
    }

    public static void adminUtility(DatabaseService databaseService , Admin currentAdmin) {
        boolean iterator = true;
        while (iterator) {
            MessagesGenerator.listGenerator("adminList");

            int option = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            switch ( option ) {
                case 1 -> {
                    MessagesGenerator.listGenerator("manageProductsList");
                    int browsOption = scanner.nextInt();
                    scanner.nextLine();  // Consume the newline
                    currentAdmin.manageProducts(browsOption, databaseService);
                }
                case 2 -> {

                }
                case 3 -> {

                }
                case 4 -> iterator = false;
                default -> logger.info("Invalid choice! \nPlease enter 1, 2, ... 4.\n");
            }

        }

    }


    public static void installerUtility(DatabaseService databaseService, Installer currentInstaller) {


    }

    public static int signUpUtility(String email,String password){
        String Name,location,phoneNumber;
        int validationStatus = ValidationUser.validation(email, password);
        if (validationStatus == ValidationUser.VALID) {
            logger.info("Please enter your name: ");
            Name=scanner.nextLine();
            logger.info("Please enter your address: ");
            location=scanner.nextLine();
            logger.info("Please enter your phoneNumber: ");
            phoneNumber=scanner.nextLine();
            Profile profile=new Profile(-1,Name,phoneNumber,location);
            SignUp signUp = new SignUp(email,password,false,validationStatus,profile);
            signUp.creatAccount();
        }
        return validationStatus;
    }

    public static User signInUtility(String email, String password,int validationStatus ){
        if (validationStatus == ValidationUser.VALID)
        {
            SignIn signIn = new SignIn(email, password, "", false, validationStatus);
            return  signIn.performLogIn();
        }
        return null;
    }

    private static void adminControlUserUrility(DatabaseService databaseService, Admin currentAdmin) {
        boolean iterator = true;
        while (iterator) {
            MessagesGenerator.listGenerator("adminControlUserList");

            int option = scanner.nextInt();
            scanner.nextLine();  // Consume the newline
            switch (option) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:
                    iterator=false;
                    break;
                default:
                    logger.info("Invalid choice! \nPlease enter 1, 2, ... 4.\n");
            }
        }
    }
}
