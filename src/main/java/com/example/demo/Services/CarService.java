package com.example.demo.Services;

import com.example.demo.DTO.CarRequest;
import com.example.demo.Entities.Car;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.CarRepository;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Autowired
    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public Car getCar(UUID carId) {
        if(carId == null) {
            throw new IllegalArgumentException("No id was entered");
        }

        return carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("Could not find car!"));
    }

    public List<Car> getAllCarsByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return user.getCars();
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car addCarToUser(CarRequest carRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        Car car = new Car();

        car.setMake(carRequest.getMake());
        car.setModel(carRequest.getModel());
        car.setLicencePlate(carRequest.getLicencePlate());
        car.setYear(carRequest.getYear());
        car.setMilage(carRequest.getMilage());
        car.setPrice(carRequest.getPrice());
        car.setUser(user);

        return carRepository.save(car);
    }
}
