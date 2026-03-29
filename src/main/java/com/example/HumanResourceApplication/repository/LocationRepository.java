package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Location;
import com.example.HumanResourceApplication.projection.LocationDetailProjection;
import com.example.HumanResourceApplication.projection.LocationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@RepositoryRestResource(
        collectionResourceRel = "locations",
        path = "locations",
        excerptProjection = LocationDetailProjection.class  // add this
)
public interface LocationRepository extends JpaRepository<Location,Integer> {

}
