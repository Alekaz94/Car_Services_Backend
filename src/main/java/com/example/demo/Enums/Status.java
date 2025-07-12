package com.example.demo.Enums;

public enum Status {
//    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    @Override
    public String toString() {
        return "STATUS_" + name();
    }
}
