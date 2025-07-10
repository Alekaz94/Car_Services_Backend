package com.example.demo.Enums;


public enum Roles {
    ADMIN,
    EMPLOYEE,
    CUSTOMER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
