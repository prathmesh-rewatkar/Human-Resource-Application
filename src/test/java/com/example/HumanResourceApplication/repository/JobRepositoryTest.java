package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class JobRepositoryTest {

    @Autowired
    private JobRepository repository;

    
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
        var list = repository.findByJobTitle("Programmer");
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
void testFindEmployeesByJobTitle() {

    var list = repository.findJobWithEmployeesByJobTitle("Programmer");

    assertThat(list).isNotEmpty();

    Job job = list.get(0);

    // print job
    System.out.println("Job: " + job.getJobTitle());

    // print count
    System.out.println("Total Employees: " + job.getEmployees().size());

    // print employees (FIRST + LAST name + email)
    job.getEmployees().forEach(emp ->
        System.out.println(
                emp.getFirstName() + " " +
                emp.getLastName() + " - " +
                emp.getEmail()
        )
);

    // assertion
    assertThat(job.getEmployees()).isNotEmpty();

    assertThat(job.getEmployees()).allMatch(emp -> emp.getEmail() != null);
}
}