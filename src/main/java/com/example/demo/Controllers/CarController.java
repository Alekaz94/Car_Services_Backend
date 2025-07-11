package com.example.demo.Controllers;

import com.example.demo.DTO.CarRequest;
import com.example.demo.Entities.Car;
import com.example.demo.Services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/{carId}")
    public ResponseEntity<Car> getCar(@PathVariable UUID carId) {
        Car car = carService.getCar(carId);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/my-cars")
    public ResponseEntity<List<Car>> getUsersCars(Principal principal) {
        List<Car> cars = carService.getAllCarsByUserEmail(principal.getName());
        return ResponseEntity.ok(cars);
    }

    @GetMapping()
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @PostMapping()
    public ResponseEntity<Car> createCar(@RequestBody CarRequest carRequest, Principal principal) {
        Car car = carService.addCarToUser(carRequest, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }
}
