package com.main.skillexchangeapi.app.utils;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;
import com.main.skillexchangeapi.domain.constants.ResourceSource;
import com.main.skillexchangeapi.domain.constants.ResourceType;

import java.util.Arrays;
import java.util.Optional;

public class FileUitls {
    static final String[] ACCEPTED_CHAT_FILES_EXTENSIONS = { "jpg", "jpeg", "png", "doc", "pdf", "docx", "ppt", "pptx",
            "xls", "xlsx", "txt" };
    static final String[] ACCEPTED_MULTIMEDIA_RESOURCES_EXTENSIONS = { "jpg", "jpeg", "png", "bmp", "gif", "mp4", "mov",
            "wmv", "avi" };
    static final String[] ACCEPTED_RESOURCES_EXTENSIONS_IMAGES = { "jpg", "jpeg", "png", "bmp", "gif" };
    static final String[] ACCEPTED_RESOURCES_EXTENSIONS_VIDEOS = { "mp4", "mov", "wmv", "avi" };
    static final String[] ACCEPTED_RESOURCES_EXTENSIONS_DOCUMENTS = { "doc", "pdf", "docx", "ppt", "pptx", "xls",
            "xlsx", "txt" };
    static final String[] MEDIOS_MULTIMEDIA_RESOURCES = { "video", "imagen", "web-externa", "facebook", "instagram",
            "tiktok", "youtube", "twitter", "linkedin" };
    static final String[] ACCEPTED_METADATA_MODALIDAD_PAGO = { "jpg", "jpeg", "png" };
    static final String[] METADATA_SERVICIO_TYPE = { "payment", "recurso-multimedia" };

    public static Optional<String> getExtension(String fileName, ResourceSource resourceSource) {
        if (fileName == null || !fileName.contains(".")) {
            return Optional.empty();
        }

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        ResourceSource source = resourceSource != null ? resourceSource : ResourceSource.CHAT;

        boolean admitExtension = switch (source) {
            case CHAT -> Arrays.stream(ACCEPTED_CHAT_FILES_EXTENSIONS).anyMatch(extension::equalsIgnoreCase);
            case MULTIMEDIA ->
                Arrays.stream(ACCEPTED_MULTIMEDIA_RESOURCES_EXTENSIONS).anyMatch(extension::equalsIgnoreCase);
            case PAYMENT -> Arrays.stream(ACCEPTED_METADATA_MODALIDAD_PAGO).anyMatch(extension::equalsIgnoreCase);
        };

        return admitExtension ? Optional.of(extension) : Optional.empty();
    }

    public static boolean checkFileType(String fileName, ResourceSource resourceSource, ResourceType resourceType) {
        Optional<String> estensionFile = getExtension(fileName, resourceSource);
        if (estensionFile.isPresent()) {
            String extension = estensionFile.get();

            return switch (resourceType) {
                case IMAGE -> Arrays.stream(ACCEPTED_RESOURCES_EXTENSIONS_IMAGES).anyMatch(extension::equalsIgnoreCase);
                case VIDEO -> Arrays.stream(ACCEPTED_RESOURCES_EXTENSIONS_VIDEOS).anyMatch(extension::equalsIgnoreCase);
                case DOCUMENT ->
                    Arrays.stream(ACCEPTED_RESOURCES_EXTENSIONS_DOCUMENTS).anyMatch(extension::equalsIgnoreCase);
            };
        } else {
            return false;
        }
    }

    /* Checkout: refactorizar método para aplicar una sola lógica */
    public static Optional<String> getExtensionFromMultimediaResource(String fileName) {
        String[] parts = fileName.split("\\.");
        if (parts.length > 1) {
            String extension = parts[parts.length - 1];
            if (Arrays.stream(ACCEPTED_MULTIMEDIA_RESOURCES_EXTENSIONS).anyMatch(extension::equalsIgnoreCase)) {
                return Optional.of(extension);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**/
    public static Optional<Medio> getMedioFromMultimediaResource(String fileName) {
        Optional<String> extension = getExtensionFromMultimediaResource(fileName);
        if (extension.isPresent()) {
            Medio medio = null;

            switch (extension.get().toLowerCase()) {
                case "jpg":
                case "jpeg":
                case "png":
                case "bmp":
                case "gif":
                    medio = Medio.imagen;
                    break;
                case "mp4":
                case "mov":
                case "wmv":
                case "avi":
                    medio = Medio.video;
                    break;
            }
            return Optional.of(medio);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<String> getExtensionFromMetadataModalidadPago(String fileName) {
        String[] parts = fileName.split("\\.");
        if (parts.length > 1) {
            String extension = parts[parts.length - 1];
            if (Arrays.stream(ACCEPTED_METADATA_MODALIDAD_PAGO).anyMatch(extension::equalsIgnoreCase)) {
                return Optional.of(extension);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public static boolean checkMetadataServicioType(String metadataServicioType) {
        return Arrays.stream(METADATA_SERVICIO_TYPE).anyMatch(metadataServicioType::equalsIgnoreCase);
    }
}
