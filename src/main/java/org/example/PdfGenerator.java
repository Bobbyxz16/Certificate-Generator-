package org.example;

/*-------------------------------------*/
/* SID: 2267684 - TEAM: TECH ACHIEVERS */
/*-------------------------------------*/

import com.itextpdf.html2pdf.HtmlConverter; // Import HtmlConverter class from the iText library for HTML to PDF conversion
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfGenerator {

    // Method to generate PDF certificate
    public static void generatePDF() {
        // Create a new certificate instance
        Certificate certificate = Certificate.createCertificate();
        // Get the template choice selected by the user
        int choice = certificate.getTemplateChoice();
        // Get the current user's username
        String username = RegistrationManager.UserRegistration.getCurrentUser();

        // Path to the HTML template based on the user's choice
        String templatePath = String.format("src/main/java/org/example/Certificates_Templates/certificate%d.html", choice);

        try {
            // Read the contents of the HTML template file
            String template = new String(Files.readAllBytes(Paths.get(templatePath)));

            // Replace placeholders in the template with actual certificate data
            template = template.replace("{{name}}", certificate.getName());
            template = template.replace("{{title}}", certificate.getTitle());
            template = template.replace("{{description}}", certificate.getDescription());

            // If the username is null, set it to "guest"
            if (username == null){
                username = "guest";
            }
            // Path to save the generated PDF certificate
            String outputPath = String.format("src/main/java/org/example/User_Certificates/%s_Certificate.pdf", username);

            // Create an output stream to write the PDF data
            OutputStream outputStream = new FileOutputStream(outputPath);

            // Convert the HTML template to PDF and write it to the output stream
            HtmlConverter.convertToPdf(template, outputStream);

            // Close the output stream
            outputStream.close();

            // Print a message indicating successful certificate creation
            System.out.println("\n** Certificate Created **");
            System.out.println("Thank you for using CERTIGRAPH!");
        } catch (IOException e) {
            // Print stack trace if an IOException occurs
            e.printStackTrace();
        }
    }
}
