package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY) 
private List<Employee> employees = new ArrayList<>();

    // @OneToMany(mappedBy = "job") 
    // // private List<JobHistory> jobHistories;

    //Custom constructor
    public Job(String jobId, String jobTitle, Double minSalary, Double maxSalary) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }
}