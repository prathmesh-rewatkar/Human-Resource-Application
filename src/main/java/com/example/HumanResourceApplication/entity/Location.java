package com.example.HumanResourceApplication.entity;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "location_id" ,columnDefinition = "DECIMAL(4,0)")
    private Double locationId;

    @Column(name = "street_address", length = 40)
    private String streetAddress;

    @Column(name = "postal_code", length = 12)
    private String postalCode;

    @Column(name = "city", length = 30)
    private String city;

    @Column(name = "state_province", length = 25)
    private String stateProvince;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country ;

    @OneToMany(mappedBy = "location" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<Department> departments ;

}