package com.example.HumanResourceApplication.projection;

import com.example.HumanResourceApplication.entity.Job;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

@Projection(name = "jobDetails", types = Job.class)
public interface JobProjection {
    String getJobId();
    String getJobTitle();
    BigDecimal getMinSalary();
    BigDecimal getMaxSalary();
}