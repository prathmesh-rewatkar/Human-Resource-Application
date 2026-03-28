package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Department;
import com.example.HumanResourceApplication.projection.DepartmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "department",collectionResourceRel = "departments",
        excerptProjection = DepartmentProjection.class)
public interface DepartmentRepository extends JpaRepository<Department,Integer>
{
    Optional<Department> findByDepartmentNameIgnoreCase(String departmentName);

    // (Page 2) - Search/Filter by exact name
    List<Department> findByDepartmentName(@Param("name")String name);

    // (Page 2) - Search by name containing (partial search)
    List<Department> findByDepartmentNameContainingIgnoreCase(@Param("name")String name);

    // (Page 2) - Filter by location city
    List<Department> findByLocation_City(@Param("city") String city);

    // (Page 3) - Find department with its employees
    Optional<Department> findByDepartmentId(Integer id);

    // Exists and find
//    Optional<Department> findByDepartmentId(Double departmentId);

    // (Page 2) - Filter departments that have a manager assigned
    List<Department> findByManagerIsNotNull();

    // (Page 2) - Filter departments by location id
    List<Department> findByLocation_LocationId(Integer locationId);

    long count();
}
