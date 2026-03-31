package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "locations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "location_id")
    private Integer locationId;

    @Size(max = 40, message = "Street address must not exceed 40 characters")
    @Column(name = "street_address", length = 40)
    private String streetAddress;           // optional — not @NotBlank

    @Size(max = 12, message = "Postal code must not exceed 12 characters")
    @Column(name = "postal_code", length = 12)
    private String postalCode;             // optional — not @NotBlank

    @NotBlank(message = "City is required")
    @Size(max = 30, message = "City must not exceed 30 characters")
    @Column(name = "city", columnDefinition = "VARCHAR(30)")
    private String city;

    @Size(max = 25, message = "State/province must not exceed 25 characters")
    @Column(name = "state_province", length = 25)
    private String stateProvince;          // optional — not @NotBlank

    @NotNull(message = "Country is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Department> departments;
}