package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

// Import classes from the com.sun.net.httpserver package
import com.sun.net.httpserver.Headers; // Represents the headers of an HTTP request or response
import com.sun.net.httpserver.HttpExchange; // Represents an HTTP request or response
import com.sun.net.httpserver.HttpHandler; // Interface for handling HTTP requests

// Import classes from the java.io package
import java.io.IOException; // Exception thrown when an I/O operation fails
import java.io.InputStream; // Abstract class for reading data from a source
import java.io.OutputStream; // Abstract class for writing data to a destination

// Import classes from the java.nio.file package
import java.nio.file.Files; // Utility class for working with files and directories
import java.nio.file.Path; // Represents a path on the file system
import java.nio.file.Paths; // Utility class for working with paths
import java.nio.file.StandardCopyOption; // Options for the copy operation

// Import classes from the java.util package
import java.util.List; // Interface for working with ordered collections
import java.util.Map; // Interface for working with key-value pairs
import java.util.UUID; // Utility class for generating unique identifiers
import java.util.logging.Level; // Defines the level of logging
import java.util.logging.Logger; // Utility class for logging messages
public class ImageUploadHandler implements HttpHandler {
    // Logger for logging messages
    private static final Logger logger = Logger.getLogger(ImageUploadHandler.class.getName());
    // Maximum allowed file size for upload in bytes (10 MB)
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;
    // Get the user's current working directory
    static Path userPath = Paths.get(System.getProperty("user.dir"));
    // Path to the folder where uploaded files will be saved
    private static final Path UPLOAD_FOLDER = Paths.get(userPath + "\\src\\main\\java\\org\\example\\Designer_Templates");
    // Allowed file extensions for uploaded images
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".png", ".gif"};

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Check if the request method is POST
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            try {
                // Get the request headers
                Headers headers = exchange.getRequestHeaders();
                // Get the Content-Type header
                String contentType = headers.getFirst("Content-Type");

                // Check if the Content-Type is multipart/form-data
                if (contentType != null && contentType.startsWith("multipart/form-data")) {
                    // Create a MultipartFormDataHandler to handle the multipart form data
                    MultipartFormDataHandler formDataHandler = new MultipartFormDataHandler(exchange);
                    // Get the uploaded files from the form data
                    Map<String, List<UploadedFile>> files = formDataHandler.getUploadedFiles();

                    // Check if there is a file with the name "file"
                    if (files.containsKey("file")) {
                        List<UploadedFile> uploadedFiles = files.get("file");
                        // Check if there are any uploaded files
                        if (!uploadedFiles.isEmpty()) {
                            for (UploadedFile file : uploadedFiles) {
                                // Check if the file is an image file (jpg, png, gif)
                                if (isImageFile(file.getFilename())) {
                                    // Check if the file size is within the limit
                                    if (file.getContentLength() <= MAX_FILE_SIZE) {
                                        // Move the file to the uploads folder with a unique filename
                                        Path targetFile = UPLOAD_FOLDER.resolve(generateUniqueFilename(file.getFilename()));
                                        Files.move(file.getPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);
                                        System.out.println("File saved successfully");
                                        sendErrorResponse(exchange, 415, "File saved successfully");
                                    } else {
                                        // Log a warning if the file size exceeds the limit
                                        logger.warning("File size exceeds the limit of " + MAX_FILE_SIZE / (1024 * 1024) + " MB: " + file.getFilename());
                                        // Send an error response with a 413 status code (Request Entity Too Large)
                                        sendErrorResponse(exchange, 413, "File size exceeds the limit of " + MAX_FILE_SIZE / (1024 * 1024) + " MB");
                                    }
                                } else {
                                    // Print a message if the file type is not supported
                                    System.out.println("Unsupported file type. The file must be jpg, png, gif.");
                                    // Send an error response with a 415 status code (Unsupported Media Type)
                                    sendErrorResponse(exchange, 415, "Unsupported Media Type");
                                }
                            }

                            // Send a success response and redirect the client
                            String successMessage = "Files saved successfully";
                            byte[] responseBytes = successMessage.getBytes("UTF-8");
                            exchange.getResponseHeaders().set("Content-Type", "text/plain");
                            exchange.getResponseHeaders().set("Location", "/");
                            exchange.sendResponseHeaders(302, responseBytes.length);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(responseBytes);
                            }
                        } else {
                            // Log a warning if no file was uploaded
                            logger.warning("No file was uploaded");
                            // Send an error response with a 400 status code (Bad Request)
                            sendErrorResponse(exchange, 400, "No file was uploaded");
                        }
                    } else {
                        // Log a warning if no file was uploaded
                        logger.warning("No file was uploaded");
                        // Send an error response with a 400 status code (Bad Request)
                        sendErrorResponse(exchange, 400, "No file was uploaded");
                    }
                } else {
                    // Log a warning if the Content-Type is not supported
                    logger.warning("Unsupported Media Type");
                    // Send an error response with a 415 status code (Unsupported Media Type)
                    sendErrorResponse(exchange, 415, "Unsupported Media Type");
                }
            } catch (IOException e) {
                // Send an error response with a 500 status code (Internal Server Error) if an IOException occurs
                sendErrorResponse(exchange, 500, "Internal Server Error");
            } catch (Exception e) {
                // Log a severe error and send an error response with a 500 status code (Internal Server Error) if an unexpected exception occurs
                logger.log(Level.SEVERE, "Unexpected error", e);
                sendErrorResponse(exchange, 500, "Internal Server Error");
            }
        } else {
            // Send a 405 status code (Method Not Allowed) if the request method is not POST
            exchange.sendResponseHeaders(405, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("Method Not Allowed".getBytes("UTF-8"));
            }
        }
    }

    // Helper method to send an error response
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes("UTF-8"));
        }
    }

    // Helper method to generate a unique filename for the uploaded file
    private String generateUniqueFilename(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        return UUID.randomUUID().toString() + extension;
    }

    // Helper method to check if a file is an image file (jpg, png, gif)
    private boolean isImageFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        for (String allowedExtension : ALLOWED_EXTENSIONS) {
            if (extension.equalsIgnoreCase(allowedExtension)) {
                return true;
            }
        }
        return false;
    }

    // Inner class to handle multipart form data
    static class MultipartFormDataHandler {
        private static final String BOUNDARY_PREFIX = "--";
        private static final String CONTENT_DISPOSITION = "Content-Disposition";
        private static final String CONTENT_TYPE = "Content-Type";
        private static final String CONTENT_LENGTH = "Content-Length";

        private final HttpExchange exchange;
        private final String boundary;
        private final Map<String, List<UploadedFile>> uploadedFiles;

        // Constructor to initialize the MultipartFormDataHandler
        public MultipartFormDataHandler(HttpExchange exchange) throws IOException {
            this.exchange = exchange;
            // Get the boundary string from the Content-Type header
            String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
            int boundaryIndex = contentType.indexOf("boundary=");
            if (boundaryIndex >= 0) {
                boundary = contentType.substring(boundaryIndex + 9);
                uploadedFiles = parseMultipartFormData();
            } else {
                boundary = null; // or handle the case when the "boundary=" is not present
                uploadedFiles = new java.util.HashMap<>(); // initialize with an empty map or handle differently
            }
        }

        // Getter method to retrieve the uploaded files
        public Map<String, List<UploadedFile>> getUploadedFiles() {
            return uploadedFiles;
        }

        // Helper method to parse the multipart form data and extract the uploaded files
        private Map<String, List<UploadedFile>> parseMultipartFormData() throws IOException {
            if (boundary == null || boundary.isEmpty()) {
                return new java.util.HashMap<>(); // or handle the case differently
            }

            Map<String, List<UploadedFile>> files = new java.util.HashMap<>();
            InputStream requestBody = exchange.getRequestBody();
            String line = readLine(requestBody);
            String boundaryString = BOUNDARY_PREFIX + boundary;

            while (line != null) {
                if (line.startsWith(boundaryString)) {
                    Map<String, String> headers = new java.util.HashMap<>();
                    line = readLine(requestBody);
                    while (line != null && !line.equals(boundaryString)) {
                        if (line.isEmpty()) {
                            // This is the separator between headers and content
                            break;
                        }
                        String[] headerParts = line.split(":");
                        if (headerParts.length == 2) {
                            headers.put(headerParts[0].trim(), headerParts[1].trim());
                        }
                        line = readLine(requestBody);
                    }

                    // Extract the file name, content type, and content length from the headers
                    String fileName = getFileName(headers.get(CONTENT_DISPOSITION));
                    String contentType = headers.get(CONTENT_TYPE);
                    long contentLength = 0;
                    if (headers.containsKey(CONTENT_LENGTH)) {
                        contentLength = Long.parseLong(headers.get(CONTENT_LENGTH));
                    }

                    // Create a temporary file to store the uploaded file contents
                    Path tempFile = Files.createTempFile(null, null);
                    Files.copy(requestBody, tempFile, StandardCopyOption.REPLACE_EXISTING);

                    // Create an UploadedFile object and add it to the list of uploaded files
                    UploadedFile uploadedFile = new UploadedFile(tempFile, fileName, contentLength);
                    String fieldName = getFieldName(headers.get(CONTENT_DISPOSITION));
                    files.computeIfAbsent(fieldName, k -> new java.util.ArrayList<>()).add(uploadedFile);

                    // Advance to the next boundary or end of stream
                    line = readLine(requestBody);
                } else {
                    line = readLine(requestBody);
                }
            }
            return files;
        }

        // Helper method to read a line from the input stream
        private String readLine(InputStream input) throws IOException {
            StringBuilder line = new StringBuilder();
            int c;
            while ((c = input.read()) != -1) {
                if (c == '\r') {
                    if (input.read() == '\n') {
                        break;
                    }
                }
                line.append((char) c);
            }
            if (line.length() == 0 && c == -1) {
                return null;
            }
            return line.toString();
        }

        // Helper method to extract the file name from the Content-Disposition header
        private String getFileName(String contentDisposition) {
            String[] parts = contentDisposition.split(";");
            for (String part : parts) {
                if (part.trim().startsWith("filename")) {
                    return part.substring(part.indexOf("=") + 1).trim().replace("\"", "");
                }
            }
            return null;
        }

        // Helper method to extract the field name from the Content-Disposition header
        private String getFieldName(String contentDisposition) {
            String[] parts = contentDisposition.split(";");
            for (String part : parts) {
                if (part.trim().startsWith("name")) {
                    return part.substring(part.indexOf("=") + 1).trim().replace("\"", "");
                }
            }
            return null;
        }
    }

    // Inner class to represent an uploaded file
    static class UploadedFile {
        private final Path path;
        private final String filename;
        private final long contentLength;

        // Constructor to initialize an UploadedFile object
        public UploadedFile(Path path, String filename, long contentLength) {
            this.path = path;
            this.filename = filename;
            this.contentLength = contentLength;
        }

        // Getter methods to access the file path, filename, and content length
        public Path getPath() {
            return path;
        }

        public String getFilename() {
            return filename;
        }

        public long getContentLength() {
            return contentLength;
        }

        @Override
        public String toString() {
            return "UploadedFile{" +
                    "path=" + path +
                    ", filename='" + filename + '\'' +
                    ", contentLength=" + contentLength +
                    '}';
        }
    }
}