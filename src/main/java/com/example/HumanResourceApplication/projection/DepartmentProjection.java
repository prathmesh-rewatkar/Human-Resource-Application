package com.example.HumanResourceApplication.projection;

import com.example.HumanResourceApplication.entity.Department;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = " deptView", types = { Department.class })
public interface DepartmentProjection {
//    String getName();
//
//    EmployeeInfo getManager();
//    LocationInfo getLocation();
//
//    interface EmployeeInfo {
//        String getName();
//    }
//
//    interface LocationInfo {
//        String getCity();
//        String getStreet();
//    }

    String getDepartmentName();

    LocationSummary getLocation();

    ManagerSummary getManager();

    // Nested Location
    interface LocationSummary {
        String getCity();
        String getStreetAddress();
        String getStateProvince();
    }

    // Nested Manager (Employee)
    interface ManagerSummary {
        String getFirstName();
        String getLastName();
        String getEmail();
        String getPhoneNumber();
    }
}