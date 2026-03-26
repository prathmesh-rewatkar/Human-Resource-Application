package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = EmployeeProjection.class)
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}