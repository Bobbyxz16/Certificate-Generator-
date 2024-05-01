package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RootHandler implements HttpHandler {

    // Override method to handle HTTP requests
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Get the current working directory
        Path userPath = Paths.get(System.getProperty("user.dir"));

        // Define the path to the Designer_webpage.html file
        Path indexPath = Paths.get(userPath + "\\src\\main\\java\\org\\example\\Designer_Templates\\Designer_webpage.html");

        // Read the content of the Designer_webpage.html file into a byte array
        byte[] bytes = Files.readAllBytes(indexPath);

        // Set HTTP response headers
        exchange.sendResponseHeaders(200, bytes.length);

        // Get the response body output stream
        OutputStream os = exchange.getResponseBody();

        // Write the byte array content to the response body
        os.write(bytes);

        // Close the response body stream
        os.close();
    }
}
