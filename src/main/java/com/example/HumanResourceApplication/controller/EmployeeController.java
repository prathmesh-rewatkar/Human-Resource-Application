package com.example.HumanResourceApplication.controller;


import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeRepository repo;

    public EmployeeController(EmployeeRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/listallManagerDetails")
    public List<Employee> getAllManagers() {

        List<Long> ids = repo.findDistinctManagerIds();
        return repo.getManagers(ids);
    }
}