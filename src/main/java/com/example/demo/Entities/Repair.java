package com.example.demo.Entities;

import com.example.demo.Enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "repairs")
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    private User customer;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true)
    private User assignedMechanic;
    @ManyToOne
    private Car customerCar;

    private String issueDescription;
    private Date createdAt;
    private Date updatedAt;
    private double cost;

}
