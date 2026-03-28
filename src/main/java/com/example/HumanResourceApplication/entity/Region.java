
// package com.example.HumanResourceApplication.entity;

// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// import java.util.List;

// @Entity
// @Table(name="regions")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// public class Region {
//     @Id
//     @Column(name = "region_id" ,  nullable=false)
//     private Long regionId;

//     @Column(name = "region_name", length = 25)
//     private String regionName;
//     @OneToMany(mappedBy = "region" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
//     private List<Country> countries ;
// }

package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "regions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "region_id", columnDefinition = "DECIMAL(10,0)")
    private Double regionId;

    @Column(name = "region_name", length = 25)
    private String regionName;

//    @OneToMany(mappedBy = "region")
//    private List<Location> locations;
}

