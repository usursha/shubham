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
public class CeBankWiseMTDEntity {
	
	@Id
	private String srNo;
	private String mtdUptime;

}
