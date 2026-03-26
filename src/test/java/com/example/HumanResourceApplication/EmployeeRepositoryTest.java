package com.example.HumanResourceApplication;

import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testFindByEmployeeId() {

        Optional<Employee> result = employeeRepository.findByEmployeeId(100L);

        assertTrue(result.isPresent());
        System.out.println(result.get().getFirstName());
    }


    @Test
    void testGetManagers() {


        List<ManagerProjection> managers = employeeRepository.findDistinctBySubordinatesIsNotEmpty();

        assertNotNull(managers);
        assertTrue(managers.size() > 0);
        managers.forEach(m -> System.out.println(m.getFirstName()));
    }





    @Test
    void testFindManagerByEmail(){
        var result = employeeRepository
                .findDistinctBySubordinatesIsNotEmptyAndEmail("SHIGGINS");

        // ✅ Assertions
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("SHIGGINS");
    }
}