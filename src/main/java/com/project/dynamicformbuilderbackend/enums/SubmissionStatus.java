package com.project.dynamicformbuilderbackend.enums;

public enum SubmissionStatus {
    DRAFT("DRAFT"), SUBMITTED("SUBMITTED"), DELETED("DELETED");

    private final String status;

    private SubmissionStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
