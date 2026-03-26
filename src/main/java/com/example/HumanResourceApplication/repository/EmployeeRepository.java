package com.example.HumanResourceApplication.repository;


import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.ManagerIdProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;


import java.util.List;
import java.util.Optional;


//@RepositoryRestResource(path = "employees")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {



    Optional<Employee> findByEmployeeId(Long empId);
    List<ManagerProjection> findDistinctBySubordinatesIsNotEmpty();

    @RestResource(path = "by-email", rel = "managerByEmail")
    ManagerProjection findDistinctBySubordinatesIsNotEmptyAndEmail(String email);


}