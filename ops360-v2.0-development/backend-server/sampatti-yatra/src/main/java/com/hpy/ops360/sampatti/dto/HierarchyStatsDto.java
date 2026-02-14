package com.hpy.ops360.sampatti.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class HierarchyStatsDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3402740092537991842L;
	@JsonIgnore
	private Long id;
//	@JsonIgnore
//	protected String createdBy;
//	@JsonIgnore
//	protected LocalDateTime createdAt;
//	@JsonIgnore
//	protected String modifiedBy;
//	@JsonIgnore
//	protected LocalDateTime modifiedAt;
	
	private Long srno;
    private String level;
    private String superiorUserId;
    private Integer teamCount;
    private Integer totalCount;
	public HierarchyStatsDto(Long srno, String level, String superiorUserId, Integer teamCount, Integer totalCount) {
		super();
		this.srno = srno;
		this.level = level;
		this.superiorUserId = superiorUserId;
		this.teamCount = teamCount;
		this.totalCount = totalCount;
	}
	
    
    
}
