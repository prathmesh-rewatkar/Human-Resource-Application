package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Job;
import com.example.HumanResourceApplication.projection.JobProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "jobs", excerptProjection = JobProjection.class)
public interface JobRepository extends JpaRepository<Job, String> {

    Optional<Job> findByJobTitleIgnoreCase(String jobTitle);

    List<Job> findByJobTitle(String jobTitle); 

    List<Job> findByJobTitleContainingIgnoreCase(String jobTitle);

    List<Job> findByMinSalaryGreaterThanEqual(BigDecimal minSalary);

    List<Job> findByMaxSalaryLessThanEqual(BigDecimal maxSalary);
    
    List<Job> findByMinSalaryBetween(BigDecimal min, BigDecimal max);


}


