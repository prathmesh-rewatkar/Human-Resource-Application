// package com.example.HumanResourceApplication.entity;

// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// import java.util.List;

// @Entity
// @Table(name = "countries")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// public class Country {

//     @Id
//     @Column(name = "country_id", length = 4 , nullable = false)
//     private String countryId;

//     @Column(name = "country_name", length = 60)
//     private String countryName;
//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name="region_id")
//     private Region region;
//     // @OneToMany(mappedBy = "location" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
//     // private List<Location> locations ;
//     @OneToMany(mappedBy= "country")
//     private List<Location> locations ;

// }