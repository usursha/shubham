package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.PortalUsersLatLongDetails;

@Repository
public interface PortalUsersLatLongRespository extends JpaRepository<PortalUsersLatLongDetails, String>{
	
	

}
