package spp.businesslogic.dto;

import spp.businesslogic.enums.DocumentReviewStatus;

import java.time.LocalDateTime;

public class InternDocumentReviewDTO extends InternDocumentDTO {
    private int evaluationId;
    private int instructorReviewerId;
    private DocumentReviewStatus documentReviewStatus;
    private String comments;
    private double grade;
    private LocalDateTime reviewDate;
    private boolean graded;

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getInstructorReviewerId() {
        return instructorReviewerId;
    }

    public void setInstructorReviewerId(int instructorReviewerId) {
        this.instructorReviewerId = instructorReviewerId;
    }

    public DocumentReviewStatus getDocumentReviewStatus() {
        return documentReviewStatus;
    }

    public void setDocumentReviewStatus(DocumentReviewStatus documentReviewStatus) {
        this.documentReviewStatus = documentReviewStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }
}
