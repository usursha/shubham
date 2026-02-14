package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PermanentCEFilterBankEntity {
	@Id
    private Long srno;
	
	@Column(name="bank_name")
	private String bankName;
    
    private String count;
}
