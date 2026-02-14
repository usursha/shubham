package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PortalUsersLatLongDetails {
	
	@Id
	private String username;
	private String baseLocation;
	private String baseLocationLatitude;
	private String baseLocationLongitude;
}
