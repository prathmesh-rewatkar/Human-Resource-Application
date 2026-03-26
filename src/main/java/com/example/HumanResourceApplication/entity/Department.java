package com.example.HumanResourceApplication.entity;
//import com.example.HumanResourceApplication.entity.Location;

import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.entity.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.Manager;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="departments")
public class Department
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name")
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