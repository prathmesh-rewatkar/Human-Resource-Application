package com.example.HumanResourceApplication.controller;

import com.example.HumanResourceApplication.dto.UpdateManagerDTO;
import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.exception.ResourceNotFoundException;
import com.example.HumanResourceApplication.projection.EmployeeRecordProjection;
import com.example.HumanResourceApplication.projection.ManagerIdProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import com.example.HumanResourceApplication.repository.DepartmentRepository;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import com.example.HumanResourceApplication.repository.JobHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;



@RestController
@RequestMapping("/managers")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @GetMapping("/")
    public List<ManagerProjection> getAllManagers( @PageableDefault(size = 5, sort = "employeeId")Pageable pageable) {
        List<ManagerProjection>managers= employeeRepository.findDistinctBySubordinatesIsNotEmpty(pageable);
        if (managers.isEmpty()) {
            throw new ResourceNotFoundException("No managers found");
        }
        return managers;
    }

    @GetMapping("/{id}/subordinates")
    public List<EmployeeRecordProjection> getSubordinates(@PathVariable Integer id) {

        if (!employeeRepository.existsById(id)){
            throw new ResourceNotFoundException("Manager not found");
        }

        return employeeRepository.findByManager_EmployeeId(id);
    }


    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteManager(@PathVariable Integer id){
        Employee e=employeeRepository.findByEmployeeId(id)
                .orElseThrow(()->new ResourceNotFoundException("Employee Not Found to be deleted"));

        Employee manager=e.getManager();
        System.out.println(manager.getEmployeeId());
        List<Employee>subordinates=e.getSubordinates();
        System.out.println(subordinates);
        System.out.println(manager.getClass());
        if(subordinates != null && !subordinates.isEmpty()){
            for(Employee emp:subordinates){
                emp.setManager(manager);
                //employeeRepository.save(emp);

            }
            e.getSubordinates().clear();
            //employeeRepository.saveAll(subordinates);
            //employeeRepository.flush();

            employeeRepository.flush();
        }
        jobHistoryRepository.deleteByEmployeeEmployeeId(e.getEmployeeId());
        employeeRepository.delete(e);
        return ResponseEntity.ok("Manager deleted and subordinates reassigned");
    }


    @GetMapping("/by-email")
    public ManagerProjection getManagerByEmail(@RequestParam String email) {
        return employeeRepository
                .findDistinctBySubordinatesIsNotEmptyAndEmail(email);
    }

    @GetMapping("/by-department")
    public List<ManagerProjection> getManagerByDepartment(@RequestParam Integer departmentId) {

        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department not found");
        }
        return employeeRepository
                .findDistinctBySubordinatesIsNotEmptyAndDepartment_DepartmentId(departmentId);
    }


    @GetMapping("/by-firstname")
    public List<ManagerProjection> getManagersByFirstName(@RequestParam String firstname){
        return employeeRepository.findDistinctBySubordinatesIsNotEmptyAndFirstNameContainingIgnoreCase(firstname);
    }

    @GetMapping("/by-lastname")
    public List<ManagerProjection> getManagersByLastName(@RequestParam String lastname){
        return employeeRepository.findDistinctBySubordinatesIsNotEmptyAndLastNameContainingIgnoreCase(lastname);
    }



    @PatchMapping("/update-manager")
    public ResponseEntity<?> updateManager(@RequestBody UpdateManagerDTO dto) {

        Employee employee = employeeRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Employee newManager = employeeRepository.findByEmployeeId(dto.getNewManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("New Manager not found"));

        if (employee.getEmployeeId().equals(newManager.getEmployeeId())) {
            throw  new IllegalArgumentException("Employee cannot be their own manager");
        }

        if (employeeRepository.getHierarchy(newManager.getEmployeeId())
                .stream()
                .anyMatch(e -> e.getEmployeeId().equals(employee.getEmployeeId()))) {
            throw new IllegalArgumentException("Circular hierarchy detected");
        }


        employee.setManager(newManager);

        employeeRepository.save(employee);
        return ResponseEntity.ok("Manager updated successfully");
    }


    @GetMapping("/{id}/hierarchy")
    public List<Employee> getHierarchy(@PathVariable Integer id) {

        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found");
        }

        return employeeRepository.getHierarchy(id);
    }


}