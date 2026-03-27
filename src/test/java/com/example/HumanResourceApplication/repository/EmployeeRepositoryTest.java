package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("findAll - should return all persisted employees")
    void testFindAll_ReturnsAllEmployees() {

        List<Employee> employees = employeeRepository.findAll();

        assertThat(employees).isNotNull();
        assertThat(employees.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("findAll - validate employee fields")
    void testFindAll_ValidateEmployeeFields() {

        List<Employee> employees = employeeRepository.findAll();

        Employee employee = employees.get(0);

        assertThat(employee.getEmployeeId()).isNotNull();
        assertThat(employee.getFirstName()).isNotNull();
        assertThat(employee.getLastName()).isNotNull();
    }

    @Test
    @DisplayName("findAll - should return consistent size")
    void testFindAll_SizeConsistency() {

        List<Employee> employees1 = employeeRepository.findAll();
        List<Employee> employees2 = employeeRepository.findAll();

        assertThat(employees1.size()).isEqualTo(employees2.size());
    }

    @Test
    @DisplayName("findAll - employee IDs should be unique")
    void testFindAll_UniqueIds() {

        List<Employee> employees = employeeRepository.findAll();

        long uniqueCount = employees.stream()
                .map(Employee::getEmployeeId)
                .distinct()
                .count();

        assertThat(uniqueCount).isEqualTo(employees.size());
    }

    @Test
    @DisplayName("findAll - employees should have valid data")
    void testFindAll_ValidData() {

        List<Employee> employees = employeeRepository.findAll();

        employees.forEach(emp -> {
            assertThat(emp.getEmployeeId()).isNotNull();
            assertThat(emp.getFirstName()).isNotBlank();
            assertThat(emp.getLastName()).isNotBlank();
        });
    }
}