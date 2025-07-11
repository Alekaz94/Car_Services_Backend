package com.example.demo.DTO;

public record CarRequest( String make,
                          String model,
                          String licencePlate,
                          int year,
                          int milage,
                          int price) {

}
