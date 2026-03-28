package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @Column(name = "job_id", length = 10)
    private String jobId;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "min_salary", precision = 6, scale = 0)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 6, scale = 0)
    private BigDecimal maxSalary;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<Employee> employees;
}