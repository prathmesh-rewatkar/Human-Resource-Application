package com.example.HumanResourceApplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="departments",uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"department_name", "location_id"}  // combination must be unique
        )
})
public class Department
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "department_name",length= 30, nullable = false)
    @NotBlank(message = "Department name cannot be empty")
    @Size(max = 30, message = "Department name must be <= 30 characters")
    private String departmentName;

    //  ONE department → MANY employees
    @JsonIgnore
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employees;

    // ONE department → ONE manager (who is an Employee)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    // Many departments -> one location
    @ManyToOne
    @JoinColumn(name = "location_id",nullable = false)
    @NotNull(message = "Location is required")
    private Location location;

}
