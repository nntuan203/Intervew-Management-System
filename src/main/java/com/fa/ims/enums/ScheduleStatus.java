package com.fa.ims.enums;

public enum ScheduleStatus {
    WAITING_FOR_INTERVIEW("Waiting for interview"),
    IN_PROGRESS("In-progress"),
    CANCELLED_INTERVIEW("Cancelled interview"),
    PASSED_INTERVIEW("Passed interview"),
    FAILED_INTERVIEW("Failed interview");

    private final String value;

    ScheduleStatus(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}
