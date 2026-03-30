package com.example.HumanResourceApplication.api;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CountryApiTest {

    @Autowired
    MockMvc mockMvc;

    //  TEST 1: Successfully add a new country (USE UNIQUE ID)
    @Test
    @Transactional
    public void testAddCountry_Success() throws Exception {
        String requestBody = """
                {
                    "countryId": "ZZ1",
                    "countryName": "Test Country",
                    "region": "http://localhost/regions/10"
                }
                """;

        mockMvc.perform(post("/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryName").value("Test Country"));
    }

    //  TEST 2: Duplicate country → 409
    @Test
    @Transactional
    public void testAddCountry_AlreadyExists_ReturnsConflict() throws Exception {
        String requestBody = """
                {
                    "countryId": "US",
                    "countryName": "United States of America",
                    "region": "http://localhost/regions/20"
                }
                """;

        mockMvc.perform(post("/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());
    }

    // TEST: Add country with non-existing region → should fail
    @Test
    @Transactional
    public void testAddCountry_WithNonExistingRegion_Fails() throws Exception {
        String requestBody = """
            {
                "countryId": "ZZ",
                "countryName": "Test Country",
                "region": "http://localhost/regions/9999"
            }
            """;

        mockMvc.perform(post("/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is4xxClientError());
    }
    // ======================================= RUD ======================================

    // PUT existing → 204
    @Test
    @Transactional
    public void testUpdateCountry_PUT_Success() throws Exception {
        String requestBody = """
            {
                "countryId": "US",
                "countryName": "United States Updated",
                "region": "http://localhost/regions/2"
            }
            """;

        mockMvc.perform(put("/countries/US")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    //  PUT non-existing → 201 (CREATES NEW)
    @Test
    @Transactional
    public void testUpdateCountry_PUT_NotFound() throws Exception {
        String requestBody = """
            {
                "countryId": "ZZ2",
                "countryName": "Ghost Country",
                "region": "http://localhost/regions/10"
            }
            """;

        mockMvc.perform(put("/countries/ZZ2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    //  PATCH existing → 204
    @Test
    @Transactional
    public void testUpdateCountry_PATCH_Success() throws Exception {
        String requestBody = """
            {
                "countryName": "United States Patched"
            }
            """;

        mockMvc.perform(patch("/countries/US")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    // PATCH non-existing → 404
    @Test
    @Transactional
    public void testUpdateCountry_PATCH_NotFound() throws Exception {
        String requestBody = """
            {
                "countryName": "Nobody"
            }
            """;

        mockMvc.perform(patch("/countries/ZZ3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    //  DELETE existing → 204
    @Test
    @Transactional
    public void testDeleteCountry_Success() throws Exception {
        mockMvc.perform(delete("/countries/US"))
                .andExpect(status().isNoContent());
    }

    //  DELETE non-existing → 404
    @Test
    @Transactional
    public void testDeleteCountry_NotFound() throws Exception {
        mockMvc.perform(delete("/countries/ZZ4"))
                .andExpect(status().isNotFound());
    }
}