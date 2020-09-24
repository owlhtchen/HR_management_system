package model.application;

import java.io.Serializable;

public enum ApplicationStatus implements Serializable {

    SUBMITTED("submitted"),
    HIRED("hired"),
    REJECTED("rejected"),
    CLOSED("closed");

    private String status;

    ApplicationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
