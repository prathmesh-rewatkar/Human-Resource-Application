package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region  , Integer> {
    Region findByRegionName(String europe);
}
