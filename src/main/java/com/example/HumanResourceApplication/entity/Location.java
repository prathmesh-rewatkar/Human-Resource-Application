package com.example.HumanResourceApplication.entity;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import java.util.List;

@Entity
@Table(name = "locations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locationId;


    @Column(name = "street_address", length = 40)
    private String streetAddress;

    @Column(name = "postal_code", length = 12)
    private String postalCode;

    @Column(name = "city",columnDefinition = "VARCHAR(30)")
    private String city;

    @Column(name = "state_province", length = 25)
    private String stateProvince;


    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "country_id")
    // private Country country ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country ;


    @OneToMany(mappedBy = "location" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<Department> departments ;

}