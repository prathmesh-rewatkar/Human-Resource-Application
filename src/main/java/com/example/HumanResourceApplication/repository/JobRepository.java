package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "jobs")
public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByJobTitle(String jobTitle); 

    List<Job> findByJobTitleContainingIgnoreCase(String jobTitle);

    List<Job> findByMinSalaryGreaterThanEqual(Double minSalary);

    List<Job> findByMaxSalaryLessThanEqual(Double maxSalary);

    List<Job> findByMinSalaryBetween(Double min, Double max);

}

