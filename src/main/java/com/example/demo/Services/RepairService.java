package com.example.demo.Services;

import com.example.demo.DTO.RepairRequest;
import com.example.demo.DTO.RepairUpdateRequest;
import com.example.demo.Entities.Car;
import com.example.demo.Entities.Repair;
import com.example.demo.Entities.User;
import com.example.demo.Enums.Status;
import com.example.demo.Repositories.CarRepository;
import com.example.demo.Repositories.RepairRepository;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RepairService {

    private final RepairRepository repairRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Autowired
    public RepairService(RepairRepository repairRepository, UserRepository userRepository, CarRepository carRepository) {
        this.repairRepository = repairRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    public List<Repair> getAllRepairs() {
        return repairRepository.findAll();
    }

    public List<Repair> getCustomersRepairOrders(String email) {
        User user = getUserByEmail(email);
        return repairRepository.findAllByCustomerEmail(email);
    }

    public Repair createRepair(RepairRequest repairRequest, String email) {
        User user = getUserByEmail(email);
        Repair repair = new Repair();
        User mechanic = userRepository.findById(repairRequest.mechanicId()).orElseThrow(() -> new IllegalArgumentException("Mechanic not found!"));
        Car car = carRepository.findById(repairRequest.carId()).orElseThrow(() -> new IllegalArgumentException("Car not found!"));

        repair.setStatus(Status.IN_PROGRESS);
        repair.setCustomer(user);
        repair.setAssignedMechanic(mechanic);
        repair.setCustomerCar(car);
        repair.setIssueDescription(repairRequest.issueDescription());
        repair.setCost(repairRequest.cost());
        repair.setCreatedAt(new Date());
        repair.setUpdatedAt(new Date());
        repairRepository.save(repair);
        return repair;
    }

    public Repair getJobFromEmployee(String email) {
        User mechanic = getUserByEmail(email);
        return Optional.ofNullable(repairRepository.findRepairByAssignedMechanicId(mechanic.getId())).orElseThrow(() ->
                new IllegalArgumentException("Could not find repair job for that employee!"));
    }

    public Repair updateRepairForMechanic(UUID repaidId, RepairUpdateRequest repairUpdateRequest, String email) {
        User mechanic = getUserByEmail(email);
        Repair repair = getRepairById(repaidId);

        if(!repair.getAssignedMechanic().getId().equals(mechanic.getId())) {
            throw new IllegalArgumentException("You can only update repairs assigned to you!");
        }

        repair.setStatus(repairUpdateRequest.status());
        repair.setUpdatedAt(new Date());

        return repairRepository.save(repair);
    }

    public Repair assignMechanic(UUID repairId, String email) {
        User mechanic = getUserByEmail(email);
        Repair repairToAssign = getRepairById(repairId);
        if(repairToAssign.getAssignedMechanic() != null) {
            throw new IllegalArgumentException("Repair job already has a mechanic assigned!");
        }

        repairToAssign.setAssignedMechanic(mechanic);
        repairToAssign.setUpdatedAt(new Date());
        return repairRepository.save(repairToAssign); 
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Repair getRepairById(UUID id) {
        return repairRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Could not find repair job!"));
    }


}
