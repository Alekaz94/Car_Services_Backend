package com.example.demo.Entities;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
public class Car {
    private UUID id;
    private String make;
    private String model;
    private int year;
    private int milage;
    private int price;

    public Car(UUID id, String make, String model, int year, int milage, int price){
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.milage = milage;
        this.price = price;
    }

    public UUID getId() {
        return this.id;
    }

    public String getMake() {
        return this.make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMilage() {
        return this.milage;
    }

    public void setMilage(int milage) {
        this.milage = milage;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
