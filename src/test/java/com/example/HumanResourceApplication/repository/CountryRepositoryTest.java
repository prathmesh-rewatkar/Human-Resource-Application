package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Country;
import com.example.HumanResourceApplication.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private LocationRepository locationRepository ;



    // TEST 1: Find country by name → get its ID
    @Test
    public void testFindByCountryName_ReturnsCorrectId() {
        Country country = countryRepository.findByCountryName("India");

        assertThat(country).isNotNull();
        assertThat(country.getCountryId()).isEqualTo("IN");
    }

    // TEST 2: Find country by name → country does not exist
    @Test
    public void testFindByCountryName_NotFound_ReturnsNull() {
        Country country = countryRepository.findByCountryName("Wakanda");

        assertThat(country).isNull();
    }

    // TEST 3: Find country by ID → verify name matches
    @Test
    public void testFindById_ReturnsCorrectCountryName() {
        Country country = countryRepository.findById("IN").orElseThrow(()-> new ResourceNotFoundException("Country id invalid "));

        assertThat(country).isNotNull();
        assertThat(country.getCountryName()).isEqualTo("India");
    }

    // TEST 4: Find country by non-existing ID → returns empty
    @Test
    public void testFindById_NotFound_ReturnsEmpty() {
        Country country = countryRepository.findById("ZZ").orElse(null);

        assertThat(country).isNull();
    }
    // TEST: Count locations per country
    @Test
    public void testCountLocationsByCountry() {
        long count = locationRepository.countByCountry_CountryId("US");
        assertThat(count).isEqualTo(4); // US has 4 locations in Oracle HR
    }
}