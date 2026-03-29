package com.example.HumanResourceApplication.api;

import com.example.HumanResourceApplication.entity.Country;
import com.example.HumanResourceApplication.entity.Location;
import com.example.HumanResourceApplication.exception.ResourceNotFoundException;
import com.example.HumanResourceApplication.repository.CountryRepository;
import com.example.HumanResourceApplication.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationApiTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private LocationRepository locationRepository ;

//get all locations available
    @Test
    public void testGetAllLocations() throws Exception {
        mockMvc.perform(get("/locations")) //mocks the api endpoint
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.locations").isArray());
    }


//===============================Crud Operations =========================================================
// TEST: PUT - fully update an existing location
    @Test
    @Transactional
    public void testUpdateLocation_PUT_Success() throws Exception {
        String requestBody = """
            {
                "streetAddress": "Updated Street 99",
                "postalCode": "00989",
                "city": "Roma",
                "stateProvince": "Lazio",
                "country": "http://localhost/countries/IT"
            }
            """;

        mockMvc.perform(put("/locations/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());

    }

    // TEST: PUT - update non-existent location → 404
    @Test
    public void testUpdateLocation_PUT_NotFound() throws Exception {
        String requestBody = """
            {
                "streetAddress": "Ghost Street",
                "postalCode": "00000",
                "city": "Nowhere",
                "stateProvince": "None",
                "country": "http://localhost/countries/IT"
            }
            """;

        mockMvc.perform(put("/locations/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    // TEST: PATCH - partially update an existing location
    @Test
    @Transactional
    public void testUpdateLocation_PATCH_Success() throws Exception {
        String requestBody = """
            {
                "city": "Milan",
                "stateProvince": "Lombardy"
            }
            """;

        mockMvc.perform(patch("/locations/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    // TEST: PATCH - patch non-existent location → 404
    @Test
    public void testUpdateLocation_PATCH_NotFound() throws Exception {
        String requestBody = """
            {
                "city": "Nowhere"
            }
            """;

        mockMvc.perform(patch("/locations/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    // TEST: DELETE - successfully delete an existing location
    @Test
    @Transactional
    public void testDeleteLocation_Success() throws Exception {
        mockMvc.perform(delete("/locations/1000"))
                .andExpect(status().isNoContent()); // 204
    }

    // TEST: DELETE - delete non-existent location → 404
    @Test
    public void testDeleteLocation_NotFound() throws Exception {
        mockMvc.perform(delete("/locations/9999"))
                .andExpect(status().isNotFound());
    }

    // TEST: Successfully add a new location linked to an existing country
    @Test
    @Transactional
    public void testAddLocation_Success() throws Exception {
        String requestBody = """
            {
                "streetAddress": "123 Test Street",
                "postalCode": "411001",
                "city": "Nagpur",
                "stateProvince": "Maharashtra",
                "country": "http://localhost/countries/IN"
            }
            """;

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value("Nagpur"))
                .andExpect(jsonPath("$.stateProvince").value("Maharashtra"));
    }


}