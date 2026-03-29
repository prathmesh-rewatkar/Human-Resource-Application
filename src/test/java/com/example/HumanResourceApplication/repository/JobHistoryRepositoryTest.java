package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.JobHistory;
import com.example.HumanResourceApplication.repository.JobHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JobHistoryRepositoryTest {

    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    // TEST 1: findAll() → validate real DB fields
    @Test
    @DisplayName("Repository: findAll should match DB data")
    void testFindAll_FromDatabase() {

        List<JobHistory> list = jobHistoryRepository.findAll();

        assertThat(list).isNotEmpty();

        JobHistory jh = list.get(0);

        assertThat(jh.getId()).isNotNull();
        assertThat(jh.getId().getEmployeeId()).isNotNull();
        assertThat(jh.getId().getStartDate()).isNotNull();
        assertThat(jh.getEndDate()).isNotNull();

        assertThat(jh.getJob()).isNotNull();
        assertThat(jh.getJob().getJobTitle()).isNotBlank();

        if (jh.getDepartment() != null) {
            assertThat(jh.getDepartment().getDepartmentName()).isNotBlank();
        }
    }

    // TEST 2: findByIdEmployeeId() using real DB employee (101 exists)
    @Test
    @DisplayName("Repository: findByIdEmployeeId should return records for employee 101")
    void testFindByEmployeeId_FromDatabase() {

        List<JobHistory> result = jobHistoryRepository.findByIdEmployeeId(101);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isGreaterThanOrEqualTo(2);

        result.forEach(jh ->
                assertThat(jh.getId().getEmployeeId()).isEqualTo(101)
        );
    }
}
