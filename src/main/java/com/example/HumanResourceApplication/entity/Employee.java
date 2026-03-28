package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Employee {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id", columnDefinition = "DECIMAL(6,0)")
    private Double employeeId;



    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "hire_date")
    private LocalDate hireDate;

//    @Column(name = "job_id")
//    private String jobId;

    @Column(name = "salary", columnDefinition = "DECIMAL(8,2)")
    private Double salary;

    @Column(name = "commission_pct",columnDefinition = "DECIMAL(2,2)")
    private Double commissionPct;

//    @Column(name = "manager_id")
//    private Long managerId;
//
//    @Column(name = "department_id")
//    private Long departmentId;


    @ManyToOne
    @JoinColumn(name="manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager" )
    private List<Employee> subordinates;

    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;


    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;
}