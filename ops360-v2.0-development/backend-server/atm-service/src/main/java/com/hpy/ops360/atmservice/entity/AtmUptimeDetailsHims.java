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
public class AtmUptimeDetailsHims {
	 @Id
	    @Column(name = "atmid")
	    private String atmId;

	    @Column(name = "monthtotilldateuptime")
	    private Double monthToTillDateUptime;

	    @Column(name = "lastmonthuptime")
	    private Double lastMonthUptime;
    
}

