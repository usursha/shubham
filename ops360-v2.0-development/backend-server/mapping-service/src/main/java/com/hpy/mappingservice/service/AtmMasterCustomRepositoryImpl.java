package com.hpy.mappingservice.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.AtmMaster;
import com.hpy.mappingservice.repository.AtmMasterCustomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AtmMasterCustomRepositoryImpl implements AtmMasterCustomRepository {
	
	private final JdbcTemplate jdbcTemplate;

	@Override
	public void bulkInsertOrUpdate(List<AtmMaster> atmMasters) {
		log.info("Starting insert/update for {} ATMs");
		String sql="""
	            MERGE INTO atm_master AS target
	            USING (VALUES (?, ?, ?, ?, ?, ?, ?, ?)) AS source(atm_code, bank_name, grade, city, state, address, zone, source)
	            ON target.atm_code = source.atm_code
	            WHEN MATCHED THEN UPDATE SET
	                bank_name = source.bank_name,
	                grade = source.grade,
	                city = source.city,
	                state = source.state,
	                address = source.address,
	                zone = source.zone,
	                source = source.source
	            WHEN NOT MATCHED THEN INSERT (atm_code, bank_name, grade, city, state, address, zone, source)
	            VALUES (source.atm_code, source.bank_name, source.grade, source.city, source.state, source.address, source.zone, source.source);
	        """;
		
		List<Object[]> batchArgs = atmMasters.stream()
	            .map(a -> new Object[]{
	                a.getAtmCode(), a.getBankName(), a.getGrade(),
	                a.getCity(), a.getState(), a.getAddress(),
	                a.getZone(), a.getSource()
	            })
	            .toList();

	        jdbcTemplate.batchUpdate(sql, batchArgs);	
	}
	
	

}
