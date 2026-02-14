package com.hpy.ops360.atmservice.dto;
	
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankNameAndAtmUpTimeDto extends GenericDto{
	
	@JsonIgnore
    private Long id;
    private int srno;
    private String bankName;
  //  private String mtdUptime;
    private BigDecimal mtdUptime;
    @JsonFormat(pattern = "dd MMM yyyy, hh:mm a")
	private LocalDateTime dateTime; // Added field for T-1 timestamp

}
	