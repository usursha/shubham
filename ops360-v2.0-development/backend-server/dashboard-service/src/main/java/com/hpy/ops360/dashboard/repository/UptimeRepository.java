package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.dto.AtmUptimeDto;

import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;

@Repository
public interface UptimeRepository {

	@Query(value = "EXEC GET_ATM_UPTIME :atm_id", nativeQuery = true)
	AtmUptimeDto getUptimeByAtmId(String atmId);
}
