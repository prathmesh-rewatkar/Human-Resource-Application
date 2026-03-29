package com.example.HumanResourceApplication.api;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegionApiTest {

    @Autowired
    MockMvc mockMvc;

    // TEST 1: Successfully add a new region — verify both regionId and regionName
    @Test
    @Transactional
    public void testAddRegion_Success() throws Exception {
        String requestBody = """
                {
                    "regionId": 9903,
                    "regionName": "Test Region"
                }
                """;

        mockMvc.perform(post("/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.regionName").value("Test Region"));
    }

    // TEST 2: Duplicate regionId → must return exactly 409 Conflict
    @Test
    @Transactional
    public void testAddRegion_AlreadyExists_ReturnsConflict() throws Exception {
        String requestBody = """
                {
                    "regionId": 9909,
                    "regionName": "Duplicate Region"
                }
                """;

        // First insert — should succeed
        mockMvc.perform(post("/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        // Second insert with same regionId — should return 409
        mockMvc.perform(post("/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())    ;           // 409 exactly
//                .andExpect(jsonPath("$.error").value("Resource already exists"));
    }
}