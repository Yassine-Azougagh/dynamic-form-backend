package com.project.dynamicformbuilderbackend.enums;

public enum FormStatus {
    DRAFT("DRAFT"), PUBLISHED("PUBLISHED"), CLOSED("CLOSED");

    private final String status;

    private FormStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
