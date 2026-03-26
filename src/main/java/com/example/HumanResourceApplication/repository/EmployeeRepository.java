package com.example.HumanResourceApplication.repository;


import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.ManagerIdProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//@RepositoryRestResource(path = "employees")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(Long empId);


    // List<ManagerIdProjection> findDistinctByManagerIdIsNotNull();

    // Step 2: Return managers as PROJECTION
    // List<ManagerProjection> findByEmployeeIdIn(List<Long> managerIds);
}