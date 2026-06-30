package spp.businesslogic.dto;


import spp.businesslogic.enums.DocumentType;

import java.time.LocalDateTime;


public class InternDocumentDTO {

    private int internDocumentId;
    private String originalName;
    private String savedName;
    private String filePath;
    private Double sizeMb;
    private String extension;
    private LocalDateTime uploadDate;
    private DocumentType documentType;

    public int getInternDocumentId() {
        return internDocumentId;
    }

    public void setInternDocumentId(int internDocumentId) {
        this.internDocumentId = internDocumentId;
    }

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

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    @Override
    public String toString() {
        String typeName = (documentType != null) ? documentType.name() : "DOCUMENTO";
        return typeName + " - " + originalName;
    }
}

