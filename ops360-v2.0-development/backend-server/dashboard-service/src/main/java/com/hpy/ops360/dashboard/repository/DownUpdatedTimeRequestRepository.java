package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.DownUpdatedTime;

@Repository
public interface DownUpdatedTimeRequestRepository extends JpaRepository<DownUpdatedTime, Long> {

}
