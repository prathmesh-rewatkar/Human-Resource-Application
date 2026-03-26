package com.example.HumanResourceApplication;


import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.ManagerIdProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ManagerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepo;


//    @Test
//    void testListAllManagers() throws Exception {
//
//        mockMvc.perform(get("/api/v1/employees/listallManagerDetails"))
//                .andExpect(status().isOk());
//    }



    // @Test
    // void testFindByEmployeeId() {

    //     Optional<Employee> result = employeeRepo.findByEmployeeId(1L);

    //     assertTrue(result.isPresent());
    //     System.out.println(result.get().getFirstName());
    // }


    // @Test
    // void testGetManagers() {

    //     List<ManagerIdProjection> ids = employeeRepo.findDistinctByManagerIdIsNotNull();


    //     List<Long> managerIds = ids.stream()
    //             .map(ManagerIdProjection::getManagerId)
    //             .toList();
    //     List<ManagerProjection> managers = employeeRepo.findByEmployeeIdIn(managerIds);

    //     assertNotNull(managers);
    //     assertTrue(managers.size() > 0);

    //     managers.forEach(m -> System.out.println(m.getFirstName()));
    // }



    // @Test
    // void testFindDistinctByManagerId() {

    //     List<ManagerIdProjection> result = employeeRepo.findDistinctByManagerIdIsNotNull();

    //     assertNotNull(result);
    //     assertTrue(result.size() > 0);

    //     result.forEach(e -> System.out.println(e));
    // }


}





