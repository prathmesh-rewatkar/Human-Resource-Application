package com.example.HumanResourceApplication.projection;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

public interface ManagerProjection {

    String getFirstName();

    String getLastName();

    String getEmail();

    Double getSalary();

    String getPhoneNumber();
    String getDepartment_DepartmentName();

}