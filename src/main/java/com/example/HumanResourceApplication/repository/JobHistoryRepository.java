package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.JobHistory;
import com.example.HumanResourceApplication.entity.JobHistoryId;
import com.example.HumanResourceApplication.projection.JobHistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "job_history", excerptProjection = JobHistoryProjection.class)
public interface JobHistoryRepository extends JpaRepository<JobHistory, JobHistoryId> {

}
