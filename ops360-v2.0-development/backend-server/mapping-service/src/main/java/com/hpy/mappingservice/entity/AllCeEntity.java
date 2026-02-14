package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class AllCeEntity {
	
	@Id
	@Column(name="sr_no")
	private Long srno;
	
	@Column(name="username")
	private String username;
	
	@Column(name="display_name")
	private String displayName;
	
	@Column(name="acitve_user")
	private String userStatus;

}
