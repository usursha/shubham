package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.location.entity.UserLocation;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long>{

}
