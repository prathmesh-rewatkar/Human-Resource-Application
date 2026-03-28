package com.example.HumanResourceApplication.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateManagerDTO {

    @NotNull(message = "Employee ID cannot be null")
    @Positive(message = "Employee ID must be positive")
    private Double employeeId;

    @NotNull(message = "New Manager ID cannot be null")
    @Positive(message = "New Manager ID must be positive")
    private Double newManagerId;
}