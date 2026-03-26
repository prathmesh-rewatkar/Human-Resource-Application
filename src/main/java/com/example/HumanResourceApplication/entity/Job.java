package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs")   
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @Column(name = "job_id")
    private String jobId;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;
}