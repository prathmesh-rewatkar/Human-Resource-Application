package com.example.HumanResourceApplication.handler;

import com.example.HumanResourceApplication.entity.*;
import com.example.HumanResourceApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RepositoryEventHandler(Employee.class)
public class EmployeeEventHandler {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @HandleBeforeCreate
    @HandleBeforeSave
    public void handle(Employee employee) {

        if (employee.getJobTitle() != null) {
            Job job = jobRepository.findByJobTitleIgnoreCase(employee.getJobTitle())
                    .orElseThrow(() -> new RuntimeException("Invalid job title"));

            employee.setJob(job);
        }

        if (employee.getDepartmentName() != null) {
            Department dept = departmentRepository
                    .findByDepartmentNameIgnoreCase(employee.getDepartmentName())
                    .orElseThrow(() -> new RuntimeException("Invalid department"));

            employee.setDepartment(dept);
        }

        if (employee.getManagerId() != null) {

            Employee manager = employeeRepository.findById(employee.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));

            employee.setManager(manager);
        }


        if (employee.getEmployeeId() != null) {

            Employee existing = employeeRepository.findByEmployeeId(employee.getEmployeeId())
                    .orElseThrow();

            boolean deptChanged = existing.getDepartment() != null &&
                    employee.getDepartment() != null &&
                    !existing.getDepartment().getDepartmentId()
                            .equals(employee.getDepartment().getDepartmentId());

            boolean jobChanged = existing.getJob() != null &&
                    employee.getJob() != null &&
                    !existing.getJob().getJobId()
                            .equals(employee.getJob().getJobId());

            //  ONLY ONE HISTORY ENTRY
            if (deptChanged || jobChanged) {

                JobHistory history = new JobHistory();

                JobHistoryId id = new JobHistoryId();
                id.setEmployeeId(existing.getEmployeeId());
                id.setStartDate(LocalDate.now()); // ️ must be unique

                history.setId(id);
                history.setEndDate(LocalDate.now());

                //  store OLD values (before update)
                history.setJob(existing.getJob());
                history.setDepartment(existing.getDepartment());

                history.setEmployee(existing);

                jobHistoryRepository.save(history);
            }
        }
    }
}