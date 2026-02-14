package com.hpy.ops360.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.UserLocation;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long>{

}
