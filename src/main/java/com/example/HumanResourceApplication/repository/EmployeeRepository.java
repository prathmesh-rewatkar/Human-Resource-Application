package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.EmployeeProjection;
import com.example.HumanResourceApplication.projection.EmployeeRecordProjection;
import com.example.HumanResourceApplication.projection.ManagerIdProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(
        path = "employees",
        excerptProjection = EmployeeProjection.class
)
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    @RestResource(path = "search-by-id", rel = "searchById")
    Optional<Employee> findByEmployeeId(Integer empId);

    @RestResource(path = "search-by-name", rel = "searchByName")
    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);

    @RestResource(path = "by-job-title", rel = "byJobTitle")
    Page<Employee> findByJob_JobTitle(
            String jobTitle, Pageable pageable);

    @RestResource(path = "by-department-name", rel = "byDepartmentName")
    Page<Employee> findByDepartment_DepartmentName(
            String departmentName, Pageable pageable);

    @RestResource(path = "by-name-and-dept", rel = "byNameAndDept")
    Page<Employee> findByDepartment_DepartmentNameAndFirstNameContainingIgnoreCaseOrDepartment_DepartmentNameAndLastNameContainingIgnoreCase(
            String dept1, String firstName, String dept2, String lastName, Pageable pageable);

    @RestResource(path = "by-name-and-job", rel = "byNameAndJob")
    Page<Employee> findByJob_JobTitleAndFirstNameContainingIgnoreCaseOrJob_JobTitleAndLastNameContainingIgnoreCase(
            String job1, String firstName, String job2, String lastName, Pageable pageable);

    @RestResource(path = "by-dept-and-job", rel = "byDeptAndJob")
    Page<Employee> findByDepartment_DepartmentNameAndJob_JobTitle(
            String departmentName, String jobTitle, Pageable pageable);

    @RestResource(path = "by-name-dept-and-job", rel = "byNameDeptAndJob")
    Page<Employee> findByDepartment_DepartmentNameAndJob_JobTitleAndFirstNameContainingIgnoreCaseOrDepartment_DepartmentNameAndJob_JobTitleAndLastNameContainingIgnoreCase(
            String dept1, String job1, String firstName, String dept2, String job2, String lastName, Pageable pageable);


    List<EmployeeProjection> findEmployeesByJob_JobId(String jobId);

    List<ManagerProjection> findDistinctBySubordinatesIsNotEmpty(Pageable pageable);

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
    List<ManagerIdProjection> findDistinctBySubordinatesIsNotEmptyAndDepartment_DepartmentId(Integer departmentId);

    long countByManager_EmployeeId(Integer managerId);

    List<ManagerProjection> findByFirstNameContainingIgnoreCase(
            String firstName
    );

    List<ManagerProjection> findByLastNameContainingIgnoreCase(
            String LastName
    );
}