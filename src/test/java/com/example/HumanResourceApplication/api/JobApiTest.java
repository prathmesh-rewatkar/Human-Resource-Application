package com.example.HumanResourceApplication.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JobApiTest {

    @Autowired
    private MockMvc mockMvc;

    // 1GET ALL JOBS
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
            .andDo(print())
            .andExpect(status().isOk());

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



// CREATE JOB
@Test
@DisplayName("POST create job")
void testCreateJob() throws Exception {

    String json = """
            {
                "jobId": "SE_DEV",
                "jobTitle": "Software Engineer",
                "minSalary": 5000,
                "maxSalary": 15000
            }
            """;

    mockMvc.perform(post("/jobs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(header().string("Location",
                    org.hamcrest.Matchers.containsString("/jobs/SE_DEV")));
}

        @Test
        @DisplayName("POST job - validation error")
        void testCreateJobValidationFail() throws Exception {

        String json = """
                {
                        "jobId": "",
                        "jobTitle": "",
                        "minSalary": 5000,
                        "maxSalary": 15000
                }
                """;

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST job - duplicate ID")
        void testCreateDuplicateJob() throws Exception {

    String json = """
            {
                "jobId": "IT_PROG",
                "jobTitle": "Duplicate Job",
                "minSalary": 5000,
                "maxSalary": 10000
            }
            """;

    mockMvc.perform(post("/jobs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isCreated());   
}

        @Test
        @DisplayName("GET job - invalid ID format")
        void testInvalidIdFormat() throws Exception {

        mockMvc.perform(get("/jobs/123@invalid"))
                .andExpect(status().isNotFound());
        }

// FULL UPDATE (PUT)
@Test
@Transactional
@DisplayName("PUT update job")
void testFullUpdateJob() throws Exception {

    String json = """
            {
                "jobId": "IT_PROG",
                "jobTitle": "Senior Programmer",
                "minSalary": 6000,
                "maxSalary": 12000
            }
            """;

    mockMvc.perform(put("/jobs/IT_PROG")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isNoContent());

    // verify update
    mockMvc.perform(get("/jobs/IT_PROG"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.jobTitle").value("Senior Programmer"));
}


// PARTIAL UPDATE (PATCH)
@Test
@Transactional
@DisplayName("PATCH update job")
void testPartialUpdateJob() throws Exception {

    String json = """
            {
                "jobTitle": "Lead Programmer"
            }
            """;

    mockMvc.perform(patch("/jobs/IT_PROG")
                    .contentType("application/merge-patch+json")
                    .content(json))
            .andExpect(status().isNoContent());

    // verify patch
//     mockMvc.perform(get("/jobs/IT_PROG"))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.jobTitle").value("Lead Programmer"));
}

}
