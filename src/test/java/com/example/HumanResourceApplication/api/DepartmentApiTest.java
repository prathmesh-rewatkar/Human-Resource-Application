package com.example.HumanResourceApplication.api;

import com.example.HumanResourceApplication.entity.Department;
import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.entity.Location;
import com.example.HumanResourceApplication.repository.DepartmentRepository;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import com.example.HumanResourceApplication.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DepartmentApiTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LocationRepository locationRepository;


    // Helper Methods -
//    private Department createDepartment(String name) {
//
//        Location loc = locationRepository.save(new Location());
//        Employee manager = employeeRepository.save(new Employee());
//
//        Department dept = new Department();
//        dept.setDepartmentName(name);
//        dept.setLocation(loc);
//        dept.setManager(manager);
//
//        return dept;
//    }

    // Get All Departments
    @Test
    void testGetAllDepartments() throws Exception
    {
//        Location loc = locationRepository.save(new Location());
//        Employee manager = employeeRepository.save(new Employee());
//
//        Department dept = new Department();
//        dept.setDepartmentName("HR");
//        dept.setLocation(loc);
//        dept.setManager(manager);
//
//        repository.save(dept);

        mockMvc.perform(get("/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").isArray());
    }

    // Get Department By ID (Valid)
    @Test
    void testGetDepartmentById() throws Exception {

//        Department dept = repository.save(createDepartment("HR"));

        mockMvc.perform(get("/department/20" ))
                //.param("departmentId", "20L"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName").value("Marketing"));
    }

    // Get Department By ID(Invalid)
    @Test
    void testGetDepartmentById_NotFound() throws Exception {

        mockMvc.perform(get("/department/9999" ))
//                        .param("departmentId", "9999L"))
                .andExpect(status().isBadRequest());
    }

    // Search Department By Name
    @Test
    void testGetDepartmentByName() throws Exception {

//        repository.save(createDepartment("Finance"));

        mockMvc.perform(get("/department/search/findByDepartmentName")
                        .param("name", "Shipping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments[0].departmentName").value("Shipping"));
    }

    // Search Department By Name (No Result)
    @Test
    void testGetDepartmentByName_NotFound() throws Exception {

        mockMvc.perform(get("/department/search/findByDepartmentName")
                        .param("name", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.department").doesNotExist());
    }

//    Count Departments
//@Test
//void testCountDepartment() throws Exception {
//
//    repository.save(createDepartment("HR"));
//    repository.save(createDepartment("Finance"));
//
//    mockMvc.perform(get("/department/count"))
//            .andExpect(status().isOk())
//            .andExpect(content().string("2"));
//}



}