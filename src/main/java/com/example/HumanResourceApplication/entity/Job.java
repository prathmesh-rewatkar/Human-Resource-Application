package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @Column(name = "job_id",columnDefinition = "VARCHAR(10)")
    private String jobId;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;

    @OneToMany(mappedBy = "job")
    private List<Employee> employees;

    // @OneToMany(mappedBy = "job")
    // private List<JobHistory> jobHistories;
}