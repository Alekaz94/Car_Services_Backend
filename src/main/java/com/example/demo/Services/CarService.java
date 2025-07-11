package com.example.demo.Services;

import com.example.demo.DTO.CarRequest;
import com.example.demo.DTO.CarUpdateRequest;
import com.example.demo.Entities.Car;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.CarRepository;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
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
        User user = getUserByEmail(email);
        return user.getCars();
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car addCarToUser(CarRequest carRequest, String email) {
        User user = getUserByEmail(email);
        Car car = new Car();

        car.setMake(carRequest.make());
        car.setModel(carRequest.model());
        car.setLicencePlate(carRequest.licencePlate());
        car.setYear(carRequest.year());
        car.setMilage(carRequest.milage());
        car.setPrice(carRequest.price());
        car.setUser(user);

        return carRepository.save(car);
    }

    public Car updateCar(UUID carId, CarUpdateRequest carRequest, String email) throws AccessDeniedException {
        Car carToUpdate = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("Car not found!"));

        if(!carToUpdate.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You do not own this car!");
        }

        carToUpdate.setLicencePlate(carRequest.licencePlate());
        carToUpdate.setMilage(carRequest.milage());
        carToUpdate.setPrice(carRequest.price());

        return carRepository.save(carToUpdate);
    }

    public Car deleteCar(UUID carId, String email) throws AccessDeniedException {
        Car carToDelete = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("Car not found"));
        User user = getUserByEmail(email);

        if(!carToDelete.getUser().getEmail().equals(email) && !user.getRole().toString().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You do not own this car!");
        }

        carRepository.deleteById(carId);
        return carToDelete;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
