package com.example.HumanResourceApplication.test;


import com.example.HumanResourceApplication.entity.Job;
import com.example.HumanResourceApplication.repository.JobRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobRepositoryTest
{
    @Autowired
    private JobRepository repository;

    private Job job;

    @BeforeEach
    void setUp()
    {
        job = new Job();
        job.setJobTitle("Programmer"); 
        repository.save(job);
    }

    @Test
    void testFindAllJobs() {
        var list = repository.findAll();

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getJobTitle()).isEqualTo("Programmer");
    }

    @Test
    void testAddJob()
    {
        Job job = new Job();
        job.setJobTitle("HR");

        Job saved = repository.save(job);

        Job fetched = repository.findById(saved.getJobId()).orElse(null);

        assertThat(fetched).isNotNull();

        assertThat(saved).isNotNull();
        assertThat(saved.getJobId()).isNotNull(); // ✅ auto ID
        assertThat(saved.getJobTitle()).isEqualTo("HR");

        System.out.println(saved);
    }
}