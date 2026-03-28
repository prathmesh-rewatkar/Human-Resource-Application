package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country , String> {

}
