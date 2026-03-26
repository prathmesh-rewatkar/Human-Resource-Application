package com.example.HumanResourceApplication.projection;

import com.example.HumanResourceApplication.entity.Job;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name = "jobView", types = Job.class)
public interface JobProjection {

    String getJobTitle();
    Double getMinSalary();
    Double getMaxSalary();

    List<EmployeeInfo> getEmployees();

    interface EmployeeInfo {
        String getFirstName();
        String getLastName();
        Double getSalary();
    }
}