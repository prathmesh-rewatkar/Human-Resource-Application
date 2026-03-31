package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "countries")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Country {

    @Id
    @NotBlank(message = "Country ID is required")
    @Size(max = 4, message = "Country ID must not exceed 4 characters")
    @Column(name = "country_id", columnDefinition = "CHAR(4)")
    private String countryId;

    @NotBlank(message = "Country name is required")
    @Size(max = 60, message = "Country name must not exceed 60 characters")
    @Column(name = "country_name", length = 60)
    private String countryName;

    @NotNull(message = "Region is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "country")
    private List<Location> locations;
}