package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Department;
import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.entity.Location;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class DepartmentRepositoryTest
{
    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LocationRepository locationRepository;

    private Department dept;
    private Employee manager;
    private Location loc;

//    @BeforeEach
//    void setUp()
//    {
//        loc = new Location();
//        loc.setCity("Nagpur");
//        locationRepository.save(loc);
//
//        manager = new Employee();
//        manager.setFirstName("John");
//        employeeRepository.save(manager);
//
//        dept = new Department();
//        dept.setDepartmentName("Finance");
//        dept.setManager(manager);
//        dept.setLocation(loc);
//
//        repository.save(dept);
//    }

    // Helper method to create unique manager
//    private Employee createManager(String name) {
//        Employee m = new Employee();
//        m.setFirstName(name);
//        return employeeRepository.save(m);
//    }

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
//        Employee manager = createManager("John");
//        dept.setManager(manager);

//        Department dept = new Department();
//        dept.setDepartmentName("HR");
//        dept.setManager(manager);
//        dept.setLocation(loc);

//        repository.save(dept);

        Department found = repository.findByDepartmentId((double)20).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getDepartmentName()).isEqualTo("Marketing");
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

    // Search Department By Name
    @Test
    void testFindByName()
    {
//        Employee manager = createManager("John");
//
//        Department dept = new Department();
//        dept.setDepartmentName("HR");
//        dept.setManager(manager);
//        dept.setLocation(loc);
//
//        repository.save(dept);

        List<Department> result = repository.findByDepartmentName("Purchasing");

        assertThat(result).isNotEmpty();
    }

    // Count Departments
    @Test
    void testCountDepartments()
    {
//        Employee manager1 = createManager("John");
//
//        Department d1 = new Department();
//        d1.setDepartmentName("HR");
//        d1.setManager(manager1);
//        d1.setLocation(loc);
//
//        Employee manager2 = createManager("Bob");
//        Department d2 = new Department();
//        d2.setDepartmentName("Accounts");
//        d2.setManager(manager2);
//        d2.setLocation(loc);
//
//        repository.save(d1);
//        repository.save(d2);

        long count = repository.count();

        assertThat(count).isEqualTo(27);
    }

    // Exists By ID
    @Test
    void testExistsById()
    {
//        Employee manager = createManager("John");
//
//        Department dept = new Department();
//        dept.setDepartmentName("HR");
//        dept.setManager(manager);
//        dept.setLocation(loc);
//
//        Department saved = repository.save(dept);

        boolean exists = repository.existsById((double)240);

        assertThat(exists).isTrue();
    }

}
