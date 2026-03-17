package com.project.dynamicformbuilderbackend.enums;

public enum SubmissionStatus {
    DRAFT("DRAFT"), SUBMITTED("SUBMITTED"), VALIDATED("VALIDATED"), DELETED("DELETED");

    private final String status;

    private SubmissionStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
