package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(path = "jobs")
public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByJobTitle(String jobTitle); 

    List<Job> findByJobTitleContainingIgnoreCase(String jobTitle);

    List<Job> findByMinSalaryGreaterThanEqual(BigDecimal minSalary);
    List<Job> findByMaxSalaryLessThanEqual(BigDecimal maxSalary);
    List<Job> findByMinSalaryBetween(BigDecimal min, BigDecimal max);

}

