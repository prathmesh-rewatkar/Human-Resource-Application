package com.example.HumanResourceApplication.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JobApiTest {

    @Autowired
    private MockMvc mockMvc;

    // 1️⃣ GET ALL JOBS
    @Test
    @DisplayName("GET all jobs API")
    void testGetAllJobs() throws Exception {
        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobs").exists());
    }

    // GET JOB BY ID (EXISTING)
    @Test
    void testGetJobById() throws Exception {
        mockMvc.perform(get("/jobs/IT_PROG"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.jobTitle").value("Programmer"));
}
    // GET JOB BY ID (NOT FOUND)
    @Test
    @DisplayName("GET job by ID - not found")
    void testGetJobByIdNotFound() throws Exception {
        mockMvc.perform(get("/jobs/INVALID_ID"))
                .andExpect(status().isNotFound());
    }

    // SEARCH BY JOB TITLE
    @Test
    @DisplayName("Search jobs by exact title")
    void testFindByJobTitle() throws Exception {
        mockMvc.perform(get("/jobs/search/findByJobTitle")
                        .param("jobTitle", "Programmer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobs").exists());
    }

    // SEARCH BY TITLE CONTAINING
    @Test
    void testFindByJobTitleContaining() throws Exception {
        mockMvc.perform(get("/jobs/search/findByJobTitleContainingIgnoreCase")
                    .param("jobTitle", "prog"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.jobs").exists());
}

    // SEARCH BY MIN SALARY
    @Test
    @DisplayName("Search jobs by min salary >= value")
    void testFindByMinSalary() throws Exception {
        mockMvc.perform(get("/jobs/search/findByMinSalaryGreaterThanEqual")
                        .param("minSalary", "4000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobs").exists());
    }

    // SEARCH BY MAX SALARY
    @Test
    @DisplayName("Search jobs by max salary <= value")
    void testFindByMaxSalary() throws Exception {
        mockMvc.perform(get("/jobs/search/findByMaxSalaryLessThanEqual")
                        .param("maxSalary", "10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobs").exists());
    }

    // SEARCH BY SALARY RANGE
    @Test
    @DisplayName("Search jobs by salary range")
    void testFindBySalaryRange() throws Exception {
        mockMvc.perform(get("/jobs/search/findByMinSalaryBetween")
                        .param("min", "4000")
                        .param("max", "10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobs").exists());
    }
}
