package com.example.HumanResourceApplication.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class JobHistoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("API: Projection test")
    void testProjection() throws Exception {
        mockMvc.perform(get("/job_history")
                        .param("projection", "jobHistoryView"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Validate all projection fields")
    void testProjectionValidation() throws Exception {
        mockMvc.perform(get("/job_history")
                        .param("projection", "jobHistoryView"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$._embedded.jobHistories").isArray())

                .andExpect(jsonPath("$._embedded.jobHistories[0].employeeName").exists())
                .andExpect(jsonPath("$._embedded.jobHistories[0].startDate").exists())
                .andExpect(jsonPath("$._embedded.jobHistories[0].endDate").exists())
                .andExpect(jsonPath("$._embedded.jobHistories[0].jobTitle").exists())
                .andExpect(jsonPath("$._embedded.jobHistories[0].departmentName").exists());
    }
}
