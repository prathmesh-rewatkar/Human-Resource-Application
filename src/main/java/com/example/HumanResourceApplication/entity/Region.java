package com.example.HumanResourceApplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "regions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @Column(name = "region_id")
    private Integer regionId;

    @Column(name = "region_name", length = 25)
    private String regionName;
    @OneToMany(mappedBy = "region")
    private List<Country> countries ;

}