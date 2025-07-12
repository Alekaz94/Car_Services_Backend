package com.example.demo.Controllers;

import com.example.demo.DTO.RepairRequest;
import com.example.demo.DTO.RepairUpdateRequest;
import com.example.demo.Entities.Repair;
import com.example.demo.Services.RepairService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/repairs")
public class RepairController {

    private final RepairService repairService;

    @Autowired
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Repair>> getAllRepairs() {
        List<Repair> repairs = repairService.getAllRepairs();
        return ResponseEntity.ok(repairs);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<Repair>> getCustomerRepairs(Principal principal) {
        List<Repair> repairs = repairService.getCustomersRepairOrders(principal.getName());
        return ResponseEntity.ok(repairs);
    }

    @GetMapping("/assigned")
    public ResponseEntity<Repair> getJobAssignedToEmployee(Principal principal) {
        Repair repair = repairService.getJobFromEmployee(principal.getName());
        return ResponseEntity.ok(repair);
    }

    @PutMapping("/assigned/{repairId}")
    public ResponseEntity<Repair> updateStatusForSpecificJob(@PathVariable UUID repairdId,
                                                             @Valid @RequestBody RepairUpdateRequest repairUpdateRequest,
                                                             Principal principal) {
        Repair repairToUpdate = repairService.updateRepairForMechanic(repairdId, repairUpdateRequest, principal.getName());
        return ResponseEntity.ok(repairToUpdate);
    }

    @PutMapping("/{repairId}")
    public ResponseEntity<Repair> assigneMechanicToJob(@PathVariable UUID repairId,
                                                       Principal principal) {
        Repair repairToAssign = repairService.assignMechanic(repairId, principal.getName());
        return ResponseEntity.ok(repairToAssign);
    }

    @PostMapping
    public ResponseEntity<Repair> createRepair(@Valid @RequestBody RepairRequest repairRequest, Principal principal) {
        Repair repair = repairService.createRepair(repairRequest, principal.getName());
        return ResponseEntity.ok(repair);
    }
}
