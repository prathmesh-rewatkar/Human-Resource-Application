package com.example.HumanResourceApplication.repository;


import com.example.HumanResourceApplication.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//@RepositoryRestResource(path = "employees")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT DISTINCT e.managerId FROM Employee e WHERE e.managerId IS NOT NULL")
    List<Long> findDistinctManagerIds();

    Optional<Employee> findByEmployeeId(Long empId);

    default List<Employee> getManagers(List<Long> managerIds) {
        List<Employee> managers = new ArrayList<>();

        for (Long id : managerIds) {
            if (id != null) {
                findByEmployeeId(id).ifPresent(managers::add);
            }
        }

        return managers;
    }
}