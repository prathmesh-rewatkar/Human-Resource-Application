package com.example.HumanResourceApplication.projection;

import com.example.HumanResourceApplication.entity.Employee;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employeeView", types = Employee.class)
public interface EmployeeProjection {
    String getFirstName();
    String getLastName();
    String getEmail();
}