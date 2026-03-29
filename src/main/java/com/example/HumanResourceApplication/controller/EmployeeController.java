package com.example.HumanResourceApplication.controller;

import com.example.HumanResourceApplication.dto.UpdateManagerDTO;
import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.exception.ResourceNotFoundException;
import com.example.HumanResourceApplication.projection.EmployeeRecordProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/managers")
    public List<ManagerProjection> getAllManagers() {
        List<ManagerProjection>managers= employeeRepository.findDistinctBySubordinatesIsNotEmpty();
        if (managers.isEmpty()) {
            throw new ResourceNotFoundException("No managers found");
        }
        return managers;
    }

    @GetMapping("/managers/{id}/subordinates")
    public List<EmployeeRecordProjection> getSubordinates(@PathVariable Integer id) {

        if (!employeeRepository.existsById(id)){
            throw new ResourceNotFoundException("Manager not found");
        }

        return employeeRepository.findByManager_EmployeeId(id);
    }

    @DeleteMapping("/deleteManager/{id}")
    public ResponseEntity<?>deleteManager(@PathVariable Integer id){
        Employee e=employeeRepository.findByEmployeeId(id)
                .orElseThrow(()->new ResourceNotFoundException("Employee Not Found to be deleted"));

        Employee manager=e.getManager();
        List<Employee>subordinates=e.getSubordinates();
        if(!subordinates.isEmpty()){
            for(Employee emp:subordinates){
                emp.setManager(manager);
            }
        }
        employeeRepository.delete(e);
        return ResponseEntity.ok("Manager deleted and subordinates reassigned");
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