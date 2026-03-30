package com.example.HumanResourceApplication.handler;

import com.example.HumanResourceApplication.entity.*;
import com.example.HumanResourceApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Employee.class)
public class EmployeeEventHandler {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @HandleBeforeCreate
    @HandleBeforeSave
    public void handle(Employee employee) {

        if (employee.getJobTitle() != null) {
            Job job = jobRepository.findByJobTitleIgnoreCase(employee.getJobTitle())
                    .orElseThrow(() -> new RuntimeException("Invalid job title"));

            employee.setJob(job);
        }

        if (employee.getDepartmentName() != null) {
            Department dept = departmentRepository
                    .findByDepartmentNameIgnoreCase(employee.getDepartmentName())
                    .orElseThrow(() -> new RuntimeException("Invalid department"));

            employee.setDepartment(dept);
        }

        if (employee.getManagerId() != null) {

            Employee manager = employeeRepository.findById(employee.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));

            employee.setManager(manager);
        }
    }
}