package com.example.HumanResourceApplication.controller;

import com.example.HumanResourceApplication.projection.ManagerIdProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/managers")
    public List<ManagerProjection> getAllManagers() {

        List<Long> ids = employeeRepository.findDistinctByManagerIdIsNotNull()
                .stream()
                .map(ManagerIdProjection::getManagerId)
                .toList();

        return employeeRepository.findByEmployeeIdIn(ids);
    }
}