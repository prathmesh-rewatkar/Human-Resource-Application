package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ AUTO INCREMENT
    private Long jobId;

    private String jobTitle;
}