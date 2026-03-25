package com.example.HumanResourceApplication.entity;
//import com.example.HumanResourceApplication.entity.Location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Many Departments -> one manager (employee)
//    @ManyToOne


    // Many departments -> one location
//    @ManyToOne
//    @JoinColumn(name = "location_id")
//    private Location location;
}
