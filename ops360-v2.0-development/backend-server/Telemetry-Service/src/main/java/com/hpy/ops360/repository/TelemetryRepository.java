package com.hpy.ops360.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.entity.UserTelemetry;
@Repository
public interface TelemetryRepository extends JpaRepository<UserTelemetry,Long> {

	// Using method name query
    List<UserTelemetry> findByJourneyId(String journeyId);

}
