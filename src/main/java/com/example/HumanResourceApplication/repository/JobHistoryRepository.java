package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.JobHistory;
import com.example.HumanResourceApplication.entity.JobHistoryId;
import com.example.HumanResourceApplication.projection.JobHistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource(path = "job_history", excerptProjection = JobHistoryProjection.class)
public interface JobHistoryRepository extends JpaRepository<JobHistory, JobHistoryId> {

    @RestResource(path = "by-employee", rel = "jobHistoryByEmployee")
    List<JobHistory> findByIdEmployeeId(@Param("employeeId") Integer employeeId);

    @Transactional
    void deleteByEmployeeEmployeeId(Integer employeeId);
}
