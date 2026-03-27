package com.example.HumanResourceApplication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class JobHistoryId implements Serializable {

    @Column(name = "employee_id",columnDefinition = "DECIMAL(6,0)")
    private Double employeeId;

    @Column(name = "start_date")
    private LocalDate startDate;
}