package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "department")
public interface DepartmentRepository extends JpaRepository<Department,Long>
{
    List<Department> findByDepartmentName(String name);
}
