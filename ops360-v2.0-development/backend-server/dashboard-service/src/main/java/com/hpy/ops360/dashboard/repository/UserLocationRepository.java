package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.location.entity.UserLocation;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

}