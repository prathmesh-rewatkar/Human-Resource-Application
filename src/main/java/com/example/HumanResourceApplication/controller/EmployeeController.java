package com.example.HumanResourceApplication.controller;

import com.example.HumanResourceApplication.dto.UpdateManagerDTO;
import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.exception.ResourceNotFoundException;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;


    // @GetMapping("/managers")
    // public List<ManagerProjection> getAllManagers() {

    //     List<Long> ids = employeeRepository.findDistinctByManagerIdIsNotNull()
    //             .stream()
    //             .map(ManagerIdProjection::getManagerId)
    //             .toList();

    //     return employeeRepository.findByEmployeeIdIn(ids);
    // }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
}

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Double id) {
    return employeeRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
    @GetMapping("/managers")
    public List<ManagerProjection> getAllManagers() {
        List<ManagerProjection>managers= employeeRepository.findDistinctBySubordinatesIsNotEmpty();
        if (managers.isEmpty()) {
            throw new ResourceNotFoundException("No managers found");
        }
        return managers;
    }


    @GetMapping("/manager/by-email")
    public ManagerProjection getManagerByEmail(@RequestParam String email) {
        return employeeRepository
                .findDistinctBySubordinatesIsNotEmptyAndEmail(email);
    }

    @GetMapping("/manager/by-department")
    public List<ManagerProjection> getManagerByDepartment(@RequestParam String departmentName) {
        return employeeRepository
                .findDistinctBySubordinatesIsNotEmptyAndDepartment_DepartmentName(departmentName);
    }


    @PutMapping("/update-manager")
    public ResponseEntity<?> updateManager(@RequestBody UpdateManagerDTO dto) {


        Employee employee = employeeRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Employee newManager = employeeRepository.findByEmployeeId(dto.getNewManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("New Manager not found"));

        if (employee.getEmployeeId().equals(newManager.getEmployeeId())) {
            throw  new ResourceNotFoundException("Employee cannot be their own manager");
        }

        if (employeeRepository.getHierarchy(newManager.getEmployeeId())
                .stream()
                .anyMatch(e -> e.getEmployeeId().equals(employee.getEmployeeId()))) {
            throw new ResourceNotFoundException("Circular hierarchy detected");
        }


        employee.setManager(newManager);

        employeeRepository.save(employee);
        return ResponseEntity.ok("Manager updated successfully");
    }

}