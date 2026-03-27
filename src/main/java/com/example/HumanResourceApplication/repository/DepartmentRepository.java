package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "department")
public interface DepartmentRepository extends JpaRepository<Department, Double>
{
    List<Department> findByDepartmentName(String name);
    Optional<Department> findByDepartmentId(Double id);
    long count();
}
