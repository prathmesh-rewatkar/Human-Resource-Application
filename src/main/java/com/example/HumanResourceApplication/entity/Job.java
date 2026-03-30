package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

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
    @NotBlank(message = "Job ID is required")
    @Column(name = "job_id", length = 10)
    private String jobId;

    @NotBlank(message = "Job title is required")
    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "min_salary", precision = 6, scale = 0)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 6, scale = 0)
    private BigDecimal maxSalary;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    // @RestResource(exported = false)
    private List<Employee> employees;
}