package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Region;
import com.example.HumanResourceApplication.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RegionRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private LocationRepository locationRepository ;

    // TEST 1: Find region by name → get its ID
    @Test
    public void testFindByRegionName_ReturnsCorrectId() {
        Region region = regionRepository.findByRegionName("Europe");

        assertThat(region).isNotNull();
        assertThat(region.getRegionId()).isEqualTo(10);
    }

    // TEST 2: Find region by name → region does not exist
    @Test
    public void testFindByRegionName_NotFound_ReturnsNull() {
        Region region = regionRepository.findByRegionName("Wakanda");

        assertThat(region).isNull();
    }

    // TEST 3: Find region by ID → verify name matches
    @Test
    public void testFindById_ReturnsCorrectRegionName() {
        Region region = regionRepository.findById(10).orElseThrow(()->new ResourceNotFoundException("Region not found"));

        assertThat(region).isNotNull();
        assertThat(region.getRegionName()).isEqualTo("Europe");
    }

    // TEST 4: Find region by non-existing ID → returns empty
    @Test
    public void testFindById_NotFound_ReturnsEmpty() {
        Region region = regionRepository.findById(9999).orElse(null);

        assertThat(region).isNull();
    }

    // TEST: Count locations per region
    @Test
    public void testCountLocationsByRegion() {
        long count = locationRepository.countByCountry_Region_RegionId(20); // Americas
        assertThat(count).isEqualTo(2); // Americas has 5 locations in Oracle HR
    }
}