package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalUsersLatLongDto {
	
	@JsonIgnore
	private Long id;
	
	@JsonIgnore
	private String parentId;
	
	@NotEmpty(message="parentType cannot be blank")
	private String parentType;
	
	@NotEmpty(message="mediaType cannot be blank")
	private String mediaType;
	
	@JsonIgnore
	private String docPath;
	
	@NotEmpty(message="fileName cannot be blank")
	private String fileName;
	
	@Transient
	@NotEmpty(message="base64String cannot be blank")
	private String base64String;
	
	@NotEmpty(message="username cannot be blank")
	private String username;
	@NotEmpty(message="latitude cannot be blank")
	private String latitude;
	@NotEmpty(message="longitude cannot be blank")
	private String longitude;

}
