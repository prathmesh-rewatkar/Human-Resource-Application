package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.EmployeeProjection;
import com.example.HumanResourceApplication.projection.EmployeeRecordProjection;
import com.example.HumanResourceApplication.projection.ManagerIdProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(
        path = "employees",
        excerptProjection = EmployeeProjection.class
)
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmployeeId(Integer empId);

    List<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    List<Employee> findByJob_JobTitle(String jobTitle);

    List<Employee> findByDepartment_DepartmentName(String departmentName);

    List<ManagerProjection> findDistinctBySubordinatesIsNotEmpty();

    //List<EmployeeProjection>findByManagerId(Long Id);

    List<EmployeeRecordProjection> findByManager_EmployeeId(Integer id);

    @RestResource(path = "by-email", rel = "managerByEmail")
    ManagerProjection findDistinctBySubordinatesIsNotEmptyAndEmail(String email);

    default List<Employee> getHierarchy(Integer id) {
        List<Employee> hierarchy = new ArrayList<>();

        Employee current = findByEmployeeId(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        while (current.getManager() != null) {
            current = current.getManager();
            hierarchy.add(current);
        }

        return hierarchy;
    }

    @RestResource(path = "by-department", rel = "managersByDepartment")
    List<ManagerProjection> findDistinctBySubordinatesIsNotEmptyAndDepartment_DepartmentName(String departmentName);

    long countByManager_EmployeeId(Integer managerId);
}