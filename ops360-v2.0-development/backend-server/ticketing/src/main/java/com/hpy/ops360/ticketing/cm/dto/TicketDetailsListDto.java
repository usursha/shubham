package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailsListDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private Integer sr;
    private String atm_id;
    private String ticket_number;
    private String created_date;
    private String created_time;
    private String error;
    private String remark;
    private DocumentListDto documentList;
	public TicketDetailsListDto(Integer sr, String atm_id, String ticket_number, String created_date,
			String created_time, String error, String remark, DocumentListDto documentList) {
		super();
		this.sr = sr;
		this.atm_id = atm_id;
		this.ticket_number = ticket_number;
		this.created_date = created_date;
		this.created_time = created_time;
		this.error = error;
		this.remark = remark;
		this.documentList = documentList;
	}
    
    
	
    
}