package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobRepositoryTest
{
    @Autowired
    private JobRepository repository;

    @BeforeEach
    void setUp()
    {
        repository.deleteAll();

        repository.save(new Job("IT_PROG", "Programmer", 3000.0, 10000.0));
        repository.save(new Job("HR_REP", "HR Representative", 2000.0, 8000.0));
        repository.save(new Job("FIN_ACC", "Accountant", 4000.0, 15000.0));
    }

    @Test
    void testFindAllJobs()
    {
        var list = repository.findAll();
        assertThat(list).hasSize(3);
    }

    @Test
    void testFindByJobId()
    {
        Job found = repository.findById("IT_PROG").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getJobTitle()).isEqualTo("Programmer");
    }

    @Test
    void testFindByJobTitle()
    {
        var list = repository.findByJobTitle("Programmer");
        assertThat(list).isNotEmpty();
    }

    @Test
    void testFindByJobTitleContaining()
    {
        var list = repository.findByJobTitleContainingIgnoreCase("prog");
        assertThat(list).isNotEmpty();
    }

    @Test
    void testFindByMinSalaryGreaterThanEqual()
    {
        var list = repository.findByMinSalaryGreaterThanEqual(3000.0);
        assertThat(list).isNotEmpty();
    }

    @Test
    void testFindByMaxSalaryLessThanEqual()
    {
        var list = repository.findByMaxSalaryLessThanEqual(15000.0);
        assertThat(list).isNotEmpty();
    }

    @Test
    void testFindBySalaryRange()
    {
        var list = repository.findByMinSalaryBetween(2000.0, 8000.0);
        assertThat(list).isNotEmpty();
    }

    @Test
    void testFindByJobIdNotFound()
    {
        Job found = repository.findById("INVALID_ID").orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void testFindByJobTitleNotFound()
    {
        var list = repository.findByJobTitle("NonExistentJob");
        assertThat(list).isEmpty();
    }

    @Test
    void testFindAllWhenEmpty()
    {
        repository.deleteAll();
        var list = repository.findAll();
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