package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SubcallTypeEntityForAtmTicketHistory {

	   @Id
	    @Column(name = "srno")
	    private Long srno;

	    @Column(name = "subcallype")
	    private String subcallype;

	    @Column(name = "owner_name")
	    private String ownerName;
}
