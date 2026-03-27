package com.example.HumanResourceApplication.repository;


import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.EmployeeProjection;
import com.example.HumanResourceApplication.projection.ManagerIdProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//@RepositoryRestResource(path = "employees")
public interface EmployeeRepository extends JpaRepository<Employee, Double> {

    Optional<Employee> findByEmployeeId(Double empId);
    List<ManagerProjection> findDistinctBySubordinatesIsNotEmpty();

    //List<EmployeeProjection>findByManagerId(Long Id);

    List<EmployeeProjection> findByManager_EmployeeId(Double id);
    @RestResource(path = "by-email", rel = "managerByEmail")
    ManagerProjection findDistinctBySubordinatesIsNotEmptyAndEmail(String email);


    default public List<Employee> getHierarchy(Double id) {
        List<Employee> hierarchy = new ArrayList<>();

        Employee current = findByEmployeeId(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        while (current.getManager() != null) {
            current = current.getManager();
            hierarchy.add(current);
        }

        return hierarchy;
    }


    long countByManager_EmployeeId(Double managerId);

}