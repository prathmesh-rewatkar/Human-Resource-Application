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
}