package com.hpy.ops360.atmservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OwnerOption {
	
	private int ownerId;
	private String ownerName;
	private int ownerCount;
}