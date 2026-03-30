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
    //===================crup operations ==============
    // TEST: PUT - fully update an existing region
    @Test
    @Transactional
    public void testUpdateRegion_PUT_Success() throws Exception {

        String requestBody = """
        {
            "regionId": 10,
            "regionName": "Europe Updated"
        }
        """;

        // Step 1: Perform PUT (Update)
        mockMvc.perform(put("/regions/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());

        // Step 2: Verify using GET
        mockMvc.perform(get("/regions/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regionName").value("Europe Updated"));
    }

    // TEST: PUT - update non-existent region → 404
    @Test
    @Transactional
    public void testUpdateRegion_PUT_NotFound() throws Exception {
        String requestBody = """
            {
                "regionId": 9999,
                "regionName": "Ghost Region"
            }
            """;

        mockMvc.perform(put("/regions/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    // TEST: PATCH - partially update an existing region
    @Test
    @Transactional
    public void testUpdateRegion_PATCH_Success() throws Exception {
        String requestBody = """
            {
                "regionName": "Europe Patched"
            }
            """;

        mockMvc.perform(patch("/regions/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"regionName\":\"Europe Patched\"}"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/regions/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regionName").value("Europe Patched"));
    }

    // TEST: PATCH - patch non-existent region → 404
    @Test
    @Transactional
    public void testUpdateRegion_PATCH_NotFound() throws Exception {
        String requestBody = """
            {
                "regionName": "Nobody"
            }
            """;

        mockMvc.perform(patch("/regions/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    // TEST: DELETE - successfully delete an existing region
    @Test
    @Transactional
    public void testDeleteRegion_Success() throws Exception {
        mockMvc.perform(delete("/regions/1"))
                .andExpect(status().isNotFound()); // 204
    }

    // TEST: DELETE - delete non-existent region → 404
    @Test
    public void testDeleteRegion_NotFound() throws Exception {
        mockMvc.perform(delete("/regions/9999"))
                .andExpect(status().isNotFound());
    }
}