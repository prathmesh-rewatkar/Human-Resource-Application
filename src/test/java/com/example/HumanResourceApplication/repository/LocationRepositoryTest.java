package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Country;
import com.example.HumanResourceApplication.entity.Location;
import com.example.HumanResourceApplication.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    public  CountryRepository countryRepository ;

    @Test
    public void testFindByLocationId_Exists() {
        Optional<Location> result = locationRepository.findById(1000); // replace with real ID from your DB

        assertThat(result).isPresent();
        assertThat(result.get().getLocationId()).isEqualTo(1000);
    }

    @Test
    public void testFindByLocationId_NotExists() {
        Optional<Location> result = locationRepository.findById(9999); // ID that does NOT exist in your DB

        assertThat(result).isNotPresent();
    }

    @Test
    public void TestCountLocations(){
       long count =  locationRepository.count();
       assertThat(count).isEqualTo(23);
    }

}