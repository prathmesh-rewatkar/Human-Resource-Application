package com.example.HumanResourceApplication.projection;

import com.example.HumanResourceApplication.entity.Department;
import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.entity.Job;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;

@Projection(name = "employeeView", types = Employee.class)
public interface EmployeeProjection {
    Integer getEmployeeId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getPhoneNumber();
    LocalDate getHireDate();
    Double getSalary();
    Department getDepartment();
    Job getJob();
}