package com.live100x.iterator;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LinkedInDBSpreadsheetIterator implements Iterator {
    private final String sheetName;
    private final int startRow, endRow;
    private final List<List<Object>> list;
    // pageNo minimal value should be 1
    public LinkedInDBSpreadsheetIterator(String sheetName, int pageNo, int pageSize) {
        this.sheetName = sheetName;
        startRow = Integer.max((pageNo-1) * pageSize, 1);
        endRow = (pageNo * pageSize) - 1;
        list = getData();
    }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public List<Object> next() {
        List<Object> row = list.get(0);
        list.remove(0);
        return row;
    }

    private List<List<Object>> getData() {
        List<List<Object>> values = new ArrayList<>();
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            // hr-activity-link is sheet name
            final String range = String.format("%s!A%s:G%s", sheetName, startRow, endRow);
            Sheets service =
                    new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                            .setApplicationName(APPLICATION_NAME)
                            .build();
            ValueRange response = service.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
            values = response.getValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    private static final String APPLICATION_NAME = "Google Sheets API Java";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
     * your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

    private static final String CREDENTIALS_FILE_PATH = "/linkedin-db-credentials.json";
    public static final String SPREADSHEET_ID = "1T8ykA1BW6XrWsG3QsdXyq_2Qfp8YaBSCi9uD0j6h6i0";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in =
                LinkedInDBSpreadsheetIterator.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline")
                        .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
