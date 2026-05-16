package spp.businesslogic.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class InitialDocumentDTO {

    private String originalName;
    private String savedName;
    private String filePath;
    private Double sizeMb;
    private String extension;
    private LocalDateTime uploadDate;
    private String documentType;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getSavedName() {
        return savedName;
    }

    public void setSavedName(String savedName) {
        this.savedName = savedName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Double getSizeMb() {
        return sizeMb;
    }

    public void setSizeMb(Double sizeMb) {
        this.sizeMb = sizeMb;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
