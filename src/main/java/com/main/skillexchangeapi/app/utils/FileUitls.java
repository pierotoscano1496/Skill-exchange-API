package com.main.skillexchangeapi.app.utils;

import java.util.Arrays;
import java.util.Optional;

public class FileUitls {
    static final String[] ACCEPTED_EXTENSIONS = {"jpg", "jpeg", "png", "doc", "docx", "pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx"};

    public static Optional<String> getExtension(String fileName) {
        String[] parts = fileName.split("\\.");
        if (parts.length > 1) {
            String extension = parts[parts.length - 1];
            if (Arrays.stream(ACCEPTED_EXTENSIONS).anyMatch(extension::equalsIgnoreCase)) {
                return Optional.of(extension);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
