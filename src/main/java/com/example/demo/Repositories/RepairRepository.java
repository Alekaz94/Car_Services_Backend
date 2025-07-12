package com.example.demo.Repositories;

import com.example.demo.Entities.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepairRepository extends JpaRepository<Repair, UUID> {
    List<Repair> findAllByCustomerEmail(String email);

    Repair findRepairByAssignedMechanicId(UUID id);

}
