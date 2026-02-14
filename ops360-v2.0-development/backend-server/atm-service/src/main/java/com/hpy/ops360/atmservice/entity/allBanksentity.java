package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class allBanksentity {
	@Id
    private String sr_no; 
    @Column(name = "bank_name")
    private String bankName;
    }

