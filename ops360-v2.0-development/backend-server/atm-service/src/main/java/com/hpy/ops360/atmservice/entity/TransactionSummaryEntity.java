package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionSummaryEntity {
	
    @Id
    private int srno;
    private String section;
    private int num;
    private String name;
    private double achieved;
    private double target;
}
