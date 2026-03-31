package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import javax.annotation.processing.Generated;
import java.util.List;

@Entity
@Table(name = "regions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Region {

    @Id
    @Column(name = "region_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@NotNull(message = "Region ID is required")
    private Integer regionId;

    @NotBlank(message = "Region name is required")
    @Size(max = 25, message = "Region name must not exceed 25 characters")
    @Column(name = "region_name", length = 25)
    private String regionName;

    @OneToMany(mappedBy = "region")
    private List<Country> countries;
}