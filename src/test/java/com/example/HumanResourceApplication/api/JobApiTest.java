package com.example.HumanResourceApplication.api;

import com.example.HumanResourceApplication.entity.Job;
import com.example.HumanResourceApplication.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JobApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;

    @BeforeEach
    void setUp() {
        jobRepository.deleteAll();
        jobRepository.save(new Job("IT_PROG", "Programmer", 3000.0, 10000.0));
        jobRepository.save(new Job("HR_REP", "HR Representative", 2000.0, 8000.0));
        jobRepository.save(new Job("FIN_ACC", "Accountant", 4000.0, 15000.0));
    }

    @Test
    @DisplayName("GET all jobs API")
    void testGetAllJobs() throws Exception {
        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobs").exists());
    }
}