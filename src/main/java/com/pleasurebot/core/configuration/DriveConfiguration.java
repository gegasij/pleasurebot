package com.pleasurebot.core.configuration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/* class to demonstarte use of Drive files list API */
@Configuration
public class DriveConfiguration {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/august-now.json";
    private static final String SERVICE_ACCOUNT = "service-account@august-now-373714.iam.gserviceaccount.com";
    private static final String APPLICATION_NAME = "august-now-373714";
    private static final List<String> SCOPES = List.of(DriveScopes.DRIVE);
    private Drive driveService;

    // CREATE THE CONNEXION TO THE API
    @Bean
    public Drive buildApiService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        InputStream resourceAsStream = DriveConfiguration.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                ServiceAccountCredentials.fromStream(resourceAsStream).createScoped(SCOPES).createDelegated(SERVICE_ACCOUNT));
        driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer).setApplicationName(APPLICATION_NAME).build();
        return driveService;
    }

    // GET THE METADATA OF THE FILES STORED IN YOUR GOOGLE DRIVE
//    public static List<File> getChildren() throws IOException {
//        FileList result = driveService.files().list()
//                .setPageSize(10)
//                .setFields("nextPageToken, files(id, name)")
//                .execute();
//        return result.getFiles();
//    }

    public void getOutputStream(File file, OutputStream outputStream) throws IOException {
        try {
            // YOU CAN DOWNLOAD GOOGLE DRIVE FILES IN PDF FOR EXAMPLE
//            if (file.getMimeType().contains("application/vnd.google-apps.")) {
//                driveService.files().export(file.getId(), "application/pdf").executeMediaAndDownloadTo(outputStream);
//                // OTHER FILES ARE RETRIEVED IN THEIR ORIGINAL MIME TYPE
//            } else {
            driveService.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
            //}
        } catch (IOException e) {
            throw new IOException("Error getting outputStream for file:" + file + " - error: " + e);
        }
    }
}