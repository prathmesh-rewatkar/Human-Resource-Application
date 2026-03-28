package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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




//     @Test
//     void testFindEmployeesByJobTitle()
//     {
//     var list = repository.findJobWithEmployeesByJobTitle("Programmer");
    
//     assertThat(list).isNotEmpty();
    
//     // Check job details
//     Job job = list.get(0);
//     assertThat(job.getJobTitle()).isEqualTo("Programmer");
//     assertThat(job.getJobId()).isEqualTo("IT_PROG");
    
//     // Check employees list is not empty
//     assertThat(job.getEmployees()).isNotNull();
//     assertThat(job.getEmployees()).isNotEmpty();
    
//     // Print job and its employees
//     System.out.println("Job: " + job.getJobTitle());
//     System.out.println("Employees in this job:");
//     job.getEmployees().forEach(emp -> 
//         System.out.println(" - " + emp.getFirstName() + " " + emp.getLastName())
//     );
// }
}