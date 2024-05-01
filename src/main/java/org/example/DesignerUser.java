package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

// Import necessary classes for HTTP server functionality
import com.sun.net.httpserver.HttpServer;

// Import necessary classes for file system operations
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Import necessary class for logging
import java.util.logging.Level;
import java.util.logging.Logger;

public class DesignerUser {

    private static final Logger logger = Logger.getLogger(DesignerUser.class.getName()); // Logger instance for logging messages related to DesignerUser class
    static Path userPath = Paths.get(System.getProperty("user.dir")); // Get the current working directory
    private static final Path UPLOAD_FOLDER = Paths.get(userPath + "\\src\\main\\java\\org\\example\\Designer_Templates"); // Path to the folder where uploaded files will be stored


    // Method to handle operations related to designer user accounts
    public static void Designer_Account(){
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", 8082), 0); // Create HTTP server instance
        } catch (java.io.IOException e) {
            if (e instanceof java.net.BindException) {
                // Catch the java.net.BindException
                System.out.println("Sorry, you are not allow to upload any template right now, please try later");
            }

            return; // Indicate server failed to start
        }

        // Create the upload folder if it doesn't exist
        try {
            Files.createDirectories(UPLOAD_FOLDER);
        } catch (java.io.IOException e) {
            logger.log(Level.SEVERE, "Failed to create upload folder", e); // Log the exception if folder creation fails
        }

        server.start(); // Start the HTTP server

        // Handle GET requests to the root "/"
        server.createContext("/", new RootHandler());

        // Handle POST requests for image upload
        server.createContext("/upload", new ImageUploadHandler());

        // Inform the user about capabilities of designer account and provide link for uploading templates
        System.out.println("The user with designer account can upload their own templates");
        System.out.println("Please, click this link http://localhost:8082/ to upload templates");
    }
}
