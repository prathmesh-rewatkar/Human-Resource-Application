package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Department;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class DepartmentRepositoryTest
{
    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LocationRepository locationRepository;

    // Get All Departments
    @Test
    void testFindAllDepartments() {
        List<Department> list = repository.findAll();
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getDepartmentName()).isEqualTo("Administration");
    }

    // Get Department By ID
    @Test
    void testFindDepartmentById()
    {
        Department found = repository.findById(20).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getDepartmentName()).isEqualTo("Marketing");
    }

    // Search Department By Name
    @Test
    void testFindByName()
    {
        List<Department> result = repository.findByDepartmentName("Purchasing");
        assertThat(result).isNotEmpty();
    }

    // Count Departments
    @Test
    void testCountDepartments()
    {
        long count = repository.count();
        assertThat(count).isEqualTo(27);
    }

    // Exists By ID
    @Test
    void testExistsById()
    {
        boolean exists = repository.existsById(240);
        assertThat(exists).isTrue();
    }

    // Partial name search filter
    @Test
    void testFindByDepartmentNameContaining()
    {
        List<Department> result = repository.findByDepartmentNameContainingIgnoreCase("IT");
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getDepartmentName()).containsIgnoringCase("IT");
    }

    // Filter by location city
    @Test
    void testFindByLocationCity()
    {
        List<Department> result = repository.findByLocation_City("Seattle");
        assertThat(result).isNotEmpty();
        result.forEach(d ->
                assertThat(d.getLocation().getCity()).isEqualTo("Seattle"));
    }

    // Filter departments that have a manager
    @Test
    void testFindDepartmentsWithManager() {
        List<Department> result = repository.findByManagerIsNotNull();
        assertThat(result).isNotEmpty();
        result.forEach(d -> assertThat(d.getManager()).isNotNull());
    }

    // Filter by location ID
    @Test
    void testFindByLocationId() {
        List<Department> result = repository.findByLocation_LocationId(1700);
        assertThat(result).isNotEmpty();
        result.forEach(d ->
                assertThat(d.getLocation().getLocationId()).isEqualTo(1700)
        );
    }

    // Department name should not be null or empty
    @Test
    void testDepartmentNameNotNull() {
        List<Department> list = repository.findAll();
        list.forEach(d -> assertThat(d.getDepartmentName()).isNotBlank());
    }

    // ─── NEW: PAGE 3 TESTS (Employees in a Department) ──────────────

    // Fetch department WITH employees (Page 3 core query)
    @Test
    void testFindDepartmentWithEmployees() {
        Optional<Department> result = repository.findById(50);
        assertThat(result).isPresent();
        assertThat(result.get().getEmployees()).isNotEmpty();
    }

    // Employees in a department belong to that department
    @Test
    void testEmployeesBelongToCorrectDepartment() {
        Optional<Department> result = repository.findById(60);
        assertThat(result).isPresent();
        result.get().getEmployees().forEach(emp ->
                assertThat(emp.getDepartment().getDepartmentId()).isEqualTo(60)
        );
    }

    // Department with no employees still loads (e.g. Treasury = 120)
    @Test
    void testDepartmentWithNoEmployees() {
        Optional<Department> result = repository.findById(120);
        assertThat(result).isPresent();
        assertThat(result.get().getEmployees()).isEmpty();
    }

    // Manager info accessible from department (for Page 3 header)
    @Test
    void testDepartmentManagerInfo() {
        Department dept = repository.findById(90).orElse(null);
        assertThat(dept).isNotNull();
        assertThat(dept.getManager()).isNotNull();
        assertThat(dept.getManager().getFirstName()).isNotBlank();
    }

    // Location info accessible from department (for Page 3 header)
    @Test
    void testDepartmentLocationInfo() {
        Department dept = repository.findById(20).orElse(null);
        assertThat(dept).isNotNull();
        assertThat(dept.getLocation()).isNotNull();
        assertThat(dept.getLocation().getCity()).isNotBlank();
    }

    // Exception: Department not found
    @Test
    void testFindDepartmentById_NotFound() {
        Optional<Department> result = repository.findById(9999);
        assertThat(result).isEmpty();
    }


    // Add Department
//    @Test
//    void testAddDepartment()
//    {
//        Employee manager = createManager("John");
//        dept.setManager(manager);
//
//        Department dept = new Department();
//        dept.setDepartmentName("HR");
//        dept.setManager(manager);
//        dept.setLocation(loc);
//
//        Department saved = repository.save(dept);
//
//        assertThat(saved.getDepartmentId()).isNotNull();
//        assertThat(saved.getManager()).isNotNull();
//    }


}