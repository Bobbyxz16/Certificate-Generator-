# README
SID: 2267684 - TEAM: TECH ACHIEVERS
## Overall

This code is developed in IntelliJ IDEA Community 2023 with JDK version 21.

To run the code, navigate to the Main class and click on the green button. After execution, you can find the generated PDF certificate in the "User_Certificates" folder.

Make sure you extract and use only the "Certigraph" file
## Explanation

Depending on the type of account the user chooses, different templates are available, all stored in the "Certificates_Templates" folder. If the user selects a normal account, they can access all templates except for premium ones, which are exclusive to pro and designer accounts. For pro accounts, accessing premium templates requires payment, necessitating the input of banking details during registration. The same applies to designer accounts; users also need to provide banking information, but they can upload their templates through a system-provided link. User-uploaded templates are stored in the "Designer_Templates" folder. Additionally, users with designer accounts can access premium templates. For those who prefer not to create an account, they can proceed directly to certificate creation, albeit with access to limited templates. To access what we would call a guest account, select the Login option, answer "no" when asked if you have an account, and then "yes" to continue as a guest account.

All login data is stored in the text file "Login_userdata" with the format: | Username, Password, Type of account (1= Normal account, 2=Pro Account, 3=Designer Account | example: David, bestengineer, 2. User registration data is stored in the "Profile_Userdata" folder. If registration includes banking information, it is stored in the "Bank_Userdata" folder.

To generate certificates in PDF format, two libraries and HTML codes as templates are utilized. The libraries are iTextPDF, for PDF editing, and html2pdf, for transforming HTML (templates) into PDF by modifying data using placeholders. Additionally, slf4j-api and logback-classic libraries capture information messages when generating a certificate at the program's end. Configuration is done in logback.xml, located in src/main/resources, to direct messages to a file named mylog.log in the logs folder, minimizing unnecessary output.

This is the dependencies
```java
<dependencies>
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itextpdf</artifactId>
            <version>5.5.13.2</version>
        </dependency>
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>html2pdf</artifactId>
            <version>3.0.2</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.32</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
    </dependencies>
```

Finally, for designer accounts, an HttpServer instance on port 8082 of localhost serves as a link for users to upload their templates. The RootHandler class manages GET requests, while the ImageUploadHandler handles POST requests. Designers can use the provided link http://localhost:8082/ to upload templates into the system. 

Due to Java inconveniences, it is not possible to open the web page twice in the same program since we would be trying to access the same localhost port. That's why I display a message for designers to try again later.