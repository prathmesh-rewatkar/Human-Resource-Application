package com.example.HumanResourceApplication.projection;

import com.example.HumanResourceApplication.entity.JobHistory;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Projection(name = "jobHistoryView", types = JobHistory.class)
public interface JobHistoryProjection {

    @Value("#{target.employee.firstName + ' ' + target.employee.lastName}")
    String getEmployeeName();

    @Value("#{target.id.startDate}")
    LocalDate getStartDate();

    LocalDate getEndDate();

    @Value("#{target.job.jobTitle}")
    String getJobTitle();

    @Value("#{target.department.departmentName}")
    String getDepartmentName();
}