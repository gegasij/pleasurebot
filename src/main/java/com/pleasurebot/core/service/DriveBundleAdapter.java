package com.pleasurebot.core.service;

import com.google.api.services.drive.Drive;
import com.pleasurebot.core.model.BundleList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriveBundleAdapter {
    private final Drive drive;
    @Getter
    private BundleList bundleList;
//    private static final String fileId = "1rjdeeaL-xEb9Y77uMmLDG1aCZqTYFAeh";

    @SneakyThrows
    public BundleList getDriveBundle() {
        if (bundleList == null) {
            updateDriveBundle();
        }
        return getBundleList();
    }

    @SneakyThrows
    public void updateDriveBundle() {
        String fileId = drive.files().list().setPageSize(10).execute()
                .getFiles().stream()
                .filter(it-> it.getMimeType().equals("text/plain"))
                .findFirst()
                .get()
                .getId();
        bundleList = new BundleList();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        drive.files()
                .get(fileId)
                .executeMediaAndDownloadTo(output);
        List<String> messages = Arrays.stream(output.toString().split(System.lineSeparator()))
                .toList();
        bundleList.setMessages(messages);
    }
}
