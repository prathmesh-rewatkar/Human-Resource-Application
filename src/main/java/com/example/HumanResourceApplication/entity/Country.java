package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity 
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @Column(name = "country_id",columnDefinition = "CHAR(4)")
    private String countryId;

    @Column(name = "country_name", length = 60)
    private String countryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="region_id")
    @NotNull(message = "Region is required")  //
    private Region region;

//    @OneToMany(mappedBy = "location" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
//    private List<Location> locations ;

    @OneToMany(mappedBy = "country")
    private List<Location> locations;
}