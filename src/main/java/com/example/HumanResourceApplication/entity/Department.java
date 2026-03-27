package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="departments")
public class Department
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "department_id", columnDefinition = "DECIMAL(4,0)")
    private Double departmentId;

    @Column(name = "department_name",length= 30, nullable = false)
    private String departmentName;

    //  ONE department → MANY employees
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employees;

    // ONE department → ONE manager (who is an Employee)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    // Many departments -> one location
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

}
