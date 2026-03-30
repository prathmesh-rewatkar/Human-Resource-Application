package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.entity.Job;
import com.example.HumanResourceApplication.projection.EmployeeProjection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class JobRepositoryTest {

    @Autowired
    private JobRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Test
    void testFindAllJobs() {
        var list = repository.findAll();
        assertThat(list).isNotEmpty();
    }

    @Test
    void testFindByJobId() {
        Job job = repository.findById("IT_PROG").orElse(null);
        assertThat(job).isNotNull();
    }

    @Test
    void testFindByJobIdNotFound() {
        Job job = repository.findById("INVALID_ID").orElse(null);
        assertThat(job).isNull();
    }

    @Test
    void testFindByJobTitle() {
        var list = repository.findByJobTitle("Accountant");
        assertThat(list).isNotEmpty();
    }

    @Test
    void testFindByMinSalaryGreaterThanEqual() {
    var list = repository.findByMinSalaryGreaterThanEqual(new BigDecimal("4000"));
    assertThat(list).isNotEmpty();
}

@Test
void testFindByMaxSalaryLessThanEqual() {
    var list = repository.findByMaxSalaryLessThanEqual(new BigDecimal("10000"));
    assertThat(list).isNotEmpty();
}

@Test
void testFindBySalaryRange() {
    var list = repository.findByMinSalaryBetween(
        new BigDecimal("4000"),
        new BigDecimal("10000")
    );
    assertThat(list).isNotEmpty();
}

    @Test
    void testFindByJobTitleNotFound() {
        var list = repository.findByJobTitle("NonExistentJob");
        assertThat(list).isEmpty();
    }


//PAGE 3

@Test
    void testFindEmployeesByJobId() {

        List<EmployeeProjection> employees = employeeRepository.findEmployeesByJob_JobId("AC_ACCOUNT");

        assertThat(employees).isNotEmpty();

        EmployeeProjection employee = employees.get(0);

        assertThat(employee.getFirstName()).isNotNull();
        assertThat(employee.getLastName()).isNotNull();
        assertThat(employee.getEmail()).isNotNull();
        assertThat(employee.getJobTitle()).isNotNull();
        assertThat(employee.getDepartmentName()).isNotNull();
    }
}